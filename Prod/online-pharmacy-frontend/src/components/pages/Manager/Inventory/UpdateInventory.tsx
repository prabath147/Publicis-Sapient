import { Box, Button, FileInput, Grid, Modal, Space, Text, useMantineTheme } from "@mantine/core";
import { IconBulb, IconFileDownload, IconUpload } from "@tabler/icons";
import { useState } from "react";
import { useAppDispatch } from "../../../../app/hooks";
import { generateInventorySheet } from "./GenerateExcelTemplate";
import { updateInventorySheet } from "./InventoryAPI";

const UpdateInventoryModal = ({ storeId, openUpdateInventoryModal, setOpenUpdateInventoryModal }) => {
    const theme = useMantineTheme();
    const dispatch = useAppDispatch();
    const [inventorySheet, setInventorySheet] = useState<File | null>(null);


    const loadExcelSheet = () => {
        generateInventorySheet(storeId)
    };

    const closeModal = () => {
        setOpenUpdateInventoryModal(false);
    }

    const handleFileUpload = () => {
        console.log(inventorySheet?.size);

        dispatch(updateInventorySheet(inventorySheet, storeId));
        setInventorySheet(null);
        closeModal();
    }

    return (
        <Modal
            opened={openUpdateInventoryModal}
            onClose={closeModal}
            title="Update Inventory"
            overlayColor={theme.colorScheme === 'dark' ? theme.colors.dark[9] : theme.colors.gray[2]}
            overlayOpacity={0.55}
            overlayBlur={3}
            size="45%"
        >
            <Grid grow>




                <Grid.Col lg={12} sx={{ textAlign: "center" }} >
                    {/* <Center> */}

                    <Space h="lg" />

                    <Button
                        onClick={() => loadExcelSheet()}
                        fullWidth variant='default' leftIcon={<IconFileDownload />}>
                        Download Inventory
                    </Button>

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
                            Fill inventory details according to the Excel Template and upload the same to add items in the stores inventory
                        </Text>
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
                    data-testid="finput"
                        sx={(theme) => ({
                            backgroundColor: theme.colors.gray[1],
                            // textAlign: 'center',
                            padding: theme.spacing.xl,
                            border: "1px dashed gray",
                            borderRadius: theme.radius.md,
                        })}
                        accept="application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
                        value={inventorySheet} onChange={setInventorySheet}
                        placeholder="shop_inventory.xlxs" icon={<IconUpload size={14} />} />
                    {/* </Box> */}
                    <Space h="lg" />
                    <Button data-testid="uploadBtn" onClick={() => handleFileUpload()} fullWidth leftIcon={<IconUpload size={14} />}>
                        Upload
                    </Button>
                    {/* </Center> */}
                </Grid.Col>
            </Grid>

        </Modal>
    );
}



export default UpdateInventoryModal