import pandas
from IPython.display import display
import pandas as pd
import requests

pd.set_option('display.max_colwidth', None)
pd.set_option('display.max_rows', None)
pd.set_option('display.max_columns', None)
pandas.set_option('display.expand_frame_repr', False)

iot_service_url = "http://localhost:8080/api/iot/v1"

if __name__ == "__main__":
    df = pd.read_csv('graph_sensor_locations_bay.csv')
    for index, row in df.iterrows():
        device_id_name = int(row['device_id_no'])
        print(row['latitude'])
        print(row['longitude'])
        current_location = str(row['latitude']) + "," + str(row['longitude'])
        device_id_name = f"Prediction Device ID {device_id_name}"
        print(device_id_name)

        data = {
            "name": device_id_name,
            "deviceIdNo": int(row['device_id_no']),
            "location": current_location,
            "active": 1
        }

        response = requests.post(iot_service_url, json=data)
        print(response)