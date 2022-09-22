export interface ResetPasswordForm {
    oldPassword: string;
    newPassword: string;
    confirmPassword: string;
}

export interface ResetPasswordRequest {
    oldPassword: string;
    newPassword: string;
  }