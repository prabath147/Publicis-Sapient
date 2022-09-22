import { Card, Grid, Text } from "@mantine/core";
import { Link } from "react-router-dom";

export default function OrderListItem({
  id,
  customer,
  orderDate,
  price,
  shippingAddress,
}) {
  // const monthNames = [
  //   "January",
  //   "February",
  //   "March",
  //   "April",
  //   "May",
  //   "June",
  //   "July",
  //   "August",
  //   "September",
  //   "October",
  //   "November",
  //   "December",
  // ];

  return (
    <Card withBorder radius={"md"} px={30} mb={"sm"}>
      <Card.Section withBorder>
        <Grid>
          <Grid.Col span={2}>
            <Text color={"dimmed"} transform="uppercase">
              Order Placed
            </Text>
            <Text>{orderDate.toString()}</Text>
          </Grid.Col>

          <Grid.Col span={2}>
            <Text align="center" color={"dimmed"} transform="uppercase">
              Total
            </Text>
            <Text align="center">{price}</Text>
          </Grid.Col>

          <Grid.Col span={2}>
            <Text color={"dimmed"} transform="uppercase">
              Ship to
            </Text>
            <Text>{customer}</Text>
          </Grid.Col>

          <Grid.Col span={3}>
            <Text color={"dimmed"} transform="uppercase">
              Shipping Address
            </Text>
            <Text lineClamp={2}>{
              `${customer}, ${shippingAddress.street} ${shippingAddress.city} ${shippingAddress.state} ${shippingAddress.country} ${shippingAddress.pinCode}`
            }</Text>
          </Grid.Col>

          <Grid.Col span={2}>
            <Link to={"/orders/" + id}>
              ORDER # {id}
            </Link>
          </Grid.Col>
        </Grid>
      </Card.Section>
    </Card>
  );
}
