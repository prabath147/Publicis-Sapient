import { AxiosResponse } from "axios"
import { getItemDetailAPI } from "../../Cart/CartAPI"
import { ItemDetailDTO, ItemMiniDTO } from "../../Cart/models"

export const ItemMiniDTOToProductMiniDTO = async (item_min: ItemMiniDTO) => {
    const response: AxiosResponse<ItemDetailDTO> = await getItemDetailAPI(item_min.itemIdFk)
    return {
        price: response.data.price,
        productIdFk: response.data.product.productId,
        quantity: item_min.itemQuantity
    }
}