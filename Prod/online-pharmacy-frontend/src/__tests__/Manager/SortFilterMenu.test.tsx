import { render, screen } from "@testing-library/react";
import user from "@testing-library/user-event";
import { Provider } from "react-redux";
import { BrowserRouter } from "react-router-dom";
import { store } from "../../app/store";
import SortFilterMenu from "../../components/pages/Manager/Inventory/SortFilterMenu";

const setOrder = jest.fn();

const setK = jest.fn();

it("component created", () => {
  // mockAxios.get.mockImplementationOnce(() => Promise.reject());
  render(
    <Provider store={store}>
      <BrowserRouter>
        <SortFilterMenu keyword={"acer"} order={"asc"} setOrder={setOrder} setKeyword={setK} />
      </BrowserRouter>
    </Provider>
  );
  // screen.debug();
  expect(screen).toMatchSnapshot();
});

it("click the filters button", async () => {
  // mockAxios.get.mockImplementationOnce(() => Promise.reject());
  render(
    <Provider store={store}>
      <BrowserRouter>
        <SortFilterMenu keyword={"acer"} order={"asc"} setOrder={setOrder} setKeyword={setK} />
      </BrowserRouter>
    </Provider>
  );

  global.ResizeObserver = jest.fn().mockImplementation(() => ({
    observe: jest.fn(),
    unobserve: jest.fn(),
    disconnect: jest.fn(),
  }));

  await user.click(screen.getByRole('button'));

  await user.click(screen.getByTestId('menu1'));
  await user.click(screen.getByTestId('menu2'));
  // await user.click(screen.getByTestId('menu3'));
  // await user.click(screen.getByTestId('menu4'));

  //  waitFor(async () =>{
  //   expect(screen).toMatchSnapshot();
  //   await user.click(screen.getByTestId('menu1'));
  //   // screen.debug();
  //  })
});

it("click the filters button and click 2nd option", async () => {
  // mockAxios.get.mockImplementationOnce(() => Promise.reject());
  render(
    <Provider store={store}>
      <BrowserRouter>
        <SortFilterMenu keyword={"acer"} order={"asc"} setOrder={setOrder} setKeyword={setK} />
      </BrowserRouter>
    </Provider>
  );

  global.ResizeObserver = jest.fn().mockImplementation(() => ({
    observe: jest.fn(),
    unobserve: jest.fn(),
    disconnect: jest.fn(),
  }));

  await user.click(screen.getByRole('button'));

  await user.click(screen.getByTestId('menu2'));
  await user.click(screen.getByTestId('menu3'));

  //  waitFor(async () =>{
  //   expect(screen).toMatchSnapshot();
  //   await user.click(screen.getByTestId('menu1'));
  //   // screen.debug();
  //  })
});
it("click the filters clicking last 2 options", async () => {
  // mockAxios.get.mockImplementationOnce(() => Promise.reject());
  render(
    <Provider store={store}>
      <BrowserRouter>
        <SortFilterMenu keyword={"acer"} order={"asc"} setOrder={setOrder} setKeyword={setK} />
      </BrowserRouter>
    </Provider>
  );

  global.ResizeObserver = jest.fn().mockImplementation(() => ({
    observe: jest.fn(),
    unobserve: jest.fn(),
    disconnect: jest.fn(),
  }));

  await user.click(screen.getByRole('button'));


  await user.click(screen.getByTestId('menu3'));

  //  waitFor(async () =>{
  //   expect(screen).toMatchSnapshot();
  //   await user.click(screen.getByTestId('menu1'));
  //   // screen.debug();
  //  })
});
it("click the filters button", async () => {
  // mockAxios.get.mockImplementationOnce(() => Promise.reject());
  render(
    <Provider store={store}>
      <BrowserRouter>
        <SortFilterMenu keyword={"acer"} order={"asc"} setOrder={setOrder} setKeyword={setK} />
      </BrowserRouter>
    </Provider>
  );

  global.ResizeObserver = jest.fn().mockImplementation(() => ({
    observe: jest.fn(),
    unobserve: jest.fn(),
    disconnect: jest.fn(),
  }));

  await user.click(screen.getByRole('button'));

  await user.click(screen.getByTestId('menu4'));

  //  waitFor(async () =>{
  //   expect(screen).toMatchSnapshot();
  //   await user.click(screen.getByTestId('menu1'));
  //   // screen.debug();
  //  })
});