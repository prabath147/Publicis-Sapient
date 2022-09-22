import { Center, Container, Pagination, Title } from "@mantine/core";
import { showNotification } from "@mantine/notifications";
import { useEffect, useState } from "react";
import { useAppSelector } from "../../../app/hooks";
import LoadingComponent from "../../ui/LoadingComponent/LoadingComponent";
import { getUserData } from "../Auth/Login/UserSlice";
import { Order } from "./models";
import { getOrders } from "./OrderAPI";
import OrderListItem from "./OrderListItem";

export default function OrderList() {
  const user = useAppSelector(getUserData);

  const [activePage, setPage] = useState(1);
  const [pageCount, setPageCount] = useState(1);
  const [loading, setLoading] = useState(true);

  const [orderList, setOrderList] = useState<Order[]>([])

  useEffect(() => {
    setLoading(true)
    getOrders(user.id)
      .then((response) => {
        console.log(response.data)
        setOrderList(response.data.data)
        setPageCount(response.data.totalPages)
        setLoading(false)
      })
      .catch(error => {
        showNotification({ color: "red", message: "oops, something went wrong" })
      })


    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [activePage]);

  const orderItems = orderList.map((order: Order) => (
    <OrderListItem
      id={order.orderId}
      orderDate={order.orderDate}
      price={order.price}
      customer={user.username}
      shippingAddress={order.orderAddress}
    />
  ));

  if (loading) return <LoadingComponent />

  return (
    <div>
      <Container>
        <Title>Your Orders</Title>
        {/* <Divider my="sm" /> */}
        {orderItems}
        {/* <Divider my="sm" /> */}
        <Center>
          <Pagination page={activePage} onChange={setPage} total={pageCount} />
        </Center>
      </Container>
    </div>
  );
}
