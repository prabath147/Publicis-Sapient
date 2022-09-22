import { createSlice, PayloadAction } from "@reduxjs/toolkit";
import { RootState } from "../../../../app/store";
import { Store } from "./models";

export interface StoresState {
  stores: Store[];
}

const initialState: StoresState = {
  stores: [],
};

const StoreSlice=createSlice({
    name:"storeL",
    initialState,
    reducers:{
        loadStoreData: (state:StoresState, actions: PayloadAction<Store[]>)=>{
            const stores:Store[]=actions.payload.map((item:Store)=>item);
            state.stores=stores;
        },
        updateStoreData:(state:StoresState, actions: PayloadAction<Store>)=>{
            state.stores=state.stores.map(store => {
                if (store.storeId === actions.payload.storeId) {
                    return actions.payload;
                }
                else {
                    return store;
                }
            });
        }
            
    },
});

export const { loadStoreData, updateStoreData }=StoreSlice.actions;
export const selectStores=(state:RootState)=> state.storeL.stores;
export default StoreSlice.reducer;
