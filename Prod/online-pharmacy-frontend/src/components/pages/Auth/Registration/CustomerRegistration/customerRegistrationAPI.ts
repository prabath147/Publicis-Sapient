import { CustomerRegistrationRequest } from "./models";

import axiosInstance from "../../../../../app/axiosInstance";

export const userRegister = (user: CustomerRegistrationRequest) =>
  axiosInstance.post("/api/auth/signup", user);
