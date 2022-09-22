import { render, screen, waitFor } from "@testing-library/react";
import user from "@testing-library/user-event";

import { Provider } from "react-redux";
import { BrowserRouter } from "react-router-dom";
import { store } from "../../app/store";
import ProductCard from "../../components/pages/Customer/search/ProductCard";
import mockAxios from "../../__mocks__/axios";

describe("Tests", () => {
  const mockedUsedNavigate = jest.fn();

  const modalResponse = {
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

  afterEach(() => {
    jest.resetAllMocks();
  });

  jest.mock("react-router-dom", () => ({
    ...(jest.requireActual("react-router-dom") as any),
    useNavigate: () => mockedUsedNavigate,
  }));
  it("component created", () => {
    render(
      <Provider store={store}>
        <BrowserRouter>
          <ProductCard
            item={{
              id: 1,
              description: "ajdjjasjdhkahs",
              dosageForm: "liqid",
              imageUrl: "jhakjshdkhk",
              itemId: 45,
              price: 300,
              productId: 33,
              productName: "Crocin",
              productType: true,
              proprietaryName: "jsahdk",
              quantity: 1,
            }}
            added={true}
            loading={false}
          />
        </BrowserRouter>
      </Provider>
    );
    expect(screen).toMatchSnapshot();
  });

  it("Data Loaded for Search component", () => {
    render(
      <Provider store={store}>
        <BrowserRouter>
          <ProductCard
            item={{
              id: 1,
              description: "ajdjjasjdhkahs",
              dosageForm: "liqid",
              imageUrl: "jhakjshdkhk",
              itemId: 45,
              price: 300,
              productId: 33,
              productName: "Crocin",
              productType: true,
              proprietaryName: "jsahdk",
              quantity: 1,
            }}
            added={true}
            loading={false}
          />
        </BrowserRouter>
      </Provider>
    );
    expect(screen).toMatchSnapshot();
  });

  it("should display card properly", async () => {
    render(
      <Provider store={store}>
        <BrowserRouter>
          <ProductCard
            item={{
              id: 1,
              description: "ajdjjasjdhkahs",
              dosageForm: "liqid",
              imageUrl: "jhakjshdkhk",
              itemId: 45,
              price: 300,
              productId: 33,
              productName: "Crocin",
              productType: true,
              proprietaryName: "jsahdk",
              quantity: 1,
            }}
            added={true}
            loading={false}
          />
        </BrowserRouter>
      </Provider>
    );

    const img = screen.getByRole("img", {
      name: /img/i,
    });
    const atoCartBtn = screen.getByRole("button", {
      name: /view cart/i,
    });

    const prodName = screen.getByText(/crocin/i);
    const price = screen.getByText(/300/i);
    const dform = screen.getByText(/liqid/i);
    const prodType = screen.getByText(/generic/i);
    const propName = screen.getByText(/jsahdk/i);

    expect(img).toBeInTheDocument();
    expect(atoCartBtn).toBeInTheDocument();
    expect(prodName).toBeInTheDocument();
    expect(price).toBeInTheDocument();
    expect(dform).toBeInTheDocument();
    expect(prodType).toBeInTheDocument();
    expect(propName).toBeInTheDocument();

    expect(screen.queryByText("Product Summary")).not.toBeInTheDocument();

    await user.click(atoCartBtn);

    global.ResizeObserver = jest.fn().mockImplementation(() => ({
      observe: jest.fn(),
      unobserve: jest.fn(),
      disconnect: jest.fn(),
    }));

    mockAxios.get.mockImplementationOnce(() => Promise.resolve(modalResponse));

    await user.click(img);

    await waitFor(() => {
      expect(screen.getByText("Product Summary")).toBeInTheDocument();
      expect(screen.getByText("NAME")).toBeInTheDocument();
      expect(screen.getByText("PRICE")).toBeInTheDocument();
      expect(screen.getByText("PROPRIETARY NAME")).toBeInTheDocument();
      expect(screen.getByText("DOSAGE FORM")).toBeInTheDocument();
      expect(screen.getByText("Description")).toBeInTheDocument();
      expect(screen.getByText("STORE")).toBeInTheDocument();
      expect(screen.getByText("MFG DATE")).toBeInTheDocument();
      expect(screen.getByText("EXPIRY DATE")).toBeInTheDocument();
      expect(screen.getByText("CATEGORY")).toBeInTheDocument();

      expect(
        screen.getByRole("button", {
          name: /view cart/i,
        })
      ).toBeInTheDocument();

      user.click(
        screen.getByRole("button", {
          name: /view cart/i,
        })
      );
    });

    user.keyboard("[Escape]");

    await waitFor(() => {
      expect(screen.queryByText("Product Summary")).not.toBeInTheDocument();
    });
    // screen.debug();
  }, 30000);

  it("should display card properly with add to cart button", async () => {
    render(
      <Provider store={store}>
        <BrowserRouter>
          <ProductCard
            item={{
              id: 1,
              description: "",
              dosageForm: "liqid",
              imageUrl: "jhakjshdkhk",
              itemId: 45,
              price: 300,
              productId: 33,
              productName: "Crocin",
              productType: false,
              proprietaryName: "jsahdk",
              quantity: 1,
            }}
            added={false}
            loading={false}
          />
        </BrowserRouter>
      </Provider>
    );

    const img = screen.getByRole("img", {
      name: /img/i,
    });
    const atoCartBtn = screen.getByRole("button", {
      name: /add to cart/i,
    });
    const response = {
      data: {
        cartId: 1,
        items: [
          {
            itemId: 1,
            itemIdFk: 45,
            itemQuantity: 1,
            price: 300,
          },
        ],
        price: 300,
        quantity: 1,
      },
    };
    mockAxios.post.mockImplementationOnce(() => Promise.resolve(response));
    await user.click(atoCartBtn);
    // screen.debug();
    // await waitFor(() => {
    //   expect(
    //     screen.getByRole("button", {
    //       name: /view cart/i,
    //     })
    //   ).toBeInTheDocument();
    // });

    // global.ResizeObserver = jest.fn().mockImplementation(() => ({
    //   observe: jest.fn(),
    //   unobserve: jest.fn(),
    //   disconnect: jest.fn(),
    // }));

    // await user.click(img);

    // await waitFor(() => {
    //   expect(screen.getByText("Product Summary")).toBeInTheDocument();
    //   expect(screen.getByText("CONTAINS")).toBeInTheDocument();
    //   expect(screen.getByText("PRICE")).toBeInTheDocument();
    //   expect(screen.getByText("PROPRIETARY NAME")).toBeInTheDocument();
    //   expect(screen.getByText("DOSAGE FORM")).toBeInTheDocument();
    //   expect(screen.getByText("Description")).toBeInTheDocument();
    //   expect(
    //     screen.getByRole("button", {
    //       name: /view cart/i,
    //     })
    //   ).toBeInTheDocument();
    // });
  });

  it("throw error on api call failure", async () => {
    render(
      <Provider store={store}>
        <BrowserRouter>
          <ProductCard
            item={{
              id: 1,
              description: "",
              dosageForm: "liqid",
              imageUrl: "jhakjshdkhk",
              itemId: 45,
              price: 300,
              productId: 33,
              productName: "Crocin",
              productType: false,
              proprietaryName: "jsahdk",
              quantity: 1,
            }}
            added={false}
            loading={false}
          />
        </BrowserRouter>
      </Provider>
    );

    const prodName = screen.getByText(/crocin/i);
    const atoCartBtn = screen.getByRole("button", {
      name: /add to cart/i,
    });

    // const response = {
    //   data: {
    //     cartId: 1,
    //     items: [
    //       {
    //         itemId: 1,
    //         itemIdFk: 45,
    //         itemQuantity: 1,
    //         price: 300,
    //       },
    //     ],
    //     price: 300,
    //     quantity: 1,
    //   },
    // };

    mockAxios.post.mockImplementationOnce(() => Promise.reject());

    await user.click(atoCartBtn);

    // await user.click(atoCartBtn);
    // await waitFor(() => {
    //   expect(screen.getByRole("alert")).toBeInTheDocument();
    // });
  });

  it("should display No data Found when description is empty", async () => {
    render(
      <Provider store={store}>
        <BrowserRouter>
          <ProductCard
            item={{
              id: 1,
              dosageForm: "liqid",
              imageUrl: "jhakjshdkhk",
              itemId: 45,
              price: 300,
              productId: 33,
              productName: "Crocin",
              productType: false,
              proprietaryName: "jsahdk",
              quantity: 0,
            }}
            added={false}
            loading={false}
          />
        </BrowserRouter>
      </Provider>
    );
    const prodName = screen.getByText(/crocin/i);
    const stockStatus = screen.getByText(/outofstock/i);

    expect(stockStatus).toBeInTheDocument();

    global.ResizeObserver = jest.fn().mockImplementation(() => ({
      observe: jest.fn(),
      unobserve: jest.fn(),
      disconnect: jest.fn(),
    }));

    mockAxios.get.mockImplementationOnce(() => Promise.resolve(modalResponse));
    await user.click(prodName);

    await waitFor(() => {
      expect(screen.getByText("Product Summary")).toBeInTheDocument();

      expect(screen.getByText("No Data Found!")).toBeInTheDocument();
    });
  });
});
