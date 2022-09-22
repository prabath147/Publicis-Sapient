

const response = {
  data: {
    userId: 1,
    userSubsSet: [
      {
        userSubId: 1,
        userId: 1,
        subscriptions: {
          subscriptionId: 16,
          subscriptionName: "Bronzen offer 7",
          description: "50pk jfn ejfqedf",
          startDate: "2022-09-17",
          endDate: "2022-09-18",
          subscriptionCost: 50,
          benefits: {
            delivery_discount: 50,
            one_day_delivery: false
          },
          lastPaidDate: "2022-09-19",
          subEndDate: "2022-09-20",
          status: "PAID",
        }
      }
    ]
  }
}

it("component created", async () => {
  // mockAxios.get.mockImplementationOnce(() => Promise.resolve(response));
  // // mockAxios.get.mockImplementationOnce(() => Promise.resolve(response));

  // render(
  //   <Provider store={store}>
  //     <BrowserRouter>
  //       <OrderCreatePage/>
  //     </BrowserRouter>
  //   </Provider>
  // );
  // expect(screen).toMatchSnapshot();
  // await waitForElementToBeRemoved(screen.getByRole('presentation'));
  // screen.debug();

});

it("component created without data", async () => {
  // mockAxios.get.mockImplementationOnce(() => Promise.reject());

  // render(
  //   <Provider store={store}>
  //     <BrowserRouter>
  //       <OrderCreatePage/>
  //     </BrowserRouter>
  //   </Provider>
  // );
  // expect(screen).toMatchSnapshot();
  // // await waitForElementToBeRemoved(screen.getByRole('presentation'));


  // // await user.type(screen.getByPlaceholderText(/street/i),"")
  // screen.debug();

});

//   it("component created", async () => {
//     mockAxios.get.mockImplementationOnce(() => Promise.resolve(response));

//     render(
//       <Provider store={store}>
//         <BrowserRouter>
//           <OrderCreatePage/>
//         </BrowserRouter>
//       </Provider>
//     );
//     expect(screen).toMatchSnapshot();
//     await waitForElementToBeRemoved(screen.getByRole('presentation'));
//     screen.debug();

//   });

