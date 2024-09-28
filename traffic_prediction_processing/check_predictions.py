import pandas
from IPython.display import display
import pandas as pd
import numpy as np
from numpy import load
import h5py
import matplotlib.pyplot as plt

pd.set_option('display.max_colwidth', None)
# pd.set_option('display.max_rows', None)
# pd.set_option('display.max_columns', None)
pandas.set_option('display.expand_frame_repr', False)

if __name__ == "__main__":

    test_data = load('test.npz')
    print(test_data)
    print(f"test x data shape: {test_data['x'].shape}")
    print(f"test y data shape: {test_data['y'].shape}")
    # display(test_data['x'])
    # display(test_data['y'])






    print("Predictions: *****\n")
    data = load('680_dcrnn_predictions.npz')
    print(data['prediction'].shape)
    prediction_data = np.array(data['prediction'])
    truth_data = np.array(data['truth'])

    print(f"prediction data shape: {prediction_data.shape}")


    # plot each horizon

    # for horizon in range(0, len(prediction_data)):
    #     predictions_df = pd.DataFrame(data=prediction_data[horizon])
    #     print(f"Horizon DF {horizon}")
    #     display(predictions_df)
    #     predictions_df.plot(title=f'Predictions Horizon {horizon}', figsize=(16, 8), legend=True)
    #     plt.show()

    # get first horizon
    selected_ids = []
    with open("selected_sensors/device_ids_680.txt") as f:
        content = f.read()
        selected_ids = content.split(',')
    predictions_df = pd.DataFrame(data=prediction_data[5], columns=selected_ids)
    predictions_df.plot(title='Predictions One Horizon', figsize=(16, 8), legend=True)


    device_id_data_400665 = predictions_df[str(int('400665'))]
    display(device_id_data_400665)
    device_id_data_400665.to_csv('device_id_data_400665.csv')

    # first_sensor = predictions_df[0]
    # print(first_sensor.keys())
    # first_sensor.plot(title='Predictions One Sensor', figsize=(16, 8), legend=True)
    plt.show()


    # truth_df = pd.DataFrame(data=truth_data[0])
    # truth_df.plot(title='Ground Truth One Horizon', figsize=(16, 8), legend=True)
    # plt.show()
