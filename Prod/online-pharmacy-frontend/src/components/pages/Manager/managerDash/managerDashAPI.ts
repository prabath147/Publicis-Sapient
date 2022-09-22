import axiosInstance from "../../../../app/axiosInstance";

export const getStoreCnt = (managerId: number) =>
    axiosInstance.get(`/pharmacy/store/total-stores/${managerId}`);

export const getRev = (managerId: number) =>
    axiosInstance.get(`/pharmacy/store/total-revenue/${managerId}`);