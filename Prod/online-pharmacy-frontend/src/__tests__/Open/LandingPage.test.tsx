import { render, screen } from "@testing-library/react";
import user from "@testing-library/user-event";
import { Provider } from "react-redux";
import { BrowserRouter } from "react-router-dom";
import { store } from "../../app/store";
import LandingPage from "../../components/pages/Open/landingPage/LandingPage";

it("component created", () => {
  render(
    <Provider store={store}>
      <BrowserRouter>
        <LandingPage />
      </BrowserRouter>
    </Provider>
  );
  expect(screen).toMatchSnapshot();
});

it("Should click Login Button", async () => {
  render(
    <Provider store={store}>
      <BrowserRouter>
        <LandingPage />
      </BrowserRouter>
    </Provider>
  );

  const LoginBtn = screen.getByRole("button", {
    name: /login/i,
  });

  await user.click(LoginBtn);
});

it("Should click Register Button", async () => {
  render(
    <Provider store={store}>
      <BrowserRouter>
        <LandingPage />
      </BrowserRouter>
    </Provider>
  );

  const RegBtn = screen.getByRole("button", {
    name: /register/i,
  });

  await user.click(RegBtn);
});
