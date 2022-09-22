import { cleanup, render, screen } from "@testing-library/react";
import user from "@testing-library/user-event";
import { BrowserRouter } from "react-router-dom";
import ManagerRegistration from "../../components/pages/Auth/Registration/ManagerRegistration/ManagerRegistration";
import mockAxios from "../../__mocks__/axios";

it("Should render component", () => {
  render(
    <BrowserRouter>
      <ManagerRegistration />
    </BrowserRouter>
  );
  expect(screen).toMatchSnapshot();
});

describe("Manager Form", () => {
  afterEach(() => {
    cleanup();
    jest.resetAllMocks();
  });
  it("should fill form1", async () => {
    render(
      <BrowserRouter>
        <ManagerRegistration />
      </BrowserRouter>
    );

    const userNameInput = screen.getByLabelText(/user name \*/i);
    const nameInput = screen.getByTestId("namet");
    const passWordInput = screen.getByTestId("mPT");
    const confirmPasswordInput = screen.getByLabelText(/confirm password \*/i);

    await user.type(userNameInput, "jana123");
    expect(userNameInput).toHaveValue("jana123");

    await user.type(nameInput, "DurgaJana");
    expect(nameInput).toHaveValue("DurgaJana");

    await user.type(passWordInput, "John@1234");
    expect(passWordInput).toHaveValue("John@1234");

    await user.type(confirmPasswordInput, "jana123");
    expect(confirmPasswordInput).toHaveValue("jana123");
  }, 30000);

  it("Should fill form2", async () => {
    render(
      <BrowserRouter>
        <ManagerRegistration />
      </BrowserRouter>
    );
    const phoneInput = screen.getByTestId("phone");
    const emailInput = screen.getByRole("textbox", {
      name: /email/i,
    });
    const licenseInput = screen.getByLabelText(/license /i);

    await user.type(emailInput, "jana@gmail.com");
    expect(emailInput).toHaveValue("jana@gmail.com");

    await user.type(phoneInput, "987887878");
    expect(phoneInput).toHaveValue("0987887878");

    await user.type(licenseInput, "648428");
    expect(licenseInput).toHaveValue("0648428");
  }, 10000);

  it("Should fillform3 password mismatch", async () => {
    render(
      <BrowserRouter>
        <ManagerRegistration />
      </BrowserRouter>
    );

    const userNameInput = screen.getByLabelText(/user name \*/i);
    const nameInput = screen.getByTestId("namet");
    const passWordInput = screen.getByTestId("mPT");
    const confirmPasswordInput = screen.getByLabelText(/confirm password \*/i);

    const phoneInput = screen.getByTestId("phone");
    const emailInput = screen.getByRole("textbox", {
      name: /email/i,
    });
    const licenseInput = screen.getByLabelText(/license /i);

    const regBtn = screen.getByRole("button", {
      name: /register/i,
    });

    await user.type(userNameInput, "jjajkaj");

    await user.type(nameInput, "jaljl gafj");

    await user.type(passWordInput, "hakjskD#al123");

    await user.type(confirmPasswordInput, "Jankd@1234");

    await user.type(phoneInput, "9989786789");

    await user.type(emailInput, "ahksk@gmail.com");

    await user.type(licenseInput, "12345");

    mockAxios.post.mockImplementationOnce(() => Promise.resolve());
    await user.click(regBtn);
  }, 30000);

  it("Should fillform3", async () => {
    render(
      <BrowserRouter>
        <ManagerRegistration />
      </BrowserRouter>
    );

    const userNameInput = screen.getByLabelText(/user name \*/i);
    const nameInput = screen.getByTestId("namet");
    const passWordInput = screen.getByTestId("mPT");
    const confirmPasswordInput = screen.getByLabelText(/confirm password \*/i);

    const phoneInput = screen.getByTestId("phone");
    const emailInput = screen.getByRole("textbox", {
      name: /email/i,
    });
    const licenseInput = screen.getByLabelText(/license /i);

    const regBtn = screen.getByRole("button", {
      name: /register/i,
    });

    await user.type(userNameInput, "jjajkaj");

    await user.type(nameInput, "jaljl gafj");

    await user.type(passWordInput, "hakjskD#al123");

    await user.type(confirmPasswordInput, "Jankd@1234");

    await user.type(phoneInput, "9989786789");

    await user.type(emailInput, "ahksk@gmail.com");

    await user.type(licenseInput, "12345");
    const error = {
      response: {
        data: {
          message: "Error",
        },
      },
    };

    mockAxios.post.mockImplementationOnce(() => Promise.reject(error));
    await user.click(regBtn);
  }, 30000);

  it("Should fillform without data", async () => {
    render(
      <BrowserRouter>
        <ManagerRegistration />
      </BrowserRouter>
    );

    // const userNameInput = screen.getByLabelText(/user name \*/i);
    // const nameInput = screen.getByTestId("namet");
    // const passWordInput = screen.getByTestId("mPT");
    // const confirmPasswordInput = screen.getByLabelText(/confirm password \*/i);

    // const phoneInput = screen.getByTestId("phone");
    // const emailInput = screen.getByRole("textbox", {
    //   name: /email/i,
    // });
    // const licenseInput = screen.getByLabelText(/license /i);

    const regBtn = screen.getByRole("button", {
      name: /register/i,
    });


    // mockAxios.post.mockImplementationOnce(() => Promise.reject());
    await user.click(regBtn);
  }, 30000);

  it("Should fillform correctly", async () => {
    render(
      <BrowserRouter>
        <ManagerRegistration />
      </BrowserRouter>
    );

    const userNameInput = screen.getByLabelText(/user name \*/i);
    const nameInput = screen.getByTestId("namet");
    const passWordInput = screen.getByTestId("mPT");
    const confirmPasswordInput = screen.getByLabelText(/confirm password \*/i);

    const phoneInput = screen.getByTestId("phone");
    const emailInput = screen.getByRole("textbox", {
      name: /email/i,
    });
    const licenseInput = screen.getByLabelText(/license /i);

    const regBtn = screen.getByRole("button", {
      name: /register/i,
    });

    await user.type(userNameInput, "jjajkaj");

    await user.type(nameInput, "jaljl gafj");

    await user.type(passWordInput, "hakjskD#al123");

    await user.type(confirmPasswordInput, "hakjskD#al123");

    await user.type(phoneInput, "9989786789");

    await user.type(emailInput, "ahksk@gmail.com");

    await user.type(licenseInput, "12345");

    const response = {
      data: {

      },
    };
    mockAxios.post.mockImplementationOnce(() => Promise.resolve(response));
    await user.click(regBtn);
  }, 30000);

  //     const countryInput = screen.getByLabelText(/country/i);
  //     const pinCodeInput = screen.getByRole("spinbutton", {
  //       name: /pincode/i,
  //     });
  //     const regBtn = screen.getByRole("button", {
  //       name: /register/i,
  //     });

  //     await user.type(stateInput, "ap");
  //     expect(stateInput).toHaveValue("ap");

  //     await user.type(countryInput, "ind");
  //     expect(countryInput).toHaveValue("ind");

  //     await user.type(pinCodeInput, "334345");
  //     expect(pinCodeInput).toHaveValue(334345);

  //     await user.click(regBtn);
  //     // expect(regBtn).toBeEnabled();
  //   }, 10000);

  //   it("Should call userRefister fun", async () => {
  //     render(
  //       <BrowserRouter>
  //         <CustomerRegistration />
  //       </BrowserRouter>
  //     );
  //     const userNameInput = screen.getByTestId("uT");
  //     const fullNameInput = screen.getByTestId("fT");
  //     const passWordInput = screen.getByTestId("pT");
  //     const phoneInput = screen.getByTestId("phone");
  //     const confirmPasswordInput = screen.getByTestId("cpT");
  //     const emailInput = screen.getByRole("textbox", {
  //       name: /email/i,
  //     });
  //     const streetInput = screen.getByPlaceholderText(/street/i);
  //     const cityInput = screen.getByRole("textbox", {
  //       name: /city/i,
  //     });

  //     const stateInput = screen.getByRole("textbox", {
  //       name: /state/i,
  //     });

  //     const countryInput = screen.getByLabelText(/country/i);
  //     const pinCodeInput = screen.getByRole("spinbutton", {
  //       name: /pincode/i,
  //     });
  //     const regBtn = screen.getByRole("button", {
  //       name: /register/i,
  //     });

  //     await user.type(userNameInput, "jjajkaj");

  //     await user.type(fullNameInput, "jaljl gafj");

  //     await user.type(passWordInput, "hakjskD#al123");

  //     await user.type(confirmPasswordInput, "Jankd@1234");

  //     await user.type(phoneInput, "9989786789");

  //     await user.type(emailInput, "ahksk@gmail.com");

  //     await user.type(streetInput, "sjslfjl");

  //     await user.type(cityInput, "ajsd");

  //     await user.type(stateInput, "ap");

  //     await user.type(countryInput, "ind");

  //     await user.type(pinCodeInput, "334345");

  //     // const response = {
  //     //   username: "jana123",
  //     //   email: "jana123@gmail.com",
  //     //   password: "jana@123D",
  //     //   role: ["customer"],
  //     // }

  //     mockAxios.post.mockImplementationOnce(() => Promise.resolve());

  //     await user.click(regBtn);
  //   }, 10000);

  //   it("Should call ctch block", async () => {
  //     render(
  //       <BrowserRouter>
  //         <CustomerRegistration />
  //       </BrowserRouter>
  //     );
  //     const userNameInput = screen.getByTestId("uT");
  //     const fullNameInput = screen.getByTestId("fT");
  //     const passWordInput = screen.getByTestId("pT");
  //     const phoneInput = screen.getByTestId("phone");
  //     const confirmPasswordInput = screen.getByTestId("cpT");
  //     const emailInput = screen.getByRole("textbox", {
  //       name: /email/i,
  //     });
  //     const streetInput = screen.getByPlaceholderText(/street/i);
  //     const cityInput = screen.getByRole("textbox", {
  //       name: /city/i,
  //     });

  //     const stateInput = screen.getByRole("textbox", {
  //       name: /state/i,
  //     });

  //     const countryInput = screen.getByLabelText(/country/i);
  //     const pinCodeInput = screen.getByRole("spinbutton", {
  //       name: /pincode/i,
  //     });
  //     const regBtn = screen.getByRole("button", {
  //       name: /register/i,
  //     });

  //     await user.type(userNameInput, "jjajkaj");

  //     await user.type(fullNameInput, "jaljl gafj");

  //     await user.type(passWordInput, "hakjskD#al123");

  //     await user.type(confirmPasswordInput, "Jankd@1234");

  //     await user.type(phoneInput, "9989786789");

  //     await user.type(emailInput, "ahksk@gmail.com");

  //     await user.type(streetInput, "sjslfjl");

  //     await user.type(cityInput, "ajsd");

  //     await user.type(stateInput, "ap");

  //     await user.type(countryInput, "ind");

  //     await user.type(pinCodeInput, "334345");

  //     const error = {
  //       response: {
  //         data: {
  //           message: "Error",
  //         },
  //       },
  //     };

  //     mockAxios.post.mockImplementationOnce(() => Promise.reject(error));

  //     await user.click(regBtn);
  //   }, 10000);
});
