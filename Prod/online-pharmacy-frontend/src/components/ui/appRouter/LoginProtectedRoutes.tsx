import { Navigate, Outlet } from 'react-router-dom';
import { useAppSelector } from '../../../app/hooks';
import { getUserData } from '../../pages/Auth/Login/UserSlice';

export default function LoginProtectedRoutes() {
    const user = useAppSelector(getUserData)
    // console.log(user)

    // if (user.id !== -1 && user.role === 'ROLE_PRE_MANAGER') {
    //     return <PendingManagerLandingPage />
    // }

    if (user.id !== -1) {
        return <>
            <Outlet />
        </>;
    } else {
        return <Navigate to="/login" />;
    }
}
