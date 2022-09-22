import { useEffect, useState } from "react";

import {
  Card,
  Container,
  Divider,
  Grid,
  Group,
  Text,
  Title
} from "@mantine/core";
import { showNotification } from "@mantine/notifications";
import { useParams } from "react-router-dom";
import LoadingComponent from "../../ui/LoadingComponent/LoadingComponent";
import { ItemMiniDTO } from "../Cart/models";
import CartItemCard from "../item/CartItemCard";
import { DeliveryCharges } from "../OrderCreatePage/Constants";
import { Order } from "../orderList/models";
import { getOrderDetailsById } from "./OrderAPI";
// import { loadData, selectItems } from "./OrderSlice";

export default function OrderDetailPage() {
  let { id } = useParams();
  const orderId = parseInt(id || "0");

  const [orderDetail, setOrderDetail] = useState<Order | null>(null)
  const [loading, setLoading] = useState(true)


  // const itemList = useAppSelector(selectItems);
  // const dispatch = useAppDispatch();

  function itemSubTotal(items: ItemMiniDTO[]): number {
    let total = 0;
    items.forEach((item) => {
      total += item.itemQuantity * item.price
    })
    return total;
  }

  // const navigate = useNavigate()
  useEffect(() => {
    // load details by id
    setLoading(true)
    getOrderDetailsById(orderId)
      .then(response => {
        setOrderDetail(response.data)
        setLoading(false)
      })
      .catch(err => {
        showNotification({ color: "red", message: "oops, Something went wrong" })
        // navigate("/orders/list")
      })

    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [orderId, id]);


  if (loading || orderDetail === null) return <LoadingComponent />

  const orderItems = orderDetail.items.map((item) => (
    <CartItemCard key={item.itemId} item={item} />
  ));

  return (
    <div>
      <Container>
        <Title order={1}>Order Details</Title>
        <Group noWrap>
          <Text>Ordered on {orderDetail.orderDate.toString()}</Text>
          <Text>Order# {orderDetail.orderId}</Text>
        </Group>

        <Divider my="sm" />

        <Card>
          <Grid gutter={"lg"}>
            <Grid.Col xs={4}>
              <Title order={5} mb="md">
                Shipping Address
              </Title>
              <Text>
                {orderDetail.orderAddress.street}, {orderDetail.orderAddress.city}, {orderDetail.orderAddress.state},
                <br />
                {orderDetail.orderAddress.pinCode}, {orderDetail.orderAddress.country}
              </Text>
            </Grid.Col>

            <Grid.Col xs={4}>
              <Title order={5} mb="md">
                Payment Method
              </Title>
              <Text>
                Pay on Delivery (Cash/Card). Cash on delivery (COD) available.
                Card/Net banking acceptance subject to device availability.
              </Text>
            </Grid.Col>

            <Grid.Col xs={4}>
              <Title order={5} mb="md">
                Order Summary
              </Title>
              <Group noWrap spacing={0} position={"apart"}>
                <Text>Item(s) Subtotal:</Text>
                <Text> ₹{itemSubTotal(orderDetail.items)}</Text>
              </Group>

              <Group noWrap spacing={0} position={"apart"}>
                <Text>Shipping:</Text>
                <Text> ₹ {DeliveryCharges}</Text>
              </Group>

              <Group noWrap spacing={0} position={"apart"}>
                <Text>Promotion Applied:</Text>
                <Text> {orderDetail.price - itemSubTotal(orderDetail.items) - 100} </Text>
              </Group>

              <Group noWrap spacing={0} position={"apart"}>
                <Text weight={800}>Grand Total:</Text>
                <Text weight={800}> {orderDetail.price} </Text>
              </Group>
            </Grid.Col>
          </Grid>
        </Card>

        {orderItems}
      </Container>
    </div>
  );
}
