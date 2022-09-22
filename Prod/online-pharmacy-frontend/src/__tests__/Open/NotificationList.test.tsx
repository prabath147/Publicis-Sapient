import { render } from "@testing-library/react";
import { Provider } from "react-redux";
import { BrowserRouter } from "react-router-dom";
import { store } from "../../app/store";
import { Notification } from "../../components/pages/Open/notificationList/Notification";
import NotificationList from "../../components/pages/Open/notificationList/NotificationList";
import NotificationReducer, {
  loadData,
  NotificationsState,
  toggleNotifyStatus
} from "../../components/pages/Open/notificationList/NotificationSlice";

it("component created", () => {
  // const component = renderer.create(
  //     <Provider store={store}>
  //         <BrowserRouter>
  //             <NotificationList />
  //         </BrowserRouter>
  //     </Provider>
  // )
  // const tree = component.toJSON();
  // expect(tree).toMatchSnapshot();
  //TODO "CHECK"
});

it("component data loaded", () => {
  // const component = renderer.create(
  //     <Provider store={store}>
  //         <BrowserRouter>
  //             <NotificationList />
  //         </BrowserRouter>
  //     </Provider>
  // )
  // const tree = component.toJSON();
  // expect(tree).toMatchSnapshot();
  //TODO "CHECK"
});

it("should handle initial state", () => {
  expect(NotificationReducer(undefined, { type: "unknown" })).toEqual({
    notifications: [],
  });
});

it("load data", () => {
  const initialState: NotificationsState = {
    notifications: [],
  };
  const data: Notification[] = [
    {
      id: 1,
      severity: "WARNING",
      message: "sample message",
      description: "sample description",
      status: "UNSEEN",
      createdOn: new Date("2022-08-16"),
    },
  ];
  const actual = NotificationReducer(initialState, loadData(data));
  expect(actual.notifications).toEqual(data);
});

it("toggle status empty", () => {
  const initialState: NotificationsState = {
    notifications: [],
  };

  const actual = NotificationReducer(initialState, toggleNotifyStatus(1));
  expect(actual.notifications.length).toEqual(0);
});

it("toggle status unseen to seen", () => {
  const initialState: NotificationsState = {
    notifications: [
      {
        id: 1,
        severity: "WARNING",
        message: "sample message",
        description: "sample description",
        status: "UNSEEN",
        createdOn: new Date("2022-08-16"),
      },
    ],
  };

  const actual = NotificationReducer(initialState, toggleNotifyStatus(1));
  expect(actual.notifications[0].status).toEqual("SEEN");
});

it("toggle status seen to unseen", () => {
  const initialState: NotificationsState = {
    notifications: [
      {
        id: 1,
        severity: "WARNING",
        message: "sample message",
        description: "sample description",
        status: "SEEN",
        createdOn: new Date("2022-08-16"),
      },
    ],
  };

  const actual = NotificationReducer(initialState, toggleNotifyStatus(1));
  expect(actual.notifications[0].status).toEqual("UNSEEN");
});

it("verify contents of table", () => {
  const component = render(
    <Provider store={store}>
      <BrowserRouter>
        <NotificationList />
      </BrowserRouter>
    </Provider>
  );

  const table = component.getByTestId("notify-table");
  const rows = table.querySelectorAll("tr");
  expect(rows.length).toBe(1);
  rows.forEach((row) => {
    expect(row).toBeInTheDocument();
  });
});

    //     const component = render(
    //         <Provider store={store}>
    //             <BrowserRouter>
    //                 <NotificationList />
    //             </BrowserRouter>
    //         </Provider>
    //     );

    //     const table = component.getByTestId('notify-table');
    //     const rows = table.querySelectorAll('tr');
    //     expect(rows.length).toBe(1);
    //     rows.forEach((row) => {
    //         expect(row).toBeInTheDocument();
    //     })

    // });

    // TODO : test parse time function

// it('test fetch', async () => {
//     const data = {
//         data: [
//             {
//                 id: 1,
//                 severity: "WARNING",
//                 message: "sample message",
//                 description: "sample description",
//                 status: "UNSEEN",
//                 createdOn: new Date("2022-08-16"),
//             },
//         ],
//     };
// });
