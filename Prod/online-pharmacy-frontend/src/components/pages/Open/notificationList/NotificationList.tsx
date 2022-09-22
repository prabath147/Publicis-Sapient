import {
  faEnvelope,
  faEnvelopeOpen,
  faEye
} from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { ActionIcon, Badge, Container, Table, Tooltip } from "@mantine/core";
import { showNotification } from "@mantine/notifications";
import { Dispatch } from "@reduxjs/toolkit";
import { useEffect, useState } from "react";
import { useAppDispatch, useAppSelector } from "../../../../app/hooks";
import { getUserData } from "../../Auth/Login/UserSlice";
import { CustomPagination } from "./CustomPagination";
import { Notification } from "./Notification";
import {
  getNotificationsAPI,
  toggleNotificationStatus
} from "./NotificationAPI";
import * as myConst from "./NotificationConstants";
import NotificationModal from "./NotificationModal";
import {
  loadData,
  selectNotifications,
  toggleNotifyStatus
} from "./NotificationSlice";
import SortableHeader from "./SortableHeader";

export default function NotificationList() {
  const NotifyList = useAppSelector(selectNotifications);
  const user = useAppSelector(getUserData);
  const dispatch = useAppDispatch();
  // define states for pagination
  const [activePage, setPage] = useState(1);
  const [itemsPerPage, setItemsPerPage] = useState(10);
  const [totalPages, setTotalPage] = useState(0);

  // define states for sortable cols
  const sortCols = { Id: 0, Timestamp: 0 };
  const [toggle, setToggle] = useState(sortCols);

  // define state for modal
  const [opened, setOpened] = useState(false);
  const initialState: Notification = {
    id: 0,
    message: "",
    description: "",
    status: "SEEN",
    severity: "MESSAGE",
    createdOn: new Date(),
  };
  const [notificationData, setNotificationData] =
    useState<Notification>(initialState);

  useEffect(() => {
    dispatch(loadDataAction());
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [activePage, itemsPerPage, toggle]);

  const loadDataAction = () => async (dispatch: Dispatch) => {
    try {
      let sortBy = "createdOn";
      let sortOrder = "descending";
      if (toggle.Id > 0) {
        sortBy = "id";
        sortOrder = toggle.Id === 1 ? "ascending" : "descending";
      } else {
        sortBy = "createdOn";
        sortOrder = toggle.Timestamp === 1 ? "ascending" : "descending";
      }
      const payload = (
        await getNotificationsAPI(
          user.id,
          itemsPerPage,
          activePage - 1,
          sortBy,
          sortOrder
        )
      ).data;
      const notifications: Notification[] = [];
      setTotalPage(payload.totalPages);
      payload.data.forEach((element: Notification) => {
        notifications.push(element);
      });
      dispatch({
        type: loadData,
        payload: notifications,
      });
    } catch (error) {
      showNotification({ message: "Oops! Something went wrong", color: "red" });
    }
  };

  const toggleStatus = (id: number) => {
    toggleNotificationStatus(id).then(() =>
      dispatch({
        type: toggleNotifyStatus,
        payload: id,
      })
    );
  };

  // const parseTimestamp = (timestamp: string) => {
  //   return (
  //     timestamp.slice(0, 10).split("-").reverse().join("-") +
  //     " " +
  //     timestamp.slice(11, 19)
  //   );
  // };

  const rows = NotifyList.map((row: Notification) => (
    <tr
      key={row.id}
      style={
        row.status === "UNSEEN" ? myConst.UNSEEN_STYLE : myConst.SEEN_STYLE
      }
    >
      <td>{row.id}</td>
      <td>
        <Badge
          color={
            row.severity === "MESSAGE"
              ? myConst.MESSAGE_COLOR
              : row.severity === "WARNING"
                ? myConst.WARNING_COLOR
                : myConst.ERROR_COLOR
          }
        >
          {row.severity}
        </Badge>
      </td>
      <td>{row.message}</td>
      <td>{row.description.slice(0, 30)}</td>
      <td>
        <Tooltip
          label={
            row.status === "SEEN"
              ? myConst.ON_SEEN_MESSAGE
              : myConst.ON_UNSEEN_MESSAGE
          }
        >
          <ActionIcon onClick={() => toggleStatus(row.id)}>
            <FontAwesomeIcon
              icon={row.status === "SEEN" ? faEnvelope : faEnvelopeOpen}
            />
          </ActionIcon>
        </Tooltip>
      </td>
      <td>{new Date(row.createdOn.toString()).toString().substring(4, 24)}</td>
      <td>
        <ActionIcon
          onClick={() => {
            setOpened(true);
            setNotificationData(row);
          }}
        >
          <FontAwesomeIcon icon={faEye} />
        </ActionIcon>
      </td>
    </tr>
  ));

  return (
    <div>
      <NotificationModal
        opened={opened}
        setOpened={setOpened}
        row={notificationData}
      />
      <Container>
        <h2>All Notifications</h2>
        <Table highlightOnHover data-testid="notify-table">
          <thead>
            <tr>
              <SortableHeader
                label={"Id"}
                setToggle={setToggle}
                toggle={toggle}
              />
              <th>Severity</th>
              <th>Message</th>
              <th>Description</th>
              <th>Status</th>
              <SortableHeader
                label={"Timestamp"}
                setToggle={setToggle}
                toggle={toggle}
              />
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>{rows}</tbody>
        </Table>
        <CustomPagination
          activePage={activePage}
          setPage={setPage}
          totalPages={totalPages}
          itemsPerPage={itemsPerPage}
          setItemsPerPage={setItemsPerPage}
        />
      </Container>
    </div>
  );
}
