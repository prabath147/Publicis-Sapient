import { faCartPlus, faInr } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
  Badge,
  Button,
  Card,
  Container,
  Divider,
  Grid,
  Group,
  Image,
  Modal,
  Skeleton,
  Space,
  Spoiler,
  Table,
  Text,
} from "@mantine/core";
import { showNotification } from "@mantine/notifications";
import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { useAppDispatch, useAppSelector } from "../../../../app/hooks";
import LoadingComponent from "../../../ui/LoadingComponent/LoadingComponent";
import { getUserData } from "../../Auth/Login/UserSlice";
import { addToCartAPI, getItemDetailAPI } from "../../Cart/CartAPI";
import { loadCart } from "../../Cart/CartSlice";
import { ItemDetailDTO } from "../../Cart/models";
import { Item } from "./Itemobjs";

export default function ProductCard({ item, loading, added }) {
  const dispatch = useAppDispatch();
  const user = useAppSelector(getUserData);
  const navigate = useNavigate();
  const [opened, setOpened] = useState(false);
  // const [clicked, setClicked] = useState(false); // for addToCart button
  const [itemDetail, setItemDetail] = useState<ItemDetailDTO | null>(null);
  const [detailLoading, setDetailLoading] = useState(true);
  const description =
    item.description !== undefined ? item.description : "No Data Found!";
  const stockStatus =
    item.quantity !== undefined ? item.quantity : "No Data Found!";

  useEffect(() => {
    if (opened) {
      setDetailLoading(true);
      getItemDetailAPI(item.id)
        .then((response) => {
          setItemDetail(response.data);
          console.log(response);
          setDetailLoading(false);
          // save data
        })
        .catch((error) => {
          console.log(error);
          showNotification({
            color: "red",
            message: "oops, something went wrong",
          });
        });
    }
  }, [item.id, opened]);

  const handleAddtoCart = (item: Item) => {
    const data = {
      itemIdFk: item.itemId,
      itemQuantity: 1,
      price: item.price,
    };

    addToCartAPI(user.id, data)
      .then((response) => {
        console.log(response);
        dispatch(loadCart(response.data.items));
        // setClicked(true);
      })
      .catch((error) => {
        console.log(error);
        showNotification({
          message: "Oops! Something went wrong",
          color: "red",
        });
      });
  };

  const modal = (
    <Modal
      title="Product Summary"
      opened={opened}
      onClose={() => {
        setOpened(false);
        console.log(opened);
      }}
      size={"sm"}
    >
      {" "}
      {detailLoading || itemDetail === null ? (
        <LoadingComponent />
      ) : (
        <>
          <Table striped highlightOnHover>
            <tbody>
              <tr>
                <td>
                  <strong>NAME</strong>
                </td>
                <td>
                  <Text size="md" weight={600}>
                    {itemDetail.product.productName.charAt(0).toUpperCase() +
                      itemDetail.product.productName.slice(1)}
                  </Text>
                </td>
              </tr>
              <tr>
                <td>
                  <strong>PRICE</strong>
                </td>
                <td>
                  <Text size="md" weight={600}>
                    <FontAwesomeIcon icon={faInr} size="sm" />
                    &nbsp;{itemDetail.price}
                  </Text>
                </td>
              </tr>

              <tr>
                <td>
                  <strong>PROPRIETARY NAME</strong>
                </td>
                <td>
                  <Text size="md" weight={600}>
                    {itemDetail.product.proprietaryName
                      .charAt(0)
                      .toUpperCase() +
                      itemDetail.product.proprietaryName.slice(1)}
                  </Text>
                </td>
              </tr>
              <tr>
                <td>
                  <strong>DOSAGE FORM</strong>
                </td>
                <td>
                  <Text size="md" weight={600}>
                    {itemDetail.product.dosageForm.charAt(0).toUpperCase() +
                      itemDetail.product.dosageForm.slice(1)}
                  </Text>
                </td>
              </tr>

              <tr>
                <td>
                  <strong>STORE</strong>
                </td>
                <td>
                  <Text size="md" weight={600}>
                    {itemDetail.store.storeName.charAt(0).toUpperCase() +
                      itemDetail.store.storeName.slice(1)}
                  </Text>
                </td>
              </tr>
              <tr>
                <td>
                  <strong>MFG DATE</strong>
                </td>
                <td>
                  <Text size="md" weight={600}>
                    {itemDetail.manufacturedDate.toString()}
                  </Text>
                </td>
              </tr>
              <tr>
                <td>
                  <strong>EXPIRY DATE</strong>
                </td>
                <td>
                  <Text size="md" weight={600}>
                    {itemDetail.expiryDate.toString()}
                  </Text>
                </td>
              </tr>

              <tr>
                <td>
                  <strong>CATEGORY</strong>
                </td>
                <td>
                  <Text size="md" weight={600}>
                    {itemDetail.product.categorySet.map((cn, index) => {
                      return (
                        <p key={cn.categoryId}>
                          {cn.categoryName}{" "}
                          {index < itemDetail.product.categorySet.length - 1
                            ? ", "
                            : ""}
                        </p>
                      );
                    })}
                  </Text>
                </td>
              </tr>
              {/* <tr>
          <td>
            <strong>TYPE</strong>
          </td>
          <td>
            <Text size={"md"} weight={"600"}>
              {item.productType ? "Generic" : "Non Generic"}
            </Text>
          </td>
        </tr> */}
            </tbody>
          </Table>
          <Space h="xs" />
          <Divider size="md" />
          <Space h="xs" />

          <Container>
            <Text size="md" weight={600}>
              <strong>Description</strong>
            </Text>
            <Space h="sm" />
            <Text size="md" weight={600}>
              <Spoiler
                maxHeight={120}
                showLabel="Read more"
                hideLabel="Read Less"
              >
                {description}
              </Spoiler>
            </Text>
          </Container>

          <Space h={"xs"} />
          {
            // cartItems.indexOf(item.itemId) !== -1 ||
            added ? (
              <Button
                data-testid="aT"
                sx={{ float: "right", marginRight: "10px" }}
                variant="light"
                color="orange"
                radius="md"
                onClick={() => {
                  navigate("/cart");
                }}
              >
                {"View Cart"}
              </Button>
            ) : (
              <Button
                sx={{ float: "right", marginRight: "10px" }}
                variant="light"
                leftIcon={<FontAwesomeIcon icon={faCartPlus} />}
                color="blue"
                radius="md"
                onClick={() => {
                  handleAddtoCart(item);
                }}
              >
                {"Add to Cart"}
              </Button>
            )
          }
        </>
      )}
    </Modal>
  );

  return (
    <Card shadow="sm" p="xs" radius="md" withBorder>
      {modal}

      <Grid>
        <Grid.Col span={3}>
          <Skeleton visible={loading}>
            <Card.Section>
              <Image
                sx={{ cursor: "pointer" }}
                radius="md"
                src={item.imageUrl}
                height={80}
                width={110}
                alt="img"
                onClick={() => {
                  setOpened(true);
                  console.log(opened);
                }}
              />
            </Card.Section>
          </Skeleton>
          <Space h={"md"} />
          <Skeleton visible={loading}>
            {item.productType ? (
              <Badge color="violet" variant="light" size="sm">
                {"Generic"}
              </Badge>
            ) : (
              <Badge color="pink" variant="light" size="xs">
                Non-Generic
              </Badge>
            )}

            <Space h={"xs"} />

            {stockStatus > 0 || stockStatus === "No Data Found!" ? (
              <Badge color="green" variant="light" size="sm">
                Instock
              </Badge>
            ) : (
              <Badge color="red" variant="light" size="xs">
                OutofStock
              </Badge>
            )}
          </Skeleton>
        </Grid.Col>

        <Grid.Col span={9}>
          <Skeleton visible={loading}>
            <Text
              sx={{ cursor: "pointer" }}
              size="lg"
              weight={600}
              onClick={() => {
                setOpened(true);
                console.log(opened);
              }}
            >
              {item.productName.charAt(0).toUpperCase() +
                item.productName.slice(1)}
            </Text>
          </Skeleton>
          <Space h={"xs"} />
          <Skeleton visible={loading} height="30px">
            <Text size="sm" color={"dimmed"} weight={600}>
              {item.proprietaryName.charAt(0).toUpperCase() +
                item.proprietaryName.slice(1)}
            </Text>
          </Skeleton>

          <Space h={"xs"} />

          <Skeleton visible={loading} height="30px">
            <Text size="md" color={"dark"} weight={600}>
              {item.dosageForm}
            </Text>
          </Skeleton>
          <Space h={"xs"} />

          <Skeleton visible={loading} mt={"10px"}>
            <Group position="apart" spacing={"lg"}>
              <Text size="xl" weight={600}>
                <FontAwesomeIcon icon={faInr} size="sm" />
                &nbsp;{item.price}
              </Text>
              {
                // cartItems.indexOf(item.itemId) !== -1 ||
                added ? (
                  <Button
                    variant="light"
                    color="orange"
                    radius="md"
                    onClick={() => {
                      navigate("/cart");
                    }}
                  >
                    {"View Cart"}
                  </Button>
                ) : (
                  <Button
                    variant="light"
                    leftIcon={<FontAwesomeIcon icon={faCartPlus} />}
                    color="blue"
                    radius="md"
                    onClick={() => {
                      handleAddtoCart(item);
                    }}
                    disabled={item.quantity <= 0}
                  >
                    {"Add to Cart"}
                  </Button>
                )
              }
            </Group>
          </Skeleton>
        </Grid.Col>
      </Grid>
    </Card>
  );
}
