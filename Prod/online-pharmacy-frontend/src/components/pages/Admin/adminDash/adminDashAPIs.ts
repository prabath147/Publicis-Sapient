import axiosInstance from "../../../../app/axiosInstance";

export const getManagerList = () =>
  axiosInstance.get(`/pharmacy/manager/total-managers/approved`);



export const getStoreList_count = () =>
axiosInstance.get(`/admin/store`);

export const activeSub = () =>
    axiosInstance.get('/admin/subscription/get-subscription/by-status?status=ACTIVE');

export const activeCustomers = () =>
    axiosInstance.get('/order/user-details/get-user-count');

export const orders_count = () =>
    axiosInstance.get('/order/order/total-orders');