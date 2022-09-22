import {
  act,
  fireEvent,
  render,
  waitForElementToBeRemoved,
} from "@testing-library/react";
import { Provider } from "react-redux";
import { BrowserRouter } from "react-router-dom";
import { store } from "../../app/store";
import ManagerList from "../../components/pages/Admin/ManagerList/ManagerList";
import { ManagerListResponse } from "../../components/pages/Admin/ManagerList/models";
import mockAxios from "../../__mocks__/axios";

const MockManagerList = () => {
  return (
    <Provider store={store}>
      <BrowserRouter>
        <ManagerList />
      </BrowserRouter>
    </Provider>
  );
};

const managers: ManagerListResponse = {
  data: [
    {
      managerId: 1,
      name: "John",
      approvalStatus: "APPROVED",
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

describe("ManagerList", () => {
  afterEach(() => {
    jest.resetAllMocks();
  });

  it("component created without data", () => {
    mockAxios.get.mockImplementationOnce(() => Promise.reject());
    const { asFragment } = render(<MockManagerList />);
    expect(asFragment()).toMatchSnapshot();
  });

  it("component created with fail event", async () => {
    mockAxios.get.mockImplementationOnce(() => Promise.reject());
    const { asFragment, getByTestId } = render(<MockManagerList />);

    await waitForElementToBeRemoved(getByTestId(/Loading/i));

    expect(asFragment()).toMatchSnapshot();
  });

  it("component creatednwith manager data", async () => {
    mockAxios.get.mockImplementationOnce(() =>
      Promise.resolve({ data: managers })
    );
    const { asFragment, getByTestId } = render(<MockManagerList />);

    await waitForElementToBeRemoved(getByTestId(/Loading/i));

    expect(asFragment()).toMatchSnapshot();
  });

  it("component created with pending managers and approve 1", async () => {
    mockAxios.get.mockImplementation(() =>
      Promise.resolve({ data: managers_pending })
    );

    mockAxios.put.mockImplementationOnce(() => {
      return Promise.resolve();
    });

    const { asFragment, getByTestId } = render(<MockManagerList />);

    await waitForElementToBeRemoved(getByTestId(/Loading/i));

    await act(async () => {
      fireEvent.click(getByTestId("pending-switch"));
    });

    expect(asFragment()).toMatchSnapshot();

    await act(async () => {
      fireEvent.click(getByTestId("manager-approve-1"));
    });

    expect(asFragment()).toMatchSnapshot();
  });

  it("component created with pending managers and reject 1", async () => {
    mockAxios.get.mockImplementation(() =>
      Promise.resolve({ data: managers_pending })
    );

    mockAxios.put.mockImplementationOnce(() => {
      return Promise.resolve();
    });

    const { asFragment, getByTestId } = render(<MockManagerList />);

    await waitForElementToBeRemoved(getByTestId(/Loading/i));

    await act(async () => {
      fireEvent.click(getByTestId("pending-switch"));
    });

    expect(asFragment()).toMatchSnapshot();

    await act(async () => {
      fireEvent.click(getByTestId("manager-reject-1"));
    });

    expect(asFragment()).toMatchSnapshot();
  });

  it("component created with pending managers and approve 1 and fail", async () => {
    mockAxios.get.mockImplementation(() =>
      Promise.resolve({ data: managers_pending })
    );

    mockAxios.put.mockImplementationOnce(() => {
      return Promise.reject();
    });

    const { asFragment, getByText, getByTestId } = render(<MockManagerList />);

    await waitForElementToBeRemoved(getByTestId(/Loading/i));

    await act(async () => {
      fireEvent.click(getByTestId("pending-switch"));
    });

    expect(asFragment()).toMatchSnapshot();

    await act(async () => {
      fireEvent.click(getByTestId("manager-approve-1"));
    });

    expect(asFragment()).toMatchSnapshot();
  });
});
