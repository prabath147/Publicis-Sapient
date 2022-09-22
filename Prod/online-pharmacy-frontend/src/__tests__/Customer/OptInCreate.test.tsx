import { fireEvent, render } from '@testing-library/react';
import { Provider } from 'react-redux';
import { BrowserRouter } from 'react-router-dom';
import { act } from 'react-test-renderer';
import { store } from '../../app/store';
import OptInCreate from '../../components/pages/Customer/OptInCreate/OptInCreate';
import mockAxios from '../../__mocks__/axios';


const MockOptinCreate = () => {
    return <Provider store={store}>
        <BrowserRouter>
            <OptInCreate />
        </BrowserRouter>
    </Provider>
}

describe('component created', () => {
    jest
        .useFakeTimers()
        .setSystemTime(new Date('2020-01-01'));

    afterEach(() => {
        jest.resetAllMocks();
    });


    it('should render the component', () => {
        mockAxios.post.mockImplementationOnce(() =>
            Promise.resolve()
        )
        const { asFragment } = render(<MockOptinCreate />)
        expect(asFragment()).toMatchSnapshot();
    })

    it('should render the handle create call', async () => {
        mockAxios.post.mockImplementationOnce(() =>
            Promise.resolve()
        )
        const { asFragment, getByLabelText, getByRole } = render(<MockOptinCreate />)


        await act(async () => {
            fireEvent.click(getByRole('button', { name: /Place Order/i }));
        })
        expect(asFragment()).toMatchSnapshot()

        await act(async () => {
            fireEvent.change(getByLabelText(/Name/i), { target: { value: "name1" } })
            fireEvent.change(getByLabelText(/Interval in days/i), { target: { value: 5 } })
            fireEvent.change(getByLabelText(/Number of Deliveries/i), { target: { value: 5 } })

            fireEvent.change(getByLabelText(/Street/i), { target: { value: "s1" } })
            fireEvent.change(getByLabelText(/City/i), { target: { value: "c2" } })
            fireEvent.change(getByLabelText(/state/i), { target: { value: "s1" } })
            fireEvent.change(getByLabelText(/Country/i), { target: { value: "c1" } })
            fireEvent.change(getByLabelText(/PinCode/i), { target: { value: "123456" } })
        })

        await act(async () => {
            fireEvent.click(getByRole('button', { name: /Place Order/i }));
        })
        expect(asFragment()).toMatchSnapshot()
    })
})
