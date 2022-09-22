import axiosInstance from "../../../../app/axiosInstance";

export const addStoreAPI = (data: any) =>
  axiosInstance.post("/pharmacy/store/create-store", data);
