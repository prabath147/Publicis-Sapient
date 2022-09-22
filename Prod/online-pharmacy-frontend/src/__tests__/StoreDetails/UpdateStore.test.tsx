import { render, screen } from "@testing-library/react";
import user from "@testing-library/user-event";
import { Provider } from "react-redux";
import { useAppDispatch } from "../../app/hooks";
import { store } from "../../app/store";
import UpdateStore from "../../components/pages/Admin/StoreDetails/UpdateStore";
import { loadStoreData } from "../../components/pages/Admin/storeList/StoreSlice";
import mockAxios from "../../__mocks__/axios";



const setOpen = jest.fn();

it("component without data", () => {

    render(
      <Provider store={store}>
          <UpdateStore open={true} setOpen={setOpen} id={1}/>
      </Provider>
    );
    // screen.debug();
    expect(screen).toMatchSnapshot();
  });

const dummyStores = [
    {
        storeId: 1,
        storeName: "gaujgjs",
        address: {
            addressId: 1,
            street: "lsjfkl",
            city: "nkmfnkk",
            state: "akdsk",
            pinCode: 333456,
            country: "jldfl",
        },
        createdDate: "string",
        manager: {
            managerId: 1,
            name: "slkf",
            phoneNo: "8988987876",
            licenseNo: "33456",
            registrationDate: "01-01-2021",
            approvalStatus: "APPROVED",
        },
        revenue: 20000,
    },
    {
        storeId: 2,
        storeName: "gaujgjs",
        address: {
            addressId: 1,
            street: "lsjfkl",
            city: "nkmfnkk",
            state: "akdsk",
            pinCode: 333456,
            country: "jldfl",
        },
        createdDate: "string",
        manager: {
            managerId: 1,
            name: "slkf",
            phoneNo: "8988987876",
            licenseNo: "33456",
            registrationDate: "01-01-2021",
            approvalStatus: "APPROVED",
        },
        revenue: 20000,
    }
]

const MockUpdateStore = () => {
    const dispatch = useAppDispatch()
    dispatch(loadStoreData(dummyStores))

    return <UpdateStore open={true} setOpen={setOpen} id={1}/>
    
}

it("Data Loaded for component", () => {
    render(
        <Provider store={store}>
          <MockUpdateStore/>
          </Provider>
    );

    expect(screen).toMatchSnapshot();
    // screen.debug();
  });

  it("Submitting without filling", () => {
    render(
        <Provider store={store}>
          <MockUpdateStore/>
          </Provider>
    );

    user.click(screen.getByTestId("Upad"));
    expect(screen).toMatchSnapshot();
    // screen.debug();
  });

  it("Submit Empty Form", async () => {
    render(
        <Provider store={store}>
          <MockUpdateStore/>
          </Provider>
    );

    await user.clear(screen.getByRole('textbox', {
        name: /store name/i
      }));
    await user.clear(screen.getByLabelText("Street"));
    await user.clear(screen.getByLabelText("City"));
    await user.clear(screen.getByLabelText("State"));
    await user.clear(screen.getByLabelText("Country"));
    await user.clear(screen.getByLabelText("PIN Code"));

    await user.click(screen.getByTestId('Upad'));


  },30000);

  it("Fill correctly and api call success", async () => {
    render(
        <Provider store={store}>
          <MockUpdateStore/>
          </Provider>
    );

    await user.clear(screen.getByRole('textbox', {
        name: /store name/i
      }));
      await user.type(screen.getByRole('textbox', {
        name: /store name/i
      }),"janaa");

    await user.clear(screen.getByLabelText("Street"));
    await user.type(screen.getByLabelText("Street"),"radham");

    await user.clear(screen.getByLabelText("City"));
    await user.type(screen.getByLabelText("City"),"Rjy");

    await user.clear(screen.getByLabelText("State"));
    await user.type(screen.getByLabelText("State"),"AP");

    await user.clear(screen.getByLabelText("Country"));
    await user.type(screen.getByLabelText("Country"),"IND");

    await user.clear(screen.getByLabelText("PIN Code"));
    await user.type(screen.getByLabelText("PIN Code"),"233453");

    mockAxios.put.mockImplementationOnce(() => Promise.resolve({data:{}}));

    await user.click(screen.getByTestId('Upad'));


  },30000);

  it("Fill correctly and api call success", async () => {
    render(
        <Provider store={store}>
          <MockUpdateStore/>
          </Provider>
    );

    await user.clear(screen.getByRole('textbox', {
        name: /store name/i
      }));
      await user.type(screen.getByRole('textbox', {
        name: /store name/i
      }),"janaa");

    await user.clear(screen.getByLabelText("Street"));
    await user.type(screen.getByLabelText("Street"),"radham");

    await user.clear(screen.getByLabelText("City"));
    await user.type(screen.getByLabelText("City"),"Rjy");

    await user.clear(screen.getByLabelText("State"));
    await user.type(screen.getByLabelText("State"),"AP");

    await user.clear(screen.getByLabelText("Country"));
    await user.type(screen.getByLabelText("Country"),"IND");

    await user.clear(screen.getByLabelText("PIN Code"));
    await user.type(screen.getByLabelText("PIN Code"),"233453");

    mockAxios.put.mockImplementationOnce(() => Promise.reject({error:{message:"Oops!"}}));

    await user.click(screen.getByTestId('Upad'));


  },30000);