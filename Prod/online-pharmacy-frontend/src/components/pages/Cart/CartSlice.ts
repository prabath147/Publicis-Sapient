import { createSlice, PayloadAction } from "@reduxjs/toolkit";
import { RootState } from "../../../app/store";
import { CartSliceType, ItemMiniDTO } from "./models";

const initialState: CartSliceType = {
  items: [],
  selected: [],
  totalAmount: 0,
  totalItem: 0,
};

const CartSlice = createSlice({
  name: "cart",
  initialState,
  reducers: {
    loadCart: (state: CartSliceType, actions: PayloadAction<ItemMiniDTO[]>) => {
      console.log(actions.payload)
      state.items = actions.payload;
      // state.totalItem = actions.payload.quantity;
      // state.totalAmount = actions.payload.price;

      let totalPrice = 0;
      let totalQuantity = 0;

      for (const item of actions.payload) {
        totalPrice += item.itemQuantity * item.price;
        totalQuantity += item.itemQuantity
      }
      state.totalAmount = totalPrice;
      state.totalItem = totalQuantity;
    },


    addItemToCart: (state: CartSliceType, actions: PayloadAction<ItemMiniDTO>) => {
      state.items.push(actions.payload);
      state.totalItem += 1;
      state.totalAmount += actions.payload.price * actions.payload.itemQuantity;
    },

    removeItemFromCart: (
      state: CartSliceType,
      actions: PayloadAction<ItemMiniDTO>
    ) => {
      state.items = state.items.filter(
        (item) => item.itemIdFk !== actions.payload.itemIdFk
      );

      state.selected = state.selected.filter(
        (item) => item !== actions.payload.itemIdFk
      );

      state.totalItem -= 1;
      state.totalAmount -= actions.payload.price * actions.payload.itemQuantity;
    },

    incrementQuantity: (
      state: CartSliceType,
      actions: PayloadAction<number>
    ) => {
      state.items = state.items.map((curElem) => {
        if (curElem.itemIdFk === actions.payload) {
          state.totalAmount += curElem.price;
          // console.log('done');
          return { ...curElem, itemQuantity: curElem.itemQuantity + 1 };
        }
        return curElem;
      });
    },

    decrementQuantity: (
      state: CartSliceType,
      actions: PayloadAction<number>
    ) => {
      let total = 0;
      state.items = state.items
        .map((curElem) => {
          if (curElem.itemId === actions.payload) {
            state.totalAmount -= curElem.price;
            return { ...curElem, itemQuantity: curElem.itemQuantity - 1 };
          }
          return curElem;
        })
        .filter((curElem) => {
          if (curElem.itemQuantity !== 0) {
            total += 1;
            return true;
          }
          return false;
        });

      state.totalItem = total;
    },

    toggleSelect: (state: CartSliceType, actions: PayloadAction<number>) => {
      const index = state.selected.indexOf(actions.payload);

      if (index > -1) {
        state.selected = state.selected.filter((id) => id !== actions.payload);
      } else {
        state.selected.push(actions.payload);
      }
    },

    clearCart: (state: CartSliceType) => {
      // console.log("clear");

      state.items = [];
      state.selected = [];
      state.totalAmount = 0;
      state.totalItem = 0;
    },
  },
});

export const getCartItems = (state: RootState): ItemMiniDTO[] => {
  // return [{ itemId: 344, itemIdFk: 371157, price: 1, itemQuantity: 64 }]
  return state.cart.items
};

export const getSelectedCartItems = (state: RootState): ItemMiniDTO[] => {
  const selectedItems = state.cart.items.filter(
    (cartItem) => state.cart.selected.indexOf(cartItem.itemIdFk) !== -1
  );
  return selectedItems
};

export const getCartItemCount = (state: RootState): number => {
  return state.cart.totalItem;
};

export const getCartTotalAmount = (state: RootState): number => {
  // return Math.round(state.cart.totalAmount * 100) / 100;
  return state.cart.totalAmount
};

export const getCartSelectedIds = (state: RootState): number[] => {
  return state.cart.selected;
};

export const {
  loadCart,
  addItemToCart,
  removeItemFromCart,
  toggleSelect,
  incrementQuantity,
  decrementQuantity,
  clearCart,
} = CartSlice.actions;

export default CartSlice.reducer;
