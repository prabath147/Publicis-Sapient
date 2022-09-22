import { render, screen } from "@testing-library/react";
import ProductUpdateForm from "../../components/pages/productUpdate/ProductUpdateForm";
import mockAxios from "../../__mocks__/axios";


const response = {
  data: {
    id: 1,
    productName: "acer",
    proprietaryName: "haha Labs",
    description: "Lorem ipsum",
    dosageForm: "ointment",
    categories: ["aa", "bb"],
    quantity: 1,
    image: "img",
    productType: true,
  },
};

describe("ProductUpdateForm", () => {
  afterEach(() => {
    jest.resetAllMocks();
  });

  it("should update the product", () => {

    mockAxios.get.mockImplementationOnce(() => Promise.resolve(response));
    // mockAxios.get.mockImplementationOnce(() => Promise.resolve(response));

    render(<ProductUpdateForm />);
    expect(screen).toMatchSnapshot();

  })

  it('render with call passed', () => {
    mockAxios.get.mockImplementationOnce(() => Promise.resolve(response));
    mockAxios.post.mockImplementationOnce(() => Promise.resolve());

    const { asFragment, getByTestId } = render(
      <ProductUpdateForm />
    )

    mockAxios.post.mockImplementationOnce(() =>
      Promise.resolve()
    )
  })

});
