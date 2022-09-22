import { render, waitForElementToBeRemoved } from '@testing-library/react';
import dayjs from 'dayjs';
import { Provider } from 'react-redux';
import { BrowserRouter } from 'react-router-dom';
import { store } from '../../app/store';
import OptinList from '../../components/pages/Customer/OptinList/OptinList';
import { addressInitialValues } from '../../components/ui/forms/AddressForm';
import mockAxios from '../../__mocks__/axios';

const MockOptinList = () => (
    <Provider store={store} >
        <BrowserRouter>
            <OptinList />
        </BrowserRouter>
    </Provider >
)



describe('optinList', () => {

    jest
        .useFakeTimers()
        .setSystemTime(new Date('2020-01-01'));

    afterEach(() => {
        jest.resetAllMocks();
    });

    const data = {
        "data": [
            {
                id: 2,
                intervalInDays: 2,
                name: "fever",
                deliveryDate: dayjs(new Date()).add(3, "days").toDate(),
                numberOfDeliveries: 3,
                userId: 20,
                address: addressInitialValues(),
                repeatOrderItems: []
            },
        ],
        "pageNumber": 0,
        "pageSize": 10,
        "totalRecords": 0,
        "totalPages": 0,
        "isLastPage": true
    }

    it('should render correctly', async () => {
        mockAxios.get.mockImplementationOnce(() =>
            Promise.reject(),
        )

        const { asFragment } = render(<MockOptinList />)
        expect(asFragment()).toMatchSnapshot();
    })

    it('should render with data', async () => {

        mockAxios.get.mockImplementationOnce(() =>
            Promise.resolve({ data: data }),
        )

        const { asFragment, getByTestId } = render(<MockOptinList />)

        await waitForElementToBeRemoved(getByTestId(/Loading/i));
        // expect(element.length).toEqual(2)

        expect(asFragment()).toMatchSnapshot()

    })
})
