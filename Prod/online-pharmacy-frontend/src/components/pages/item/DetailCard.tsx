import {
    Card,
    Grid,
    Group,
    Image,
    Text,
    Title
} from "@mantine/core";

export interface DetaildataType {
    name: string;
    imgUrl: string;
    proprietaryName: string;
    price: number;
    description: string;
    qty: number;
}

export interface DetailCardType {
    item: DetaildataType;
    children?: React.ReactNode;
    checkbox?: React.ReactNode;
}
export function DetailCard({ item, children, checkbox }: DetailCardType) {

    return (
        <Card withBorder radius="md" px={10} py={20} mb="md">
            <Grid>
                <Grid.Col xs={4}>
                    <Image src={item.imgUrl} />
                </Grid.Col>
                <Grid.Col xs={8}>
                    <Group position="apart" mt="md" mb="xs">
                        <Group spacing={5} noWrap>
                            {checkbox}

                            <Title order={4}>{item.name}</Title>
                            <Text size="xs" color="dimmed">
                                •
                            </Text>
                            <Text color="dimmed" size="xs">
                                {item.proprietaryName}
                            </Text>
                        </Group>

                        <Title order={4}>₹{item.price}</Title>
                    </Group>

                    {/* <Badge color="green" variant="light">
                        InStock
                    </Badge> */}

                    <Text lineClamp={2} mt="xs" mb="md">
                        {item.description + " "}
                    </Text>
                    <Grid>
                        <Grid.Col span={4}>
                            <Text color="dimmed">{item.qty} qty</Text>
                        </Grid.Col>
                        {children}
                    </Grid>
                </Grid.Col>
            </Grid>
        </Card>
    );
}
