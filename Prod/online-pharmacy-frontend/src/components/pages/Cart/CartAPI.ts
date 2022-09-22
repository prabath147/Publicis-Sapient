import axiosInstance from "../../../app/axiosInstance";
import { ItemMiniDTO } from "./models";

export const getCartAPI = (id: number) => {
  return axiosInstance.get("order/cart/get-cart/" + id);
};

export const getItemDetailAPI = (item_fk_id: number) => {
  return axiosInstance.get("pharmacy/item/get-item/" + item_fk_id)
}

export const getProductDetailAPI = (productId: number) => {
  return axiosInstance.get("/pharmacy/product/get-product/" + productId)
}

export const addToCartAPI = (id: number, data: ItemMiniDTO) => {
  const purl = "/order/cart/add-to-cart/" + id;
  return axiosInstance.post(purl, data);
};

export const increamentInCartAPI = (id: number, data: ItemMiniDTO) => {
  const purl = "/order/cart/add-item/" + id;
  return axiosInstance.post(purl, data);
};

export const decreamentInCartAPI = (id: number, data: ItemMiniDTO) => {
  const purl = "/order/cart/subtract-item/" + id;
  return axiosInstance.post(purl, data);
};

export const EmptyCartAPI = (id: number) => {
  const purl = "/order/cart/empty-cart/" + id;
  return axiosInstance.delete(purl);
};

// export const removeFromCartAPI = (id: number, data: ItemMiniDTO) => {
//   const purl = "/order/cart/remove-from-cart/" + id;
//   return axiosInstance.put(purl, data);
// };