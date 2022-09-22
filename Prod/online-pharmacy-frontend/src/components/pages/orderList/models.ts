import { AddressInterface } from "../../ui/forms/AddressForm";
import { ItemMiniDTO } from "../Cart/models";




export interface Order {
  items: ItemMiniDTO[];
  optionalOrderDetails: boolean;
  orderAddress: AddressInterface;
  orderDate: Date;
  orderDetails?: OrderDetails;
  orderId: number;
  price: number;
  quantity: number;
  userId: number;
}

export interface OrderDetails {
  orderDetailsId: number;
}