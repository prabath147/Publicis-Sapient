import { showNotification } from "@mantine/notifications";
import axiosInstance from "../../../../app/axiosInstance";
import { createStore, load, loadStoreList } from '../ManagerSlice';
// export const getStoreByManagerIdAPI = (data:Object) =>
//   axiosInstance.post("/pharmacy/store/create-store", data);
// const headers = {
//     'Content-Type': 'application/json',
//     'Authorization': token
// }

export const getAllStoresBymanagerId = (id, pageNumber, pageSize) => async (dispatch) => {
    try {

        dispatch(load());

        const { data } = await axiosInstance.get(`/pharmacy/store/get-manager-stores/${id}?pageNumber=${pageNumber}&pageSize=${pageSize}`);
        // const { data } = await axios.get(`http://localhost:8082/pharmacy/store/get-store?pageNumber=${pageNumber}&pageSize=${pageSize}`,{
        //     headers: headers
        // });

        dispatch(loadStoreList(data));

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

export const createNewStore = (storeData) => async (dispatch) => {
    try {

        dispatch(load());

        const { data } = await axiosInstance.post(`/pharmacy/store/create-store`, storeData);
        // const { data } = await axios.post(`http://localhost:8082/pharmacy/store/create-store`,storeData,{
        //     headers: headers
        // });

        dispatch(createStore(data));

        showNotification({
            message: "New store created",
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
    }
}