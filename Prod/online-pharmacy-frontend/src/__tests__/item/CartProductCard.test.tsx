import { render, screen, waitForElementToBeRemoved } from "@testing-library/react";
import { Provider } from "react-redux";
import { BrowserRouter } from "react-router-dom";
import { store } from "../../app/store";
import CartProductCard from "../../components/pages/item/CartProductCard";
import mockAxios from "../../__mocks__/axios";


const item = {
  productId: 1,
  productIdFk: 1,
  quantity: 10,
  price: 100,
}

const response = {
  data: {
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
  }
}

describe("Product Details", () => {
  afterEach(() => {
    jest.resetAllMocks();
  });
  it("component created with data", () => {
    mockAxios.get.mockImplementationOnce(() => Promise.resolve(response));
    render(
      <Provider store={store}>
        <BrowserRouter>
          <CartProductCard item={item} />
        </BrowserRouter>
      </Provider>
    );
    expect(screen).toMatchSnapshot();
  });

  it("component created without spinner", async () => {
    mockAxios.get.mockImplementationOnce(() => Promise.resolve(response));
    render(
      <Provider store={store}>
        <BrowserRouter>
          <CartProductCard item={item} />
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
          <CartProductCard item={item} />
        </BrowserRouter>
      </Provider>
    );
  });

  it("component created with api error", async () => {
    mockAxios.get.mockImplementationOnce(() => Promise.reject());
    render(
      <Provider store={store}>
        <BrowserRouter>
          <CartProductCard item={item} />
        </BrowserRouter>
      </Provider>
    );
    // await waitForElementToBeRemoved(screen.getByRole('presentation'));
    // screen.debug();
  });
})