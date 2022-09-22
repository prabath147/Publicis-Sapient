import axiosInstance from "../../../../app/axiosInstance";
import { OptInType } from "./models";

// export const getOptinListAPI = (userid: number) => axiosInstance.get("/order/optin/user/" + userid);

// export const getOptinDetailAPI = (optinId: number) =>
//   axiosInstance.get("api/optin/" + optinId);
export const getOptInByIdAPI = (id: number) =>
  axiosInstance.get("order/optin/" + id);

export const deleteOptInByIdAPI = (id: number) =>
  axiosInstance.delete("order/optin/delete/" + id);

export const optInCreateAPI = (data: OptInType) =>
  axiosInstance.post("order/optin/create-optin", data);

export const optInUpdateAPI = (data: OptInType) =>
  axiosInstance.put("order/optin/update-optin", data);

export const getOptInForUserAPI = (userId: number, pageNumber: number, pageSize: number) =>
  axiosInstance.get(`order/optin/user/${userId}?pageNumber=${pageNumber}&pageSize=${pageSize}`);
