import {
  Badge,
  Button,
  Center,
  Checkbox,
  Container,
  Grid,
  Group,
  Space,
  Text,
  Title,
  UnstyledButton
} from "@mantine/core";
import { showNotification } from "@mantine/notifications";
import { IconNotes } from "@tabler/icons";
import { FaMinus, FaPlus, FaShoppingCart, FaTrash } from "react-icons/fa";
import { Link } from "react-router-dom";
import { useAppDispatch, useAppSelector } from "../../../app/hooks";
import { getUserData } from "../Auth/Login/UserSlice";
import ItemLoadableCard from "../item/CartItemCard";
import {
  decreamentInCartAPI,
  EmptyCartAPI,
  increamentInCartAPI
} from "./CartAPI";
import {
  clearCart,
  decrementQuantity,
  getCartItemCount,
  getCartItems,
  getCartSelectedIds,
  getCartTotalAmount,
  incrementQuantity,
  removeItemFromCart,
  toggleSelect
} from "./CartSlice";

export default function Cart() {
  const cart = useAppSelector(getCartItems);
  const totalItem = useAppSelector(getCartItemCount);
  const totalAmount = useAppSelector(getCartTotalAmount);
  const cartSelected = useAppSelector(getCartSelectedIds);
  // console.log(cart)

  // console.log(cart);

  // const navigate = useNavigate();
  const uid = useAppSelector(getUserData).id;

  const dispatch = useAppDispatch();

  return (
    <>
      <Container>
        <Title>Cart</Title>

        <Group position="right">
          <Link to="/prescription">
            <IconNotes />
          </Link>

          <FaShoppingCart size={28} />
          <Text>{totalItem}</Text>
        </Group>

        {/* item total and price total */}
        <Group position="apart">
          <div>
            <Text>
              You have{" "}
              <Text color="blue" inline={true}>
                {totalItem}
              </Text>
              items in cart!
            </Text>
          </div>

          <div>
            <Text>Cart Total : ₹{totalAmount.toFixed(2)}</Text>
          </div>
        </Group>

        {/* cart actions */}
        <Center>
          <div style={{ borderRadius: 20 }}>
            <Link to={cartSelected.length === 0 ? "#" : "/orders/place"}>
              <Button disabled={cartSelected.length === 0} compact>
                Checkout
              </Button>
            </Link>

            <Link to={cartSelected.length === 0 ? "#" : "/optin/create"}>
              <Button
                compact
                variant="outline"
                disabled={cartSelected.length === 0}
              >
                Create Opt In
              </Button>
            </Link>

            <Button
              compact
              color="red"
              onClick={() => {
                EmptyCartAPI(uid)
                  .then((response) => {
                    dispatch(clearCart());
                    showNotification({
                      color: "green",
                      message: "Successfully emptied the cart",
                    });
                  })
                  .catch((err) => {
                    showNotification({
                      color: "red",
                      message: "oops, something went wrong",
                    });
                    console.log(err);
                  });
              }}
            >
              Empty Cart
            </Button>
          </div>
        </Center>

        <Space h="xl" />

        <Center>
          <div className="cart-items-container">
            {cart.map((curItem) => {
              return (
                <ItemLoadableCard
                  key={curItem.itemId}
                  item={curItem}
                  checkbox={
                    <Checkbox
                      checked={cartSelected.includes(curItem.itemIdFk)}
                      onChange={() => dispatch(toggleSelect(curItem.itemIdFk))}
                    />
                  }
                >
                  <>
                    <Grid.Col span={4}>
                      <UnstyledButton
                        onClick={() =>
                          // TODO add remove from cart link
                          decreamentInCartAPI(uid, {
                            itemIdFk: curItem.itemIdFk,
                            price: curItem.price,
                            itemQuantity: 1,
                            itemId: curItem.itemId,
                          })
                            .then((response) => {
                              dispatch(decrementQuantity(curItem.itemIdFk));
                            })
                            .catch((err) => {
                              showNotification({
                                color: "red",
                                message: "oops, something went wrong",
                              });
                              // console.log("lol");
                            })
                        }
                      >
                        <FaMinus size={9} />
                      </UnstyledButton>

                      <Badge size="md" style={{ margin: 4 }}>
                        {" "}
                        {curItem.itemQuantity}{" "}
                      </Badge>

                      <UnstyledButton
                        onClick={() => {
                          increamentInCartAPI(uid, {
                            itemIdFk: curItem.itemIdFk,
                            price: curItem.price,
                            itemQuantity: 1,
                            itemId: curItem.itemId,
                          })
                            .then((response) => {
                              dispatch(incrementQuantity(curItem.itemIdFk));
                            })
                            .catch((err) => {
                              console.log(err);
                            });
                        }}
                      >
                        <FaPlus size={9} />
                      </UnstyledButton>
                    </Grid.Col>

                    <Grid.Col span={4}>
                      <UnstyledButton
                        onClick={() =>
                          decreamentInCartAPI(uid, {
                            itemIdFk: curItem.itemIdFk,
                            price: curItem.price,
                            itemQuantity: curItem.itemQuantity,
                            itemId: curItem.itemId,
                          })
                            .then((response) => {
                              dispatch(
                                removeItemFromCart({
                                  itemIdFk: curItem.itemIdFk,
                                  price: curItem.price,
                                  itemQuantity: curItem.itemQuantity,
                                  itemId: curItem.itemId,
                                })
                              );
                            })
                            .catch((err) => {
                              console.log(err);
                            })
                        }
                      >
                        <FaTrash size={14} />
                      </UnstyledButton>
                    </Grid.Col>
                  </>
                </ItemLoadableCard>
              );
            })}
          </div>
        </Center>
      </Container>
    </>
  );
}
