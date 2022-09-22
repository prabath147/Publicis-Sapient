import axios, { AxiosRequestConfig } from "axios";
import LocalUserService from "../services/LocalUserService";
import TokenService from "../services/TokenService";
import { baseURL } from "./baseUrl";

const axiosInstance = axios.create({
  baseURL: baseURL,
  timeout: 5000,
  headers: {
    "Content-Type": "application/json",
    accept: "application/json",
  },
});

axiosInstance.interceptors.request.use(
  (config: AxiosRequestConfig) => {
    const token = TokenService.getLocalAccessToken();
    if (token && config.headers !== undefined) {
      config.headers["Authorization"] = "Bearer " + token; // for Spring Boot back-end
      // config.headers["x-access-token"] = token; // for Node.js Express back-end
      config.headers["ngrok-skip-browser-warning"] = "";
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

axiosInstance.interceptors.response.use(
  (res) => {
    return res;
  },
  async (err) => {
    const originalConfig = err.config;
    if (originalConfig.url !== "api/auth/signin" && err.response) {
      // Access Token was expired
      if (err.response.status === 401 && !originalConfig._retry) {
        originalConfig._retry = true;
        try {
          const rs = await axiosInstance.post("api/auth/refresh-token", {
            refreshToken: TokenService.getLocalRefreshToken(),
          });
          const { accessToken } = rs.data;
          TokenService.updateLocalAccessToken(accessToken);
          return axiosInstance(originalConfig);
        } catch (_error) {
          TokenService.removeLocalAccessToken()
          TokenService.removeLocalRefreshToken()
          LocalUserService.removeLocalUser()
          // window.location.reload();
          return Promise.reject(_error);
        }
      }
    }
    return Promise.reject(err);
  }
);
export default axiosInstance;
