import { Center, Loader } from "@mantine/core"
import { showNotification } from "@mantine/notifications"
import { useEffect, useState } from "react"
import { getItemDetailAPI } from "../Cart/CartAPI"
import { ItemDetailDTO, ItemMiniDTO } from "../Cart/models"
import { DetailCard } from "./DetailCard"


export interface CartItemCardProps {
    item: ItemMiniDTO;
    children?: React.ReactNode;
    checkbox?: React.ReactNode;
}
export default function CartItemCard({ item, children, checkbox }: CartItemCardProps) {

    const [itemDetails, setItemDetails] = useState<ItemDetailDTO | null>(null)
    const [loading, setLoading] = useState(true)
    useEffect(() => {
        setLoading(true)

        getItemDetailAPI(item.itemIdFk)
            .then(res => {
                setItemDetails(res.data)
                setLoading(false)
            })
            .catch(err => {
                setLoading(false)
                showNotification({ color: "red", message: "oops, something went wrong" })
            })

    }, [item.itemIdFk])

    if (loading) {
        return (
            <Center style={{ height: "80%" }}>
                <Loader data-testid="Loading" />
            </Center >
        )
    } else if (itemDetails === null) {
        return (
            <></>
        )
    }

    // console.log(itemDetails);


    return (
        <DetailCard item={{
            name: itemDetails.product.productName,
            description: itemDetails.product.description,
            imgUrl: itemDetails.product.imageUrl,
            price: itemDetails.price,
            proprietaryName: itemDetails.product.proprietaryName,
            qty: item.itemQuantity
        }} checkbox={checkbox}>
            {children}
        </DetailCard>
    )
}
