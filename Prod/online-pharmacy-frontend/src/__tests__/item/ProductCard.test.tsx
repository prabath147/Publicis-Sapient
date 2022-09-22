import { render } from "@testing-library/react";
import { Provider } from "react-redux";
import { BrowserRouter } from "react-router-dom";
import { store } from "../../app/store";
import ProductCard from "../../components/pages/item/ProductCard";

const item = {
  description: "ajkdkasdk",
  dosageForm: "ahskdjkas",
  id: "1",
  imageUrl: "ajskdjkas",
  itemId: 6,
  price: 200,
  productId: 12,
  productName: "Acer",
  productType: true,
  proprietaryName: "ahkkimw",
  quantity: 10,
  storeId: 1,
}

it("component created with data", async () => {
  // mockAxios.get.mockImplementationOnce(() => Promise.resolve(response));
  render(
    <Provider store={store}>
      <BrowserRouter>
        <ProductCard item={item} />
      </BrowserRouter>
    </Provider>
  );
  // screen.debug();
});
