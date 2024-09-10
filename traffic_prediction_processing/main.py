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
         math.cos(lat1) * math.cos(lat2));
    rad = 6371
    c = 2 * math.asin(math.sqrt(a))
    return rad * c

def get_sensor_distances(device_id_locations):
    from_to_distances = []

    for i in range(0, len(device_id_locations)):
        for j in range(0, len(device_id_locations)):
            if (device_id_locations[i]['deviceId'] == device_id_locations[j]['deviceId']):
                continue
            print(f"Comparing {device_id_locations[i]['deviceId']} to {device_id_locations[j]['deviceId']}")
            lat1 = float(device_id_locations[i]['latitude'])
            long1 = float(device_id_locations[i]['longitude'])

            lat2 = float(device_id_locations[j]['latitude'])
            long2 = float(device_id_locations[j]['longitude'])
            distance = get_haversine_distance(lat1, long1, lat2, long2)
            print(f"Distance: {distance}")
            from_to_distances.append({'from': device_id_locations[i]['deviceId'], 'to': device_id_locations[j]['deviceId'], 'cost': distance})

    return from_to_distances

def process_data():
    client = MongoClient(uuidRepresentation='pythonLegacy')

    db = client.trafficdata

    collection = db.trafficdata


    trafficdata = collection.find({
        'MAJOR_ROAD': "US101"
    }).sort({'timestamp': 1})

    trafficdata_list = list(trafficdata)
    current_speed_values = []
    pst_created_at_dates = []

    device_id_locations_set = set()
    device_id_locations = []
    device_id_values = {}
    device_id_values['Date'] = []

    for traffic in trafficdata_list:
        # get speed value from traffic data JSON
        trafficData = json.loads(traffic['trafficData'])
        current_speed = trafficData['flowSegmentData']['currentSpeed']
        current_speed_values.append(current_speed)

        # convert utc to pst
        created_at_pst = traffic['createdAt'] - timedelta(hours=7)
        created_at_pst_formatted = created_at_pst.strftime('%Y-%m-%d %H:%M')
        if created_at_pst_formatted not in pst_created_at_dates:
            pst_created_at_dates.append(created_at_pst_formatted)

        if traffic['deviceId'] not in device_id_locations_set:
            device_id_locations_set.add(traffic['deviceId'])
            location_split = traffic['location'].split(',')
            if traffic['deviceId'] not in device_id_values:
                device_id_values[traffic['deviceId']] = []
            device_id_locations.append({'deviceId': traffic['deviceId'], 'latitude': location_split[0], 'longitude': location_split[1]})
        device_id_values.get(traffic['deviceId'], []).append(current_speed)


    # generate from to distances
    from_to_distances = get_sensor_distances(device_id_locations)

    df_from_to_distances = pd.DataFrame.from_records(from_to_distances)
    display(df_from_to_distances)

    device_id_locations_df = pd.DataFrame.from_records(device_id_locations)
    display(device_id_locations_df)


    print(len(device_id_values.keys()))
    print(f" length of dates: {len(pst_created_at_dates)}")

    device_id_values['Date'] = pst_created_at_dates
    print(f"pst dates: {pst_created_at_dates}")
    print(device_id_values)
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
    process_data()
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
