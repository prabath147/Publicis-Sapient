import { fireEvent, render } from '@testing-library/react';
import { useState } from 'react';
import { Provider } from 'react-redux';
import { BrowserRouter } from 'react-router-dom';
import { act } from 'react-test-renderer';
import { store } from '../../app/store';
import SideMenu from '../../components/ui/sideMenu/sideMenu';



describe('SideMenu', () => {
	interface MockProps {
		role: string;
		opened: boolean;
	}

	const MockSideMenu = ({ role, opened }: MockProps) => {
		const [open2, setopen2] = useState<boolean>(opened)
		return <Provider store={store}>
			<BrowserRouter>
				<SideMenu role={role} opened={open2} setOpened={setopen2} />
			</BrowserRouter>
		</Provider >
	}

	it('Testing Side Menu as admin', () => {
		const { asFragment } = render(
			<MockSideMenu role={'admin'} opened={false} />
		)

		expect(asFragment()).toMatchSnapshot();
	});

	it('Testing Side Menu as admin', () => {
		const { asFragment } = render(
			<MockSideMenu role={'manager'} opened={false} />
		)

		expect(asFragment()).toMatchSnapshot();
	});

	it('Testing Side Menu as admin', () => {
		const { asFragment } = render(
			<MockSideMenu role={'admin'} opened={true} />
		)

		expect(asFragment()).toMatchSnapshot();
	});


	it("testing burger click", async () => {
		const { asFragment, getByTestId } = render(
			<MockSideMenu role={'admin'} opened={true} />
		)

		await act(async () => {
			fireEvent.click(getByTestId("burger-btn-sidebar"))
		})

		expect(asFragment()).toMatchSnapshot();
	})


})