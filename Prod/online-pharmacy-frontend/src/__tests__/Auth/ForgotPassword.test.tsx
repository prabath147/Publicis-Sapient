import { render, screen } from "@testing-library/react";
import { Provider } from "react-redux";
import { BrowserRouter } from "react-router-dom";
import { store } from "../../app/store";
import ForgotPassword from "../../components/pages/Auth/ForgotPassword/ForgotPassword";
import mockAxios from "../../__mocks__/axios";

const response = {
  status: {
    code: 200,
    text: "OK"
  }
}

it("component created without data", async () => {
  try {
    mockAxios.get.mockImplementationOnce(() => Promise.resolve(response));
    render(
      <Provider store={store}>
        <BrowserRouter>
          <ForgotPassword />
        </BrowserRouter>
      </Provider>
    );
    // screen.debug();

    expect(screen).toMatchSnapshot();

  } catch (e) {
    // 
  }
});


//   it("component created without data", () => {
//     mockAxios.get.mockImplementationOnce(() => Promise.reject());
//     render(
//       <Provider store={store}>
//         <BrowserRouter>
//           <ForgotPassword/>
//         </BrowserRouter>
//       </Provider>
//     );
//     screen.debug();

//     expect(screen).toMatchSnapshot();
//   });