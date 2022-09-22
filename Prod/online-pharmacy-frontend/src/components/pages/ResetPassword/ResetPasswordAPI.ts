import axiosInstance from "./../../../app/axiosInstance";
// import { useAppSelector } from '../../../app/hooks';
// import { getUserData } from "../Auth/Login/UserSlice";
import { ResetPasswordRequest } from "./model";

export const resetPasswordAPI = (userId: number, data:ResetPasswordRequest) =>
  axiosInstance.post(`api/auth/reset-password/${userId}`, data);

