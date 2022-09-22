import { ActionIcon, Box, Button, CopyButton, Divider, FileInput, Grid, Modal, MultiSelect, Space, Text, Title, Tooltip, useMantineTheme } from '@mantine/core';
import { showNotification } from '@mantine/notifications';
import { IconBulb, IconCheck, IconCopy, IconFileDownload, IconUpload } from '@tabler/icons';
import { AxiosResponse } from 'axios';
import { useEffect, useState } from 'react';
import { useAppDispatch } from '../../../../app/hooks';
import AddItemForm from './AddItemForm';
import AddProductForm from './AddProductForm';
import { generateExcelTemplate } from './GenerateExcelTemplate';
import { getCategories, uploadInventorySheet } from './InventoryAPI';

const limit = 51200; // bytes or 50KB

interface ModalStateType {
  storeId: number;
  openInventoryModal: boolean;
  setOpenInventoryModal: (boolean) => void;
}

export default function AddInventory({ storeId, openInventoryModal, setOpenInventoryModal }: ModalStateType) {
  const theme = useMantineTheme();
  const dispatch = useAppDispatch();
  const [inventorySheet, setInventorySheet] = useState<File | null>(null);
  const [productFormOpen, setProductFormOpen] = useState<boolean>(false);
  const [categories, setCategories] = useState([
    {
      categoryId: 0,
      categoryName: "",
    }
  ]);
  const [categoryList, setCategoryList] = useState<string[]>([]);
  useEffect(() => {
    getCategory();

  }, []);

  const loadExcelSheet = () => {
    generateExcelTemplate()
  };

  const closeModal = () => {
    setCategoryList([]);
    setOpenInventoryModal(false);
    setProductFormOpen(false);
  }
  const handleFileUpload = () => {
    if (inventorySheet !== null && inventorySheet?.size <= limit) {

      dispatch(uploadInventorySheet(inventorySheet, storeId));
      setInventorySheet(null);
      closeModal();

    } else {
      showNotification({
        message: "Cannot upload file more than 50KB",
        color: "red"
      });
    }


  }
  const getCategory = async () => {
    const categoryResponse: AxiosResponse = await getCategories();
    setCategories(categoryResponse.data?.data);
  }
  const ArrayToString = (arr) => {
    let finalString = "";
    arr.forEach((element, index) => {
      finalString += element;
      if (index + 1 < arr.length) {
        finalString += ",";
      }
    });
    return finalString;
  }
  function CopyToClipBoard() {
    const text = ArrayToString(categoryList);
    return (
      <CopyButton value={text} timeout={2000}>
        {({ copied, copy }) => (
          <Tooltip label={copied ? 'Copied' : 'Copy'} withArrow position="right">
            <ActionIcon color={copied ? 'teal' : 'gray'} onClick={copy}>
              {copied ? <IconCheck size={16} /> : <IconCopy size={16} />}
            </ActionIcon>
          </Tooltip>
        )}
      </CopyButton>
    );
  }
  return (
    <Modal
      opened={openInventoryModal}
      closeOnClickOutside={true}
      closeOnEscape={true}
      onClose={closeModal}
      title="Add Inventory"
      overlayColor={theme.colorScheme === 'dark' ? theme.colors.dark[9] : theme.colors.gray[2]}
      overlayOpacity={0.55}
      overlayBlur={3}
      size="80%"
    >
      <Grid grow>
        <Grid.Col lg={7}>
          {productFormOpen ? <AddProductForm setProductFormOpen={setProductFormOpen} />
            : <AddItemForm storeId={storeId} closeModal={closeModal} setProductFormOpen={setProductFormOpen} />}

        </Grid.Col>

        <Divider orientation="vertical" />

        <Grid.Col lg={4} sx={{ textAlign: "center" }} >
          {/* <Center> */}
          <Space h="xl" />
          <Space h="xl" />
          <Space h="xl" />
          <Title order={6} >
            Add Items to Inventory
          </Title>
          <Space h="lg" />
          {/* <a href='https://d1j9yqk9s11go2.cloudfront.net/template/inventory.xlsx' download style={{ textDecoration: 'none' }}> */}
          <Button
            data-testid="uploadBtn"
            onClick={() => loadExcelSheet()}
            fullWidth variant='default' leftIcon={<IconFileDownload />}>
            Download Template
          </Button>
          {/* </a> */}
          {/* <Button value={inventorySheet}>file</Button> */}
          <Space h="lg" />
          <Box
            sx={(theme) => ({
              backgroundColor: theme.colors.gray[1],
              // textAlign: 'center',
              padding: theme.spacing.xl,
              borderRadius: theme.radius.md,
            })}
          >
            <Text size={"sm"} align="justify">
              <IconBulb size={14} />
              Fill inventory details according to the Excel Template and upload the same to add items in the stores inventory. Maximum file size is 50KB or 1000 rows. Add category as "," separated names.
              Select the categories and copy to clipboard.
            </Text>
            <Grid grow>
              <Grid.Col lg={10}>
                <MultiSelect
                  data={categories.map(cat => ({ value: cat.categoryName, label: cat.categoryName }))}
                  value={categoryList} onChange={setCategoryList}
                />
              </Grid.Col>
              <Grid.Col lg={1}>
                <CopyToClipBoard />
              </Grid.Col>
            </Grid>
          </Box>
          <Space h="lg" />
          {/* <Box
                    sx={(theme) => ({
                        backgroundColor: theme.colors.gray[1],
                        // textAlign: 'center',
                        // padding: theme.spacing.xl,
                        border: "1px dashed gray",
                        borderRadius: theme.radius.md,                                           
                    })}
                    > */}
          <FileInput
            sx={(theme) => ({
              backgroundColor: theme.colors.gray[1],
              // textAlign: 'center',
              padding: theme.spacing.xl,
              border: "1px dashed gray",
              borderRadius: theme.radius.md,
            })}
            accept="application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
            value={inventorySheet}
            onChange={setInventorySheet}
            placeholder="shop_inventory.xlxs"
            icon={<IconUpload size={14} />}
          />
          {/* </Box> */}
          <Space h="lg" />
          <Button
            data-testid="uploadBtn"
            onClick={() => handleFileUpload()}
            fullWidth
            leftIcon={<IconUpload size={14} />}
          >
            Upload
          </Button>
          {/* </Center> */}
        </Grid.Col>
      </Grid>
    </Modal>
  );
}
