import { Navigate, Outlet } from 'react-router-dom';
import { useAppSelector } from '../../../app/hooks';
import { getUserData } from '../../pages/Auth/Login/UserSlice';
import CustomerNavbar from '../CustomerNavbar/CustomerNavbar';

export default function CustomerProtectedRoutes() {
    const user = useAppSelector(getUserData)

    const role = "ROLE_CUSTOMER"

    if (user.id !== -1 && user.role === role) {
        return <>
            <CustomerNavbar />
            <Outlet />
        </>;
    } else {
        return <Navigate to="/login" />;
    }
}
