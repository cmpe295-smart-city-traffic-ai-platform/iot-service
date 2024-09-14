import bson
import h5py
import numpy as np
import pandas
import pymongo
from pymongo import MongoClient
import json
import math
from datetime import datetime, timedelta
from dateutil import tz
from zoneinfo import ZoneInfo
from pytz import timezone

from numpy import load
from IPython.display import display
import pandas as pd

pd.set_option('display.max_colwidth', None)
pd.set_option('display.max_rows', None)
pd.set_option('display.max_columns', None)
pandas.set_option('display.expand_frame_repr', False)

# reference: https://www.geeksforgeeks.org/haversine-formula-to-find-distance-between-two-points-on-a-sphere/
def get_haversine_distance(lat1, lon1, lat2, lon2):
    # distance between latitudes
    # and longitudes
    dLat = (lat2 - lat1) * math.pi / 180.0
    dLon = (lon2 - lon1) * math.pi / 180.0

    # convert to radians
    lat1 = (lat1) * math.pi / 180.0
    lat2 = (lat2) * math.pi / 180.0

    # apply formula
    a = (pow(math.sin(dLat / 2), 2) +
         pow(math.sin(dLon / 2), 2) *
         math.cos(lat1) * math.cos(lat2))
    rad = 6371
    c = 2 * math.asin(math.sqrt(a))
    return rad * c


# for each device id, compare distance with all other sensors
def get_sensor_distances(device_id_locations):
    from_to_distances = []

    for i in range(0, len(device_id_locations)):
        for j in range(0, len(device_id_locations)):
            # skip comparing distance to itself
            if (device_id_locations[i]['sensor_id'] == device_id_locations[j]['sensor_id']):
                continue
            # print(f"Comparing {device_id_locations[i]['deviceId']} to {device_id_locations[j]['deviceId']}")
            lat1 = float(device_id_locations[i]['latitude'])
            long1 = float(device_id_locations[i]['longitude'])

            lat2 = float(device_id_locations[j]['latitude'])
            long2 = float(device_id_locations[j]['longitude'])
            # use haversine distance between two points
            distance = get_haversine_distance(lat1, long1, lat2, long2)
            # print(f"Distance: {distance}")
            from_to_distances.append({'from': device_id_locations[i]['sensor_id'], 'to': device_id_locations[j]['sensor_id'], 'cost': distance})

    return from_to_distances

def process_data(MAJOR_ROAD):
    # query mongodb database collection for traffic data by major road
    client = MongoClient(uuidRepresentation='pythonLegacy')
    db = client.trafficdata
    collection = db.trafficdata

    trafficdata = collection.find({
        'MAJOR_ROAD': MAJOR_ROAD
    }).sort({'timestamp': 1})

    trafficdata_list = list(trafficdata)

    # keep track of current speed values
    current_speed_values = []
    pst_created_at_dates = []

    # keep track of device ids and device id locations
    device_id_locations_set = set()
    device_id_locations = []

    # keep track of speed values for each device id
    device_id_values = {}
    device_id_values['Date'] = []

    device_id_location_index = 0

    # go through each traffic data record in ascending order by timestamp
    for traffic in trafficdata_list:
        # get speed value from traffic data JSON
        trafficData = json.loads(traffic['trafficData'])
        current_speed = trafficData['flowSegmentData']['currentSpeed']
        current_speed_values.append(current_speed)

        # convert utc to pst, 7 hour difference
        created_at_pst = traffic['createdAt'] - timedelta(hours=7)
        created_at_pst_formatted = created_at_pst.strftime('%Y-%m-%d %H:%M')
        if created_at_pst_formatted not in pst_created_at_dates:
            pst_created_at_dates.append(created_at_pst_formatted)

        # keep track of distinct device ids
        if traffic['deviceId'] not in device_id_locations_set:
            device_id_locations_set.add(traffic['deviceId'])
            location_split = traffic['location'].split(',')
            if traffic['deviceId'] not in device_id_values:
                device_id_values[traffic['deviceId']] = []
            # map device id to location
            device_id_locations.append({'index': device_id_location_index, 'sensor_id': traffic['deviceId'], 'latitude': location_split[0], 'longitude': location_split[1]})
            device_id_location_index += 1
        # add speed value for device id
        device_id_values.get(traffic['deviceId'], []).append(current_speed)

    # device ids
    print("Writing graph sensor ids txt file....")
    device_ids_txt_file = open(f"{MAJOR_ROAD}_graph_sensor_ids.txt", "w")
    for device_id in device_id_locations_set:
        device_ids_txt_file.write(f"{str(device_id)},")
    device_ids_txt_file.close()

    # generate from to distances
    from_to_distances = get_sensor_distances(device_id_locations)
    df_from_to_distances = pd.DataFrame.from_records(from_to_distances)
    display(df_from_to_distances)
    df_from_to_distances.to_csv(f"{MAJOR_ROAD}_distances.csv", index=False)

    # device id locations
    device_id_locations_df = pd.DataFrame.from_records(device_id_locations)
    display(device_id_locations_df)
    device_id_locations_df.to_csv(f"{MAJOR_ROAD}_graph_sensor_locations.csv", index=False)


    print(f"PST dates length: {len(pst_created_at_dates)}")

    # update dataframe for speed values
    min_value_length = math.inf
    max_value_length = 0
    for device_id in device_id_values.keys():
        if (device_id == 'Date'):
            continue
        print(f"Key: {device_id}")
        print(f"Length of values: {len(device_id_values.get(device_id))}")
        # find minimum length
        if len(device_id_values.get(device_id)) < min_value_length:
            min_value_length = len(device_id_values.get(device_id))
        # find maximum length
        if len(device_id_values.get(device_id)) > max_value_length:
            max_value_length = len(device_id_values.get(device_id))

    print(f"Min Value Length: {min_value_length}")
    print(f"Max Value Length: {max_value_length}")

    # if min != max, need to remove values to meet min values
    if min_value_length != max_value_length:
        max_min_diff = abs(max_value_length - min_value_length)
        print(f"Need to remove {max_min_diff} values")

        # remove N dates
        pst_created_at_dates = pst_created_at_dates[max_min_diff:]
        device_id_values['Date'] = pst_created_at_dates

        # for each device id values remove N values if not equal to min
        print("Removing values...")
        for device_id in device_id_values.keys():
            current_device_id_values = device_id_values[device_id]
            # update values after removing N dates
            if len(current_device_id_values) != min_value_length:
                print("updating...")
                diff_value = abs(len(current_device_id_values) - min_value_length)
                device_id_values[device_id] = current_device_id_values[diff_value:]

    for device_id in device_id_values.keys():
        print(f"Key: {device_id}")
        print(f"Length of values: {len(device_id_values.get(device_id))}")

    speeds_by_ids_df = pd.DataFrame.from_dict(device_id_values)
    display(speeds_by_ids_df)


    # df = pd.DataFrame.from_records(trafficdata_list)
    # df = df.drop('trafficData', axis=1)
    # df = df.drop('_id', axis=1)
    # df = df.drop('_class', axis=1)
    # df = df.drop('createdAt', axis=1)
    # df.insert(2, "Speed", current_speed_values, True)
    # df.insert(3, "Created At PST", pst_created_at_dates, True)
    # display(df)


    # df = pd.read_csv('280_1_trafficdata.9_5_2024.csv')
    # display(df.columns)
    # df = df.drop('_class', axis=1)
    # display(df)
    # df['deviceId'] = df['deviceId'].apply(lambda x : bson)
    # display(df['deviceId'])





    # d1 = np.random.randint(0, 100, size =1000)
    #
    # hf = h5py.File('data.h5', 'w')
    # hf.create_dataset('test_1', data=d1)
    # hf.close()
    #
    # filename = "metr-la.h5"
    # # hf = h5py.File(filename, 'r')
    # # print(hf.keys())
    # # print(np.array(hf.get('speed')))
    #
    #
    # df = pd.read_hdf(filename)
    # display(df)




    # df = pd.DataFrame(np.array(h5py.File(filename)['speed']))
    # display(df)

    # # df.drop("400017")
    # # display(df.keys())

    # data = load('train.npz')
    # print(data['x'])

    # print("dcrnn predictions: \n")
    # data = load('dcrnn_predictions_bay.npz')
    # lst = data.files
    # print(lst)
    #
    # print("predictions: \n")
    # predictions = data['prediction']
    # predicted_values = []
    #
    # for prediction in data['prediction']:
    #     for value in prediction:
    #         predicted_values.append(value)
    #
    #
    # predictions_df = pandas.DataFrame(predicted_values)
    # display(predictions_df)


if __name__ == "__main__":
    process_data("I880")
    # filename = "metr-la.h5"
    # hf = h5py.File(filename, 'r')
    # print(hf)
    # print(list(hf.keys()))
    # group_df = hf['df']
    #
    # print(type(group_df))
    # print(group_df.values())
    # print(group_df.keys())
    # print(group_df['axis0'])
    # print(group_df['axis1'][:])
    # print(list(group_df.items()))
    #
    # dataset1 = np.array(group_df['axis0'])
    # dataset2 = np.array(group_df['axis1'])
    # dataset3 = np.array(group_df['block0_items'])
    # dataset4 = np.array(group_df['block0_values'])
    # for value in dataset1:
    #     print(value.decode('utf-8'))

    # print(len(dataset2))
    # print(dataset2[0])
    # for value in dataset2:
    #     print(value)


    # print(len(dataset3))
    # print(dataset3[0])

    # print(len(dataset4))
    # print(len(dataset4[0]))
    # print(dataset4[0])



    # print(dataset.keys())
    # print(dataset.values())
    # print(dataset.items())
    # print(list(group_df.get('axis0')))
    # print(list(group_df.get('axis1')))
    # print(list(dataset.get('block0_items')))
    # print(list(dataset.get('block0_values')))



    # print(dataset.groups)


    #
    # df = pd.read_hdf(filename)
    # print(df.keys())
    # print(type(df))
    # print(type(df.columns))
    # print(df.head())
    # print(df.iloc[0])
