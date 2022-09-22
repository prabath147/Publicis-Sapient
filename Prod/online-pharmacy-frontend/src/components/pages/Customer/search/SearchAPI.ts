import axiosInstance from "../../../../app/axiosInstance";

// const baseURL =
//   "http://localhost:8082/pharmacy/";

// const axiosInstance = axios.create({
//   baseURL: baseURL,
//   timeout: 10000,
//   headers: {
//     "Content-Type": "application/json",
//     accept: "application/json",
//     authorization : 'Bearer pharmacySecretKey'
//   },
// });

export const getAllItems = (checked: boolean,sortByPrice : string,pageNumber : number) => {
  return axiosInstance.get("pharmacy/search/all-items",{
    params: {
      pageNumber,
      pageSize: 10,
      productType: checked,
      sortByPrice,
    }
  });
};

export const getSuggestions = (kword : string) => {
  return axiosInstance.get("pharmacy/search/get-suggestions",{
    params: {
      productName: kword,
    },
  });
}

export const getItemsAPI = (
  keyword: string,
  pageNumber: number,
  sortByPrice: string,
  checked: boolean
) => {
  return axiosInstance.get("pharmacy/search/items", {
    params: {
      pageNumber,
      pageSize: 10,
      productName: keyword,
      productType: checked,
      sortByPrice,
    },
  });
};
