import axiosInstance from "../../../../app/axiosInstance";
import { Category } from "./models";

export const getCategoryList = (pageNumber: number, pageSize: number) =>
    axiosInstance.get(`/pharmacy/category/get-category?pageNumber=${pageNumber}&pageSize=${pageSize}`);

export const createCategory = (obj: Category) =>
    axiosInstance.post("/pharmacy/category/create-category", obj);

// export const updateCategory = (obj: Category) =>
//     axiosInstance.put("/pharmacy/category/update-category", obj);

export const deleteCategory = (id: number) =>
    axiosInstance.delete(`/pharmacy/category/delete-category/${id}`);
