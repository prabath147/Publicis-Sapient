import { AddressInterface } from "../../../ui/forms/AddressForm";
import { PageableResponse } from "../../Admin/storeList/models";
import { ProductMiniDTO } from "../../Cart/models";

export interface OptInType {
  address: AddressInterface;
  deliveryDate: Date | string;
  id: number;
  intervalInDays: number;
  name: string;
  numberOfDeliveries: number;
  repeatOrderItems: ProductMiniDTO[];
  userId: number;
}

// export interface Address {
//   addressId?: number;
//   city: string;
//   country: string;
//   pinCode: string|number;
//   state: string;
//   street: string;
// }

// export interface RepeatOrderItem {
//   price: number;
//   productId?: number;
//   productIdFk: number;
//   quantity: number;
// }

export interface OptInListResponse extends PageableResponse {
  data: OptInType[];
}
