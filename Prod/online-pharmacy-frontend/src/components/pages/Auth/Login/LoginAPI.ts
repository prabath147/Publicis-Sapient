import axiosInstance from "../../../../app/axiosInstance";
import { LoginRequest } from "./models";

export const loginAPI = (data: LoginRequest) =>
  axiosInstance.post("api/auth/signin", data);
