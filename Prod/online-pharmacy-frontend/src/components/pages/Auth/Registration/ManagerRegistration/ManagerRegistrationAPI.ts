import { ManagerRegistrationRequest } from "./models";

import axiosInstance from "../../../../../app/axiosInstance";

export const userRegister = (user: ManagerRegistrationRequest) =>
  axiosInstance.post("/api/auth/signup", user);
