import axiosInstance from "../../../../app/axiosInstance";

export const getNotificationsAPI = (userId:number, pageSize:number, activePage:number, sortBy:string, sortOrder:string) => axiosInstance.get("notify/notification/"+userId, { params: { pageNumber: activePage, pageSize:pageSize, sortBy:sortBy, sortOrder:sortOrder } });
export const toggleNotificationStatus = (id:number) => axiosInstance.put("notify/notification/toggle-status/"+id);