import { act, fireEvent, render } from "@testing-library/react";
import { Provider } from "react-redux";
import { BrowserRouter } from "react-router-dom";
import { useAppDispatch } from "../app/hooks";
import { store } from "../app/store";
import { LoginResponse } from "../components/pages/Auth/Login/models";
import { LoginAction } from "../components/pages/Auth/Login/UserSlice";
import UserBadge from "../components/ui/UserBadge/UserBadge";

const MockUserBadge = () => {
    const dispatch = useAppDispatch()
    const user: LoginResponse = {
        id: 1,
        username: "username",
        email: "email",
        refreshToken: "",
        token: "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJjdXN0b21lcjgiLCJpYXQiOjE2NjM0MDcxOTIsImV4cCI6MTY2MzQxMDc5Miwicm9sZSI6IlJPTEVfQ1VTVE9NRVIiLCJpZCI6MTQ1MX0.EDuS9qoJlYheDEDIUvhmC_JE51B2nbNuif3-49_GFtyc5IGMz5iWQx6XDoPHI4hTu6bzt3b1Zw_OUAHyoMrevw"
    }

    dispatch(LoginAction(user))


    return (
        <BrowserRouter>
            <UserBadge />
        </BrowserRouter>
    )
}

describe("UserBadgeComponent", () => {
    global.ResizeObserver = require('resize-observer-polyfill')

    afterEach(() => {
        jest.resetAllMocks();
    });

    it('Component Created', async () => {
        const { getByTestId, asFragment } = render(
            <Provider store={store}>
                <MockUserBadge />
            </Provider>
        );
        expect(asFragment()).toMatchSnapshot();
    })

    it('Component Opened', async () => {
        const { getByTestId, asFragment } = render(
            <Provider store={store}>
                <MockUserBadge />
            </Provider>
        );

        await act(async () => {
            fireEvent.click(getByTestId("btn-badge"))
        })

        expect(asFragment()).toMatchSnapshot();
    })

    // it('Logout failed', async () => {
    //     mockAxios.post.mockImplementationOnce(() =>
    //         Promise.reject(),
    //     )
    //     const { getByTestId, asFragment } = render(
    //         <Provider store={store}>
    //             <MockUserBadge />
    //         </Provider>
    //     );

    //     await act(async () => {
    //         fireEvent.click(getByTestId("btn-badge"))
    //     })

    //     await act(async () => {
    //         fireEvent.click(getByTestId("btn-logout"))
    //     })

    //     expect(asFragment()).toMatchSnapshot();

    // })

    // it('Logout passed', async () => {
    //     mockAxios.post.mockImplementationOnce(() =>
    //         Promise.resolve(),
    //     )
    //     const { getByTestId, asFragment } = render(
    //         <Provider store={store}>
    //             <MockUserBadge />
    //         </Provider>
    //     );

    //     await act(async () => {
    //         fireEvent.click(getByTestId("btn-badge"))
    //     })

    //     await act(async () => {
    //         fireEvent.click(getByTestId("btn-logout"))
    //     })

    //     expect(asFragment()).toMatchSnapshot();

    // })

    it('reset clicked ', async () => {
        const { getByTestId, asFragment } = render(
            <Provider store={store}>
                <MockUserBadge />
            </Provider>
        );

        await act(async () => {
            fireEvent.click(getByTestId("btn-badge"))
        })

        await act(async () => {
            fireEvent.click(getByTestId("btn-pwd-reset"))
        })

        expect(asFragment()).toMatchSnapshot();
    })

    it('subscription clicked ', async () => {
        const { getByTestId, asFragment } = render(
            <Provider store={store}>
                <MockUserBadge />
            </Provider>
        );

        await act(async () => {
            fireEvent.click(getByTestId("btn-badge"))
        })

        await act(async () => {
            fireEvent.click(getByTestId("btn-subs"))
        })

        expect(asFragment()).toMatchSnapshot();
    })

    it('orders clicked ', async () => {
        const { getByTestId, asFragment } = render(
            <Provider store={store}>
                <MockUserBadge />
            </Provider>
        );

        await act(async () => {
            fireEvent.click(getByTestId("btn-badge"))
        })

        await act(async () => {
            fireEvent.click(getByTestId("btn-orders"))
        })

        expect(asFragment()).toMatchSnapshot();
    })


    it('optin clicked ', async () => {
        const { getByTestId, asFragment } = render(
            <Provider store={store}>
                <MockUserBadge />
            </Provider>
        );

        await act(async () => {
            fireEvent.click(getByTestId("btn-badge"))
        })

        await act(async () => {
            fireEvent.click(getByTestId("btn-optins"))
        })

        expect(asFragment()).toMatchSnapshot();
    })

})

