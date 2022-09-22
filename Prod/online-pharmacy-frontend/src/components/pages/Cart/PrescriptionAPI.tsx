import { showNotification } from "@mantine/notifications";
import axiosInstance from "../../../app/axiosInstance";
import { loadCart } from "./CartSlice";
import { PrescriptionItem } from "./models";

export const searchItemAPI = async (item_name: string) => {
  return axiosInstance.get("/pharmacy/search/items", {
    params: {
      pageNumber: 0,
      pageSize: 1,
      productName: item_name,
      sortByPrice: "asc",
      productType: true,
    },
  });
};
export const searchItemsAPI = async (items: Array<string>) => {
  let output: Array<PrescriptionItem> = [];
  items.forEach(async (item) => {
    let item_splt = item.split(" ");

    let name = item_splt
      .slice(0, item_splt.length - 1)
      .join(" ")
      .trim();
    let quantity = item_splt[item_splt.length - 1];

    console.log(name, quantity);
    if (name !== "" && name !== "Medicine") {
      searchItemAPI(name)
        .then((response) => {
          console.log(response.data.content);
          if (response.data.content.length !== 0) {
            output.push({
              id: response.data.content[0].id,
              name: response.data.content[0].productName,
              quantity: Number(quantity),
              price: response.data.content[0].price,
            });
          } else {
            showNotification({
              message: "Product " + name + " not found",
              color: "red",
            });
          }
        })
        .catch((error) => {
          console.log(error);
        });
    }
  });

  // console.log(output);

  return output;
};

export const AddToPrescriptionCart = async (
  id: number,
  items: Array<PrescriptionItem>,
  dispatch
) => {
  let request: Array<request> = [];
  items.forEach((item) => {
    request.push({
      itemId: 0,
      itemIdFk: item.id,
      itemQuantity: item.quantity,
      price: item.price,
    });
  });

  axiosInstance
    .post(`/order/cart/add-prescription-to-cart/${id}`, request)
    .then((response) => {
      dispatch(loadCart(response.data));
      console.log(response);
    });
};

interface request {
  itemId: 0;
  itemIdFk: number;
  itemQuantity: number;
  price: number;
}
