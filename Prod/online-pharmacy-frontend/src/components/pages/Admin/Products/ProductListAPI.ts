import axiosInstance from "../../../../app/axiosInstance";

export const getProductList = (pageNumber:number,pageSize:number) =>{
    return axiosInstance.get(`/pharmacy/product/get-product?pageNumber=${pageNumber}&pageSize=${pageSize}`);
}
export const getProductDetailsAPI = (id) =>{
    console.log(id);
    return axiosInstance.get(`/pharmacy/product/get-product/${id}`);
}

export const registerProduct = (product) =>{
    return axiosInstance.post("/pharmacy/product/create-product", product);
}
