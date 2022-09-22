import { createSlice, PayloadAction } from "@reduxjs/toolkit";
import { decodeToken } from "react-jwt";
import SockJS from "sockjs-client";
import * as Stomp from "stompjs";
import { socketURL } from "../../../../app/socketInstance";
import { RootState } from "../../../../app/store";
import LocalUserService from "../../../../services/LocalUserService";
import TokenService from "../../../../services/TokenService";
import { connectToSocket, disconnectSocket } from "../../Open/notificationList/SocketUtils";
import { LoginResponse, tokenStructure, User } from "./models";

const initialState: User = {
  id: -1,
  username: "",
  email: "",
  role: "",
};

var socket;
var stompClient;

const UserSlice = createSlice({
  name: "user",
  initialState,
  reducers: {
    LoginAction: (state: User, actions: PayloadAction<LoginResponse>) => {
      // check if refresh token is expired
      // TODO comment out following in production
      // if (isExpired(actions.payload.refreshToken)) {
      //   // remove locally stored data
      //   TokenService.removeLocalAccessToken();
      //   TokenService.removeLocalRefreshToken();
      //   LocalUserService.removeLocalUser();
      //   console.warn("refresh token expired");

      //   return;
      // }



      // decode access token
      const myDecodedToken: tokenStructure | null = decodeToken(
        actions.payload.token
      );

      // console.log("t1", actions.payload.token)

      // console.log("t1",myDecodedToken);


      if (myDecodedToken === null) {
        console.warn("Invalid Access token");
        return;
      }

      // save data from action
      state.role = myDecodedToken.role;
      state.username = myDecodedToken.sub;
      state.id = myDecodedToken.id;
      state.email = actions.payload.email;

      // save data to local storage
      LocalUserService.setLocalUser({
        id: myDecodedToken.id,
        username: myDecodedToken.sub,
        email: actions.payload.email,
        role: myDecodedToken.role,
      });

      // save tokens to local storage
      TokenService.updateLocalAccessToken(actions.payload.token);
      TokenService.updateLocalRefreshToken(actions.payload.refreshToken);

      if (stompClient == null) {
        socket = new SockJS(socketURL, null, { transports: 'xhr-polling' });
        stompClient = Stomp.over(socket);
        connectToSocket(stompClient, actions.payload.token);
      }

      // console.log("User Logged In");
    },
    LogoutAction: (state: User) => {
      state.username = "";
      state.email = "";
      state.id = -1;
      state.role = "";

      LocalUserService.removeLocalUser();
      TokenService.removeLocalAccessToken();
      TokenService.removeLocalRefreshToken();

      disconnectSocket(stompClient);

      // console.log("user Logged Out");
    },
  },
});

export const getUserData = (state: RootState): User => ({
  username: state.user.username,
  id: state.user.id,
  role: state.user.role,
  email: state.user.email,
});

export const isLoggedIn = (state: RootState): boolean =>
  state.user.id === -1 ? false : true;

export const isAdmin = (state: RootState): boolean =>
  state.user.id === -1 || state.user.role !== "ROLE_ADMIN" ? false : true;

export const isManager = (state: RootState): boolean =>
  state.user.id === -1 || state.user.role !== "ROLE_MANAGER" ? false : true;

export const isCustomer = (state: RootState): boolean =>
  state.user.id === -1 || state.user.role !== "ROLE_CUSTOMER" ? false : true;

export const { LoginAction, LogoutAction } = UserSlice.actions;

export default UserSlice.reducer;
