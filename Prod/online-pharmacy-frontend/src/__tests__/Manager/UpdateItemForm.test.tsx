import { render, screen } from "@testing-library/react";
import { Provider } from "react-redux";
import { store } from "../../app/store";
import UpdateItemForm from "../../components/pages/Manager/Inventory/UpdateItemForm";

const mitem = {
  itemId: 1,
  itemQuantity: 50,
  price: 200,
  manufacturedDate: "July 15,2022",
  expiryDate: "September 19,2022",
  product: {
    productId: 1,
    proprietaryName: "hkqjka",
    productName: "Crocin",
    description: "alhksdkask",
    dosageForm: "liquid",
    categorySet: ["c1", "c2"],
    quantity: 30,
    imageUrl: "jaksjdk",
    productType: true,
  },
};
const manager = {
  managerId: 1,
  name: "aksjdk",
  phoneNo: "8908876787",
  licenseNo: "1234566789",
  registrationDate: "July 18, 2020",
  approvalStatus: "APPROVED",
};
const mstore = {
  storeId: 1,
  storeName: "store2",
  address: {
    addressId: 1,
    street: "akask",
    city: "akskdak",
    state: "ajjjdjawd",
    pinCode: 522345,
    country: "ljkasd",
  },
  manager,
  createdDate: "July 16, 2020",
  revenue: 50000,
};
it("component created with data", () => {
  // mockAxios.get.mockImplementationOnce(() => Promise.resolve(response));
  const setOpen = jest.fn();
  render(
    <Provider store={store}>
      <UpdateItemForm
        open={true}
        setOpen={setOpen}
        item={mitem}
        store={mstore}
      />
    </Provider>
  );
  expect(screen).toMatchSnapshot();
});
