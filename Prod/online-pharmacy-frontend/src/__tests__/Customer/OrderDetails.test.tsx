import { render, waitForElementToBeRemoved } from "@testing-library/react"
import OrderDetailPage from "../../components/pages/orderDetail/OrderDetailPage"
import { Order } from "../../components/pages/orderList/models"
import { addressInitialValues } from "../../components/ui/forms/AddressForm"
import mockAxios from "../../__mocks__/axios"

const simpleCard = ({ item, children, checkbox }) => {
    return <>
        {children}
        {checkbox}
    </>
}

jest.mock("../../components/pages/item/CartItemCard", () => () => (<>Component</>))

const MockOrderDetails = () => {
    return <OrderDetailPage />
}

const orderData: Order = {
    items: [{
        itemIdFk: -1,
        itemQuantity: 1,
        price: 1,
        itemId: 1
    }],
    optionalOrderDetails: false,
    orderAddress: addressInitialValues(),
    orderDate: new Date('2020-01-01'),
    orderId: 1,
    price: 1,
    quantity: 1,
    userId: 1,
    orderDetails: {
        orderDetailsId: 1
    }
}

describe("OrderDetailPage", () => {
    afterEach(() => {
        jest.resetAllMocks();
    });
    it("should create a new OrderDetailPage", () => {
        mockAxios.get.mockImplementationOnce(() => Promise.reject());
        const { asFragment } = render(
            <MockOrderDetails />
        )
        expect(asFragment()).toMatchSnapshot()
    })

    it("should create a new on fail", async () => {
        mockAxios.get.mockImplementationOnce(() => Promise.resolve({
            data: orderData
        }))


        const { asFragment, getByTestId } = render(
            <MockOrderDetails />
        )

        await waitForElementToBeRemoved(getByTestId(/Loading/i));

        expect(asFragment()).toMatchSnapshot()
    })
})