import axiosInstance from "../../../../app/axiosInstance";
import { Store } from '../storeList/models';

export const getStoreList = (pageNumber: number, pageSize: number) =>
  axiosInstance.get(`/admin/store?pageNumber=${pageNumber}&pageSize=${pageSize}`);

export const getStoreList_count = () =>
  axiosInstance.get(`/admin/store?isLastPage=true`);

export const deleteStore = (storeId: number) =>
  axiosInstance.delete(`pharmacy/store/delete-store-async/${storeId}`);

export const updateStore = (storeData: Store) =>
  axiosInstance.put(`/pharmacy/store/update-store`, storeData);

export const getStoreInventory = (storeId: number, pageNumber: number, pageSize: number) =>
  axiosInstance.get(`/pharmacy/store/get-store-items/${storeId}?pageNumber=${pageNumber}&pageSize=${pageSize}`);
