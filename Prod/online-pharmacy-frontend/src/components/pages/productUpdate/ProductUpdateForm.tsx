import {
  Button,
  Card,
  Center,
  Container,
  Grid,
  Image,
  Select,
  Textarea,
  TextInput,
  Title
} from "@mantine/core";
import { useForm } from "@mantine/form";
import { showNotification } from "@mantine/notifications";
import { AxiosResponse } from "axios";
import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { getCategories, getProduct, updateProduct } from "./ProductAPI";

interface UpdateProductForm {
  categoryName: string;
  description: string;
  dosageForm: string;
  imageUrl: string;
  productId: number;
  productName: string;
  productType: boolean;
  proprietaryName: string;
}

export default function ProductUpdateForm() {
  const { id } = useParams();
  const productId = parseInt(id || "0");

  const [categories, setCategories] = useState([
    {
      categoryId: 0,
      categoryName: "",
    },
  ]);

  useEffect(() => {
    loadData();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, []);

  const loadData = async () => {
    const productResponse: AxiosResponse = await getProduct(productId);
    const _product = productResponse.data;
    form.setValues({
      categoryName: _product.categories,
      description: _product.description,
      dosageForm: _product.dosageForm,
      imageUrl: _product.imageUrl,
      productId: _product.productId,
      productName: _product.productName,
      productType: _product.productType,
      proprietaryName: _product.proprietaryName,
    });

    const categoryResponse: AxiosResponse = await getCategories();
    setCategories(categoryResponse.data?.data);
  };

  const form = useForm<UpdateProductForm>({
    validateInputOnChange: true,
    initialValues: {
      categoryName: "",
      description: "",
      dosageForm: "",
      imageUrl: "",
      productId: 0,
      productName: "",
      productType: true,
      proprietaryName: "",
    },
    validate: {
      productName: (value) =>
        value.length < 1 ? "Product name cannot be empty" : null,
      proprietaryName: (value) =>
        value.length < 1 ? "Proprietary name cannot be empty" : null,
      description: (value) =>
        value.length < 1 ? "Product description cannot be empty" : null,
      imageUrl: (value) =>
        value.length < 1 ? "Product image URL cannot be empty" : null,
    },
  });

  const formSubmit = async (values) => {
    console.log(values);
    try {
      await updateProduct(values);

      showNotification({
        message: "Product updated Successfully!",
        color: "green",
      });
    } catch (error) {
      showNotification({
        message: "Oops, Something went Wrong!",
        color: "red",
      });
    }
  };

  return (
    <div>
      <Container>
        <Title order={1}>Update Product Details</Title>

        <Card withBorder p={20} mt={20}>
          <form onSubmit={form.onSubmit(formSubmit)}>
            <Grid align={"center"} gutter={"lg"}>
              <Grid.Col xs={4}>
                <Center>
                  <Image height={200} width={200} src={form.values.imageUrl} />
                </Center>
              </Grid.Col>

              <Grid.Col xs={8}>
                <TextInput
                  mt={"md"}
                  label="Product Name"
                  placeholder="Crocin"
                  withAsterisk
                  required
                  {...form.getInputProps("productName")}
                />

                <TextInput
                  mt={"md"}
                  label="Proprietary Name"
                  placeholder="Paracetamol"
                  withAsterisk
                  required
                  {...form.getInputProps("proprietaryName")}
                />
              </Grid.Col>

              <Grid.Col>
                <Textarea
                  placeholder="Product Description"
                  label="Product Description"
                  withAsterisk
                  required
                  {...form.getInputProps("description")}
                />

                <TextInput
                  mt={"md"}
                  label="Dosage Form"
                  placeholder="Liquid"
                  withAsterisk
                  required
                  {...form.getInputProps("dosageForm")}
                />

                <TextInput
                  mt={"md"}
                  label="Image URL"
                  placeholder="Link for the product image"
                  withAsterisk
                  required
                  {...form.getInputProps("imageUrl")}
                />
              </Grid.Col>

              <Grid.Col xs={6}>
                {/* TODO: Convert this to MultiSelect */}
                <Select
                  data={categories.map((category) => category.categoryName)}
                  label="Categories"
                  placeholder="Pick Categories for the product"
                  {...form.getInputProps("categoryName")}
                />
              </Grid.Col>

              <Grid.Col xs={6}>
                <Select
                  data={[
                    { label: "Generic", value: true },
                    { label: "Non-Generic", value: false },
                  ]}
                  label="Product Type"
                  {...form.getInputProps("productType")}
                />
              </Grid.Col>

              <Grid.Col>
                <Button type="submit" fullWidth>
                  Update Product
                </Button>
              </Grid.Col>
            </Grid>
          </form>
        </Card>
      </Container>
    </div>
  );
}
