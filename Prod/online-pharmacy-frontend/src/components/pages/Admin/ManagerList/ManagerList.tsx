import React, { useEffect, useState } from "react";

import {
  ActionIcon,
  Badge,
  Button,
  Card,
  Center,
  Container,
  Loader,
  Pagination,
  Select,
  Switch,
  Table,
  TextInput
} from "@mantine/core";
import { showNotification } from "@mantine/notifications";
import { IconSearch } from "@tabler/icons";
import { approveManager, getManagerList, rejectManager } from "./ManagerAPI";
import { Manager } from "./models";
export default function ManagerList() {
  // const mList = useAppSelector(selectManagers);
  // const dispatch = useAppDispatch();
  const [checked, setChecked] = useState(false);
  const [activePage, setPage] = useState(1);
  const [pageCount, setPageCount] = useState(1);
  const [loading, setLoading] = useState(true);

  const [managerList, setManagerList] = useState<Manager[]>([]);
  const [name, setName] = useState("");
  const [filter, setFilter] = useState<string | null>("");

  const limit = 10;
  useEffect(() => {
    loadMangerList();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [activePage, checked, filter]);

  const loadMangerList = () => {
    setLoading(true);
    getManagerList(
      checked ? "PENDING" : "APPROVED",
      activePage - 1,
      limit,
      name,
      filter
    )
      .then((payload) => {
        const managers: Manager[] = [];
        // console.log(payload.data);
        setPageCount(payload.data.totalPages);
        payload.data.data.forEach((element: Manager) => {
          managers.push(element);
        });
        setManagerList(managers);
        setLoading(false);
      })
      .catch((err) => {
        showNotification({
          color: "red",
          message: "Oops! Something went wrong",
        });
        setLoading(false);
      });
  };

  // const loadDataAction = () => async (dispatch: Dispatch) => {
  //   try {
  //     const payload = await getManagerList(
  //       checked ? "PENDING" : "APPROVED",
  //       activePage - 1,
  //       limit
  //     );

  //     const managers: Manager[] = [];
  //     console.log(payload.data);
  //     setPageCount(payload.data.totalPages);
  //     payload.data.data.forEach((element: Manager) => {
  //       managers.push(element);
  //     });
  //     dispatch({
  //       type: loadData,
  //       payload: managers,
  //     });
  //   } catch (error) {
  //     console.error(error);
  //   }
  // };

  const changeStatus = async (e: React.MouseEvent<HTMLButtonElement>) => {
    e.preventDefault();
    try {
      if (e.currentTarget.name === "APPROVED") {
        await approveManager(Number(e.currentTarget.value));
      } else {
        await rejectManager(Number(e.currentTarget.value));
      }

      loadMangerList();
    } catch (error) {
      showNotification({ message: "Oops! Something went wrong", color: "red" });
      console.error(error);
    }
  };

  if (loading)
    return (
      <Center style={{ height: "80%" }}>
        <Loader data-testid="Loading" />
      </Center>
    );

  const handleClick = () => {
    loadMangerList();
    setName("");
  };

  const filterData = [
    {
      label: "ManagerId",
      value: "managerId",
    },
    {
      label: "Name",
      value: "name",
    },
    {
      label: "Registration Date",
      value: "registrationDate",
    },
  ];

  const rows = managerList.map((row: Manager) => (
    <tr key={row.managerId}>
      <td>{row.managerId}</td>
      <td>{row.name}</td>
      <td>{row.phoneNo}</td>
      <td>{row.registrationDate.toString().split("T")[0]}</td>
      <td>{row.licenseNo}</td>
      <td>
        <Badge
          color={row.approvalStatus === "APPROVED" ? "green" : "red"}
          variant="dot"
        >
          {row.approvalStatus}
        </Badge>
      </td>
      {checked ? (
        <td>
          <Button
            color="green"
            value={row.managerId}
            onClick={changeStatus}
            name="APPROVED"
            data-testid={"manager-approve-" + row.managerId}
          >
            Approve
          </Button>
          &emsp;
          <Button
            color="red"
            value={row.managerId}
            onClick={changeStatus}
            name="REJECTED"
            data-testid={"manager-reject-" + row.managerId}
          >
            Reject
          </Button>
        </td>
      ) : (
        <td>No Action Needed</td>
      )}
    </tr>
  ));

  return (
    <div>
      <Card shadow="sm" p="lg" radius="md" withBorder>
        <Container>
          <Switch
            checked={checked}
            label="Pending"
            onChange={(event) => {
              setChecked(event.currentTarget.checked);
              setPage(1);
            }}
            data-testid="pending-switch"
          />
          <div style={{ float: "right" }}>
            <TextInput
              placeholder="name"
              size="xs"
              style={{ width: "150px" }}
              rightSection={
                <ActionIcon onClick={handleClick}>
                  <IconSearch size={18} stroke={1.5} />
                </ActionIcon>
              }
              onChange={(e) => setName(e.target.value)}
            />
            <Select
              style={{ width: "150px" }}
              data={filterData}
              placeholder="select filter"
              //defaultValue="managerId"
              dropdownComponent="div"
              onChange={setFilter}
            />
          </div>

          <Table highlightOnHover>
            <thead>
              <tr>
                <th>Manager id</th>
                <th>Name</th>
                <th>Phone No</th>
                <th>Registration Date</th>
                <th>license No</th>
                <th>Status</th>
                <th>Action</th>
              </tr>
            </thead>
            <tbody>{rows}</tbody>
          </Table>
          <Pagination
            position="center"
            page={activePage}
            onChange={setPage}
            total={pageCount}
          />
        </Container>
      </Card>
    </div>
  );
}
