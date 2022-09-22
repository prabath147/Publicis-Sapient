import { Action, configureStore, ThunkAction } from "@reduxjs/toolkit";
import StoreReducer from "../components/pages/Admin/storeList/StoreSlice";
import SubscriptionReducer from "../components/pages/AdminSubscriptionPack/SubscriptionvSlice";
import UserReducer from "../components/pages/Auth/Login/UserSlice";
import CartReducer from "../components/pages/Cart/CartSlice";
import ManagerReducer from "../components/pages/Manager/ManagerSlice";
import NotificationReducer from "../components/pages/Open/notificationList/NotificationSlice";
// import OrderSlice from "../components/pages/orderDetail/OrderSlice";
// import OrderListSlice from "../components/pages/orderList/OrderListSlice";

export const store = configureStore({
  reducer: {
    storeL: StoreReducer,
    user: UserReducer,
    notification: NotificationReducer,
    viewsubscription: SubscriptionReducer,
    // addsubscription: AddSubcriptionRe,
    cart: CartReducer,
    // order: OrderSlice,
    // orderL: OrderListSlice,
    manager: ManagerReducer,
  },
  middleware: (getDefaultMiddleware) =>
    getDefaultMiddleware({
      serializableCheck: false,
    }),
});

export type AppDispatch = typeof store.dispatch;
export type RootState = ReturnType<typeof store.getState>;
export type AppThunk<ReturnType = void> = ThunkAction<
  ReturnType,
  RootState,
  unknown,
  Action<string>
>;
