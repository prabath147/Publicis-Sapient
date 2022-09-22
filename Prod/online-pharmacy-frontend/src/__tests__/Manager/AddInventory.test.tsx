// const setOpen = jest.fn();
// const response = {
//   data: {
//     0: "acer",
//     1: "rubrum",
//     2: "crocin",
//   },
// };

// test("component created with data", () => {
//   mockAxios.get.mockImplementationOnce(() => Promise.resolve(response));

//   render(
//     <Provider store={store}>
//       <AddInventory
//         storeId={1}
//         openInventoryModal={true}
//         setOpenInventoryModal={setOpen}
//       />
//     </Provider>
//   );
//   expect(screen).toMatchSnapshot();
// });

//TODO "CHECK"
test("component created without data", () => {
  // mockAxios.get.mockImplementationOnce(() => Promise.reject("error"));
  // render(
  //   <Provider store={store}>
  //     <AddInventory
  //       storeId={1}
  //       openInventoryModal={true}
  //       setOpenInventoryModal={setOpen}
  //     />
  //   </Provider>
  // );
  // expect(screen).toMatchSnapshot();
});

// test("user events", async () => {
//   mockAxios.get.mockImplementationOnce(() => Promise.resolve(response));

//   render(
//     <Provider store={store}>
//       <AddInventory
//         storeId={1}
//         openInventoryModal={true}
//         setOpenInventoryModal={setOpen}
//       />
//     </Provider>
//   );

//   mockAxios.get.mockImplementationOnce(() => Promise.resolve(response));

//   // await user.click(screen.getByTestId("uploadBtn"));
//   await user.type(
//     screen.getByRole("textbox", {
//       name: /product name/i,
//     }),
//     "a"
//   );

//   screen.debug();
// });
