import { ActionIcon, Badge, Card, Divider, Grid, Group, Pagination, Text, Title } from '@mantine/core';
import { openConfirmModal } from '@mantine/modals';
import { IconEdit, IconTrash } from '@tabler/icons';
import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAppDispatch, useAppSelector } from '../../../../app/hooks';
import { getManagerStoreData } from '../ManagerSlice';
import { Item, Store } from '../models';
import { deleteItem, getInventory } from './InventoryAPI';
import UpdateItemForm from './UpdateItemForm';


interface SmallItemCardType {
    item: Item;
    children?: React.ReactNode;
    checkbox?: React.ReactNode;
    store: Store
}
function ItemCard({ item, checkbox, children, store }: SmallItemCardType) {
    const navigate = useNavigate();
    const [open, setOpen] = useState(false);
    const dispatch = useAppDispatch();
    const openDeleteModal = () =>
        openConfirmModal({
            title: 'Delete Item from Inventory',
            centered: true,
            children: (
                <Text size="sm">
                    Are you sure you want to delete this item?
                </Text>
            ),
            overlayOpacity: 0.55,
            overlayBlur: 3,
            labels: { confirm: 'Delete', cancel: "Cancel" },
            confirmProps: { color: 'red' },
            onCancel: () => console.log('Cancel'),
            onConfirm: () => dispatch(deleteItem(store.storeId, item.itemId))
        });
    const redirectToItemPage = () => {
        navigate(`/manager/store/${store.storeId}/item/${item.itemId}`);
    }
    return (
        <Card withBorder radius="md" px={10} py={10} mb="md" >
            <UpdateItemForm item={item} store={store} open={open} setOpen={setOpen} />
            <Grid>

                <Grid.Col xl={12} style={{ cursor: "pointer" }} onClick={redirectToItemPage}>
                    <Group position="apart" spacing="xl">
                        <Title order={6}>
                            {item.product.productName}
                        </Title>
                        <Badge color={item.itemQuantity === 0 ? "pink" : "green"} variant="light" style={{ marginTop: '10px' }} >
                            {item.itemQuantity === 0 ? "• Out of Stock" : "• In Stock"}
                        </Badge>
                    </Group>
                </Grid.Col>

                <Grid.Col xl={12} sx={{ marginTop: "-10px" }}>
                    <Group position='apart'>
                        <Text color="dimmed" size="sm">Type: {item.product.productType ? "Generic" : "Non-Generic"}</Text>
                        <Text color="dimmed" size="sm">Quantity: {item.itemQuantity}</Text>
                        <Text color="dimmed" size="sm">Manufacturing Date: {item.manufacturedDate.slice(0, 10)}</Text>
                        <Text color="dimmed" size="sm">Expiry: Date: {item.expiryDate.slice(0, 10)}</Text>
                        <Text color="dimmed" size="sm"> Price: ₹ {item.price}</Text>
                        <Group>
                            <ActionIcon variant='subtle' color="blue">
                                <IconEdit size={14} onClick={() => setOpen(true)} />
                            </ActionIcon>
                            <ActionIcon variant='subtle' color="red" >
                                <IconTrash size={14} onClick={openDeleteModal} />
                            </ActionIcon>
                        </Group>
                    </Group>
                </Grid.Col>
            </Grid>
        </Card>
    )
}


const Inventory = ({ order, keyword }) => {
    const { inventory, store } = useAppSelector(getManagerStoreData);
    const dispatch = useAppDispatch();
    const [activePage, setActivePage] = useState(1);
    const pageSize = 10;

    useEffect(() => {
        if (store.storeId !== 0)
            dispatch(getInventory(store.storeId, keyword, (order ? "asc" : "desc"), activePage - 1, pageSize));
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [store.storeId, activePage, keyword, order]);
    return (
        <div>
            {inventory.data.map((item: any) => (
                <ItemCard item={item} key={item.id} store={store} />
            ))}

            {inventory.data.length >= 10 && (<>
                <Divider my="sm" />
                <Group position="right">
                    <Pagination page={activePage} onChange={setActivePage} radius="lg" total={inventory.totalPages} />
                </Group>
            </>)
            }
        </div>
    )
}

export default Inventory