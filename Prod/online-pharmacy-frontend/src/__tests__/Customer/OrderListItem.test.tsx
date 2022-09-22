
import dayjs from 'dayjs';
import { Provider } from 'react-redux';
import { BrowserRouter } from 'react-router-dom';
import renderer from 'react-test-renderer';
import { store } from '../../app/store';
import OrderListItem from '../../components/pages/orderList/OrderListItem';

it('component created', () => {
    const component = renderer.create(

        <Provider store={store}>
            <BrowserRouter>
                <OrderListItem id={''} orderDate={dayjs(new Date("2022-01-10")).add(3, "days").toDate()} price={0} customer={''} shippingAddress={''} />
            </BrowserRouter>
        </Provider>
    )
    const tree = component.toJSON();
    expect(tree).toMatchSnapshot();
})
