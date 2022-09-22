import { showNotification } from "@mantine/notifications";
import { useEffect } from "react";
import { BrowserRouter } from "react-router-dom";
import { useAppDispatch, useAppSelector } from "./app/hooks";
import {
  getUserData,
  isCustomer,
  LoginAction
} from "./components/pages/Auth/Login/UserSlice";
import { getCartAPI } from "./components/pages/Cart/CartAPI";
import { loadCart } from "./components/pages/Cart/CartSlice";
import AppRouter from "./components/ui/appRouter/AppRouter";
import LocalUserService from "./services/LocalUserService";
import TokenService from "./services/TokenService";

function App() {
  const dispatch = useAppDispatch();

  const refresh_token = TokenService.getLocalRefreshToken();
  const token = TokenService.getLocalAccessToken();
  const user = LocalUserService.getLocalUser();
  const cuser = useAppSelector(getUserData);
  const isCustomerType = useAppSelector(isCustomer)


  if (refresh_token !== "") {
    dispatch({
      type: LoginAction,
      payload: {
        token: token,
        refreshToken: refresh_token,
        type: "Bearer",
        id: user.id,
        email: user.email,
        username: user.username,
        roles: [""]
      },
    });
  }
  useEffect(() => {
    if (cuser.id !== -1 && isCustomerType) {
      getCartAPI(cuser.id)
        .then((response) => {
          // console.log(response.data.items);
          dispatch(loadCart(response.data.items))
        })
        .catch((error) => {
          showNotification({ color: 'red', message: "oops, something went wrong" })
          console.log(error);
        });
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [cuser.id]);



  return (
    <div data-testid="app">
      <BrowserRouter>
        <AppRouter />
      </BrowserRouter>
    </div>
  );
}

export default App;
