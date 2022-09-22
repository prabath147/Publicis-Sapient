import { Box, Card, Container, Grid, Group, Image, LoadingOverlay, MultiSelect, Space, Text, Title } from "@mantine/core";
import { showNotification } from "@mantine/notifications";
import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { Item } from '../models';
import { getItemData } from "./InventoryAPI";

const ItemPage = () => {
    const { itemId } = useParams();
    const id = parseInt(itemId || "0");


    const [loading, setLoading] = useState(true);
    const [itemData, setItemData] = useState<Item | null>(null);
    const [categoryList, setCategoryList] = useState<string[]>([]);
    const [categoryArray, setCategoryArray] = useState([{
        categoryId: 0,
        categoryName: "",
    }]);

    useEffect(() => {
        // dispatch();
        getItemData(id).then(response => {
            console.log(itemData);

            setItemData(response.data);
            setCategoryArray(response.data.product.categorySet);
            setCategoryList(response.data.product.categorySet.map(cat => cat.categoryName));

        }).catch(error => {
            showNotification({
                message: "Oops something went wrong!!",
                color: "red"
            })
        })
        setLoading(false);
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [id]);
    return (
        <>
            {loading ? (<LoadingOverlay
                loaderProps={{ size: 'md', color: 'blue', variant: 'oval' }}
                overlayOpacity={0.3}
                overlayColor="#c5c5c5"
                visible={loading}
            />)
                :
                (<Container >
                    <Group position='apart'>
                        <Title order={4}>{itemData?.product.productName}</Title>
                    </Group>

                    <Space h="md" />
                    <Card shadow="sm" p="lg" radius="md" withBorder>
                        <Grid>
                            <Grid.Col lg={6}>
                                <Grid>
                                    <Grid.Col lg={6}>
                                        <Text weight={500} size={"sm"} pb={1}>Product ID</Text>
                                        <Box
                                            sx={(theme) => ({
                                                backgroundColor: theme.colors.gray[1],
                                                padding: theme.spacing.xs,
                                                borderRadius: theme.radius.md,
                                            })}
                                        >
                                            <Text size={"sm"} align="justify">
                                                {itemData?.product.productId}
                                            </Text>
                                        </Box>
                                    </Grid.Col>
                                    <Grid.Col lg={6}>
                                        <Text weight={500} size={"sm"} pb={1}>Item ID</Text>
                                        <Box
                                            sx={(theme) => ({
                                                backgroundColor: theme.colors.gray[1],
                                                padding: theme.spacing.xs,
                                                borderRadius: theme.radius.md,
                                            })}
                                        >
                                            <Text size={"sm"} align="justify">
                                                {itemData?.itemId}
                                            </Text>
                                        </Box>
                                    </Grid.Col>
                                    <Grid.Col lg={12}>
                                        <Text weight={500} size={"sm"} pb={1}>Product Name</Text>
                                        <Box
                                            sx={(theme) => ({
                                                backgroundColor: theme.colors.gray[1],
                                                padding: theme.spacing.sm,
                                                borderRadius: theme.radius.md,
                                            })}
                                        >
                                            <Text size={"sm"} align="justify">
                                                {itemData?.product.productName}
                                            </Text>
                                        </Box>
                                    </Grid.Col>
                                    <Grid.Col lg={12}>
                                        <Text weight={500} size={"sm"} pb={1}>Proprietary Name</Text>
                                        <Box
                                            sx={(theme) => ({
                                                backgroundColor: theme.colors.gray[1],
                                                padding: theme.spacing.sm,
                                                borderRadius: theme.radius.md,
                                            })}
                                        >
                                            <Text size={"sm"} align="justify">
                                                {itemData?.product.proprietaryName}
                                            </Text>
                                        </Box>
                                    </Grid.Col>
                                    <Grid.Col lg={12}>
                                        <Text weight={500} size={"sm"} pb={1}>Dosage Form</Text>
                                        <Box
                                            sx={(theme) => ({
                                                backgroundColor: theme.colors.gray[1],
                                                padding: theme.spacing.xs,
                                                borderRadius: theme.radius.md,
                                            })}
                                        >
                                            <Text size={"sm"} align="justify">
                                                {itemData?.product.dosageForm}
                                            </Text>
                                        </Box>
                                    </Grid.Col>
                                </Grid>

                            </Grid.Col>
                            <Grid.Col lg={6}>
                                <Image
                                    width={"100%"}
                                    height={300}
                                    fit="fill"
                                    alt="image"
                                    src={itemData?.product.imageUrl}
                                    withPlaceholder
                                    radius="md"
                                />
                            </Grid.Col>
                        </Grid>
                        <Grid>
                            <Grid.Col lg={12} px={0}>
                                <Text weight={500} size={"sm"} pb={1}>Description</Text>
                                <Box
                                    sx={(theme) => ({
                                        backgroundColor: theme.colors.gray[1],
                                        padding: theme.spacing.xs,
                                        borderRadius: theme.radius.md,
                                    })}
                                >
                                    <Text size={"sm"} align="justify">
                                        {itemData?.product.description}
                                    </Text>
                                </Box>
                            </Grid.Col>
                        </Grid>
                        <Grid>
                            <Grid.Col lg={6}>
                                <MultiSelect
                                    label="Catagory"
                                    // data={[
                                    //     { label: 'd3', value: 'd3' },
                                    //     { label: 'e', value: 'e' },
                                    // ]}
                                    // data={categoryArray?.map(cat => ({ value: cat, label: cat }))}
                                    data={categoryArray.map(cat => ({ value: cat.categoryName, label: cat.categoryName }))}
                                    value={categoryList} onChange={setCategoryList}
                                    disabled
                                />
                            </Grid.Col >
                            <Grid.Col lg={6}>
                                <Text weight={500} size={"sm"} pb={1}>Product Type</Text>
                                <Box
                                    sx={(theme) => ({
                                        backgroundColor: theme.colors.gray[1],
                                        padding: theme.spacing.xs,
                                        borderRadius: theme.radius.md,
                                    })}
                                >
                                    <Text size={"sm"} align="justify">
                                        {itemData?.product.productType ? "Generic" : "Non-Generic"}
                                    </Text>
                                </Box>
                            </Grid.Col>
                        </Grid>


                        <Grid>
                            <Grid.Col lg={6}>
                                <Text weight={500} size={"sm"} pb={1}>Manufactured Date</Text>
                                <Box
                                    sx={(theme) => ({
                                        backgroundColor: theme.colors.gray[1],
                                        padding: theme.spacing.xs,
                                        borderRadius: theme.radius.md,
                                    })}
                                >
                                    <Text size={"sm"} align="justify">
                                        {itemData?.manufacturedDate.slice(0, 10)}
                                    </Text>
                                </Box>
                            </Grid.Col>
                            <Grid.Col lg={6}>
                                <Text weight={500} size={"sm"} pb={1}>Expiry Date</Text>
                                <Box
                                    sx={(theme) => ({
                                        backgroundColor: theme.colors.gray[1],
                                        padding: theme.spacing.xs,
                                        borderRadius: theme.radius.md,
                                    })}
                                >
                                    <Text size={"sm"} align="justify">
                                        {itemData?.expiryDate.slice(0, 10)}
                                    </Text>
                                </Box>
                            </Grid.Col>
                        </Grid>
                        <Grid>
                            <Grid.Col lg={6}>
                                <Text weight={500} size={"sm"} pb={1}>Quantity</Text>
                                <Box
                                    sx={(theme) => ({
                                        backgroundColor: theme.colors.gray[1],
                                        padding: theme.spacing.xs,
                                        borderRadius: theme.radius.md,
                                    })}
                                >
                                    <Text size={"sm"} align="justify">
                                        {itemData?.itemQuantity}
                                    </Text>
                                </Box>
                            </Grid.Col>
                            <Grid.Col lg={6}>
                                <Text weight={500} size={"sm"} pb={1}>Price</Text>
                                <Box
                                    sx={(theme) => ({
                                        backgroundColor: theme.colors.gray[1],
                                        padding: theme.spacing.xs,
                                        borderRadius: theme.radius.md,
                                    })}
                                >
                                    <Text size={"sm"} align="justify">
                                        ₹ {itemData?.price}
                                    </Text>
                                </Box>
                            </Grid.Col>
                        </Grid>
                    </Card>
                </Container>)}
        </>
    )
}

export default ItemPage
