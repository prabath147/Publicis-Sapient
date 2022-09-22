import axiosInstance from "../../../app/axiosInstance";

//TODO: Update the api url
export const getOrders = (id: number) => 
    axiosInstance.get("/order/order/get-order-history/" + id);
