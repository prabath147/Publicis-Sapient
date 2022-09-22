import { Center, Loader } from "@mantine/core"
import { showNotification } from "@mantine/notifications"
import { useEffect, useState } from "react"
import { getProductDetailAPI } from "../Cart/CartAPI"
import { ProductDetailDTO, ProductMiniDTO } from "../Cart/models"
import { DetailCard } from "./DetailCard"


interface CartProductCardProps {
    item: ProductMiniDTO;
    children?: React.ReactNode;
    checkbox?: React.ReactNode;
}
export default function CartProductCard({ item, children, checkbox }: CartProductCardProps) {

    const [itemDetails, setItemDetails] = useState<ProductDetailDTO | null>(null)
    const [loading, setLoading] = useState(true)
    useEffect(() => {
        setLoading(true)

        getProductDetailAPI(item.productIdFk)
            .then(res => {
                setItemDetails(res.data)
                setLoading(false)

            })
            .catch(err => {
                showNotification({ color: "red", message: "oops, something went wrong" })
            })


    }, [item])

    if (itemDetails === null || loading) return (
        <Center style={{ height: "80%" }}>
            <Loader data-testid="Loading" />
        </Center >
    )

    console.log(itemDetails);


    return (
        <DetailCard item={{
            description: itemDetails.description,
            imgUrl: itemDetails.imageUrl,
            name: itemDetails.productName,
            price: item.price,
            proprietaryName: itemDetails.proprietaryName,
            qty: item.quantity
        }} checkbox={checkbox}>
            {children}
        </DetailCard>
    )
}
