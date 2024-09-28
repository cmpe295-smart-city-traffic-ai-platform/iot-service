import h5py
import os
import numpy as np
import pandas
from pymongo import MongoClient
import json
import math
from datetime import datetime, timedelta
from IPython.display import display
import pandas as pd

pd.set_option('display.max_colwidth', None)
pd.set_option('display.max_rows', None)
pd.set_option('display.max_columns', None)
pandas.set_option('display.expand_frame_repr', False)

def process_data():
    # query mongodb database collection for traffic data by major road
    client = MongoClient(uuidRepresentation='pythonLegacy')
    db = client.trafficdata
    collection = db.trafficdata

    trafficdata = collection.find({
        'deviceIdNo': {'$exists': True}
    }).sort({'timestamp': 1})

    trafficdata_list = list(trafficdata)

    # keep track of current speed values
    current_speed_values = []
    created_at_dates = []

    # keep track of device ids and device id locations
    device_id_set = set()
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

        # convert utc to timestamp
        # created_at_timestamp = int(round(datetime.timestamp(traffic['createdAt'])))
        created_at_timestamp = traffic['createdAt'] - timedelta(hours=7)
        if created_at_timestamp not in created_at_dates:
            created_at_dates.append(created_at_timestamp)

        # keep track of distinct device ids
        device_id_int = int(traffic['deviceIdNo'])
        print(f"device id int converted: {device_id_int}")

        # keep track of distinct device ids
        if device_id_int not in device_id_set:
            device_id_set.add(device_id_int)
            location_split = traffic['location'].split(',')
            if device_id_int not in device_id_values:
                device_id_values[device_id_int] = []
            # map device id to location
            device_id_locations.append({'index': device_id_location_index, 'sensor_id': device_id_int, 'latitude': location_split[0], 'longitude': location_split[1]})
            device_id_location_index += 1
        # add speed value for device id
        device_id_values.get(device_id_int, []).append(current_speed)

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
        created_at_dates = created_at_dates[max_min_diff:]
        device_id_values['Date'] = created_at_dates

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

    # create matrix of speed values timestamps x sensor ids
    speed_values_by_date = []

    for i in range(min_value_length):
        speed_values_current_date = []
        for device_id in device_id_set:
            speed_values_current_date.append(device_id_values.get(device_id)[i])
        speed_values_by_date.append(speed_values_current_date)
    print(f"device ids: {device_id_set}")
    print(f"speed values by date: {speed_values_by_date}")
    print(np.array(speed_values_by_date).shape)

    device_id_list = []
    for device_id in device_id_set:
        device_id_list.append(device_id)

    # create dataframe for speeds by ids
    speeds_by_ids_df = pd.DataFrame(data=speed_values_by_date, index=created_at_dates, columns=device_id_list)
    display(speeds_by_ids_df)
    df_path = f"data.h5"
    speeds_by_ids_df.to_hdf(df_path, key='speed', mode='w')

if __name__ == "__main__":
    process_data()


    #
    #
    # print("\n")
    # print("checking h5 dataset values ***********")
    # print(type(group_df))
    # print(group_df.values())
    # print(group_df.keys())
    # print(group_df['axis0'][()])
    # print(group_df['axis1'][:])
    # print(len(group_df['axis1'][:]))
    # print(group_df['block0_values'][:])
    # print(group_df['block0_items'][:])

    # print("\n")
    # print("checking data.h5*******")
    # test_filename = "data.h5"
    # hf = h5py.File(test_filename, 'r')
    # print(hf)
    # print(list(hf.keys()))
    # group_df = hf['speed']
    #
    # print(type(group_df))
    # print(group_df.keys())
    # print(group_df.items())
    # print(type(group_df['axis0']))
    # print(type(group_df['axis1']))
    # # # axis0 shape: (207,)
    # # # axis1 shape: (34272,)
    # # # block0_items shape: (207,)
    # # # block0_values shape: (34272, 207)
    # print(f"axis0 shape: {group_df.get('axis0').shape}")
    # print(f"axis1 shape: {group_df.get('axis1').shape}")
    # print(f"block0_items shape: {group_df.get('block0_items').shape}")
    # print(f"block0_values shape: {group_df.get('block0_values').shape}")
    # #
    # print(group_df.get('axis0')[()])
    # print(group_df.get('block0_values')[()])
    # print(group_df.get('axis1')[()])
    # print(group_df.get('block0_items')[()])
    #
    # df = pd.read_hdf('data.h5', 'speed')
    # print("read_hdf values ************")
    # print(df.keys())
    # print(df.values.shape)
    # print(len(df.values))
    # print(len(df.values[0]))
    # display(df)

    # data = load('dcrnn_predictions_pytorch.npz')
    # prediction_data = np.array(data['prediction'])
    # print(prediction_data.shape)
    #
    # print(prediction_data[0])
    # print(prediction_data[0].shape)
    #
    # print(prediction_data[1])
    # print(prediction_data[1].shape)
    #
    # # for data in prediction_data:
    # #     print(data)
    # predictions_df = pd.DataFrame(data=prediction_data[0])
    # display(predictions_df)




