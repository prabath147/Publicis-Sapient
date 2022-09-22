import { Grid } from "@mantine/core";
import { render, screen } from "@testing-library/react";
import user from "@testing-library/user-event";
import AddProductForm from "../../components/pages/Manager/Inventory/AddProductForm";
import mockAxios from "../../__mocks__/axios";

const setOp = jest.fn(() => true);

const response = {
  data: {
    data: [
      {
        categoryId: 1,
        categoryName: "d1",
      },
      {
        categoryId: 2,
        categoryName: "d2",
      },
      {
        categoryId: 3,
        categoryName: "d3",
      },
    ],
  },
};

describe("Add Product", () => {
  afterEach(() => {
    jest.resetAllMocks();
  });
  test("component created with data", async () => {
    mockAxios.get.mockImplementationOnce(() => Promise.resolve(response));
    const setOpen = jest.fn();

    render(
      <Grid>
        <AddProductForm setProductFormOpen={setOpen} />
      </Grid>
    );

    expect(screen).toMatchSnapshot();

    //   await act(async () => {
    //     user.type(
    //       screen.getByRole("textbox", {
    //         name: /product name/i,
    //       }),
    //       "Paracetamol"
    //     );

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

    // user.type(
    //   screen.getByRole("textbox", {
    //     name: /dosage Form/i,
    //   }),
    //   "solid"
    // );
  });

  test("should click submit without filling", async () => {
    mockAxios.get.mockImplementationOnce(() => Promise.resolve(response));
    const setOpen = jest.fn();

    render(
      <Grid>
        <AddProductForm setProductFormOpen={setOpen} />
      </Grid>
    );
    await user.click(
      screen.getByRole("button", {
        name: /add product/i,
      })
    );
  }, 30000);

  test("should click cancel", async () => {
    mockAxios.get.mockImplementationOnce(() => Promise.resolve(response));
    const setOpen = jest.fn();

    render(
      <Grid>
        <AddProductForm setProductFormOpen={setOpen} />
      </Grid>
    );
    await user.click(
      screen.getByRole("button", {
        name: /cancel/i,
      })
    );
  }, 30000);

  test("should click submit after filling", async () => {
    mockAxios.get.mockImplementationOnce(() => Promise.resolve(response));
    const setOpen = jest.fn(() => true);

    render(
      <Grid>
        <AddProductForm setProductFormOpen={setOpen} />
      </Grid>
    );

    await user.type(
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

    await user.type(
      screen.getByRole("textbox", {
        name: /proprietary name/i,
      }),
      "xyzcompany"
    );
    user.type(
      screen.getByRole("textbox", {
        name: /dosage Form/i,
      }),
      "solid"
    );

    user.type(
      screen.getByRole("textbox", {
        name: /description/i,
      }),
      "cures itching"
    );
    user.type(screen.getByLabelText(/image url \*/i), "hakdjl");
    user.type(screen.getByTestId("catT"), "d3");
    user.type(screen.getByLabelText(/product type/i), "Generic");
    // screen.debug();
    user.click(
      screen.getByRole("button", {
        name: /add product/i,
      })
    );
  }, 30000);
})