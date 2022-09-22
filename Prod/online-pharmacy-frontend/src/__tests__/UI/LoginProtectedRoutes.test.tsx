import { BrowserRouter } from 'react-router-dom';
import { useAppDispatch } from '../../app/hooks';
import { LoginAction } from '../../components/pages/Auth/Login/UserSlice';
import LoginProtectedRoutes from '../../components/ui/appRouter/LoginProtectedRoutes';

const user = {
    id: 1,
    email: "test@example.com",
    refreshToken: "",
    token: "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwicm9sZSI6IlJPTEVfTUFOQUdFUiIsImV4cCI6MTUxNjkzOTAyMiwiaWF0IjoxNTE2MjM5MDIyfQ.BhiXl89GT7rPmD6S8dLmn5b3CzIeusnN7soVZGlwTYI",
    username: "test"
}
const MockLoginProtectedRoutes = () => {
    const dispatch = useAppDispatch()

    dispatch(LoginAction(user))

    return <BrowserRouter>
        <LoginProtectedRoutes />
    </BrowserRouter>
}


describe('Login Protection Routes', () => {

    test('Login protected Routes', () => {
        // const { asFragment } = render(
        //     <Provider store={store}>
        //         <BrowserRouter>
        //             <LoginProtectedRoutes />
        //         </BrowserRouter>
        //     </Provider>
        // )
        // expect(asFragment()).toMatchSnapshot();
    })

    test('Login protected Routes', () => {
        // const { asFragment } = render(
        //     <Provider store={store}>
        //         <MockLoginProtectedRoutes />
        //     </Provider>
        // )
        // expect(asFragment()).toMatchSnapshot();
    })



})
