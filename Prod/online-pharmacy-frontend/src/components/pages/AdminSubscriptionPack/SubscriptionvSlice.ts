import { createSlice, PayloadAction } from "@reduxjs/toolkit";
import { RootState } from "../../../app/store";
import { Subscriptionv } from "./models";

export interface SubscriptionState {
  subscriptionv: Subscriptionv[];
}

const initialState: SubscriptionState = {
  subscriptionv: [],
};

const SubscriptionvSlice = createSlice({
  name: "viewsubscription",
  initialState,
  reducers: {
    loadData: (
      state: SubscriptionState,
      actions: PayloadAction<Subscriptionv[]>
    ) => {
      const subscriptionv: Subscriptionv[] = actions.payload.map(
        (item: Subscriptionv) => item
      );
      state.subscriptionv = subscriptionv;
    },
  },
});

export const { loadData } = SubscriptionvSlice.actions;
export const selectSubscriptionv = (state: RootState) =>
  state.viewsubscription.subscriptionv;
export default SubscriptionvSlice.reducer;
