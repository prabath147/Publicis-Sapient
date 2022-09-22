import { showNotification } from '@mantine/notifications';
import axiosInstance from "../../../../app/axiosInstance";
import { loadInventory, setMessage } from '../ManagerSlice';
// export const getStoreByManagerIdAPI = (data:Object) =>
//   axiosInstance.post("/pharmacy/store/create-store", data);
// const headers = {
//     'Content-Type': 'application/json',
//     'Authorization': token
// }

export const getInventory = (storeId, keyword, order, pageNumber, pageSize) => async (dispatch) => {
    try {

        // dispatch(load());

        const { data } = await axiosInstance.get(`/pharmacy/store/get-store-items-sorted/${storeId}?pageNumber=${pageNumber}&pageSize=${pageSize}&keyword=${keyword}&order=${order}`);
        // const { data } = await axios.get(`http://localhost:8082/pharmacy/store/get-store-items/${storeId}?pageNumber=${pageNumber}&pageSize=${pageSize}`,{
        //     headers: headers
        // });

        dispatch(loadInventory(data));

    } catch (error) {
        let errorMessage = "";
        if (error instanceof Error) {
            errorMessage = "Something went wrong!!"
        } else {
            errorMessage = "Something went wrong!!";
        }
        showNotification({
            message: errorMessage,
            color: "red"
        });
        dispatch(setMessage({
            type: "error",
            text: errorMessage
        }))
    }
}

export const uploadInventorySheet = (file: File | null, storeId: number) => async (dispatch) => {
    try {

        // dispatch(load());
        const b = file as File;
        const formData = new FormData();
        formData.append("excelInventoryData", b);


        // const { data } = await axios.post(`http://localhost:8082/pharmacy/store/add-inventory/${storeId}`,formData,{
        //     headers: {
        //         "Content-Type": "multipart/form-data",
        //         'Authorization': token
        //     }
        // });
        const { data } = await axiosInstance.post(`/pharmacy/store/add-inventory/${storeId}`, formData, {
            headers: {
                "Content-Type": "multipart/form-data",
                // 'Authorization': token
            }
        });

        dispatch(getInventory(storeId, "itemQuantity", "desc", 0, 10));

        showNotification({
            message: data,
            color: "green"
        });

    } catch (error) {
        let errorMessage = "Something went wrong!!";
        // if (error instanceof Error) {
        //     errorMessage = error.message
        // } else {
        //     errorMessage = "Something went wrong!!";
        // }
        showNotification({
            message: errorMessage,
            color: "red"
        });

    }
}
export const updateInventorySheet = (file: File | null, storeId: number) => async (dispatch) => {
    try {

        // dispatch(load());
        const b = file as File;
        const formData = new FormData();
        formData.append("excelInventoryData", b);


        // const { data } = await axios.post(`http://localhost:8082/pharmacy/store/update-inventory/${storeId}`,formData,{
        //     headers: {
        //         "Content-Type": "multipart/form-data",
        //         'Authorization': token
        //     }
        // });
        const { data } = await axiosInstance.post(`/pharmacy/store/update-inventory/${storeId}`, formData, {
            headers: {
                "Content-Type": "multipart/form-data",
                // 'Authorization': token
            }
        });

        dispatch(getInventory(storeId, "itemQuantity", "desc", 0, 10));

        showNotification({
            message: data,
            color: "green"
        });

    } catch (error) {
        let errorMessage = "Something went wrong!!";
        // if (error instanceof Error) {
        //     errorMessage = error.message
        // } else {
        //     errorMessage = "Something went wrong!!";
        // }
        showNotification({
            message: errorMessage,
            color: "red"
        });

    }
}

// export const getExcelTemplate = (storeId: number) =>
// axios.get(`http://localhost:8082/pharmacy/store/get-store-inventory/${storeId}`,{
//     responseType: 'arraybuffer',
//     headers: {
//         'Content-Type': 'application/json',
//         'Accept': 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet',
//         'Authorization': token
//     }
// });
export const getExcelTemplate = (storeId: number) =>
    axiosInstance.get(`/pharmacy/store/get-store-inventory/${storeId}`, {
        responseType: 'arraybuffer',
        headers: {
            'Content-Type': 'application/json',
            'Accept': 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet',
            // 'Authorization': token
        }
    });


export const addItem = (storeId, itemData) => async (dispatch) => {
    try {

        // dispatch(load());

        await axiosInstance.post(`/pharmacy/item/create-item/${storeId}`, itemData);
        // const { data } = await axios.post(`http://localhost:8082/pharmacy/item/create-item/${storeId}`,itemData,{
        //     headers: headers
        // });

        dispatch(getInventory(storeId, "itemQuantity", "desc", 0, 10));
        showNotification({
            message: "Item added successfully",
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
export const updateItem = (storeId, itemData) => async (dispatch) => {
    try {

        // dispatch(load());

        await axiosInstance.put(`/pharmacy/item/update-item`, itemData);
        // const { data } = await axios.put(`http://localhost:8082/pharmacy/item/update-item`,itemData,{
        //     headers: headers
        // });

        dispatch(getInventory(storeId, "itemQuantity", "desc", 0, 10));
        showNotification({
            message: "Item updated successfully",
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

export const deleteItem = (storeId, itemId) => async (dispatch) => {
    try {

        // dispatch(load());

        await axiosInstance.delete(`/pharmacy/item/delete-item/${itemId}`);
        // const { data } = await axios.delete(`http://localhost:8082/pharmacy/item/delete-item/${itemId}`,{
        //     headers: headers
        // });

        dispatch(getInventory(storeId, "itemQuantity", "desc", 0, 10));
        showNotification({
            message: "Item deleted successfully",
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
export const searchProduct = (productName) => axiosInstance.get(`/pharmacy/product/get-product/`);

// export const searchProduct = (productName) =>  axios.get(`http://localhost:8082/pharmacy/product/get-product/${productName}`,{
//     headers: headers
// });


// dispatch(load());

// const { data } = await axiosInstance.get(`/pharmacy/product/get-product/${productName}}`);

export const getItemData = (itemId: number) => axiosInstance.get(`/pharmacy/item/get-item/${itemId}`);
export const getProduct = (productId: number) => axiosInstance.get(`/pharmacy/product/get-product/${productId}`);
export const registerProduct = (product) => axiosInstance.post("/pharmacy/product/create-product", product);
export const getCategories = () => axiosInstance.get("/pharmacy/category/get-category");


export const getSuggestions = (kword: string) => {
    return axiosInstance.get("/pharmacy/search/get-suggestions", {
        params: {
            productName: kword,
        },
    });
}


export const getItemAPI = (
    keyword: string,
    storeId: string

) => {
    return axiosInstance.get(`/pharmacy/search/search-items-exact-match/${storeId}?pageNumber=${0}&pageSize=${10}&productName=${keyword}`);
};

export const getStoreInventory = (storeId) => axiosInstance.get(`/pharmacy/store/get-store-inventory/${storeId}`);