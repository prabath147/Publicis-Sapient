import { AddressInterface } from "../../ui/forms/AddressForm";
import { PageableResponse } from "../Admin/storeList/models";
import { ItemMiniDTO } from "../Cart/models";



export interface OrderDetail {
    items: ItemMiniDTO[];
    optionalOrderDetails: boolean;
    orderAddress: AddressInterface;
    orderDate: Date;
    deliveryDate: Date;
    orderDetails: Object;
    orderId?: number;
    price: number;
    quantity: number;
    userId: number;
}

export interface OrderHistory extends PageableResponse {
    data: OrderDetail[]
}

// export interface OrderDetails {
//     orderDetailsId: number;
// }
