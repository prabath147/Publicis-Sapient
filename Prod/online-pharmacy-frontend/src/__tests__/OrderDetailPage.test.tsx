

const response = {
  data: {

    items: [
      {
        itemId: 1,
        itemIdFk: 1,
        itemQuantity: 10,
        price: 200,
      }
      ,
      {
        itemId: 2,
        itemIdFk: 2,
        itemQuantity: 20,
        price: 300,
      }
    ],
    optionalOrderDetails: true,
    orderAddress: {
      addressId: 1,
      city: "ajsldjl",
      country: "kasudo",
      pinCode: "677688",
      state: "AP",
      street: "alsjhdk",
    },
    orderDate: new Date(2022, 9, 20),
    orderDetails: {
      orderDetailsId: 1,
    },
    orderId: 1,
    price: 100,
    quantity: 20,
    userId: 1,

  }
}

const item1 = {
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
it("component created with api error", async () => {
  // mockAxios.get.mockImplementationOnce(() => Promise.resolve(response));

  // render(
  //   <Provider store={store}>
  //     <BrowserRouter>
  //       <OrderDetailPage />
  //     </BrowserRouter>
  //   </Provider>
  // );
  // mockAxios.get.mockImplementationOnce(() => Promise.resolve(item1));
  // await waitForElementToBeRemoved(screen.getByRole('presentation'));
});
