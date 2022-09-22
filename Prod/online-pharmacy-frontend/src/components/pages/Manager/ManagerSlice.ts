import { createSlice, PayloadAction } from "@reduxjs/toolkit";
import { RootState } from "./../../../app/store";
import { InventoryPage, Message, Store, StoreInSlicetype, StoreListPage } from "./models";
const storeInit = {
  storeId: 0,
  storeName: '',
  address: {
    addressId: 0,
    street: '',
    city: '',
    state: '',
    pinCode: 0,
    country: '',
  },
  manager: {
    managerId: 0,
    name: '',
    phoneNo: '',
    licenseNo: '',
    registrationDate: '',
    approvalStatus: ''
  },
  createdDate: '',
  revenue: 0
}
const initialState: StoreInSlicetype = {
  storeListPage: new StoreListPage(),
  // store: new Store,
  store: storeInit,
  message: new Message(),
  loading: false,
  inventory: new InventoryPage()
};

const StoreSlice = createSlice({
  name: "store",
  initialState,
  reducers: {
    load: (state: StoreInSlicetype) => {
      state.storeListPage = new StoreListPage();
      state.loading = true;
      state.message = {
        text: "",
        type: ""
      };
    },
    setMessage: (state: StoreInSlicetype, actions: PayloadAction<Message>) => {
      state.message = actions.payload;
      state.loading = false;
    },
    loadStoreList: (state: StoreInSlicetype, actions: PayloadAction<StoreListPage>) => {
      state.storeListPage = actions.payload;
      state.loading = false;
    },
    loadStoreDetails: (state: StoreInSlicetype, actions: PayloadAction<Store>) => {
      state.store = actions.payload;
      state.loading = false;
    },
    createStore: (state: StoreInSlicetype, actions: PayloadAction<Store>) => {
      const newStore = actions.payload;
      // state.storeListPage = {data:[newStore,...state.storeListPage.data], ...state.storeListPage};
      state.storeListPage = {
        data: [newStore, ...state.storeListPage.data],
        pageNumber: state.storeListPage.pageNumber,
        pageSize: state.storeListPage.pageSize,
        totalRecords: state.storeListPage.totalPages,
        totalPages: state.storeListPage.totalPages,
        isLastPage: state.storeListPage.isLastPage
      };
      state.loading = false;
    },
    loadInventory: (state: StoreInSlicetype, actions: PayloadAction<InventoryPage>) => {
      state.inventory = actions.payload;
      state.loading = false;
    }
  },
});

export const { load, setMessage, loadStoreList, createStore, loadStoreDetails, loadInventory } = StoreSlice.actions;

export const getStoreList = (state: RootState): StoreListPage => {
  return state.manager.storeListPage;
};

export const getManagerStoreData = (state: RootState): StoreInSlicetype => {
  return state.manager;
};

export default StoreSlice.reducer;
