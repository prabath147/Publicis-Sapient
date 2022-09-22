import { Card, Group, Text } from '@mantine/core';
import CartItemCard from '../item/CartItemCard';
import CartProductCard from '../item/CartProductCard';
import { ItemMiniDTO, ProductMiniDTO } from './models';

interface CartCardType {
    itemCart?: ItemMiniDTO[];
    productCart?: ProductMiniDTO[];
}
export default function CartCard({ itemCart, productCart }: CartCardType) {
    let cart: any[] = []

    if (itemCart !== undefined) {
        cart = itemCart
    } else if (productCart !== undefined) {
        cart = productCart
    }
    // console.log(cart)
    if (cart.length === 0) return <Text>No data Here!</Text>


    // calculate total price
    let temp_total = 0;
    for (const item of cart) {
        temp_total += item.price * getQantity(item)
    }
    const total = Math.round(temp_total * 100) / 100

    // to calculate quantity
    function getQantity(item: any) {
        if (item.itemQuantity !== undefined) {
            return item.itemQuantity
        } else {
            return item.quantity
        }
    }

    return (
        <>
            <Card>
                <Group position="apart" >
                    <Text>
                        No of Items: {cart.length}
                    </Text>

                    <Text>
                        Total Item Cost : ₹{total}
                    </Text>
                </Group>
                <br />
                <>

                    {
                        cart.map((item) => {

                            if (item.itemQuantity === undefined)
                                return <CartProductCard key={item.itemIdFk} item={item} />
                            else
                                return <CartItemCard key={item.productIdFk} item={item} />
                        })
                    }
                </>
            </Card>
        </ >
    )
}
