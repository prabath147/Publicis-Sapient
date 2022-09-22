import { Card, Center, Container, Text, Title } from '@mantine/core';
import { IconBuildingStore, IconTrendingUp } from '@tabler/icons';
import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { useAppSelector } from '../../../../app/hooks';
import { getUserData } from '../../Auth/Login/UserSlice';
import ManagerStoreList from '../ManagerStoreList/ManagerStoreList';
import { getRev, getStoreCnt } from './managerDashAPI';



export default function ManagerDash() {
    // const [open, setOpen] = useState(false);
    const [stores, setStores] = useState<number>(0);
    const [revenue, setRevenue] = useState<number>(0);
    const cuser = useAppSelector(getUserData);

    useEffect(() => {
        getServiceAPI();
        // eslint-disable-next-line
    }, [])

    const getServiceAPI = () => {
        getStoreCnt(cuser.id).then((res) => {
            setStores(res.data);
        });

        getRev(cuser.id).then((res) => {
            setRevenue(res.data);
        })
    }

    return (
        <div >
            <Title order={2}>Manager Dashboard</Title>
            <Container style={{ display: 'flex' }} >

                {/* <CreateStore open={open} setOpen={setOpen}/>

            <Group position='apart' my={10}>
                <Text size="lg" weight={500}>Stores</Text>                
                <Button
                    leftIcon={<IconPlus />}
                    onClick={() => setOpen(true)} variant="gradient" gradient={{ from: 'teal', to: 'lime', deg: 105 }}>
                    Add Store
                </Button>
            </Group>    */}
                <Card shadow="sm" p="lg" radius="md" withBorder sx={{ width: "50%", marginTop: "20px", margin: "30px" }}>
                    <Link to="/manager/store" style={{ textDecoration: 'none' }}>
                        <Card.Section>
                            <Text weight={200} align='center' size={50} color="blue">
                                {stores}
                            </Text>
                            <Center>
                                <IconBuildingStore size={20} />
                                <Text pl={10} size="sm" color="dimmed" align='center'>
                                    STORES
                                </Text>
                            </Center>
                        </Card.Section>
                    </Link>
                </Card>

                <Card shadow="sm" p="lg" radius="md" withBorder sx={{ width: "50%", marginTop: "20px", margin: "30px" }}>
                    <Link to="" style={{ textDecoration: 'none' }}>
                        <Card.Section>
                            <Text weight={200} align='center' size={50} color="blue">
                                {revenue}
                            </Text>
                            <Center>
                                <IconTrendingUp size={20} />
                                <Text pl={10} size="sm" color="dimmed" align='center'>
                                    REVENUE
                                </Text>
                            </Center>
                        </Card.Section>

                    </Link>
                </Card>

            </Container>
            <ManagerStoreList />
        </div>

    );

}