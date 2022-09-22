import { cleanup, render, screen } from "@testing-library/react";
import user from "@testing-library/user-event";
import { Provider } from "react-redux";
import { BrowserRouter } from "react-router-dom";
import renderer from "react-test-renderer";
import { store } from "../app/store";
import ResetPassword from "../components/pages/ResetPassword/ResetPassword";
import mockAxios from "../__mocks__/axios";

const MockResetPwd = () => {
  return (
    <Provider store={store}>
      <BrowserRouter>
        <ResetPassword />
      </BrowserRouter>
    </Provider>
  );
};

it("component created", () => {
  const component = renderer.create(<MockResetPwd />);
  const tree = component.toJSON();
  expect(tree).toMatchSnapshot();
});

describe("Reset Password Tests", () => {
  afterEach(() => {
    cleanup();
  });
  it("Check if form is rendered correctly", () => {
    render(<MockResetPwd />);

    // expect(
    //   screen.getByRole("textbox", { name: /User Name/i })
    // ).toBeInTheDocument();
    // expect(screen.getByLabelText(/password/i)).toBeInTheDocument();
    // expect(
    //   screen.getByRole("button", { name: /Sign in/i })
    // ).toBeInTheDocument();

    expect(screen.getByLabelText(/Old Password/i)).toBeInTheDocument();
    expect(screen.getByLabelText(/New password/i)).toBeInTheDocument();
    expect(screen.getByLabelText(/Confirm password/i)).toBeInTheDocument();
  });

  it("should fill form1", async () => {
    render(<MockResetPwd />);

    const oldpwdInput = screen.getByLabelText(/Old Password/i);
    const newpwdInput = screen.getByLabelText(/New password/i);
    const confirmpassWordInput = screen.getByLabelText(/Confirm password/i);

    await user.type(oldpwdInput, "Test@123");
    expect(oldpwdInput).toHaveValue("Test@123");

    await user.type(newpwdInput, "NewTest@123");
    expect(newpwdInput).toHaveValue("NewTest@123");

    await user.type(confirmpassWordInput, "NewTest@123");
    expect(confirmpassWordInput).toHaveValue("NewTest@123");
  }, 30000);

  it("should fill incorrect", async () => {
    render(<MockResetPwd />);

    const oldpwdInput = screen.getByLabelText(/Old Password/i);
    const newpwdInput = screen.getByLabelText(/New password/i);
    const confirmpassWordInput = screen.getByLabelText(/Confirm password/i);

    const subBtn = screen.getByRole("button", {
      name: /submit/i,
    });

    await user.type(oldpwdInput, "Test@123");

    await user.type(newpwdInput, "newtest123");
    await user.type(confirmpassWordInput, "newtest123");

    // mockAxios.post.mockImplementationOnce(() => Promise.resolve());

    await user.click(subBtn);
  }, 30000);

  it("should fill different confirm", async () => {
    render(<MockResetPwd />);

    const oldpwdInput = screen.getByLabelText(/Old Password/i);
    const newpwdInput = screen.getByLabelText(/New password/i);
    const confirmpassWordInput = screen.getByLabelText(/Confirm password/i);

    const subBtn = screen.getByRole("button", {
      name: /submit/i,
    });

    await user.type(oldpwdInput, "Test@123");

    await user.type(newpwdInput, "NewTest@123");
    await user.type(confirmpassWordInput, "Newtest@123");

    const response = {
      data: "USUADUAS",
    };

    mockAxios.post.mockImplementationOnce(() => Promise.resolve());

    await user.click(subBtn);
  }, 30000);

  it("should fill correct", async () => {
    render(<MockResetPwd />);

    const oldpwdInput = screen.getByLabelText(/Old Password/i);
    const newpwdInput = screen.getByLabelText(/New password/i);
    const confirmpassWordInput = screen.getByLabelText(/Confirm password/i);

    const subBtn = screen.getByRole("button", {
      name: /submit/i,
    });

    await user.type(oldpwdInput, "Test@123");

    await user.type(newpwdInput, "NewTest@123");
    await user.type(confirmpassWordInput, "NewTest@123");

    const response = {
      data: "USUADUAS",
    };

    mockAxios.post.mockImplementationOnce(() => Promise.resolve());

    await user.click(subBtn);
  }, 30000);

  it("should fill correct with reject ", async () => {
    render(<MockResetPwd />);

    const oldpwdInput = screen.getByLabelText(/Old Password/i);
    const newpwdInput = screen.getByLabelText(/New password/i);
    const confirmpassWordInput = screen.getByLabelText(/Confirm password/i);

    const subBtn = screen.getByRole("button", {
      name: /submit/i,
    });

    await user.type(oldpwdInput, "Test@123");

    await user.type(newpwdInput, "NewTest@123");
    await user.type(confirmpassWordInput, "NewTest@123");

    const response = {
      response: { data: "USUADUAS" },
    };

    mockAxios.post.mockImplementationOnce(() => Promise.reject(response));

    await user.click(subBtn);
  }, 30000);
});
