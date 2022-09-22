import {
  ActionIcon, Card, Container, Divider,
  Group, Pagination,
  Table
} from "@mantine/core";
import { showNotification } from "@mantine/notifications";
import { Dispatch } from "@reduxjs/toolkit";
import { IconChevronRight } from "@tabler/icons";
import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { useAppDispatch, useAppSelector } from "../../../../app/hooks";
import { Store } from "./models";
import { getStoreList } from "./StoreAPI";
import { loadStoreData, selectStores } from "./StoreSlice";

export default function StoreList() {
  const sList = useAppSelector(selectStores);
  const dispatch = useAppDispatch();
  const [activePage, setPage] = useState(1);
  const [pageCount, setPageCount] = useState(1);

  const limit = 10;
  useEffect(() => {
    dispatch(loadDataAction());
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [activePage]);

  const loadDataAction = () => async (dispatch: Dispatch) => {
    try {
      const payload = await getStoreList(activePage - 1, limit);
      const stores: Store[] = [];
      setPageCount(payload.data.totalPages);
      payload.data.data.forEach((element: Store) => {
        stores.push(element);
      });
      dispatch({
        type: loadStoreData,
        payload: stores,
      });
    } catch (error) {
      showNotification({ message: "Oops Something went Wrong", color: "red" })
      // alert(error);
    }
  };

  const rows = sList.map((row: Store) => (
    <tr>
      <td>{row.storeId}</td>
      <td>{row.manager.managerId}</td>
      <td>{row.storeName}</td>
      <td>{row.address.city}</td>
      <td>{row.createdDate.toString().split("T")[0]}</td>
      <td>{row.revenue}</td>
      <td>
        <Link to={"/store-details/" + row.storeId} style={{ textDecoration: 'none' }}>
          <ActionIcon variant='subtle' color={"blue"}>
            <IconChevronRight size={26} />
          </ActionIcon>
        </Link>
      </td>
    </tr>
  ));

  return (

    <div>
      <Card shadow="sm" p="lg" radius="md" withBorder>
        <Container>
          <h1>List of Stores</h1>
          <Table highlightOnHover>
            <thead>
              <tr>
                <th>Store Id</th>
                <th>Manager Id</th>
                <th>Store Name</th>
                <th>City</th>
                <th>Registration Date</th>
                <th>Revenue</th>
              </tr>
            </thead>
            <tbody>{rows}</tbody>
          </Table>
          <Divider my="sm" />

          <Group position="center">
            <Pagination
              position="center"
              page={activePage}
              onChange={setPage}
              total={pageCount}
              radius="lg"
            />

          </Group>
        </Container>

      </Card>
    </div>

  );
}