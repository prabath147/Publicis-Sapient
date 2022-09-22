import { render } from "@testing-library/react";
import { Provider } from "react-redux";
import { BrowserRouter } from "react-router-dom";
import { store } from "../../app/store";
import ManagerStoreList from "../../components/pages/Manager/ManagerStoreList/ManagerStoreList";
import { StoreListPage } from "../../components/pages/Manager/models";
import mockAxios from "../../__mocks__/axios";

const MockManagerStoreList = () => {
    return <Provider store={store}>
        <BrowserRouter>
            <ManagerStoreList />
        </BrowserRouter>
    </Provider>
}

const stores: StoreListPage = {
    data: [
        {
            storeId: 101,
            storeName: "New Store",
            address: {
                addressId: 1,
                street: "New street",
                city: "New City",
                state: "New State",
                pinCode: 123456,
                country: "Country"
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
            revenue: 1000
        }
    ],
    isLastPage: true,
    pageNumber: 1,
    pageSize: 10,
    totalPages: 1,
    totalRecords: 10
}


describe("ManagerStoreList", () => {

    afterEach(() => {
        jest.resetAllMocks();
    });

    it('component created without data', () => {
        mockAxios.get.mockImplementationOnce(() =>
            Promise.reject(),
        )
        const { asFragment } = render(<MockManagerStoreList />)
        expect(asFragment()).toMatchSnapshot();
    })

    it('component created with fail event', async () => {
        mockAxios.get.mockImplementationOnce(() =>
            Promise.reject(),
        )
        const { asFragment, getByTestId } = render(<MockManagerStoreList />)

        // await waitForElementToBeRemoved(getByTestId(/Loading/i));

        expect(asFragment()).toMatchSnapshot();
    })

    it('component creatednwith store list data', async () => {
        mockAxios.get.mockImplementationOnce(() =>
            Promise.resolve({ data: stores }),
        )
        const { asFragment, getByTestId } = render(<MockManagerStoreList />)

        // await waitForElementToBeRemoved(getByTestId(/Loading/i));

        expect(asFragment()).toMatchSnapshot();
    })


})
