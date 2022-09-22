import axiosInstance from "../../../app/axiosInstance";
import { OrderDetail } from "./models";

export const orderSetAPI = (userId: number, data: OrderDetail) =>
    axiosInstance.post("order/order/set-order-details/" + userId, data);

export const orderCreateAPI = (data: OrderDetail) =>
    axiosInstance.post("order/order/place-order", data);

export const orderForUserAPI = (userId: number, pageNumber: number, pageSize: number) =>
    axiosInstance.get(`order/optin/user/${userId}?pageNumber=${pageNumber}&pageSize=${pageSize}`);
