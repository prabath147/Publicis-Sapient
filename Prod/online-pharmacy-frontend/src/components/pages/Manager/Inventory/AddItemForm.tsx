import {
  Button,
  Grid,
  Image,
  Menu,
  MultiSelect,
  NumberInput,
  Select,
  Space,
  Text,
  TextInput
} from "@mantine/core";
import { DatePicker } from "@mantine/dates";
import { useForm } from "@mantine/form";
import { showNotification } from "@mantine/notifications";
import { IconSearch, IconX } from "@tabler/icons";
import dayjs from "dayjs";
import { useEffect, useState } from "react";
import { useAppDispatch } from "../../../../app/hooks";
// import { Product } from "../models";
import {
  addItem,
  getItemAPI,
  getProduct,
  getSuggestions
} from "./InventoryAPI";

interface Product {
  productId: number;
  productName: string;
  proprietaryName: string;
  description: string;
  dosageForm: string;
  categorySet: Array<string>;
  productType: boolean;
  imageUrl: string;
}

const AddItemForm = ({ storeId, closeModal, setProductFormOpen }) => {
  const dispatch = useAppDispatch();

  // const [productDataName, setProductDataName] = useState<string>('');
  const [productData, setProductData] = useState<Product | null>(null);
  const [productDataArray, setProductDataArray] = useState<string[]>([]);
  const [loadingBt, setLoadingBt] = useState<boolean>(false);
  const [openList, setOpenList] = useState(false);
  const [categoryArray, setCategoryArray] = useState<string[]>([]);
  const [categories, setCategories] = useState([{
    categoryId: 0,
    categoryName: "",
  }]);

  const form = useForm({
    initialValues: {
      itemQuantity: 0,
      price: 0,
      manufacturedDate: new Date(),
      expiryDate: new Date(),
    },

    validate: {
      itemQuantity: (value) => (value <= 0 ? "Atleast enter 1 item" : null),
      price: (value) => (value <= 0 ? "Minimum Rs. 1" : null),
      expiryDate: (value) =>
        dayjs(value).diff(new Date(), "month") < 1
          ? "Item should last atleast 1 month"
          : null,
    },
  });

  const productSearchForm = useForm({
    initialValues: {
      productId: 0,
      productName: "",
      proprietaryName: "",
      description: "",
      dosageForm: "",
      productType: false,
      imageUrl: "",
    },

    validate: {},
  });
  const handleSubmit = (values: any) => {
    console.log(productData);
    console.log(values);
    const data = {
      itemQuantity: values.itemQuantity,
      price: values.price,
      manufacturedDate: dayjs(values.manufacturedDate).add(1, 'days').toDate(),
      expiryDate: dayjs(values.expiryDate).add(1, 'days').toDate(),
      product: {
        productId: productSearchForm.values.productId,
      },
      store: {
        storeId: storeId,
      },
    };
    console.log(data);

    dispatch(addItem(storeId, data));
    closeModal();

  }
  const clearAddItemForm = () => {
    // setProductDataName('');
    productSearchForm.reset();
    setProductData(null);
    setProductDataArray([]);
    setCategoryArray([]);
    setOpenList(false);
  }
  const handleProductSearch = (values) => {
    console.log("pdt id:", values.productId);
    if (!values.productId) {
      if (values.productName.length > 0) {
        showNotification({
          message: `${values.productName} doesnot exists!`,
          color: "red"
        });
        return;
      }
      showNotification({
        message: `Product ID cannot be empty!!`,
        color: "red"
      });
      return;
    }
    getProduct(values.productId).then(response => {

      setLoadingBt(true);
      console.log(response.data);

      // setProductDataArray(response.data.data);
      setProductSearchValues(response.data);

      setLoadingBt(false);
      // setOpenList(true);
    })
      .catch((error) => {
        showNotification({
          message: `${values.productName} doesnot exists!`,
          color: "red",
        });
      });
  };
  const setProductSearchValues = (product) => {
    productSearchForm.setValues({
      productId: product.productId,
      productName: product.productName,
      proprietaryName: product.proprietaryName,
      description: product.description,
      dosageForm: product.dosageForm,
      productType: product.productType,
      imageUrl: product.imageUrl,
    });
    if (product.categorySet === undefined) {
      getProduct(product.productId).then(response => {
        setCategoryArray(response.data.categorySet.map(cat => cat.categoryName));
        setCategories(response.data.categorySet);
      }).catch(error => {
        console.log("error in fetching categories");

      })
    } else {
      setCategoryArray(product.categorySet.map((cat) => cat.categoryName));
      setCategories(product.categorySet);
    }


  };

  const fetchProductByName = (productName) => {
    getItemAPI(productName, "")
      .then((response) => {
        if (!response.data.content && response.data.content.length === 0) {
          showNotification({
            message: "Product doesnot exists!",
            color: "red",
          });
          return;
        }
        console.log(response.data.content[0]);
        setProductSearchValues(response.data.content[0]); // TODO modify it if required
      })
      .catch((error) => {
        console.log(error);
      });
  };

  useEffect(() => {
    getSuggestions(productSearchForm.values.productName)
      .then((response) => {
        setProductDataArray(response.data);
        setOpenList(true);
      })
      .catch((error) => {
        console.log(error);
      });
  }, [productSearchForm.values.productName]);

  // const handleKeys = (event) => {
  //     if (
  //         (event.keyCode >= 65 && event.keyCode <= 90) ||
  //         (event.keyCode >= 97 && event.keyCode <= 122)
  //     ) {
  //         console.log(event.key);
  //         console.log(productSearchForm.values.productName + event.key);
  //         getSuggestions(productSearchForm.values.productName + event.key).then(response => {
  //             setProductDataArray(response.data);
  //             setOpenList(true);
  //         }).catch(error => {
  //             console.log(error);

  //         })
  //     }

  // }

  return (
    <div>
      <form onSubmit={productSearchForm.onSubmit(handleProductSearch)}>
        <Grid>
          <Grid.Col lg={6}>
            <TextInput
              withAsterisk
              label="Product Name"
              placeholder="name"
              {...productSearchForm.getInputProps("productName")}
              // onKeyDownCapture={(e) => handleKeys(e)}
              // onClick={() => ((productDataArray && productDataArray?.length > 0) && setOpenList(true))}
              // value={productDataName} onChange={(event) => setProductDataName(event.currentTarget.value)}
              rightSection={
                <IconX
                  data-testid="namex"
                  size={10}
                  onClick={() => clearAddItemForm()}
                />
              }
            />

            <Menu
              shadow="md"
              width={280}
              closeOnClickOutside={true}
              opened={openList}
              onChange={setOpenList}
            >
              {(productSearchForm.values.productName.length > 0 &&
                productDataArray?.length) > 0 && (
                  <Menu.Dropdown>
                    {productDataArray?.map((pdtName) => (
                      <Menu.Item
                        data-testid="menuitem"
                        key={pdtName}
                        onClick={() => {
                          fetchProductByName(pdtName);
                          // setProductSearchValues(pdt);
                          setOpenList(false);
                        }}
                      >
                        {pdtName}
                      </Menu.Item>
                    ))}
                  </Menu.Dropdown>
                )}
            </Menu>
          </Grid.Col>
          <Grid.Col lg={3}>
            <NumberInput
              withAsterisk
              label="Product ID"
              placeholder="172800"
              hideControls
              {...productSearchForm.getInputProps("productId")}
              rightSection={
                <IconX
                  data-testid="idx"
                  size={10}
                  onClick={() => clearAddItemForm()}
                />
              }
            />
          </Grid.Col>
          <Grid.Col lg={3}>
            <Button
              type="submit"
              variant="light"
              leftIcon={<IconSearch size={14} />}
              mt={24}
              loading={loadingBt}
            >
              search
            </Button>
          </Grid.Col>
        </Grid>
        <Text pl={1} py={3} size="xs" color="dimmed">
          Register your product if not found
          <Button
            data-testid="register"
            ml={2}
            compact
            size="xs"
            variant="subtle"
            color={"green"}
            onClick={() => setProductFormOpen(true)}
          >
            register
          </Button>
        </Text>
        <Grid>
          <Grid.Col lg={6}>
            <Image
              width={280}
              height={140}
              src={productSearchForm.values.imageUrl}
              alt="image"
              withPlaceholder
            />
          </Grid.Col>
          <Grid.Col lg={6}>
            <Grid>
              <Grid.Col lg={12}>
                <TextInput
                  label="Proprietary Name"
                  {...productSearchForm.getInputProps("proprietaryName")}
                  // value={proprietaryName}
                  disabled
                />
              </Grid.Col>
              <Grid.Col lg={12}>
                <TextInput
                  label="Dosage Form"
                  // value={dosageForm}
                  {...productSearchForm.getInputProps("dosageForm")}
                  disabled
                />
              </Grid.Col>
            </Grid>
          </Grid.Col>
        </Grid>

        <Grid.Col lg={12} px={0}>
          <TextInput
            label="Description"
            // value={description}
            {...productSearchForm.getInputProps('description')}
            disabled
          />
        </Grid.Col>
        <Grid>
          <Grid.Col lg={6}>
            <MultiSelect
              label="Category"
              value={categoryArray} onChange={setCategoryArray}
              data={categories.map(cat => ({ value: cat.categoryName, label: cat.categoryName }))}
              disabled
            />
          </Grid.Col >
          <Grid.Col lg={6}>
            <Select
              label="Product Type"
              // value={productType}
              data={[
                { label: "Generic", value: true },
                { label: "Non-Generic", value: false },
              ]}
              {...productSearchForm.getInputProps('productType')}
              disabled
            />
          </Grid.Col>
        </Grid>
      </form>
      <form onSubmit={form.onSubmit(handleSubmit)}>
        <Grid>
          <Grid.Col lg={6}>
            <DatePicker
              withAsterisk
              label="Manufactured Date"
              placeholder="Pick date"
              {...form.getInputProps('manufacturedDate')}
              maxDate={new Date()}
              mt={4}
            />
          </Grid.Col>
          <Grid.Col lg={6}>
            <DatePicker
              withAsterisk
              label="Expiry Date"
              placeholder="Pick date"
              {...form.getInputProps('expiryDate')}
              mt={4}
            />
          </Grid.Col>
        </Grid>
        <Grid>
          <Grid.Col lg={6}>
            <TextInput
              withAsterisk
              label="Quantity"
              placeholder="10"
              {...form.getInputProps('itemQuantity')}
            />
          </Grid.Col>
          <Grid.Col lg={6}>
            <TextInput
              label="Price"
              withAsterisk
              placeholder="10"
              {...form.getInputProps('price')}
            />
          </Grid.Col>
        </Grid>
        <Space h="lg" />
        <Button fullWidth type="submit" data-testid="addbtn">
          Add Item
        </Button>
      </form>
    </div>
  )
}

export default AddItemForm;