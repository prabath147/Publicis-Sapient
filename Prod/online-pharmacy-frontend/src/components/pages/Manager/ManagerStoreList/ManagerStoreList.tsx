import { ActionIcon, Button, Card, Divider, Group, LoadingOverlay, Pagination, Table, Title } from '@mantine/core';
import { IconChevronRight, IconPlus } from '@tabler/icons';
import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { useAppDispatch, useAppSelector } from "../../../../app/hooks";
import { getUserData } from '../../Auth/Login/UserSlice';
import CreateStore from '../CreateStore/CreateStore';
import { getManagerStoreData } from '../ManagerSlice';
import { getAllStoresBymanagerId } from './ManagerStoreListAPI';

// const storeListT = [
//     {
//         storeId: 101,
//         storeName: "Pharmacy Shop",
//         address: {
//             addressId: 1,
//             street: "Outer ring road",
//             city: "Bangalore",
//             state: "Karnataka",
//             pinCode: 550066,
//             country: "India"
//         },
//         createdDate: "2022-08-13T18:30:00.000+00:00",        
//         revenue: 2121.0

//     },
//     {
//         storeId: 101,
//         storeName: "Pharmacy Shop",
//         address: {
//             addressId: 1,
//             street: "Outer ring road",
//             city: "Bangalore",
//             state: "Karnataka",
//             pinCode: 550066,
//             country: "India"
//         },
//         createdDate: "2022-08-13T18:30:00.000+00:00",        
//         revenue: 2121.0

//     },
//     {
//         storeId: 101,
//         storeName: "Pharmacy Shop",
//         address: {
//             addressId: 1,
//             street: "Outer ring road",
//             city: "Bangalore",
//             state: "Karnataka",
//             pinCode: 550066,
//             country: "India"
//         },
//         createdDate: "2022-08-13T18:30:00.000+00:00",        
//         revenue: 2121.0

//     },
//     {
//         storeId: 101,
//         storeName: "Pharmacy Shop",
//         address: {
//             addressId: 1,
//             street: "Outer ring road",
//             city: "Bangalore",
//             state: "Karnataka",
//             pinCode: 550066,
//             country: "India"
//         },
//         createdDate: "2022-08-13T18:30:00.000+00:00",        
//         revenue: 2121.0

//     },
//     {
//         storeId: 101,
//         storeName: "Pharmacy Shop",
//         address: {
//             addressId: 1,
//             street: "Outer ring road",
//             city: "Bangalore",
//             state: "Karnataka",
//             pinCode: 550066,
//             country: "India"
//         },
//         createdDate: "2022-08-13T18:30:00.000+00:00",        
//         revenue: 2121.0

//     },
//     {
//         storeId: 101,
//         storeName: "Pharmacy Shop",
//         address: {
//             addressId: 1,
//             street: "Outer ring road",
//             city: "Bangalore",
//             state: "Karnataka",
//             pinCode: 550066,
//             country: "India"
//         },
//         createdDate: "2022-08-13T18:30:00.000+00:00",        
//         revenue: 2121.0

//     }

// ]
export default function ManagerStoreList() {
    // const storeListPage = useAppSelector(getStoreList);
    const { storeListPage, loading } = useAppSelector(getManagerStoreData);
    // const manager = LocalUserService.getLocalUser();
    const cuser = useAppSelector(getUserData);
    const dispatch = useAppDispatch();
    const [open, setOpen] = useState(false);
    const [activePage, setActivePage] = useState(1);
    const pageSize = 10;

    useEffect(() => {
        dispatch(getAllStoresBymanagerId(cuser.id, activePage - 1, pageSize));
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [activePage]);


    return (
        <div>
            {loading ? <LoadingOverlay
                loaderProps={{ size: 'md', color: 'blue', variant: 'oval' }}
                overlayOpacity={0.3}
                overlayColor="#c5c5c5"
                visible={loading}
                data-testid="Loading"
            /> : (<>

                <CreateStore open={open} setOpen={setOpen} />

                <Group position='apart' my={10}>
                    {/* <Text size="lg" weight={500}>Stores</Text> */}
                    <Title order={3}>Stores</Title>
                    <Button
                        leftIcon={<IconPlus />}
                        onClick={() => setOpen(true)} variant="gradient" gradient={{ from: 'teal', to: 'lime', deg: 105 }}>
                        Add Store
                    </Button>
                </Group>

                <Card shadow="sm" p="lg" radius="md" withBorder>
                    <Table highlightOnHover>
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Name</th>
                                <th>Register Date</th>
                                <th>Revenue</th>
                                <th>Address</th>
                                <th>View</th>
                            </tr>
                        </thead>
                        <tbody>{
                            storeListPage.data.map(store => (
                                <tr key={store.storeId}>
                                    <td>{store.storeId}</td>
                                    <td>{store.storeName}</td>
                                    <td>{store.createdDate.slice(0, 10)}</td>
                                    <td>₹ {store.revenue}</td>
                                    <td>{store.address.city}</td>
                                    <td>
                                        <Link to={`/manager/store/${store.storeId}`} style={{ textDecoration: 'none' }}>
                                            <ActionIcon variant='subtle' color={"blue"}>
                                                <IconChevronRight size={26} />
                                            </ActionIcon>
                                        </Link>
                                    </td>
                                </tr>
                            ))
                        }</tbody>
                    </Table>
                    <Divider my="sm" />
                    <Group position="right">
                        <Pagination page={activePage} onChange={setActivePage} radius="lg" total={storeListPage.totalPages} />
                    </Group>

                </Card>
            </>)
            }
        </div>
    )
}
