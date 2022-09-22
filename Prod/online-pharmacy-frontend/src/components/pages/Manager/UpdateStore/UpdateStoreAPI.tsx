import { showNotification } from '@mantine/notifications';
import axiosInstance from "../../../../app/axiosInstance";
import { load, loadStoreDetails, setMessage } from '../ManagerSlice';
import { Store } from '../models';

// // const headers = {
// //     'Content-Type': 'application/json',
// //     'Authorization': token
// // }

export const updateStore = (storeData: Store) => async (dispatch) => {
  try {

    dispatch(load());

    const { data } = await axiosInstance.put(`/pharmacy/store/update-store`, storeData);
    // const { data } = await axios.put(`http://localhost:8082/pharmacy/store/update-store`,storeData,{
    //     headers: headers
    // });

    dispatch(loadStoreDetails(data));
    showNotification({
      message: "Store updated successfully",
      color: "green"
    });

  } catch (error) {
    // let errorMessage = "";
    // if (error instanceof Error) {
    //     errorMessage = error.message
    // } else {
    //     errorMessage = "Something went wrong!!";
    // }
    showNotification({
      message: "Something went wrong!!",
      color: "red"
    });
    dispatch(setMessage({ text: "Invalid input", type: "error" }))
  }
}