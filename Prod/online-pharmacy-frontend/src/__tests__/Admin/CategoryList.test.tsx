import { act, fireEvent, render, waitForElementToBeRemoved } from "@testing-library/react";
import { Provider } from "react-redux";
import { BrowserRouter } from "react-router-dom";
import renderer from "react-test-renderer";
import { store } from "../../app/store";
import CategoryList from "../../components/pages/Admin/categoryList/CategoryList";
import mockAxios from "../../__mocks__/axios";

const MockCategoryL = () => {
  return (
    <Provider store={store}>
      <BrowserRouter>
        <CategoryList />
      </BrowserRouter>
    </Provider>
  );
};

it("component created", () => {
  const component = renderer.create(<MockCategoryL />);
  const tree = component.toJSON();
  expect(tree).toMatchSnapshot();
});

const category = {
  data: [{ categoryId: 10000042, categoryName: "category0" }],
  pageNumber: 0,
  pageSize: 10,
  totalRecords: 1,
  totalPages: 1,
  isLastPage: true,
};
describe("Category list", () => {
  afterEach(() => {
    jest.resetAllMocks();
  });


  it("component created without data", async () => {
    mockAxios.get.mockImplementation(() => Promise.resolve({
      data: category
    }));

    const { asFragment, getByTestId } = render(<MockCategoryL />);
    await waitForElementToBeRemoved(getByTestId(/Loading/i));

    expect(asFragment()).toMatchSnapshot();
  });

  it("component created with fail event", async () => {
    mockAxios.get.mockImplementation(() => Promise.reject());

    const { asFragment, getByTestId } = render(<MockCategoryL />);

    await waitForElementToBeRemoved(getByTestId(/Loading/i));

    expect(asFragment()).toMatchSnapshot();
  });

  it("Creating category with incorrect data", async () => {
    mockAxios.get.mockImplementation(() =>
      Promise.resolve({ data: { data: [] } })
    );
    mockAxios.post.mockImplementation(() =>
      Promise.resolve()
    );
    const { asFragment, getByTestId } = render(<MockCategoryL />);

    await waitForElementToBeRemoved(getByTestId(/Loading/i));

    await act(async () => {
      fireEvent.change(getByTestId(/CategoryInput/i), {
        target: { value: "c" }
      })
    })

    await act(async () => {
      fireEvent.click(getByTestId(/Create-Category-Button/i));
    })

    // await user
    //   .click(createBtn)
    //   .then(mockAxios.post.mockImplementationOnce(() => Promise.reject()));
  });

  it("Creating category with correct data", async () => {
    mockAxios.get.mockImplementation(() =>
      Promise.resolve({ data: { data: [] } })
    );
    mockAxios.post.mockImplementation(() =>
      Promise.resolve()
    );
    const { asFragment, getByTestId } = render(<MockCategoryL />);

    await waitForElementToBeRemoved(getByTestId(/Loading/i));

    await act(async () => {
      fireEvent.change(getByTestId(/CategoryInput/i), {
        target: { value: "c" }
      })
    })

    await act(async () => {
      fireEvent.click(getByTestId(/Create-Category-Button/i));
    })

    // await act(async () => {
    //   fireEvent.click(getByTestId(/Create-Category-Button/i))
    // .then(
    //   mockAxios.post.mockImplementationOnce(() =>
    //     Promise.resolve({
    //       categoryId: 10000226,
    //       categoryName: " category700",
    //     })
    //   )
    // )
    // .then(
    //   mockAxios.get.mockImplementationOnce(() =>
    //     Promise.resolve({
    //       data: {
    //         data: [{ categoryId: 10000226, categoryName: "category700" }],
    //         pageNumber: 0,
    //         pageSize: 10,
    //         totalRecords: 11,
    //         totalPages: 2,
    //         isLastPage: false,
    //       },
    //     })
    //   )
    // );
    // })

  });
});


