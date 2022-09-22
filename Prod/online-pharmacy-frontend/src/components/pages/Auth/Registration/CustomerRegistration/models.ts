import { AddressInterface } from './../../../../ui/forms/AddressForm';

export interface CustomerRegistrationRequest {
  username: string;
  email: string;
  password: string;
  role: ["customer"];

  detailObject: {
    fullName: string;
    mobileNumber: number;
    address: AddressInterface
  };
}

export interface CustomerRegistrationForm {
  fullName: string;
  username: string;
  mobileNumber: number;
  email: string;
  password: string;
  confirmPassword: string;
  address: AddressInterface
}
