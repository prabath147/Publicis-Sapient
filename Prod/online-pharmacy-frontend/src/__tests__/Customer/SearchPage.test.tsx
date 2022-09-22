import {
  render,
  screen,
  waitFor,
  waitForElementToBeRemoved
} from "@testing-library/react";
import user from "@testing-library/user-event";
import { Provider } from "react-redux";
import { BrowserRouter } from "react-router-dom";
import { store } from "../../app/store";
import SearchPage from "../../components/pages/Customer/search/SearchPage";
import mockAxios from "../../__mocks__/axios";

const response = {
  data: {
    content: [
      {
        id: 1,
        description: "hakshfk",
        dosageForm: "ahskdhka",
        imageUrl: "ajsdkasd",
        itemId: 16,
        price: 200,
        productId: 12,
        productName: "acer",
        productType: true,
        proprietaryName: "asdhk",
        quantity: 1,
      },
    ],
    totalPages: 10,
    pageable: {
      pageNumber: 1,
    },
  },
};

describe("SearchPage Tests", () => {
  afterEach(() => {
    jest.resetAllMocks();
  });

  it("component created without data", () => {
    // mockAxios.get.mockImplementationOnce(() => Promise.reject());
    render(
      <Provider store={store}>
        <BrowserRouter>
          <SearchPage />
        </BrowserRouter>
      </Provider>
    );
    expect(screen.getByTestId(/Loading/i)).toBeInTheDocument();
    expect(screen).toMatchSnapshot();
  });

  it("component created with item data", async () => {
    mockAxios.get.mockImplementationOnce(() => Promise.resolve(response));
    render(
      <Provider store={store}>
        <BrowserRouter>
          <SearchPage />
        </BrowserRouter>
      </Provider>
    );
    await waitForElementToBeRemoved(screen.getByRole("presentation"));

    // screen.debug();

    // expect(asFragment()).toMatchSnapshot();
  }, 30000);
});

describe("Tests", () => {
  afterEach(() => {
    jest.resetAllMocks();
  });

  it("component created", () => {
    mockAxios.get.mockImplementationOnce(() => Promise.resolve(response));

    render(
      <Provider store={store}>
        <BrowserRouter>
          <SearchPage />
        </BrowserRouter>
      </Provider>
    );
    expect(screen).toMatchSnapshot();
  });

  it("Data Loaded for Search component", () => {
    mockAxios.get.mockImplementationOnce(() => Promise.resolve(response));
    render(
      <Provider store={store}>
        <BrowserRouter>
          <SearchPage />
        </BrowserRouter>
      </Provider>
    );

    expect(screen).toMatchSnapshot();
  });

  it("elments are rendered Properly", async () => {
    mockAxios.get.mockImplementationOnce(() => Promise.resolve(response));
    render(
      <Provider store={store}>
        <BrowserRouter>
          <SearchPage />
        </BrowserRouter>
      </Provider>
    );
    await waitForElementToBeRemoved(screen.getByRole("presentation"));

    const container = screen.getByTestId("searchT");
    const SearchGoButton = screen.getByRole("button", {
      name: /searchgobutton/i,
    });
    const pages = screen.getByTestId("paginationT");
    const mockOnClick = jest.fn();
    expect(container).toBeInTheDocument();
    expect(SearchGoButton).toBeInTheDocument();
    expect(pages).toBeInTheDocument();
    // await screen.debug();
  });

  // it("drawer opens on clicking filters button", async () => {
  //   mockAxios.get.mockImplementationOnce(() => Promise.resolve(response));
  //   render(
  //     <Provider store={store}>
  //       <BrowserRouter>
  //         <SearchPage />
  //       </BrowserRouter>
  //     </Provider>
  //   );
  //   await waitForElementToBeRemoved(screen.getByRole("presentation"));

  // const container = screen.getByTestId("searchT");
  // const Searchinput = screen.getByRole("searchbox");
  // const filterButton = screen.getByRole("button", { name: /filterbutton/i });
  // const SearchGoButton = screen.getByRole("button", {
  //   name: /searchgobutton/i,
  // });
  // const drawer = screen.getByTestId("drawerT")
  // await user.type(Searchinput, "acer");
  // expect(Searchinput).toHaveValue("acer");
  // await user.click(filterButton);
  // expect(screen.getByTestId("drawerT")).toBeInTheDocument();
  // expect(screen.getByText(/select filters/i)).toBeInTheDocument();
  // expect(screen.getByTestId("toggleT")).toBeInTheDocument();
  // expect(screen.getByText(/sort by price/i)).toBeInTheDocument();
  // expect(screen.getByText(/low to high/i)).toBeInTheDocument();
  // expect(screen.getByText(/high to low/i)).toBeInTheDocument();
  // expect(
  //   screen.getByRole("button", {
  //     name: /apply filters/i,
  //   })
  //   ).toBeInTheDocument();

  //   expect(
  //     screen.getByRole("checkbox", {
  //       name: /generic/i,
  //     })
  //   ).toBeInTheDocument();

  //   // screen.debug();
  // });

  it("user events on drawer should trigger correct actions ", async () => {
    mockAxios.get.mockImplementationOnce(() => Promise.resolve(response));
    render(
      <Provider store={store}>
        <BrowserRouter>
          <SearchPage />
        </BrowserRouter>
      </Provider>
    );
    await waitForElementToBeRemoved(screen.getByRole("presentation"));

    const filterButton = screen.getByRole("button", { name: /filterbutton/i });
    await user.click(filterButton);

    user.click(
      screen.getByRole("checkbox", {
        name: /generic/i,
      })
    );

    await waitFor(() =>
      expect(
        screen.getByRole("checkbox", { name: /generic/i })
      ).toHaveAttribute("checked")
    );

    await user.click(
      screen.getByRole("button", {
        name: /apply filters/i,
      })
    );

    await waitFor(() => {
      expect(screen.queryByText("Select Filters")).not.toBeInTheDocument();
      // expect(screen.queryByText("Generic")).not.toBeInTheDocument();
      expect(screen.queryByText("Sort by Price")).not.toBeInTheDocument();
      expect(screen.queryByText("Low to High")).not.toBeInTheDocument();
      expect(screen.queryByText("High to Low")).not.toBeInTheDocument();
    });
  }, 30000);

  it("should close the filter button on clicking esc", async () => {
    // mockAxios.get.mockImplementationOnce(() => Promise.resolve(response));
    // render(
    //   <Provider store={store}>
    //     <BrowserRouter>
    //       <SearchPage />
    //     </BrowserRouter>
    //   </Provider>
    // );

    // await waitForElementToBeRemoved(screen.getByRole("presentation"));

    // const filterButton = screen.getByRole("button", { name: /filterbutton/i });
    // await user.click(filterButton);

    // user.keyboard("[Escape]");

    // await waitFor(() => {
    //   expect(screen.queryByText("Select Filters")).not.toBeInTheDocument();
    //   // expect(screen.queryByText("Generic")).not.toBeInTheDocument();
    //   expect(screen.queryByText("Sort by Price")).not.toBeInTheDocument();
    //   expect(screen.queryByText("Low to High")).not.toBeInTheDocument();
    //   expect(screen.queryByText("High to Low")).not.toBeInTheDocument();
    // });
  });

  //   it("should render Pagination", async () => {
  //      mockAxios.get.mockImplementationOnce(() => Promise.resolve(response));
  // render(
  //       <Provider store={store}>
  //         <BrowserRouter>
  //           <SearchPage />
  //         </BrowserRouter>
  //       </Provider>
  //     );

  //     await waitForElementToBeRemoved(screen.getByRole('presentation'));

  //     expect(
  //       screen.getByRole("button", {
  //         name: /1/i,
  //       })
  //     ).toBeInTheDocument();
  //     const jump = screen.getByPlaceholderText(/jump to\.\.\./i);
  //     expect(jump).toBeInTheDocument();
  //     await user.type(jump, "10");

  //     await user.click(
  //       screen.getByRole("button", {
  //         name: /jump/i,
  //       })
  //     );

  //   expect(jump).toHaveValue("1");
  // }, 30000);

  //TODO "CHECK"

  it("should render Search go button", async () => {
    mockAxios.get.mockImplementationOnce(() => Promise.resolve(response));
    render(
      <Provider store={store}>
        <BrowserRouter>
          <SearchPage />
        </BrowserRouter>
      </Provider>
    );

    await waitForElementToBeRemoved(screen.getByRole("presentation"));

    const sgobtn = screen.getByRole("button", {
      name: /searchgobutton/i,
    });
    expect(sgobtn).toBeInTheDocument();

    await user.click(screen.getByRole("searchbox"));

    // let nevent;
    // document.addEventListener = jest.fn((event, cb) => {
    //     events[event]= cb;
    // });

    await user.keyboard("{Shift>}A{/Shift}");
    // events.keyup({key: 's'});

    await user.type(screen.getByRole("searchbox"), "acer");

    await user.click(sgobtn);

    await user.click(screen.getByRole("searchbox"));

    await user.keyboard("[Enter]");
    // expect(screen.getByText);
  }, 30000);

  it("Empty search should display alert", async () => {
    mockAxios.get.mockImplementationOnce(() => Promise.resolve(response));
    render(
      <Provider store={store}>
        <BrowserRouter>
          <SearchPage />
        </BrowserRouter>
      </Provider>
    );

    await waitForElementToBeRemoved(screen.getByRole("presentation"));

    const sgobtn = screen.getByRole("button", {
      name: /searchgobutton/i,
    });

    await user.click(sgobtn);
  }, 30000);

  // it("No data found if item not found", async () => {
  //      mockAxios.get.mockImplementationOnce(() => Promise.resolve(response));
  // render(
  //       <Provider store={store}>
  //         <BrowserRouter>
  //           <SearchPage />
  //         </BrowserRouter>
  //       </Provider>
  //     );
  //     await waitForElementToBeRemoved(screen.getByTestId('spinner'));

  //     const sgobtn = screen.getByRole("button", {
  //       name: /searchgobutton/i,
  //     });

  //     await user.type(screen.getByRole("searchbox"), "acer");
  //     await user.click(sgobtn);

  //     expect(screen.getByTestId("toggleLink")).toBeInTheDocument();

  //     await user.click(screen.getByTestId("toggleLink"));

  //     const response = {
  //       data: {
  //         0: "acer",
  //         1: "paracetamol",
  //         2: "crocin",
  //         3: "rubrum",
  //       },
  //     };

  //     mockAxios.get.mockImplementationOnce(() => Promise.resolve(response));

  //     fireEvent.keyDown(screen.getByTestId("sinputT"), {
  //       key: "a",
  //       code: "KeyA",
  //       keyCode: 65,
  //     });

  //     mockAxios.get.mockImplementationOnce(() => Promise.reject());

  //     fireEvent.keyDown(screen.getByTestId("sinputT"), {
  //       key: "a",
  //       code: "KeyA",
  //       keyCode: 65,
  //     });

  //     screen.debug();
  //   }, 30000);

  //TODO "CHECK"
  // it("length of the cards on render", () =>  {
  //   // const cardContainer = screen.getByTestId('item-cards');
  //   // const cards = cardContainer.querySelectorAll('Card');
  //   // expect(cards.length).toBeGreaterThan(0);
  //   screen.debug();
  //   // cards.forEach((card) => {
  //   //     expect(card).toBeInTheDocument();
  //   // })
  // });
});
