import { createSlice, PayloadAction } from "@reduxjs/toolkit";
import { RootState } from "../../../../app/store";
import { Notification } from "./Notification";

export interface NotificationsState {
    notifications: Notification[];
}

const initialState: NotificationsState = {
    notifications: [],
};

const NotificationSlice = createSlice({
    name: "notification",
    initialState,
    reducers: {
        loadData: (state: NotificationsState, actions: PayloadAction<Notification[]>) => {
            const notifications: Notification[] = actions.payload.map((item: Notification) => item);
            state.notifications = notifications
        },
        toggleNotifyStatus: (state: NotificationsState, actions: PayloadAction<number>) => {
            const notifications = state.notifications;
            const item: undefined | Notification = notifications.find((notification: Notification) => notification.id === actions.payload);
            if(item){
                if(item.status === 'SEEN')
                    item.status = 'UNSEEN';
                else
                    item.status = 'SEEN';
            }
            state.notifications = notifications;               
            console.log(item)
        },
    },
});

export const {loadData, toggleNotifyStatus} = NotificationSlice.actions;
export const selectNotifications = (state: RootState) => state.notification.notifications;
export default NotificationSlice.reducer;