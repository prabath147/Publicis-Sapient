import { Button, Grid, Image, Modal, MultiSelect, Select, Space, TextInput } from "@mantine/core";
import { useForm } from "@mantine/form";
import { showNotification } from "@mantine/notifications";
import { IconX } from "@tabler/icons";
import { useState } from "react";
import { registerProduct } from "./ProductListAPI";

interface modalKey {
    opened: boolean,
    setOpened: (boolean) => void
}

interface CreateProductForm {
    productId: number;
    description: string;
    dosageForm: string;
    imageUrl: string;
    productName: string;
    productType: boolean;
    proprietaryName: string;
}

const AddProduct = ({ opened, setOpened }: modalKey) => {

    const [categories] = useState([
        {
            categoryId: 0,
            categoryName: "",
        }
    ]);
    const [categoryList, setCategoryList] = useState<string[]>([]);
    const closeModal = () => {
        setOpened(false);
        //form.reset();
    }

    const form = useForm<CreateProductForm>({
        validateInputOnChange: true,
        initialValues: {
            productId: 0,
            description: "",
            dosageForm: "",
            imageUrl: "",
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
            dosageForm: (value) =>
                value.length < 1 ? "Dosage Form cannot be empty" : null,
            imageUrl: (value) =>
                value.length < 1 ? "Product image URL cannot be empty" : null,
        },
    });

    const formSubmit = async (values) => {
        console.log("called")
        const product = {
            ...values,
            categorySet: categoryList.map(cat => (categories.find(c => c.categoryName === cat)))
        }
        console.log(product);
        try {
            const { data } = await registerProduct(product);
            console.log(data);

            showNotification({
                message: "Product registered successfully!",
                color: "green",
            });
            closeModal();
        } catch (error) {
            showNotification({
                message: "Oops, Something went Wrong!",
                color: "red"
            })
        }
    };



    return (
        <Modal
            opened={opened}
            onClose={closeModal}
            title="Add Product"
            size="lg"
        >
            <form>
                <Grid>
                    <Grid.Col lg={5}>
                        <TextInput
                            withAsterisk
                            label="Product Name"
                            placeholder="Crocin"
                            {...form.getInputProps('productName')}
                        />
                    </Grid.Col>
                    {/* <Grid.Col lg={4}>
                        <NumberInput
                            withAsterisk
                            label="Product ID"
                            placeholder="172800"
                            hideControls
                        />
                    </Grid.Col> */}
                    <Grid.Col lg={3}>
                        <Button variant='light' color={"red"} rightIcon={<IconX size={14} />} mt={24} onClick={() => closeModal()} >Cancel</Button>
                    </Grid.Col>
                </Grid>
                <Grid>
                    <Grid.Col lg={6}>
                        <Image
                            width={260}
                            height={140}
                            src={form.values.imageUrl}
                            alt="image"
                            withPlaceholder
                        />
                    </Grid.Col>
                    <Grid.Col lg={6}>
                        <Grid>
                            <Grid.Col lg={12}>
                                <TextInput
                                    label="Proprietary Name"
                                    placeholder="Paracetamol"
                                    withAsterisk
                                    {...form.getInputProps('proprietaryName')}
                                />
                            </Grid.Col>
                            <Grid.Col lg={12}>
                                <TextInput
                                    label="Dosage Form"
                                    placeholder="solid"
                                    withAsterisk
                                    {...form.getInputProps('dosageForm')}
                                />
                            </Grid.Col>
                        </Grid>
                    </Grid.Col>
                    <Grid.Col lg={12} px={0}>
                        <TextInput
                            label="Description"
                            placeholder="describe your product"
                            withAsterisk
                            {...form.getInputProps('description')}

                        />
                    </Grid.Col>
                    <Grid.Col lg={12} px={0}>
                        <TextInput
                            label="Image URL"
                            placeholder="link for the product image"
                            withAsterisk
                            {...form.getInputProps("imageUrl")}
                        />
                    </Grid.Col>
                    <Grid>
                        <Grid.Col lg={6}>
                            <MultiSelect
                                label="Catagory"
                                data={categories.map(cat => ({ value: cat.categoryName, label: cat.categoryName }))}
                                value={categoryList} onChange={setCategoryList}
                            />
                        </Grid.Col >
                        <Grid.Col lg={6}>
                            <Select
                                data={[
                                    { label: "Generic", value: "Generic" },
                                    { label: "Non-Generic", value: "Non-Generic" },
                                ]}
                                label="Product Type"
                                {...form.getInputProps("productType")}
                            />
                        </Grid.Col>
                    </Grid>
                </Grid>
                <Space h="lg" />
                <Button fullWidth
                    onClick={() => {
                        console.log(form.validate())
                        formSubmit(form.values)
                        // form.onSubmit(values => {
                        //     console.log("ok1")
                        //     formSubmit(values)
                        // })
                    }}>
                    Add Product
                </Button>
            </form>
        </Modal>
    )
}

export default AddProduct