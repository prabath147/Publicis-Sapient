
import { ProductDetailDTO } from '../Cart/models';
import { DetailCard, DetaildataType } from './DetailCard';


interface ProductCardType {
  item: ProductDetailDTO;
  children?: React.ReactNode;
  checkbox?: React.ReactNode;
}
export default function ProductCard({ item, children, checkbox }: ProductCardType) {
  const data: DetaildataType = {
    name: item.productName,
    description: item.description,
    imgUrl: item.imageUrl,
    price: item.price,
    proprietaryName: item.proprietaryName,
    qty: item.quantity
  }
  // console.log(data);

  return (
    <DetailCard item={data} checkbox={checkbox}>
      {children}
    </DetailCard>
  )
}
