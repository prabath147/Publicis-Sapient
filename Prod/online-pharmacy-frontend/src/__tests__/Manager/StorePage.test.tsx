import { Provider } from "react-redux";
import { BrowserRouter } from "react-router-dom";
import { store } from "../../app/store";
import { ManagerListResponse } from "../../components/pages/Admin/ManagerList/models";
import { Store } from "../../components/pages/Manager/models";
import StorePage from "../../components/pages/Manager/StorePage/StorePage";

const MockStorePage = () => {
  return (
    <Provider store={store}>
      <BrowserRouter>
        <StorePage />
      </BrowserRouter>
    </Provider>
  );
};

const storePage: Store = {
  storeId: 101,
  storeName: "New Store",
  address: {
    addressId: 1,
    street: "New street",
    city: "New City",
    state: "New State",
    pinCode: 123456,
    country: "Country",
  },
  manager: {
    managerId: -1,
    name: "John",
    approvalStatus: "APPROVED",
    licenseNo: "1889299",
    phoneNo: "1234567890",
    registrationDate: "2022-08-16",
  },
  createdDate: "2022-09-09",
  revenue: 1000,
};

const managers_pending: ManagerListResponse = {
  data: [
    {
      managerId: 1,
      name: "John",
      approvalStatus: "PENDING",
      licenseNo: "1889299",
      phoneNo: "1234567890",
      registrationDate: new Date("2022-08-16"),
    },
  ],
  isLastPage: true,
  pageNumber: 1,
  pageSize: 10,
  totalPages: 1,
  totalRecords: 10,
};

describe("StorePage", () => {
  afterEach(() => {
    jest.resetAllMocks();
  });

  it("component created without data", () => {
    // mockAxios.get.mockImplementationOnce(() => Promise.reject());
    // const { asFragment } = render(<MockStorePage />);
    // expect(asFragment()).toMatchSnapshot();
  });

  //   it("component created with fail event", async () => {
  //     mockAxios.get.mockImplementationOnce(() => Promise.reject());
  //     const { asFragment, getByTestId } = render(<MockStorePage />);

  //     await waitForElementToBeRemoved(getByTestId(/Loading/i));

  //     expect(asFragment()).toMatchSnapshot();
  //   });

  it("component creatednwith manager data", async () => {
    // mockAxios.get.mockImplementationOnce(() =>
    //   Promise.resolve({ data: storePage })
    // );
    // const { asFragment, getByTestId } = render(<MockStorePage />);

    // await waitForElementToBeRemoved(getByTestId(/Loading/i));

    // await act(async () => {
    //   fireEvent.click(getByTestId("Edit"));
    // });

    // expect(asFragment()).toMatchSnapshot();
  });

  //   it("Should click search icon", async () => {
  //     mockAxios.get.mockImplementationOnce(() =>
  //       Promise.resolve({ data: storePage })
  //     );
  //     const { asFragment, getByTestId } = render(<MockStorePage />);

  //     await waitForElementToBeRemoved(getByTestId(/Loading/i));

  //     await act(async () => {
  //       fireEvent.click(getByTestId("searchIcon"));
  //     });

  //     expect(asFragment()).toMatchSnapshot();
  //   });

  //   it("Should click search Add inventory Button", async () => {
  //     mockAxios.get.mockImplementationOnce(() =>
  //       Promise.resolve({ data: storePage })
  //     );
  //     const { asFragment, getByTestId } = render(<MockStorePage />);

  //     await waitForElementToBeRemoved(getByTestId(/Loading/i));

  //     await act(async () => {
  //       fireEvent.click(getByTestId("addIn"));
  //     });

  //     expect(asFragment()).toMatchSnapshot();
  //   });

  //   it("Should click Update button", async () => {
  //     mockAxios.get.mockImplementationOnce(() =>
  //       Promise.resolve({ data: storePage })
  //     );
  //     const { asFragment, getByTestId } = render(<MockStorePage />);

  //     await waitForElementToBeRemoved(getByTestId(/Loading/i));

  //     await act(async () => {
  //       fireEvent.click(getByTestId("UpdateIn"));
  //     });

  //     expect(asFragment()).toMatchSnapshot();
  //   });

  //   it("should click edit button", async () => {
  //     mockAxios.get.mockImplementationOnce(() =>
  //       Promise.resolve({ data: storePage })
  //     );
  //     const { asFragment, getByTestId } = render(<MockStorePage />);

  //     await waitForElementToBeRemoved(getByTestId(/Loading/i));
  //     expect(asFragment()).toMatchSnapshot();
  //   });

  // it('component created with pending managers and approve 1', async () => {
  //     mockAxios.get.mockImplementation(() =>
  //         Promise.resolve({ data: managers_pending }),
  //     )

  //     mockAxios.put.mockImplementationOnce(() => {
  //         return Promise.resolve()
  //     })

  //     const { asFragment, getByTestId } = render(<MockManagerList />)

  //     await waitForElementToBeRemoved(getByTestId(/Loading/i));

  //     await act(async () => {
  //         fireEvent.click(getByTestId("pending-switch"))
  //     })

  //     expect(asFragment()).toMatchSnapshot();

  //     await act(async () => {
  //         fireEvent.click(getByTestId("manager-approve-1"))
  //     })

  //     expect(asFragment()).toMatchSnapshot();
  // })

  // it('component created with pending managers and reject 1', async () => {
  //     mockAxios.get.mockImplementation(() =>
  //         Promise.resolve({ data: managers_pending }),
  //     )

  //     mockAxios.put.mockImplementationOnce(() => {
  //         return Promise.resolve()
  //     })

  //     const { asFragment, getByTestId } = render(<MockManagerList />)

  //     await waitForElementToBeRemoved(getByTestId(/Loading/i));

  //     await act(async () => {
  //         fireEvent.click(getByTestId("pending-switch"))
  //     })

  //     expect(asFragment()).toMatchSnapshot();

  //     await act(async () => {
  //         fireEvent.click(getByTestId("manager-reject-1"))
  //     })

  //     expect(asFragment()).toMatchSnapshot();
  // })

  // it('component created with pending managers and approve 1 and fail', async () => {
  //     mockAxios.get.mockImplementation(() =>
  //         Promise.resolve({ data: managers_pending }),
  //     )

  //     mockAxios.put.mockImplementationOnce(() => {
  //         return Promise.reject()
  //     })

  //     const { asFragment, getByText, getByTestId } = render(<MockManagerList />)

  //     await waitForElementToBeRemoved(getByTestId("manager-reject-1"));

  //     await act(async () => {
  //         fireEvent.click(getByTestId("pending-switch"))
  //     })

  //     expect(asFragment()).toMatchSnapshot();

  //     await act(async () => {
  //         fireEvent.click(getByTestId("manager-approve-1"))
  //     })

  //     expect(asFragment()).toMatchSnapshot();
  // })
});
