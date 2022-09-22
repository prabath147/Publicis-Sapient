import { render, screen, waitForElementToBeRemoved } from '@testing-library/react';
import { Provider } from 'react-redux';
import { BrowserRouter } from 'react-router-dom';
import { store } from '../../app/store';
import OrderList from '../../components/pages/orderList/OrderList';
import mockAxios from '../../__mocks__/axios';

const response = {
    data: {
        data: [
            {
                orderId: 757,
                userId: 4219,
                items: [
                    {
                        itemId: 759,
                        itemIdFk: 10000207,
                        price: 8.99,
                        itemQuantity: 1
                    }
                ],
                quantity: 1,
                price: 8.99,
                orderDetails: null,
                orderAddress: {
                    addressId: 758,
                    street: "hksk",
                    city: "jhojjsjs",
                    state: "haksdka",
                    pinCode: "5566767",
                    country: "sdaksdj"
                },
                orderDate: "2022-09-19",
                deliveryDate: "2022-09-22",
                optionalOrderDetails: false
            }
        ]
    }
}

it('component created', async () => {

    mockAxios.get.mockImplementationOnce(() => Promise.resolve(response));

    render(

        <Provider store={store}>
            <BrowserRouter>
                <OrderList />
            </BrowserRouter>
        </Provider>
    )
    await waitForElementToBeRemoved(screen.getByRole('presentation'))
    expect(screen).toMatchSnapshot();
})

it('component created', async () => {

    mockAxios.get.mockImplementationOnce(() => Promise.resolve(response));

    render(

        <Provider store={store}>
            <BrowserRouter>
                <OrderList />
            </BrowserRouter>
        </Provider>
    )
    await waitForElementToBeRemoved(screen.getByRole('presentation'))
    // screen.debug();
})

it('component created', async () => {

    mockAxios.get.mockImplementationOnce(() => Promise.reject());

    render(

        <Provider store={store}>
            <BrowserRouter>
                <OrderList />
            </BrowserRouter>
        </Provider>
    )
    // await waitForElementToBeRemoved(screen.getByRole('presentation'))
    // screen.debug();
    expect(screen).toMatchSnapshot();
})
