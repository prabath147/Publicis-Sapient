import { render } from "@testing-library/react";
import { Provider } from "react-redux";
import { BrowserRouter } from "react-router-dom";
import { store } from "../app/store";
import PendingManagerLandingPage from "../components/pages/Manager/PendingManagerLandingPage/PendingManagerLandingPage";

it("component created", () => {
    // mockAxios.get.mockImplementationOnce(() => Promise.resolve(response));

    render(
      <Provider store={store}>
        <BrowserRouter>
          <PendingManagerLandingPage/>
        </BrowserRouter>
      </Provider>
    );
    expect(screen).toMatchSnapshot();
  });