import { showNotification } from '@mantine/notifications';
import axiosInstance from "../../../../app/axiosInstance";
import { load, loadStoreDetails } from '../ManagerSlice';

// const headers = {
//     'Content-Type': 'application/json',
//     'Authorization': token
// }

export const getStoreDetails = (storeId) => async (dispatch) => {
    try {

        dispatch(load());

        const { data } = await axiosInstance.get(`/pharmacy/store/get-store/${storeId}`);

        // const { data } = await axios.get(`http://localhost:8082/pharmacy/store/get-store/${storeId}`,{
        //     headers: headers
        // });

        console.log(data);

        dispatch(loadStoreDetails(data));

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
    }
}
export const deleteStore = (storeId) => axiosInstance.delete(`/pharmacy/store/delete-store-async/${storeId}`);

// export const deleteStore = (storeId) => axios.delete(`http://localhost:8082/pharmacy/store/delete-store/${storeId}`,{
//     headers: headers
// });