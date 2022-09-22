import { render } from '@testing-library/react';
import { Provider } from 'react-redux';
import { BrowserRouter } from 'react-router-dom';
import { useAppDispatch } from '../../app/hooks';
import { store } from '../../app/store';
import Cart from '../../components/pages/Cart/Cart';
import { clearCart, loadCart } from '../../components/pages/Cart/CartSlice';

jest.mock("../../components/pages/item/CartItemCard", () => () => (<>Component</>))

const itemList = [
    {
        itemIdFk: -1,
        itemQuantity: 1,
        price: 1,
        itemId: 1
    }
]

const MockCart = ({ items }) => {
    const dispatch = useAppDispatch()
    dispatch(clearCart())
    dispatch(loadCart(items))

    return <BrowserRouter>
        <Cart />
    </BrowserRouter>
}

describe('Cart', () => {

    it('component created with data', () => {
        const { asFragment } = render(
            <Provider store={store}  >
                <MockCart items={itemList} />
            </Provider>
        )
        expect(asFragment()).toMatchSnapshot()
    })

    it('component created with data and render', async () => {
        const { asFragment, getByTestId } = render(
            <Provider store={store}  >
                <MockCart items={[]} />
            </Provider>
        )

        // await waitForElementToBeRemoved(getByTestId(/Loading/i));

        expect(asFragment()).toMatchSnapshot()
    })


})
