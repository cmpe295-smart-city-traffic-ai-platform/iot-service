import pandas
from IPython.display import display
import pandas as pd
import os
import requests

pd.set_option('display.max_colwidth', None)
pd.set_option('display.max_rows', None)
pd.set_option('display.max_columns', None)
pandas.set_option('display.expand_frame_repr', False)

iot_service_url = "http://localhost:8080/api/v1/iot"

if __name__ == "__main__":
    if not os.path.exists('selected_sensors/'):
        os.makedirs('selected_sensors/')
    # create dataframes based on major road
    df = pd.read_csv('selected_prediction_devices.csv')

    index = 0

    df_280 = df[df['major_road'] == 'I280']
    device_id_file_280 = open("selected_sensors/device_ids_280.txt", "w")
    for i, row in df_280.iterrows():
        if index != len(df_280) - 1:
            device_id_file_280.write(f"{str(int(row['device_id_no']))},")
        else:
            device_id_file_280.write(f"{str(int(row['device_id_no']))}")
        index += 1
    device_id_file_280.close()

    index = 0

    df_101 = df[df['major_road'] == 'US101']
    device_id_file_101 = open("selected_sensors/device_ids_101.txt", "w")
    for i, row in df_101.iterrows():
        if index != len(df_101) - 1:
            device_id_file_101.write(f"{str(int(row['device_id_no']))},")
        else:
            device_id_file_101.write(f"{str(int(row['device_id_no']))}")
        index += 1
    device_id_file_101.close()

    index = 0


    df_85 = df[df['major_road'] == 'CA85']
    device_id_file_85 = open("selected_sensors/device_ids_85.txt", "w")
    for i, row in df_85.iterrows():
        if index != len(df_85) - 1:
            device_id_file_85.write(f"{str(int(row['device_id_no']))},")
        else:
            device_id_file_85.write(f"{str(int(row['device_id_no']))}")
        index += 1
    device_id_file_85.close()


    index = 0

    df_880 = df[df['major_road'] == 'I880']
    device_id_file_880 = open("selected_sensors/device_ids_880.txt", "w")
    for i, row in df_880.iterrows():
        if index != len(df_880) - 1:
            device_id_file_880.write(f"{str(int(row['device_id_no']))},")
        else:
            device_id_file_880.write(f"{str(int(row['device_id_no']))}")
        index += 1
    device_id_file_880.close()

    index = 0

    df_680 = df[df['major_road'] == 'I680']
    device_id_file_680 = open("selected_sensors/device_ids_680.txt", "w")
    for i, row in df_680.iterrows():
        if index != len(df_680) - 1:
            device_id_file_680.write(f"{str(int(row['device_id_no']))},")
        else:
            device_id_file_680.write(f"{str(int(row['device_id_no']))}")
        index += 1
    device_id_file_680.close()


    index = 0

    df_87 = df[df['major_road'] == 'CA87']
    device_id_file_87 = open("selected_sensors/device_ids_87.txt", "w")
    for i, row in df_87.iterrows():
        if index != len(df_87) - 1:
            device_id_file_87.write(f"{str(int(row['device_id_no']))},")
        else:
            device_id_file_87.write(f"{str(int(row['device_id_no']))}")
        index += 1
    device_id_file_87.close()

    index = 0

    df_237 = df[df['major_road'] == 'CA237']
    device_id_file_237 = open("selected_sensors/device_ids_237.txt", "w")
    for i, row in df_237.iterrows():
        if index != len(df_237) - 1:
            device_id_file_237.write(f"{str(int(row['device_id_no']))},")
        else:
            device_id_file_237.write(f"{str(int(row['device_id_no']))}")
        index += 1
    device_id_file_237.close()

    for index, row in df.iterrows():
        device_id_name = int(str(int(row['device_id_no'])))
        print(row['latitude'])
        print(row['longitude'])
        current_location = str(row['latitude']) + "," + str(row['longitude'])
        device_id_name = f"Prediction Device ID {device_id_name}"
        print(device_id_name)
        major_road = row['major_road']
        print(f"Major Road: {major_road}")

        data = {
            "name": device_id_name,
            "deviceIdNo": int(str(int(row['device_id_no']))),
            "location": current_location,
            "active": 1,
            "majorRoad": major_road
        }

        response = requests.post(iot_service_url, json=data)
        print(response)
