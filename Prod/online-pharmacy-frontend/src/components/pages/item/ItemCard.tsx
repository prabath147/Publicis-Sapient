import {
  Badge,
  Card,
  Grid,
  Group,
  Image,
  Text,
  Title
} from "@mantine/core";
import { ItemDetailDTO } from "../Cart/models";


interface ItemCardType {
  item: ItemDetailDTO;
  children?: React.ReactNode;
  checkbox?: React.ReactNode;
}
export function ItemCard({ item, children, checkbox }: ItemCardType) {

  return (
    <Card withBorder radius="md" px={10} py={20} mb="md">
      <Grid>
        <Grid.Col xs={4}>
          <Image src={item.product.imageUrl} />
        </Grid.Col>
        <Grid.Col xs={8}>
          <Group position="apart" mt="md" mb="xs">
            <Group spacing={5} noWrap>
              {checkbox}

              <Title order={4}>{item.product.productName}</Title>
              <Text size="xs" color="dimmed">
                •
              </Text>
              <Text color="dimmed" size="xs">
                {item.product.proprietaryName}
              </Text>
            </Group>

            <Title order={4}>₹{item.price}</Title>
          </Group>

          <Badge color="green" variant="light">
            InStock
          </Badge>

          <Text lineClamp={2} mt="xs" mb="md">
            {item.product.description + " "}
          </Text>
          <Grid>
            <Grid.Col span={4}>
              <Text color="dimmed">{item.itemQuantity} qty</Text>
            </Grid.Col>
            {children}
          </Grid>
        </Grid.Col>
      </Grid>
    </Card>
  );
}
