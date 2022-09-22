import { render } from "@testing-library/react";
import { Provider } from "react-redux";
import { BrowserRouter } from "react-router-dom";
import { store } from "../../app/store";
import CustomerLandingPage from "../../components/pages/Customer/customerLandingPage/customerLandingPage";

it("component created", () => {
  render(
    <Provider store={store}>
      <BrowserRouter>
        <CustomerLandingPage />
      </BrowserRouter>
    </Provider>
  );
  expect(screen).toMatchSnapshot();
});
