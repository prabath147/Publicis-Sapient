import axiosInstance from "../../../../app/axiosInstance";


export const getSubscription = (pageNo:number,pageSize:number) => {
  return axiosInstance.get(`/admin/subscription/get-subscription?pageNumber=${pageNo}&pageSize=${pageSize}`);
};

export const subscribeAPI = (userId: number, id: number) =>
  axiosInstance.post(`/admin/subscriber/subscribe/${userId}`, id);

export const unsubscribeAPI = (userId: number, subscriptionId: number) => 
  axiosInstance.put(`/admin/subscriber/unsubscribe/${userId}`, subscriptionId);

  export const usersubscriptionAPI = (userId: number) => 
  axiosInstance.get(`/admin/subscriber/get-subscriber/${userId}`);