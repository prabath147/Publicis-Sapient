import axiosInstance from "../../../app/axiosInstance";

//TODO: Update the api url
export const getProduct = (productId: number) => axiosInstance.get(`/pharmacy/product/get-product/${productId}`);
export const updateProduct = (product) => axiosInstance.put("/pharmacy/product/update-product", product);
export const getCategories = () => axiosInstance.get("/pharmacy/category/get-category");
