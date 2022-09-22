import axiosInstance from "../../../app/axiosInstance";

export const getOrderDetailsById = (orderId: number) =>
    axiosInstance.get("/order/order/get-order-details/" + orderId);
