import { cleanup, render, screen } from "@testing-library/react";
import user from "@testing-library/user-event";
import { BrowserRouter } from "react-router-dom";
import CustomerRegistration from "../../components/pages/Auth/Registration/CustomerRegistration/CustomerRegistration";
import mockAxios from "../../__mocks__/axios";

it("Should render component", () => {
  render(
    <BrowserRouter>
      <CustomerRegistration />
    </BrowserRouter>
  );
  expect(screen).toMatchSnapshot();
});

describe("ManagerList", () => {
  afterEach(() => {
    cleanup();
    jest.resetAllMocks();
  });
  it("should fill form1", async () => {
    render(
      <BrowserRouter>
        <CustomerRegistration />
      </BrowserRouter>
    );

    const userNameInput = screen.getByRole("textbox", { name: /user name/i });
    const fullNameInput = screen.getByRole("textbox", { name: /full name/i });
    const passWordInput = screen.getAllByLabelText(/password \*/i)[0];
    const confirmPasswordInput = screen.getByLabelText(/confirm password \*/i);

    await user.type(userNameInput, "jana123");
    expect(userNameInput).toHaveValue("jana123");

    await user.type(fullNameInput, "DurgaJana");
    expect(fullNameInput).toHaveValue("DurgaJana");

    await user.type(passWordInput, "John@1234");
    expect(passWordInput).toHaveValue("John@1234");

    await user.type(confirmPasswordInput, "jana123");
    expect(confirmPasswordInput).toHaveValue("jana123");
  }, 30000);

  it("Should fill form2", async () => {
    render(
      <BrowserRouter>
        <CustomerRegistration />
      </BrowserRouter>
    );

    const emailInput = screen.getByRole("textbox", {
      name: /email/i,
    });
    const streetInput = screen.getByPlaceholderText(/street/i);
    const cityInput = screen.getByRole("textbox", {
      name: /city/i,
    });

    await user.type(emailInput, "jana@gmail.com");
    expect(emailInput).toHaveValue("jana@gmail.com");

    await user.type(streetInput, "haksdk");
    expect(streetInput).toHaveValue("haksdk");

    await user.type(cityInput, "rjy");
    expect(cityInput).toHaveValue("rjy");
  }, 30000);

  it("Should fillform3", async () => {
    render(
      <BrowserRouter>
        <CustomerRegistration />
      </BrowserRouter>
    );
    const stateInput = screen.getByRole("textbox", {
      name: /state/i,
    });

    const countryInput = screen.getByLabelText(/country/i);
    const pinCodeInput = screen.getByRole("spinbutton", {
      name: /pincode/i,
    });
    const regBtn = screen.getByRole("button", {
      name: /register/i,
    });

    await user.type(stateInput, "ap");
    expect(stateInput).toHaveValue("ap");

    await user.type(countryInput, "ind");
    expect(countryInput).toHaveValue("ind");

    await user.type(pinCodeInput, "334345");
    expect(pinCodeInput).toHaveValue(334345);

    await user.click(regBtn);
    // expect(regBtn).toBeEnabled();
  }, 30000);

  it("Should mismatch  Password", async () => {
    render(
      <BrowserRouter>
        <CustomerRegistration />
      </BrowserRouter>
    );
    const userNameInput = screen.getByTestId("uT");
    const fullNameInput = screen.getByTestId("fT");
    const passWordInput = screen.getByTestId("pT");
    const phoneInput = screen.getByTestId("phone");
    const confirmPasswordInput = screen.getByTestId("cpT");
    const emailInput = screen.getByRole("textbox", {
      name: /email/i,
    });
    const streetInput = screen.getByPlaceholderText(/street/i);
    const cityInput = screen.getByRole("textbox", {
      name: /city/i,
    });

    const stateInput = screen.getByRole("textbox", {
      name: /state/i,
    });

    const countryInput = screen.getByLabelText(/country/i);
    const pinCodeInput = screen.getByRole("spinbutton", {
      name: /pincode/i,
    });
    const regBtn = screen.getByRole("button", {
      name: /register/i,
    });

    await user.type(userNameInput, "jjajkaj");

    await user.type(fullNameInput, "jaljl gafj");

    await user.type(passWordInput, "hakjskD#al123");

    await user.type(confirmPasswordInput, "Jankd@1234");

    await user.type(phoneInput, "9989786789");

    await user.type(emailInput, "ahksk@gmail.com");

    await user.type(streetInput, "sjslfjl");

    await user.type(cityInput, "ajsd");

    await user.type(stateInput, "ap");

    await user.type(countryInput, "ind");

    await user.type(pinCodeInput, "334345");

    // const response = {
    //   username: "jana123",
    //   email: "jana123@gmail.com",
    //   password: "jana@123D",
    //   role: ["customer"],
    // }

    mockAxios.post.mockImplementationOnce(() => Promise.resolve());

    await user.click(regBtn);
  }, 30000);

  it("Should call user reg function", async () => {
    render(
      <BrowserRouter>
        <CustomerRegistration />
      </BrowserRouter>
    );
    const userNameInput = screen.getByTestId("uT");
    const fullNameInput = screen.getByTestId("fT");
    const passWordInput = screen.getByTestId("pT");
    const phoneInput = screen.getByTestId("phone");
    const confirmPasswordInput = screen.getByTestId("cpT");
    const emailInput = screen.getByRole("textbox", {
      name: /email/i,
    });
    const streetInput = screen.getByPlaceholderText(/street/i);
    const cityInput = screen.getByRole("textbox", {
      name: /city/i,
    });

    const stateInput = screen.getByRole("textbox", {
      name: /state/i,
    });

    const countryInput = screen.getByLabelText(/country/i);
    const pinCodeInput = screen.getByRole("spinbutton", {
      name: /pincode/i,
    });
    const regBtn = screen.getByRole("button", {
      name: /register/i,
    });

    await user.type(userNameInput, "jjajkaj");

    await user.type(fullNameInput, "jaljl gafj");

    await user.type(passWordInput, "hakjskD#al123");

    await user.type(confirmPasswordInput, "hakjskD#al123");

    await user.type(phoneInput, "9989786789");

    await user.type(emailInput, "ahksk@gmail.com");

    await user.type(streetInput, "sjslfjl");

    await user.type(cityInput, "ajsd");

    await user.type(stateInput, "ap");

    await user.type(countryInput, "ind");

    await user.type(pinCodeInput, "334345");

    const response = {
      data: {

      },
    };

    mockAxios.post.mockImplementationOnce(() => Promise.resolve(response));

    await user.click(regBtn);
  }, 30000);

  it("Should call ctch block", async () => {
    // render(
    //   <BrowserRouter>
    //     <CustomerRegistration />
    //   </BrowserRouter>
    // );
    // const userNameInput = screen.getByTestId("uT");
    // const fullNameInput = screen.getByTestId("fT");
    // const passWordInput = screen.getByTestId("pT");
    // const phoneInput = screen.getByTestId("phone");
    // const confirmPasswordInput = screen.getByTestId("cpT");
    // const emailInput = screen.getByRole("textbox", {
    //   name: /email/i,
    // });
    // const streetInput = screen.getByPlaceholderText(/street/i);
    // const cityInput = screen.getByRole("textbox", {
    //   name: /city/i,
    // });

    // const stateInput = screen.getByRole("textbox", {
    //   name: /state/i,
    // });

    // const countryInput = screen.getByLabelText(/country/i);
    // const pinCodeInput = screen.getByRole("spinbutton", {
    //   name: /pincode/i,
    // });
    // const regBtn = screen.getByRole("button", {
    //   name: /register/i,
    // });

    // await user.type(userNameInput, "jjajkaj");

    // await user.type(fullNameInput, "jaljl gafj");

    // await user.type(passWordInput, "hakjskD#al123");

    // await user.type(confirmPasswordInput, "hakjskD#al123");

    // await user.type(phoneInput, "9989786789");

    // await user.type(emailInput, "ahksk@gmail.com");

    // await user.type(streetInput, "sjslfjl");

    // await user.type(cityInput, "ajsd");

    // await user.type(stateInput, "ap");

    // await user.type(countryInput, "ind");

    // await user.type(pinCodeInput, "334345");

    // const error = {
    //   response: {
    //     data: {
    //       message: "Error",
    //     },
    //   },
    // };

    // mockAxios.post.mockImplementationOnce(() => Promise.reject(

    // ));

    // await user.click(regBtn);
  }, 30000);
});
