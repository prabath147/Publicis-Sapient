import { act, fireEvent, render } from '@testing-library/react';
import { Provider } from 'react-redux';
import { BrowserRouter } from 'react-router-dom';
import renderer from 'react-test-renderer';
import { store } from '../../app/store';
import CustomerNavbar from '../../components/ui/CustomerNavbar/CustomerNavbar';


describe('CustomerNavbar', () => {

  it('component created', () => {
    const component = renderer.create(
      <Provider store={store}>
        <BrowserRouter>
          <CustomerNavbar />
        </BrowserRouter>
      </Provider>
    )
    const tree = component.toJSON();
    expect(tree).toMatchSnapshot();
  })

  it('component works on click event', async () => {
    const { asFragment, getByRole } = render(
      <Provider store={store}>
        <BrowserRouter>
          <CustomerNavbar />
        </BrowserRouter>
      </Provider>
    );

    await act(async () => {
      fireEvent.click(getByRole('link', {
        name: /cart/i
      }))
    })

    expect(asFragment()).toMatchSnapshot();
  })



  it('component works on click event', async () => {
    const { asFragment, getByTestId } = render(
      <div style={{ width: "200px" }}>
        <Provider store={store}>
          <BrowserRouter>
            <CustomerNavbar />
          </BrowserRouter>
        </Provider>
      </div>
    );

    await act(async () => {
      fireEvent.click(getByTestId("burger-btn"))
    })

    expect(asFragment()).toMatchSnapshot();
  })

})