import { render, screen } from "@testing-library/react";
import { BrowserRouter } from "react-router-dom";
import RegistrationOptions from "../../components/pages/Auth/Registration/RegistrationOptions/RegistrationOptions";

it("component created", () => {
  render(
    <BrowserRouter>
      <RegistrationOptions />
    </BrowserRouter>
  );
  expect(screen).toMatchSnapshot();
});

// it("Should call signup customer", async () => {
//   render(
//     <BrowserRouter>
//       <RegistrationOptions />
//     </BrowserRouter>
//   );

//   const cBtn = screen.getByTestId("customerT");
//   // const sBtn = screen.getByTestId("storeT");

//   await user.click(cBtn);

//   // expect(cBtn).not.toBeDisabled();
// });

// it("Should call signup store Manager", async () => {
//   render(
//     <BrowserRouter>
//       <RegistrationOptions />
//     </BrowserRouter>
//   );

//   // const sBtn = screen.getByTestId("storeT");

//   // await user.click(sBtn);

//   // expect(cBtn).not.toBeDisabled();
// });
