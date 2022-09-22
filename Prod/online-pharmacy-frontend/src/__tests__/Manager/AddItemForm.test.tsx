import { Grid } from "@mantine/core";
import { render, screen } from "@testing-library/react";
import user from "@testing-library/user-event";
import { Provider } from "react-redux";
import { act } from "react-test-renderer";
import { store } from "../../app/store";
import AddItemForm from "../../components/pages/Manager/Inventory/AddItemForm";
import mockAxios from "../../__mocks__/axios";

const setClose = jest.fn();
const response = {
  data: {
    0: "acer",
    1: "rubrum",
    2: "crocin",
  },
};

describe("Add Item Form", () => {
  afterEach(() => {
    jest.resetAllMocks();
  });


  test("component created without data", () => {
    mockAxios.get.mockImplementationOnce(() => Promise.reject());

    render(
      <Provider store={store}>
        <Grid>
          <AddItemForm
            storeId={1}
            closeModal={setClose}
            setProductFormOpen={true}
          />
        </Grid>
      </Provider>
    );
    expect(screen).toMatchSnapshot();
  });

  test("Should click add btn", async () => {
    // mockAxios.get.mockImplementationOnce(() => Promise.resolve(response));
    // render(
    //   <Provider store={store}>
    //     <Grid>
    //       <AddItemForm
    //         storeId={1}
    //         closeModal={setClose}
    //         setProductFormOpen={false}
    //       />
    //     </Grid>
    //   </Provider>
    // );
    // await act(async () => {
    //   user.click(screen.getByTestId("addbtn"));
    // });
    // screen.debug();
  });

  // test("Should fill the form", async () => {
  //   // mockAxios.get.mockImplementationOnce(() => Promise.resolve(response));

  //   render(
  //     <Provider store={store}>
  //       <Grid>
  //         <AddItemForm
  //           storeId={1}
  //           closeModal={setClose}
  //           setProductFormOpen={false}
  //         />
  //       </Grid>
  //     </Provider>
  //   );

  //TODO "CHECK"
  // mockAxios.get.mockImplementationOnce(() => Promise.resolve(response));
  // global.ResizeObserver = jest.fn().mockImplementation(() => ({
  //   observe: jest.fn(),
  //   unobserve: jest.fn(),
  //   disconnect: jest.fn(),
  // }));
  // await act(async () => {
  //   user.type(
  //     screen.getByRole("textbox", {
  //       name: /product name/i,
  //     }),
  //     "a"
  //   );

  //   user.type(
  //     screen.getByRole("textbox", {
  //       name: /product id/i,
  //     }),
  //     "172800"
  //   );

  //   user.click(
  //     screen.getByRole("button", {
  //       name: /search/i,
  //     })
  //   );

  //   user.type(
  //     screen.getByLabelText(/manufactured date \*/i),
  //     "September 18, 2022"
  //   );
  //   user.type(screen.getByLabelText(/expiry date \*/i), "September 18, 2023");
  //   user.type(screen.getByLabelText(/quantity \*/i), "10");
  //   user.type(screen.getByLabelText(/price \*/i), "100");
  //   user.click(screen.getByTestId("addbtn"));
  // user.type(
  //   screen.getByRole("textbox", {
  //     name: /dosage Form/i,
  //   }),
  //   "solid"
  // );

  // user.type(
  //   screen.getByRole("textbox", {
  //     name: /description/i,
  //   }),
  //   "cures itching"
  // );

  // user.type(screen.getByLabelText(/image url \*/i), "hakdjl");

  // user.selectOptions(
  //   screen.getByRole("searchbox", {
  //     name: /catagory type/i,
  //   }),
  //   "category1"
  // );

  // screen.debug();
  // }, 30000);

  //     user.type(
  //       screen.getByRole("textbox", {
  //         name: /description/i,
  //       }),
  //       "cures itching"
  //     );

  //     user.type(screen.getByLabelText(/image url \*/i), "hakdjl");

  //     user.selectOptions(
  //       screen.getByRole("searchbox", {
  //         name: /catagory type/i,
  //       }),
  //       "category1"
  //     );

  //     user.selectOptions(
  //       screen.getByRole("searchbox", {
  //         name: /product type/i,
  //       }),
  //       "Generic"
  //     );
  //   });
  // });

  test("component created with data", async () => {
    mockAxios.get.mockImplementationOnce(() => Promise.resolve(response));
    const setOp = jest.fn(() => true);

    render(
      <Provider store={store}>
        <Grid>
          <AddItemForm
            storeId={1}
            closeModal={setClose}
            setProductFormOpen={setOp}
          />
        </Grid>
      </Provider>
    );

    await act(async () => {
      user.type(
        screen.getByRole("textbox", {
          name: /product name/i,
        }),
        "Paracetamol"
      );

      user.type(
        screen.getByRole("textbox", {
          name: /product id/i,
        }),
        "172800"
      );

      user.click(screen.getByTestId("idx"));
      user.click(screen.getByTestId("namex"));

      user.click(
        screen.getByRole("button", {
          name: /search/i,
        })
      );

      user.click(screen.getByTestId("register"));

      // screen.debug();
    });
  })

  test("Should fill the form", async () => {
    mockAxios.get.mockImplementationOnce(() => Promise.resolve(response));

    render(
      <Provider store={store}>
        <Grid>
          <AddItemForm
            storeId={1}
            closeModal={setClose}
            setProductFormOpen={false}
          />
        </Grid>
      </Provider>
    );
    mockAxios.get.mockImplementationOnce(() => Promise.resolve(response));
    global.ResizeObserver = jest.fn().mockImplementation(() => ({
      observe: jest.fn(),
      unobserve: jest.fn(),
      disconnect: jest.fn(),
    }));
    await act(async () => {
      user.type(
        screen.getByRole("textbox", {
          name: /product name/i,
        }),
        "a"
      );

      user.type(
        screen.getByRole("textbox", {
          name: /product id/i,
        }),
        "172800"
      );

      user.click(
        screen.getByRole("button", {
          name: /search/i,
        })
      );

      user.type(
        screen.getByLabelText(/manufactured date \*/i),
        "September 18, 2022"
      );
      user.type(screen.getByLabelText(/expiry date \*/i), "September 18, 2023");
      user.type(screen.getByLabelText(/quantity \*/i), "10");
      user.type(screen.getByLabelText(/price \*/i), "100");
      user.click(screen.getByTestId("addbtn"));
      // user.type(
      //   screen.getByRole("textbox", {
      //     name: /dosage Form/i,
      //   }),
      //   "solid"
      // );

      // user.type(
      //   screen.getByRole("textbox", {
      //     name: /description/i,
      //   }),
      //   "cures itching"
      // );

      // user.type(screen.getByLabelText(/image url \*/i), "hakdjl");

      // user.selectOptions(
      //   screen.getByRole("searchbox", {
      //     name: /catagory type/i,
      //   }),
      //   "category1"
      // );

      // screen.debug();
    });

    //     user.type(
    //       screen.getByRole("textbox", {
    //         name: /description/i,
    //       }),
    //       "cures itching"
    //     );

    //     user.type(screen.getByLabelText(/image url \*/i), "hakdjl");

    //     user.selectOptions(
    //       screen.getByRole("searchbox", {
    //         name: /catagory type/i,
    //       }),
    //       "category1"
    //     );

    //     user.selectOptions(
    //       screen.getByRole("searchbox", {
    //         name: /product type/i,
    //       }),
    //       "Generic"
    //     );
    //   });
  });

  test("component created with data", async () => {
    // mockAxios.get.mockImplementationOnce(() => Promise.resolve(response));
    // const setOp = jest.fn(() => true);

    // render(
    //   <Provider store={store}>
    //     <Grid>
    //       <AddItemForm
    //         storeId={1}
    //         closeModal={setClose}
    //         setProductFormOpen={setOp}
    //       />
    //     </Grid>
    //   </Provider>
    // );

    // await act(async () => {
    //   user.type(
    //     screen.getByRole("textbox", {
    //       name: /product name/i,
    //     }),
    //     "Paracetamol"
    //   );

    //   user.type(
    //     screen.getByRole("textbox", {
    //       name: /product id/i,
    //     }),
    //     "172800"
    //   );

    //   user.click(screen.getByTestId("idx"));
    //   user.click(screen.getByTestId("namex"));

    //   user.click(
    //     screen.getByRole("button", {
    //       name: /search/i,
    //     })
    //   );

    //   user.click(screen.getByTestId("register"));

    //   screen.debug();
    // });
  });

})
