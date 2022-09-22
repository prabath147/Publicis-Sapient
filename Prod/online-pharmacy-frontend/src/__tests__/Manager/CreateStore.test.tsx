import { fireEvent, render } from '@testing-library/react';
import { Provider } from 'react-redux';
import { BrowserRouter } from 'react-router-dom';
import { act } from 'react-test-renderer';
import { store } from '../../app/store';
import CreateStore from '../../components/pages/Manager/CreateStore/CreateStore';
import mockAxios from '../../__mocks__/axios';


const MockCreateStore = () => {
    return <Provider store={store}>
        <BrowserRouter>
            <CreateStore open={true} setOpen={() => true} />
        </BrowserRouter>
    </Provider>
}

describe('component created', () => {
    afterEach(() => {
        jest.resetAllMocks();
    });

    mockAxios.post.mockImplementationOnce(() =>
        Promise.resolve()
    )

    it('should render the component', () => {
        const { asFragment } = render(<MockCreateStore />)
        expect(asFragment()).toMatchSnapshot();
    })

    it('should render the handle create call', async () => {
        const { asFragment, getByLabelText, getByRole } = render(<MockCreateStore />)

        // await act(async () => {
        //     fireEvent.click(getByRole('button', { name: /Add Store/i }));
        // })
        expect(asFragment()).toMatchSnapshot()

        await act(async () => {
            fireEvent.change(getByLabelText(/Store Name/i), { target: { value: "name1" } })

            fireEvent.change(getByLabelText(/Street/i), { target: { value: "s1" } })
            fireEvent.change(getByLabelText(/City/i), { target: { value: "c2" } })
            fireEvent.change(getByLabelText(/State/i), { target: { value: "s1" } })
            fireEvent.change(getByLabelText(/Country/i), { target: { value: "c1" } })
            fireEvent.change(getByLabelText(/PIN Code/i), { target: { value: "123456" } })
        })

        await act(async () => {
            fireEvent.click(getByRole('button', { name: /Create/i }));
        })
        expect(asFragment()).toMatchSnapshot()
    })
})
