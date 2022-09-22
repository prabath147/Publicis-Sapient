import "@testing-library/jest-dom";
import { fireEvent, render } from '@testing-library/react';
import { act } from 'react-dom/test-utils';
import { Provider } from 'react-redux';
import { BrowserRouter } from 'react-router-dom';
import renderer from 'react-test-renderer';
import { store } from '../../app/store';
import AddStore from '../../components/pages/Manager/addStore/addStore';
import { AddStoreRequest } from '../../components/pages/Manager/addStore/models';

const onSubmit = jest.fn();

describe('addStore', () => {

	beforeEach(() => {
		jest.clearAllMocks();
	});


	it('Testing onSubmit with valid data', async () => {

		const { getByRole } = render(<Provider store={store}>
			<BrowserRouter>
				<AddStore onSubmit={onSubmit} />
			</BrowserRouter>
		</Provider>);

		await act(async () => {
			fireEvent.change(getByRole('textbox', { name: /store name/i }), { target: { value: 'Store1' } });
			fireEvent.change(getByRole('textbox', { name: /street/i }), { target: { value: 'Marathahalli' } });
			fireEvent.change(getByRole('textbox', { name: /city/i }), { target: { value: 'Bengaluru' } });
			fireEvent.change(getByRole('textbox', { name: /state/i }), { target: { value: 'Karnataka' } });
			fireEvent.change(getByRole('textbox', { name: /country/i }), { target: { value: 'India' } });
			fireEvent.change(getByRole('textbox', { name: /pincode/i }), { target: { value: '560037' } });
		})


		const expected: AddStoreRequest = {
			storeName: 'Store1',
			street: 'Marathahalli',
			city: 'Bengaluru',
			state: 'Karnataka',
			country: 'India',
			pincode: '560037'
		}


		await act(async () => {
			fireEvent.click(getByRole('button', { name: /submit/i }));
		})

		expect(onSubmit).toBeCalledWith(expected);


	}, 10000);

	it('Testing onSubmit with valid data 2', async () => {

		const { getByRole } = render(<Provider store={store}>
			<BrowserRouter>
				<AddStore onSubmit={onSubmit} />
			</BrowserRouter>
		</Provider>);

		await act(async () => {
			fireEvent.change(getByRole('textbox', { name: /store name/i }), { target: { value: 'Store1' } });
			fireEvent.change(getByRole('textbox', { name: /street/i }), { target: { value: 'Marathahalli' } });
			fireEvent.change(getByRole('textbox', { name: /city/i }), { target: { value: 'Bengaluru' } });
			fireEvent.change(getByRole('textbox', { name: /state/i }), { target: { value: 'Karnataka' } });
			fireEvent.change(getByRole('textbox', { name: /country/i }), { target: { value: 'India' } });
			fireEvent.change(getByRole('textbox', { name: /pincode/i }), { target: { value: '560039' } });
		})


		const expected: AddStoreRequest = {
			storeName: 'Store1',
			street: 'Marathahalli',
			city: 'Bengaluru',
			state: 'Karnataka',
			country: 'India',
			pincode: '560039'
		}


		await act(async () => {
			fireEvent.click(getByRole('button', { name: /submit/i }));
		})

		expect(onSubmit).toBeCalledWith(expected);


	}, 10000);

	it('Testing Add Store Form component', () => {
		const component = renderer.create(
			<Provider store={store}>
				<BrowserRouter>
					<AddStore onSubmit={onSubmit} />
				</BrowserRouter>
			</Provider>);

	}, 10000);

})