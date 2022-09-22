import { render, screen } from "@testing-library/react";
import user from "@testing-library/user-event";
import { Provider } from "react-redux";
import { store } from "../../app/store";
import UpdateStore from "../../components/pages/Manager/UpdateStore/UpdateStore";
import { updateStore } from "../../components/pages/Manager/UpdateStore/UpdateStoreAPI";

const setOpen = jest.fn();

const dummyStore = {
  storeId: 1,
  storeName: "janastore",
  address: {
    addressId: 2,
    street: "string",
    city: "rjy",
    state: "ap",
    country: "ind",
    pinCode: 533456,
  },
  manager: {
    managerId: 1,
    name: "jana",
    phoneNo: "7876765456",
    licenseNo: "5565788",
    registrationDate: "",
    approvalStatus: "",
  },
  createdDate: "",
  revenue: 500000,
};

test("component created", () => {
  render(
    <Provider store={store}>
      <UpdateStore
        openUpdateStoreForm={true}
        setOpenUpdateStoreForm={setOpen}
        store={dummyStore}
      />
    </Provider>
  );
  expect(screen).toMatchSnapshot();
});

test("should fill form", async () => {
  render(
    <Provider store={store}>
      <UpdateStore
        openUpdateStoreForm={true}
        setOpenUpdateStoreForm={setOpen}
        store={dummyStore}
      />
    </Provider>
  );
  await user.click(
    screen.getByRole("button", {
      name: /update/i,
    })
  );
});

test("should fill form 2", async () => {
  render(
    <Provider store={store}>
      <UpdateStore
        openUpdateStoreForm={true}
        setOpenUpdateStoreForm={setOpen}
        store={dummyStore}
      />
    </Provider>
  );

  user.clear(screen.getByPlaceholderText("name"));
  user.clear(screen.getByPlaceholderText("street"));
  user.clear(screen.getByPlaceholderText("city"));
  user.clear(screen.getByPlaceholderText("state"));
  user.clear(screen.getByPlaceholderText("country"));
  user.clear(screen.getByPlaceholderText("******"));

  // mockAxios.put.mockImplementationOnce(() => Promise.resolve());

  await user.click(
    screen.getByRole("button", {
      name: /update/i,
    })
  );

  const actual = updateStore(dummyStore);
  // screen.debug();
});

//  test("checking the handle Submit fun", async() => {

//   handleSubmit.mockClear();
//   render(
//       <Provider store={store}>
//        <UpdateStore open={true} setOpen={()=> true} id={0}/>
//       </Provider>
//  );

//  handleSubmit.mockImplementation();
//  await expect(screen.getByText(/update store/i)).toBeInTheDocument();

// });
