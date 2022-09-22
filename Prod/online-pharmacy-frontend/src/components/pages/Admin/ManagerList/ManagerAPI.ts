import axiosInstance from "../../../../app/axiosInstance";

export const getManagerList = (status:string,pageNumber:number,pageSize:number,name:string,sortBy:string|null) =>
  axiosInstance.get(`/admin/manager/get-manager-with-filter?status=${status}&pageNumber=${pageNumber}&pageSize=${pageSize}&name=${name}&sortBy=${sortBy}`);

export const approveManager = (id:number) =>
axiosInstance.put(`/admin/manager/approve/${id}`);

export const rejectManager = (id:number) =>
axiosInstance.put(`/admin/manager/reject/${id}`);
