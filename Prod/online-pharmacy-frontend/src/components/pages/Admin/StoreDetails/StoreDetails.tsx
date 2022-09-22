import {
  Badge, Button, Card, Container, Divider, Grid, Group, Pagination, Space, Table, Text, Title
} from "@mantine/core";
import { openConfirmModal } from "@mantine/modals";
import { showNotification } from "@mantine/notifications";
import {
  IconBuildingStore,
  IconEdit, IconTrash
} from "@tabler/icons";
import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { useAppSelector } from "../../../../app/hooks";
import { Item } from "../../Manager/models";
import { deleteStore, getStoreInventory } from "../storeList/StoreAPI";
import { selectStores } from "../storeList/StoreSlice";
import UpdateStore from "./UpdateStore";

const StoreDetails = () => {
  // const [openSearchBar, setOpenSearchBar] = useState(false);
  const [open, setOpen] = useState(false);
  const navigate = useNavigate();
  const [activePage, setPage] = useState(1);
  const [pageCount, setPageCount] = useState(1);
  const limit = 10;
  const { id } = useParams();
  const id2 = id === undefined ? "-1" : id;
  const storeData = useAppSelector(selectStores).find(
    (item) => item.storeId === parseInt(id2)
  );
  const [inventory, setInventory] = useState<Item[]>([]);

  useEffect(() => {
    if (!storeData) return
    getStoreInventory(storeData.storeId, activePage - 1, limit)
      .then((response) => {
        console.log(response.data.data);
        setInventory(response.data.data);
        setPageCount(response.data.totalPages);
      })
      .catch((error) =>
        showNotification({
          message: "Oops, Something went wrong " + error.message,
          color: "red",
        })
      );
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [activePage]);

  if (storeData === undefined) return <Text>No Data Found</Text>;

  const deleteSpecificStore = () => {
    deleteStore(storeData.storeId)
      .then((response) => {
        if (response.status === 200) {
          navigate("/store");
          showNotification({ message: "Store deleted successfully!!", color: "red" })
        }
      })
      .catch((error) => {
        showNotification({
          message: "Oops!! Something went wrong, please try after sometime.",
          color: "red",
        });
      });
  };

  const openDeleteModal = () =>
    openConfirmModal({
      title: "Delete Store",
      centered: true,
      children: (
        <Text size="sm">Are you sure you want to delete the store?</Text>
      ),
      overlayOpacity: 0.55,
      overlayBlur: 3,
      labels: { confirm: "Delete", cancel: "Cancel" },
      confirmProps: { color: "red" },
      onCancel: () => console.log("Cancel"),
      onConfirm: () => deleteSpecificStore(),
    });

  return (
    <div>
      <div>
        {/* <Title order={3}>{storeDetails.storeName}</Title> */}
        <Container>
          <UpdateStore open={open} setOpen={setOpen} id={storeData.storeId} />
          <Grid>
            <Grid.Col span={3}>
              <Title order={4}>{storeData.storeName}</Title>
            </Grid.Col>

            <Grid.Col span={1} offset={7}>
              <IconBuildingStore />
              <Space h="md" />
            </Grid.Col>
          </Grid>
          <Card>
            <Grid>
              <Grid.Col sm={12} lg={6}>
                <Table horizontalSpacing="xl">
                  <tbody>
                    <tr>
                      <td>
                        <Title order={6}>Store ID</Title>
                      </td>
                      <td>{storeData.storeId}</td>
                    </tr>
                    <tr>
                      <td>
                        <Title order={6}>Name</Title>
                      </td>
                      <td>{storeData.storeName}</td>
                    </tr>
                    <tr>
                      <td>
                        <Title order={6}>Registration Date</Title>
                      </td>
                      <td>{storeData.createdDate.slice(0, 10)}</td>
                    </tr>
                    <tr>
                      <td>
                        <Title order={6}>Address</Title>
                      </td>
                      <td>
                        {storeData.address.street +
                          ", " +
                          storeData.address.city +
                          ", " +
                          storeData.address.pinCode}
                      </td>
                    </tr>
                    <tr>
                      <td>
                        <Title order={6}>Revenue</Title>
                      </td>
                      <td>₹ {storeData.revenue}</td>
                    </tr>
                  </tbody>
                </Table>
                <Divider my="sm" />
              </Grid.Col>
              <Grid.Col sm={12} lg={6}>
                <Table horizontalSpacing="xl">
                  <tbody>
                    <tr>
                      <td>
                        <Title order={6}>Manager Id</Title>
                      </td>
                      <td>{storeData.manager.managerId}</td>
                    </tr>
                    <tr>
                      <td>
                        <Title order={6}>Manager Name</Title>
                      </td>
                      <td>{storeData.manager.name}</td>
                    </tr>
                    <tr>
                      <td>
                        <Title order={6}>Manager Contact</Title>
                      </td>
                      <td>{storeData.manager.phoneNo}</td>
                    </tr>

                    <tr>
                      <td>
                        <Title order={6}>Action</Title>
                      </td>
                      <td>
                        <Button
                          onClick={() => setOpen(true)}
                          compact
                          variant="subtle"
                          rightIcon={<IconEdit size={14} />}
                        >
                          Edit
                        </Button>
                        <Button
                          onClick={openDeleteModal}
                          compact
                          variant="subtle"
                          color="red"
                          rightIcon={<IconTrash size={14} />}
                        >
                          Delete
                        </Button>
                      </td>
                    </tr>
                  </tbody>
                </Table>
                <Divider my="sm" />
              </Grid.Col>
            </Grid>
          </Card>
          <Space h="xl" />
          <Grid>
            <Grid.Col>
              <Group position="apart">
                <Group>
                  <Title order={4}>Inventory</Title>
                </Group>
                {/* <Group>
                  <Box>
                    <InventorySearchBar
                      openSearchBar={openSearchBar}
                      setOpenSearchBar={setOpenSearchBar}
                    />
                    <ActionIcon
                      variant="subtle"
                      color={"blue"}
                      onClick={() => setOpenSearchBar(true)}
                    >
                      <IconListSearch size={26} />
                    </ActionIcon>
                  </Box>
                  <SortFilterMenu />
                </Group> */}
              </Group>
              <Divider my="sm" />
            </Grid.Col>
          </Grid>
          <Card>
            <div>
              {/* {inventory.map(item => <Card>{item.itemId}{item.itemQuantity}</Card>)} */}
              {inventory.map((item) => (
                <Card withBorder radius="md" px={10} py={10} mb="md">
                  <Grid>
                    <Grid.Col xl={12}>
                      <Group position="apart" spacing="xl">
                        <Title order={6}>{item.product.productName}</Title>
                        <Badge
                          color={item.itemQuantity === 0 ? "pink" : "green"}
                          variant="light"
                          style={{ marginTop: "10px" }}
                        >
                          {item.itemQuantity === 0
                            ? "• Out of Stock"
                            : "• In Stock"}
                        </Badge>
                      </Group>
                    </Grid.Col>
                    <Grid.Col xl={12} sx={{ marginTop: "-10px" }}>
                      <Group position="apart">
                        <Text color="dimmed" size="sm">
                          Type:{" "}
                          {item.product.productType ? "Generic" : "Non-Generic"}
                        </Text>
                        <Text color="dimmed" size="sm">
                          Quantity: {item.itemQuantity}
                        </Text>
                        <Text color="dimmed" size="sm">
                          Manufacturing Date:{" "}
                          {item.manufacturedDate.slice(0, 10)}
                        </Text>
                        <Text color="dimmed" size="sm">
                          Expiry: Date: {item.expiryDate.slice(0, 10)}
                        </Text>
                        <Text color="dimmed" size="sm">
                          {" "}
                          Price: $ {item.price}
                        </Text>
                      </Group>
                    </Grid.Col>
                  </Grid>
                </Card>
              ))}
            </div>
            <Pagination
              position="center"
              page={activePage}
              onChange={setPage}
              total={pageCount}
              radius="lg"
            />
          </Card>
        </Container>
      </div>
    </div>
  );
};

export default StoreDetails;
