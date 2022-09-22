
export interface CategorySet {
  categoryId: number;
  categoryName: string;
}
export interface Product {
  categorySet: CategorySet[];
  description: string;
  dosageForm: string;
  imageUrl: string;
  productId: number;
  productName: string;
  productType: boolean;
  proprietaryName: string;
  quantity: number;
}

