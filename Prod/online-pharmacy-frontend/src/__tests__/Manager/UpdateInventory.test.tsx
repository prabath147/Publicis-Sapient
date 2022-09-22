import { render, screen } from "@testing-library/react";
import user from "@testing-library/user-event";
import { Provider } from "react-redux";
import { BrowserRouter } from "react-router-dom";
import { store } from "../../app/store";
import UpdateInventoryModal from "../../components/pages/Manager/Inventory/UpdateInventory";

const setOpen = jest.fn();
it("component created", () => {
  // mockAxios.get.mockImplementationOnce(() => Promise.reject());
  render(
    <Provider store={store}>
      <BrowserRouter>
        <UpdateInventoryModal storeId={1} openUpdateInventoryModal={true} setOpenUpdateInventoryModal={setOpen} />
      </BrowserRouter>
    </Provider>
  );
  expect(screen).toMatchSnapshot();
});

it("Should trigger user events after component created", async () => {
  // mockAxios.get.mockImplementationOnce(() => Promise.reject());
  render(
    <Provider store={store}>
      <BrowserRouter>
        <UpdateInventoryModal storeId={1} openUpdateInventoryModal={true} setOpenUpdateInventoryModal={setOpen} />
      </BrowserRouter>
    </Provider>
  );

  await user.click(screen.getByRole('button', {
    name: /download inventory/i
  }))

  await user.click(screen.getByTestId("finput"))

  await user.click(screen.getByTestId("uploadBtn"))

  // screen.debug();
}, 30000);