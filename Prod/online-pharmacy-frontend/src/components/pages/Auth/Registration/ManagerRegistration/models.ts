export interface ManagerRegistrationRequest {
  username: string;
  email: string;
  password: string;
  role: ["manager"];

  detailObject: {
    name: string;
    phoneNo: number;
    licenseNo: number;
  };
}

export interface ManagerRegistrationForm {
  username: string;
  email: string;
  password: string;
  confirmPassword: string;
  name: string;
  phoneNo: number;
  licenseNo: number;
}
