export interface User {
  id: number;
  username: string;
  email: string;
  role: string;
}

export interface LoginRequest {
  username: string;
  password: string;
}

export interface LoginResponse {
  id: number;
  username: string;
  email: string;

  token: string;
  refreshToken: string;
}

export interface tokenStructure {
  sub: string;
  role: string;
  iat: number;
  exp: number;
  id:number;
}
