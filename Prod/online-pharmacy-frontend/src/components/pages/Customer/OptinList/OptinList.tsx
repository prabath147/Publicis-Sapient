import { useEffect, useState } from "react";

import { Accordion, Center, Container, Grid, Group, Loader, Pagination, Space, Text, Title, UnstyledButton } from "@mantine/core";
import { showNotification } from "@mantine/notifications";
import { IconEdit, IconPill, IconTrash } from '@tabler/icons';
import { Link } from "react-router-dom";
import { useAppSelector } from "../../../../app/hooks";
import { getUserData } from "../../Auth/Login/UserSlice";
import { OptInType } from "./models";
import { deleteOptInByIdAPI, getOptInForUserAPI } from "./OptinAPI";



export default function OptinList() {
  // const optinList = useAppSelector(getOptInList)
  // const dispatch = useAppDispatch()
  const id = useAppSelector(getUserData).id
  const [activePage, setPage] = useState(1);
  const [pageCount, setPageCount] = useState(1);
  const [loading, setLoading] = useState(true);
  const [optInList, setOptInList] = useState<OptInType[]>([])
  const limit = 5;

  const loadDataAction = () => {
    setLoading(true);
    getOptInForUserAPI(id, activePage - 1, limit)
      .then(response => {
        // console.log(response.data);
        setPageCount(response.data.totalPages);
        setOptInList(response.data.data)
        setLoading(false)


        // dispatch(loadOptIn(response.data.data))
      })
      .catch(err => {
        // console.error(err)
        showNotification({ color: "red", message: "oops, something went wrong" })
        setLoading(false)
        // dispatch(loadOptIn(optinMock))
      })
  };

  useEffect(() => {
    loadDataAction();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [activePage]);

  if (loading) return (
    <Center style={{ height: "80%" }}>
      <Loader data-testid="Loading" />
    </Center >
  )

  return (
    <div>
      <Container>
        <Title align="center">
          Opt In List
        </Title>
        <br />

        <Accordion variant="contained">
          {optInList.map((optin: OptInType) => (
            <Accordion.Item value={optin.id.toString()} >
              <Accordion.Control icon={<IconPill size={20} color="green" />}>
                <Group position="apart" spacing="sm">
                  <Title size={16}>{optin.name}</Title>
                  <Text>Next: {optin.deliveryDate.toString()}</Text>
                </Group>
              </Accordion.Control>
              <Accordion.Panel>

                <Group position="right">
                  <Link to={"/optin/update/" + optin.id}>
                    <IconEdit color="green" />
                  </Link>
                  <UnstyledButton onClick={() => {
                    deleteOptInByIdAPI(optin.id)
                      .then(() => {
                        setOptInList(optInList.filter(obj => obj.id !== optin.id))
                      })
                      .catch(() => {
                        showNotification({ color: 'red', message: 'Oops, Something went wrong' })
                      })
                  }}>
                    <IconTrash color="red" />
                  </UnstyledButton>
                </Group>

                <Grid>
                  <Grid.Col span={4}>Id</Grid.Col>
                  <Grid.Col span={8}>{optin.id}</Grid.Col>

                  <Grid.Col span={4}>Name</Grid.Col>
                  <Grid.Col span={8}>{optin.name}</Grid.Col>

                  <Grid.Col span={4}>Delivery Intervals</Grid.Col>
                  <Grid.Col span={8}>{optin.intervalInDays} days</Grid.Col>

                  <Grid.Col span={4}>Next Delivery</Grid.Col>
                  <Grid.Col span={8}>{optin.deliveryDate.toString()} </Grid.Col>

                  <Grid.Col span={4}>Deliveries Left</Grid.Col>
                  <Grid.Col span={8}>{optin.numberOfDeliveries}</Grid.Col>
                </Grid>

                {/* <CartCard cart={optin.cart} /> */}
              </Accordion.Panel>
            </Accordion.Item>
          ))}
        </Accordion>

        <Space h="xl" />

        <Pagination
          position="center"
          page={activePage}
          onChange={setPage}
          total={pageCount}
        />
      </Container>
    </div >
  );
}


