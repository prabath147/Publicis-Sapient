import { Item } from "../../Manager/models";

export interface Test {
  data: Store[];
  pageNumber: number;
  pageSize: number;
  totalRecords: number;
  totalPages: number;
  isLastPage: boolean;
}


export interface Store {
  storeId: number;
  storeName: string;
  address: Address;
  createdDate: string;
  manager: Manager;
  revenue: number;
}

export interface Address {
  addressId: number;
  street: string;
  city: string;
  state: string;
  pinCode: number;
  country: string;
}

export interface Manager {
  managerId: number;
  name: string;
  phoneNo: string;
  licenseNo: string;
  registrationDate: string;
  approvalStatus: string;
}

export interface PageableResponse{
  "pageNumber":number,
  "pageSize":number,
  "totalRecords":number,
  "totalPages":number,
  "isLastPage":boolean
}

export interface getStoreInventoryResponse extends PageableResponse{
  data : Item[]
}


