import { Provider } from "react-redux";
import renderer from "react-test-renderer";
import { store } from "../../app/store";
import RegisterSubscriptionPage from "../../components/pages/Customer/RegisterSubscriptionPage/RegisterSubscriptionPage";
import mockAxios from "../../__mocks__/axios";

describe("RegisterSubscriptionPage", () => {
  jest.useFakeTimers().setSystemTime(new Date("2020-01-01"));

  afterEach(() => {
    jest.resetAllMocks();
  });

  it("component created", () => {
    mockAxios.get.mockImplementationOnce(() => Promise.reject());

    const component = renderer.create(
      <Provider store={store}>
        <RegisterSubscriptionPage />
      </Provider>
    );
    const tree = component.toJSON();
    expect(tree).toMatchSnapshot();
  });

  // TODO should update it
  // it('elements are in the document', () => {
  //   mockAxios.get.mockImplementationOnce(() =>
  //     Promise.resolve({
  //       data: {

  //       }
  //     }),
  //   )

  //   render(
  //     <Provider store={store}>
  //       <RegisterSubscriptionPage />
  //     </Provider>
  //   )
  //   const title = screen.getByRole('heading', {
  //     name: /available subscriptions/i
  //   })
  //   const submitButton = screen.getByRole('button', {
  //     name: /submit/i
  //   })

  //   expect(title).toBeInTheDocument();
  //   expect(submitButton).toBeInTheDocument();

  // })
});
