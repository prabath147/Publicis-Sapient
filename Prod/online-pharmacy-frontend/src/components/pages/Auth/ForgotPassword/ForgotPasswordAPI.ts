import axiosInstance from "../../../../app/axiosInstance";
import { ForgotPasswordRequest } from "./model";

export const sendPasswordResetLink = (email: string) =>
  axiosInstance.post(`api/auth/forget-password-email?email=${email}`);

export const validToken = (code: string) =>
  axiosInstance.get(`api/auth/valid-forget-password-token?code=${code}`);

export const forgotPassword = (code: string, data:ForgotPasswordRequest) =>
  axiosInstance.post(`api/auth/forget-password?code=${code}`, data);