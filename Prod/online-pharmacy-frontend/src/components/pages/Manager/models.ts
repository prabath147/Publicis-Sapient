
export class Product {
  productId!: number
  proprietaryName!: string
  productName!: string
  description!: string
  dosageForm!: string
  categorySet!: string[]
  quantity!: number
  imageUrl!: string
  productType!: boolean
}

export class Item {
  itemId!: number
  price!: number
  itemQuantity!: number
  manufacturedDate!: string
  expiryDate!: string
  product: Product = new Product()
}

export class InventoryPage {
  data: Item[] = [];
  pageNumber!: number;
  pageSize!: number;
  totalRecords!: number;
  totalPages!: number;
  isLastPage = false;
}
export class Manager {
  managerId!: number;
  name!: string;
  phoneNo!: string;
  licenseNo!: string;
  registrationDate!: string;
  approvalStatus!: string;
}
export class Store {
  storeId!: number;
  storeName!: string;
  address!: {
    addressId: number;
    street: string;
    city: string;
    state: string;
    pinCode: number;
    country: string;
  };
  manager: Manager = new Manager();
  createdDate!: string;
  revenue!: number;
}
export class StoreListPage {
  data: Store[] = [];
  pageNumber!: number;
  pageSize!: number;
  totalRecords!: number;
  totalPages!: number;
  isLastPage = false;
}
export class Message {
  type!: string;
  text!: string;
}
export class StoreInSlicetype {
  storeListPage: StoreListPage = new StoreListPage();
  store: Store = new Store();
  message: Message = new Message() || null;
  loading = false;
  inventory: InventoryPage = new InventoryPage();
}
const token = 'Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0ZXN0TWFuYWdlciIsImlhdCI6MTY2MjU0MDE0NSwiZXhwIjoxNjYyNTQzNzQ1LCJSb2xlIjoiUk9MRV9NQU5BR0VSIn0.u8AHQMCBX40BtRh4BTM8c-dMxBaVXLFcsOIcPIn9_r16T8JrVH3LztckPoMV5jxcBW530s7P-0BPb4Qcs9Qlqg';
export default token;
// export class Store implements IStore{
//   storeId!: number;
//   storeName!: string;
//   address!: { street: string; city: string; state: string; pincode: number; country: string; };
//   createdDate!: Date;
//   revenue!: number;

// }