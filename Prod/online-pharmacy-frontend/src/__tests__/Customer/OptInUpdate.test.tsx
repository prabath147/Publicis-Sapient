// import { fireEvent, render } from '@testing-library/react';
// import dayjs from 'dayjs';
// import { Provider } from 'react-redux';
// import { MemoryRouter, Route } from 'react-router-dom';
// import { act } from 'react-test-renderer';
// import { useAppDispatch } from '../../app/hooks';
// import { store } from '../../app/store';
// import OptInUpdate from '../../components/pages/Customer/OptInUpdate/OptInUpdate';
// import { addressInitialValues } from '../../components/ui/forms/AddressForm';
// import mockAxios from '../../__mocks__/axios';

// const MockOptinUpdate = () => {
//     const dispatch = useAppDispatch()
//     // dispatch(loadOptIn(optinMock))

//     return (
//         <OptInUpdate />
//     )
// }

// function RenderWithRouter() {
//     return (
//         <Provider store={store}>
//             <MemoryRouter initialEntries={['blogs/2']}>
//                 <Route path='blogs/:id'>
//                     <MockOptinUpdate />
//                 </Route>
//             </MemoryRouter>
//         </Provider >
//     )
// }

// describe('Opt in update', () => {
//     jest
//         .useFakeTimers()
//         .setSystemTime(new Date('2020-01-01'));

//     jest.mock('react-router-dom', () => ({
//         ...jest.requireActual('react-router-dom'), // use actual for all non-hook parts
//         useParams: () => ({
//             id: '2',
//         }),
//         useRouteMatch: () => ({ url: 'blogs/2' }),
//     }));

//     it('should render the component', () => {
//         const { asFragment } = render(
//             <RenderWithRouter />
//         )
//         expect(asFragment()).toMatchSnapshot();
//     })

//     it('should render the handle create call', async () => {
//         mockAxios.get.mockImplementationOnce(() =>
//             Promise.resolve({
//                 data: {
//                     id: 2,
//                     intervalInDays: 2,
//                     name: "fever",
//                     deliveryDate: dayjs(new Date()).add(3, "days").toDate(),
//                     numberOfDeliveries: 3,
//                     userId: 20,
//                     address: addressInitialValues(),
//                     repeatOrderItems: []
//                 },
//             }),
//         )

//         const { asFragment, getByLabelText, getByRole, getAllByText } = render(
//             <RenderWithRouter />
//         )

//         const element = await getAllByText("Opt In Form")

//         await act(async () => {
//             fireEvent.click(getByRole('button', { name: /Place Order/i }));
//         })
//         expect(asFragment()).toMatchSnapshot()

//         await act(async () => {
//             fireEvent.change(getByLabelText(/Name/i), { target: { value: "name1" } })
//             fireEvent.change(getByLabelText(/Interval in days/i), { target: { value: 5 } })
//             fireEvent.change(getByLabelText(/Number of Deliveries/i), { target: { value: 5 } })

//             fireEvent.change(getByLabelText(/Street/i), { target: { value: "s1" } })
//             fireEvent.change(getByLabelText(/City/i), { target: { value: "c2" } })
//             fireEvent.change(getByLabelText(/state/i), { target: { value: "s1" } })
//             fireEvent.change(getByLabelText(/Country/i), { target: { value: "c1" } })
//             fireEvent.change(getByLabelText(/PinCode/i), { target: { value: "123456" } })
//         })

//         await act(async () => {
//             fireEvent.click(getByRole('button', { name: /Place Order/i }));
//         })
//         expect(asFragment()).toMatchSnapshot()
//     })
// })

it("component created", () => {
    //TODO "CHECK"
});
