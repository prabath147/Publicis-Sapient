import { Box, Button, Group, Modal, Text, TextInput, Title } from '@mantine/core';
import { useForm } from '@mantine/form';
import { showNotification } from '@mantine/notifications';
import { useAppDispatch, useAppSelector } from '../../../../app/hooks';
import { Store } from '../storeList/models';
import { updateStore } from '../storeList/StoreAPI';
import { selectStores, updateStoreData } from '../storeList/StoreSlice';


interface ModalStateType {
    open: boolean,
    setOpen: (boolean) => void,
    id: number
}

interface FormValues {
    storeName: string,
    street: string,
    state: string,
    country: string,
    pinCode: number,
    city: string
}
const UpdateStore = ({ open, setOpen, id }: ModalStateType) => {
    const dispatch = useAppDispatch();

    const storeData = useAppSelector(selectStores).find(item => item.storeId === id)
    if (storeData === undefined) return <Text>No Data Found</Text>


    // eslint-disable-next-line react-hooks/rules-of-hooks
    const form = useForm<FormValues>({
        initialValues: {
            storeName: storeData.storeName,
            street: storeData.address.street,
            state: storeData.address.state,
            country: storeData.address.country,
            pinCode: storeData.address.pinCode,
            city: storeData.address.city
        },

        validate: {
            storeName: (value) => (value.length === 0 ? 'Store name cannot be empty' : null),
            street: (value) => (value.length === 0 ? 'Street name cannot be empty' : null),
            state: (value) => (value.length === 0 ? 'State name cannot be empty' : null),
            country: (value) => (value.length === 0 ? 'Country name cannot be empty' : null),
            pinCode: (value) => (value.toString().length !== 6 ? 'PinCode must be of 6 digits' : null),
            city: (value) => (value.length === 0 ? 'City name cannot be empty' : null),
        },
    });

    // useEffect(() => {
    //   form.setValues({
    //     storeName: storeData.storeName,
    //             street: storeData.address.street,
    //             state: storeData.address.state,
    //             country: storeData.address.country,
    //             pinCode: storeData.address.pinCode.toString(),
    //             city: storeData.address.city
    //   })
    // }, [storeData])


    const handleSubmit = (values: FormValues) => {
        console.log(values);
        const data: Store = {
            address: {
                addressId: storeData.address.addressId,
                city: values.city,
                country: values.country,
                pinCode: values.pinCode,
                state: values.state,
                street: values.street
            },
            storeId: storeData.storeId,
            storeName: values.storeName,
            createdDate: storeData.createdDate,
            manager: storeData.manager,
            revenue: storeData.revenue
        };
        updateStore(data).then((response) => {
            console.log(data);
            console.log('Success');
            dispatch(updateStoreData(response.data));
            showNotification({ message: "Store Updated successfully!!", color: "green" })
            // navigate('/store-details/'+storeData.storeId);
        }).catch((error) => {
            console.log('Error' + error.message);
        });
        closeModal();
    }
    const closeModal = () => {
        setOpen(false);
        form.resetDirty();

    }
    return (

        <>
            <Modal
                opened={open}
                onClose={closeModal}
                title="Update Store"
                // overlayColor={theme.colorScheme === 'dark' ? theme.colors.dark[9] : theme.colors.gray[2]}
                overlayOpacity={0.55}
                overlayBlur={3}
            >
                <Box >
                    <form onSubmit={form.onSubmit(handleSubmit)}>
                        <TextInput
                            withAsterisk
                            label="Store Name"
                            placeholder="name"
                            {...form.getInputProps('storeName')}
                        />
                        <Title my={15} order={6}>Store Address</Title>
                        <TextInput
                            label="Street"
                            placeholder="street"
                            {...form.getInputProps('street')}
                            mt={4}
                        />
                        <TextInput
                            label="City"
                            placeholder="city"
                            {...form.getInputProps('city')}
                            mt={4}
                        />
                        <TextInput
                            label="State"
                            placeholder="state"
                            {...form.getInputProps('state')}
                            mt={4}
                        />
                        <TextInput
                            label="Country"
                            placeholder="country"
                            {...form.getInputProps('country')}
                            mt={4}
                        />
                        <TextInput
                            label="PIN Code"
                            placeholder="******"
                            {...form.getInputProps('pinCode')}
                            mt={4}
                        />
                        <Group position="right" mt="md">
                            <Button data-testid="Upad" fullWidth type="submit">Update</Button>
                        </Group>
                    </form>
                </Box>
            </Modal>
        </>
    )
}

export default UpdateStore;