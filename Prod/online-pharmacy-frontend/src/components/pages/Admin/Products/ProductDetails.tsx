import { Badge, Card, Container, Divider, Grid, Image, Table, Title } from "@mantine/core";
import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { } from "../../Manager/models";
import { CategorySet } from "./models";
import { getProductDetailsAPI } from "./ProductListAPI";

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


const ProductDetails = () => {

  const { id } = useParams();
  const [productDetails, setProductDetails] = useState<Product>();

  useEffect(() => {
    console.log("this product Id from parms" + id);
    getProductDetails();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  const getProductDetails = () => {
    getProductDetailsAPI(id).then((res) => {
      setProductDetails(res.data);
    });
  }



  return (
    <Container>
      <Card shadow="sm" p="lg" radius="md" withBorder>
        <Grid>
          <Grid.Col sm={12} lg={7}>
            <Image
              width={300}
              height={250}
              src={productDetails?.imageUrl}
            >
            </Image>
          </Grid.Col>
          <Grid.Col sm={12} lg={6}>
            <Table horizontalSpacing="xl">
              <tbody>
                <tr>
                  <td><Title order={6}>Product ID</Title></td>
                  <td>{productDetails?.productId}</td>
                </tr>
                <tr>
                  <td><Title order={6}>Product Name</Title></td>
                  <td>{productDetails?.productName}</td>
                </tr>
                <tr>
                  <td><Title order={6}>Product Type</Title></td>
                  <td>{productDetails?.productType ? "Generic" : "Non-Generic"}</td>
                </tr>
                <tr>
                  <td><Title order={6}>Quantity</Title></td>
                  <td>{productDetails?.quantity}</td>
                </tr>
              </tbody>
            </Table>

            <Divider my="sm" />
          </Grid.Col>
          <Grid.Col sm={12} lg={6} >
            <Table horizontalSpacing="xl">
              <tbody>
                <tr>
                  <td><Title order={6}>proprietaryName</Title></td>
                  <td>{productDetails?.proprietaryName}</td>
                </tr>
                <tr>
                  <td><Title order={6}>Dosage Form</Title></td>
                  <td >
                    {productDetails?.dosageForm}
                  </td>
                </tr>
                <tr>
                  <td><Title order={6}>Description</Title></td>
                  <td >
                    {productDetails?.description}
                  </td>
                </tr>
                {
                  <tr>
                    <td><Title order={6}>Category</Title></td>
                    <td >
                      {productDetails?.categorySet.map((category) => {
                        return <Badge key={category.categoryId}>{category.categoryName}</Badge>
                      })}
                    </td>
                  </tr>}
                {/* <tr>
                  <td><Title order={6}>Action</Title></td>
                  <td>
                    <Link to={"/product/update/" + productDetails?.productId}>
                      <Button
                        onClick={() => { }}
                        compact variant='subtle' rightIcon={<IconEdit size={14} />}>
                        Edit
                      </Button>
                    </Link>
                    <Button onClick={() => { }} compact variant='subtle' color="red" rightIcon={<IconTrash size={14} />}>
                      Delete
                    </Button>
                  </td>
                </tr> */}
              </tbody>
            </Table>
            <Divider my="sm" mt={6} />
          </Grid.Col>
        </Grid>
      </Card>


    </Container>
  )
}

export default ProductDetails