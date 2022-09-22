import {
  ActionIcon,
  Box,
  Button,
  Card,
  Container,
  Divider,
  Grid,
  Group,
  LoadingOverlay,
  Space,
  Table,
  Text,
  Title,
} from "@mantine/core";
import { openConfirmModal } from "@mantine/modals";
import { showNotification } from "@mantine/notifications";
import {
  IconEdit,
  IconEditCircle,
  IconListSearch,
  IconPlus,
  IconTrash,
} from "@tabler/icons";
import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { useAppDispatch, useAppSelector } from "../../../../app/hooks";
import AddInventory from "../Inventory/AddInventory";
import Inventory from "../Inventory/Inventory";
import InventorySearchBar from "../Inventory/InventorySearchBar";
import SortFilterMenu from "../Inventory/SortFilterMenu";
import UpdateInventory from "../Inventory/UpdateInventory";
import { getManagerStoreData } from "../ManagerSlice";
import UpdateStore from "../UpdateStore/UpdateStore";
import { deleteStore, getStoreDetails } from "./StorePageAPI";

const StorePage = () => {
  const { storeId } = useParams();
  const navigate = useNavigate();
  const { store, loading } = useAppSelector(getManagerStoreData);
  const dispatch = useAppDispatch();
  const [openUpdateStoreForm, setOpenUpdateStoreForm] = useState(false);
  const [openInventoryModal, setOpenInventoryModal] = useState(false);
  const [openUpdateInventoryModal, setOpenUpdateInventoryModal] =
    useState(false);
  const [openSearchBar, setOpenSearchBar] = useState(false);
  const [order, setOrder] = useState(false);
  const [keyword, setKeyword] = useState("itemQuantity");

  useEffect(() => {
    console.log(storeId);

    dispatch(getStoreDetails(storeId));
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [storeId]);

  const handleDeleteStore = () => {
    deleteStore(storeId)
      .then((response) => {
        console.log("res:" + response);

        showNotification({
          message: `${store.storeName} deleted successfully`,
          color: "green",
        });
        navigate("/manager/store");
      })
      .catch((error) => {
        showNotification({
          message: error.message,
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
      onConfirm: () => handleDeleteStore(),
    });

  // const [open, setOpen] = useState(false);
  return (
    <div>
      {loading ? (
        <LoadingOverlay
          loaderProps={{ size: "md", color: "blue", variant: "oval" }}
          overlayOpacity={0.3}
          overlayColor="#c5c5c5"
          visible={loading}
          data-testid="Loading"
        />
      ) : (
        <Container>
          <UpdateStore
            openUpdateStoreForm={openUpdateStoreForm}
            setOpenUpdateStoreForm={setOpenUpdateStoreForm}
            store={store}
          />
          <Group position="apart">
            <Title order={4}>{store.storeName}</Title>
            {/* <IconBuildingStore />                    */}
          </Group>

          <Space h="md" />
          <Card shadow="sm" p="lg" radius="md" withBorder>
            <Grid>
              <Grid.Col sm={12} lg={6}>
                <Table horizontalSpacing="xl">
                  <tbody>
                    <tr>
                      <td>
                        <Title order={6}>Store ID</Title>
                      </td>
                      <td>{store.storeId}</td>
                    </tr>
                    <tr>
                      <td>
                        <Title order={6}>Name</Title>
                      </td>
                      <td>{store.storeName}</td>
                    </tr>
                    <tr>
                      <td>
                        <Title order={6}>Registration Date</Title>
                      </td>
                      <td>{store.createdDate.slice(0, 10)}</td>
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
                        <Title order={6}>Revenue</Title>
                      </td>
                      <td>₹ {store.revenue}</td>
                    </tr>
                    <tr>
                      <td>
                        <Title order={6}>Address</Title>
                      </td>
                      <td>
                        {store.address.street}, {store.address.city},{" "}
                        {store.address.state}, {store.address.pinCode}
                      </td>
                    </tr>
                    <tr>
                      <td>
                        <Title order={6}>Action</Title>
                      </td>
                      <td>
                        <Button
                          data-testid="Edit"
                          onClick={() => setOpenUpdateStoreForm(true)}
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
                <Divider my="sm" mt={6} />
              </Grid.Col>
            </Grid>
          </Card>
          <Space h="xl" />

          <Group position="apart" my={10}>
            <Title order={4}>Inventory</Title>
            <Group position="apart">
              <Box>
                <InventorySearchBar
                  openSearchBar={openSearchBar}
                  setOpenSearchBar={setOpenSearchBar}
                />
                <ActionIcon
                  data-testid="searchIcon"
                  variant="subtle"
                  color={"blue"}
                  onClick={() => setOpenSearchBar(true)}
                >
                  <IconListSearch size={26} />
                </ActionIcon>
              </Box>
              <SortFilterMenu
                keyword={keyword}
                order={order}
                setKeyword={setKeyword}
                setOrder={setOrder}
              />
              <AddInventory
                storeId={store.storeId}
                openInventoryModal={openInventoryModal}
                setOpenInventoryModal={setOpenInventoryModal}
              />
              <Button
                data-testid="addIn"
                leftIcon={<IconPlus size={16} />}
                onClick={() => setOpenInventoryModal(true)}
                size="xs"
                color={"green"}
              >
                Add Items
              </Button>
              <UpdateInventory
                storeId={store.storeId}
                openUpdateInventoryModal={openUpdateInventoryModal}
                setOpenUpdateInventoryModal={setOpenUpdateInventoryModal}
              />
              <Button
                data-testid="UpdateIn"
                leftIcon={<IconEditCircle size={16} />}
                onClick={() => setOpenUpdateInventoryModal(true)}
                size="xs"
                color={"green"}
              >
                Update Inventory
              </Button>
            </Group>
          </Group>
          <Inventory order={order} keyword={keyword} />
        </Container>
      )}
    </div>
  );
};

export default StorePage;
