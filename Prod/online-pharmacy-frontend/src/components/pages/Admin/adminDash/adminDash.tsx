import { Card, Container, Grid, Text } from '@mantine/core';
import { useMediaQuery } from '@mantine/hooks';
import { useEffect, useState } from 'react';
import { activeCustomers, activeSub, getManagerList, getStoreList_count, orders_count } from './adminDashAPIs';





export default function AdminDash() {

    const [managers, setManagers] = useState<number>(0);
    const [stores, setStores] = useState<number>(0);
    const [subs, setSubs] = useState<number>(0);
    const [custs, setCusts] = useState<number>(0);
    const [orders, setOrders] = useState<number>(0);

    useEffect(() => {
        getAllDetailsAPI();
    }, [])
    
    
    const getAllDetailsAPI = () => {

        getManagerList().then((res) => {
            setManagers(res.data);
        });
        getStoreList_count().then((res) => {
            setStores(res.data.totalRecords);
        })

        activeSub().then((res) => {
            setSubs(res.data.totalRecords);
        })

        activeCustomers().then((res) => {
            setCusts(res.data);
        })

        orders_count().then((res) => {
            setOrders(res.data);
        })
    }
    

    const matches_2 = useMediaQuery('(min-width: 770px)', true);

    return (
        <>
        <Container size="xl">
        <Grid py={30} gutter={matches_2 ? 20 : 20} justify="center">
        <Grid.Col span={ matches_2? 4 : 10}>
                <Card shadow="sm" p="lg" radius="md" withBorder>
                <Card.Section>
                    <Text weight={500} align='center'
                    color="blue"
                    size={ matches_2 ? 100 : 35 }
                    >
                        {/* {alldetails?.number_of_stores} */}
                        {custs}
                        
                    </Text>
                    </Card.Section>
                    <Text weight={500} align='center' size={matches_2? 16 : 14}
                    >
                        Active Customers
                    </Text>
                    </Card>
                </Grid.Col>
        <Grid.Col span={ matches_2? 4 : 10}>
                <Card shadow="sm" p="lg" radius="md" withBorder>
                <Card.Section>
                    <Text weight={500} align='center'
                    color="blue"
                    size={matches_2 ? 100 : 35}
                    >
                        {/* {alldetails?.number_of_stores} */}
                        {managers}
                        
                    </Text>
                    </Card.Section>
                    <Text weight={500} align='center' size={matches_2? 16 : 14}
                    >
                        Managers
                    </Text>
                    </Card>
                </Grid.Col>
        <Grid.Col span={ matches_2? 4 : 10}>
                <Card shadow="sm" p="lg" radius="md" withBorder>
                <Card.Section>
                    <Text weight={500} align='center'
                    color="blue"
                    size={matches_2 ? 100 : 35}
                    >
                        {/* {alldetails?.number_of_stores} */}
                        {stores}
                        
                    </Text>
                    </Card.Section>
                    <Text  weight={500} align='center' size={matches_2? 16 : 14}
                    >
                        Stores
                    </Text>
                    </Card>
                </Grid.Col>
        <Grid.Col span={ matches_2? 4 : 10}>
                <Card shadow="sm" p="lg" radius="md" withBorder>
                <Card.Section>
                    <Text weight={500} align='center'
                    color="blue"
                    size={matches_2 ? 100 : 35}
                    >
                        {/* {alldetails?.number_of_stores} */}
                        {subs}
                        
                    </Text>
                    </Card.Section>
                    <Text weight={500} align='center' size={matches_2? 16 : 14}
                    >
                        Subscriptions
                    </Text>
                    </Card>
                </Grid.Col>
        <Grid.Col span={ matches_2? 4 : 10}>
                <Card shadow="sm" p="lg" radius="md" withBorder>
                <Card.Section>
                    <Text weight={500} align='center'
                    color="blue"
                    size={matches_2 ? 100 : 35}
                    >
                        {/* {alldetails?.number_of_stores} */}
                        {orders}
                        
                    </Text>
                    </Card.Section>
                    <Text weight={500} align='center' size={matches_2? 16 : 14}
                    >
                        Orders
                    </Text>
                    </Card>
                </Grid.Col>

                

                
               
                </Grid>

            
        </Container>
        </>
    );
}