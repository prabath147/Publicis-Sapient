import axiosInstance from "../../../app/axiosInstance";
import { SubDetails } from "./models";


export const getSubscriptionvAPI = (pageNumber, pageSize) => {
  // const params = new URLSearchParams([['pageNumber', pageNumber_1.toString()]]);
  return axiosInstance.get(`/admin/subscription/get-subscription?pageNumber=${pageNumber}&pageSize=${pageSize}`);

  // const { data } = await axiosInstance.get(`/pharmacy/store/get-manager-stores/${managerId}?pageNumber=${pageNumber}&pageSize=${pageSize}`);
}

// export const activeSub = () =>
//   axiosInstance.get('/admin/subscription/get-subscription/by-status?status=ACTIVE');

export const ExpSub = (pageNumber, pageSize) =>
  axiosInstance.get(`/admin/subscription/get-subscription/by-status?status=EXPIRED&pageNumber=${pageNumber}&pageSize=${pageSize}`);

//  export const getSubscriptionvAPI = (arg:string | null) => 
//  {

//     if(arg === null)
//     {  
//       return axiosInstance.get("/admin/subscription/get-subscription");
//     }
//     else if(arg === "ACTIVE"){
//       return axiosInstance.get("/admin/subscription/get-subscription/by-status?status=ACTIVE");
//     }
//     else if(arg === "EXPIRED"){
//       return axiosInstance.get("/admin/subscription/get-subscription/by-status?status=EXPIRED");
//     }
//     else
//     {
//       return axiosInstance.get("/admin/subscription/get-subscription");
//     }

//  }

// export const getSubscriptionvAPI = () => async (dispatch) => {
//   try {

//       const { data } = await axiosInstance.get("/admin/subscription/allsubscription");
//       // const { data } = await axios.get(`http://localhost:8082/pharmacy/store/get-store?pageNumber=${pageNumber}&pageSize=${pageSize}`,{
//       //     headers: headers
//       // });

//       dispatch(loadData(data));

//   } catch (error) {
//       let errorMessage = "";
//       if (error instanceof Error) {
//           errorMessage = error.message
//       } else {
//           errorMessage = "Something went wrong!!";
//       }
//       showNotification({
//           message: errorMessage,
//           color: "red"
//       });
//   }
// }

export const register = (subDetails: SubDetails) => {
  return axiosInstance
    .post("/admin/subscription/create-subscription", subDetails);
}

export const sDelete = (subscription_id: number) => {
  axiosInstance.delete(`/admin/subscription/delete-subscription/${subscription_id}`);
}