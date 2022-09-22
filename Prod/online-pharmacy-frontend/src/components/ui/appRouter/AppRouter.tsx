import { Route, Routes } from "react-router-dom";
import AdminDash from "../../pages/Admin/adminDash/adminDash";
import CategoryList from "../../pages/Admin/categoryList/CategoryList";
import ManagerList from "../../pages/Admin/ManagerList/ManagerList";
import ProductDetails from "../../pages/Admin/Products/ProductDetails";
import ProductPage from "../../pages/Admin/Products/ProductPage";
import StoreDetails from '../../pages/Admin/StoreDetails/StoreDetails';
import StoreList from "../../pages/Admin/storeList/StoreList";
import SubscriptionView from "../../pages/AdminSubscriptionPack/SubscriptionView";
import ForgotPassword from '../../pages/Auth/ForgotPassword/ForgotPassword';
import ForgotPasswordEmail from '../../pages/Auth/ForgotPassword/ForgotPasswordEmail';
import TokenExpired from '../../pages/Auth/ForgotPassword/TokenExpired';
import Login from "../../pages/Auth/Login/Login";
import CustomerRegistration from "../../pages/Auth/Registration/CustomerRegistration/CustomerRegistration";
import ManagerRegistration from "../../pages/Auth/Registration/ManagerRegistration/ManagerRegistration";
import RegistrationOptions from "../../pages/Auth/Registration/RegistrationOptions/RegistrationOptions";
import Cart from "../../pages/Cart/Cart";
import Prescrption from "../../pages/Cart/Prescrption";
import About from "../../pages/Customer/about/About";
import CustomerLandingPage from "../../pages/Customer/customerLandingPage/customerLandingPage";
import OptInCreate from "../../pages/Customer/OptInCreate/OptInCreate";
import OptinList from "../../pages/Customer/OptinList/OptinList";
import OptInUpdate from "../../pages/Customer/OptInUpdate/OptInUpdate";
import RegisterSubscriptionPage from "../../pages/Customer/RegisterSubscriptionPage/RegisterSubscriptionPage";
import Subscriptions from "../../pages/Customer/RegisterSubscriptionPage/Subscriptions";
import SearchPage from "../../pages/Customer/search/SearchPage";
import AddStore from "../../pages/Manager/addStore/addStore";
import ItemPage from "../../pages/Manager/Inventory/ItemPage";
import ManagerDash from "../../pages/Manager/managerDash/managerDash";
import ManagerStoreList from "../../pages/Manager/ManagerStoreList/ManagerStoreList";
import PendingManagerLandingPage from "../../pages/Manager/PendingManagerLandingPage/PendingManagerLandingPage";
import StorePage from "../../pages/Manager/StorePage/StorePage";
import LandingPage from "../../pages/Open/landingPage/LandingPage";
import NotAllowed from "../../pages/Open/NotAllowed/NotAllowed";
import NotificationList from "../../pages/Open/notificationList/NotificationList";
import PageNotFound from "../../pages/Open/PageNotFound/PageNotFound";
import OrderCreatePage from "../../pages/OrderCreatePage/OrderCreatePage";
import OrderDetailPage from "../../pages/orderDetail/OrderDetailPage";
import OrderList from "../../pages/orderList/OrderList";
import ProductUpdateForm from "../../pages/productUpdate/ProductUpdateForm";
import ResetPassword from '../../pages/ResetPassword/ResetPassword';
import AdminProtectedRoutes from "./AdminProtectedRoutes";
import CustomerProtectedRoutes from "./CustomerProtectedRoutes";
import LoginProtectedRoutes from "./LoginProtectedRoutes";
import ManagerProtectedRoutes from "./ManagerProtectedRoutes";

export default function AppRouter() {
  return (
    <>
      <Routes>
        {/* allowed to all */}
        <Route path="" element={<LandingPage />} />
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<RegistrationOptions />} />
        <Route path="/register/customer" element={<CustomerRegistration />} />
        <Route path="/register/manager" element={<ManagerRegistration />} />
        <Route path="/forgot-password/:code" element={<ForgotPassword />} />
        <Route path="/forgot-password-email" element={<ForgotPasswordEmail />} />
        <Route path="/expired" element={<TokenExpired />} />

        {/* login required */}
        <Route element={<LoginProtectedRoutes />} >
          <Route path="/reset-password" element={<ResetPassword />} />
          <Route path="/waiting" element={<PendingManagerLandingPage />} />

          {/* customer role required */}
          <Route element={<CustomerProtectedRoutes />}>
            <Route path="/user-dashboard" element={<CustomerLandingPage />} />
            <Route path="/search" element={<SearchPage />} />
            <Route path="/about" element={<About />} />

            <Route path="/cart" element={<Cart />} />
            <Route path="/prescription" element={<Prescrption />} />


            <Route path="/orders/list" element={<OrderList />} />
            <Route path="/orders/:id" element={<OrderDetailPage />} />
            <Route path="/orders/place" element={<OrderCreatePage />} />

            <Route path="/optin/list" element={<OptinList />} />
            <Route path="/optin/create" element={<OptInCreate />} />
            <Route path="/optin/update/:id" element={<OptInUpdate />} />

            <Route path="/subscriptions" element={<Subscriptions />} />
            <Route path="/register/subscription" element={<RegisterSubscriptionPage />} />
          </Route>

          {/* manager role required */}
          <Route element={<ManagerProtectedRoutes />}>
            <Route path="/manager-dashboard" element={<ManagerDash />} />
            <Route path="/add-store" element={<AddStore />} />
            <Route path="/manager/store" element={<ManagerStoreList />} />
            <Route path="/manager/store/:storeId" element={<StorePage />} />
            <Route path="/manager/store/:storeId/item/:itemId" element={<ItemPage />} />
            <Route path="/manager/notifications" element={<NotificationList />} />

          </Route>

          {/* admin role required */}
          <Route element={<AdminProtectedRoutes />}>
            <Route path="/admin-dashboard" element={<AdminDash />} />

            <Route path="/store" element={<StoreList />} />
            <Route path="/store-details/:id" element={<StoreDetails />} />
            <Route path="/manager" element={<ManagerList />} />

            <Route path="/AdminSubscriptionPack" element={<SubscriptionView />} />

            <Route path="/products-page" element={<ProductPage />} />
            <Route path="/product/update/:id" element={<ProductUpdateForm />} />
            <Route path="/products-details/:id" element={<ProductDetails />} />

            <Route path="/admin/category" element={<CategoryList />} />

            <Route path="/admin/notifications" element={<NotificationList />} />

          </Route>

          {/* edge cases */}
          < Route path="*" element={< PageNotFound />} />
          < Route path="not-allowed" element={< NotAllowed />} />
        </Route>
      </Routes>
    </>
  )
}
