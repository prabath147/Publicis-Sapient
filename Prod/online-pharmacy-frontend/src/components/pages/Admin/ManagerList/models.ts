import { PageableResponse } from "../storeList/models";

export interface Manager 
{
  managerId: number;
  name: string;
  phoneNo: string;
  licenseNo: string;
  registrationDate: Date;
  approvalStatus: "APPROVED" | "PENDING" | "REJECTED";
}


export interface ManagerListResponse extends PageableResponse{
  data:Manager[]
}