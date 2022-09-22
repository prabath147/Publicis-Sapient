import { render, screen, waitForElementToBeRemoved } from "@testing-library/react";
import { Provider } from "react-redux";
import { BrowserRouter } from "react-router-dom";
import { store } from "../../app/store";
import CartItemCard from "../../components/pages/item/CartItemCard";
import mockAxios from "../../__mocks__/axios";

const item = {
  itemId: 1,
  itemIdFk: 1,
  itemQuantity: 10,
  price: 200,
}

const response = {
  data: {
    expiryDate: "2025-08-22",
    itemId: 68,
    itemQuantity: 10,
    manufacturedDate: "2022-06-25",
    price: 200,
    product: {
      categorySet: [
        {
          categoryId: 10000148,
          categoryName: "Category2",
        },
        {
          categoryId: 222,
          categoryName: "Category3",
        },
      ],
      description: "MineralBio",
      dosageForm: "tristique",
      imageUrl: "https://www.motrin.com/sites/",
      productId: 44,
      productName: "oracit",
      productType: true,
      proprietaryName: "Geiss, Des",
      quantity: 77,
    },
    store: {
      storeName: "store_1396_3",
    },
  },
};

describe("Cart Item Card", () => {
  afterEach(() => {
    jest.resetAllMocks();
  });
  it("component created with data", () => {


    mockAxios.get.mockImplementationOnce(() => Promise.resolve(response));
    render(
      <Provider store={store}>
        <BrowserRouter>
          <CartItemCard item={item} />
        </BrowserRouter>
      </Provider>
    );
    expect(screen).toMatchSnapshot();
  });

  it("component created with api error", async () => {
    mockAxios.get.mockImplementationOnce(() => Promise.reject());
    render(
      <Provider store={store}>
        <BrowserRouter>
          <CartItemCard item={item} />
        </BrowserRouter>
      </Provider>
    );
    await waitForElementToBeRemoved(screen.getByRole('presentation'));
    // screen.debug();
  });

  it("component created without data", async () => {
    const dummy = {
      data: {

      }
    }
    mockAxios.get.mockImplementationOnce(() => Promise.resolve(dummy));
    render(
      <Provider store={store}>
        <BrowserRouter>
          <CartItemCard item={item} />
        </BrowserRouter>
      </Provider>
    );
    // await waitForElementToBeRemoved(screen.getByRole('presentation'));
    // screen.debug();
  });

  it("component created without spinner", async () => {
    mockAxios.get.mockImplementationOnce(() => Promise.resolve(response));
    render(
      <Provider store={store}>
        <BrowserRouter>
          <CartItemCard item={item} />
        </BrowserRouter>
      </Provider>
    );
    await waitForElementToBeRemoved(screen.getByRole('presentation'));
    // screen.debug();
  });
})