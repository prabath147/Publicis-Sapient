import axiosInstance from "../../../app/axiosInstance";

export const logOutAPI = () => axiosInstance.post("api/auth/signout");