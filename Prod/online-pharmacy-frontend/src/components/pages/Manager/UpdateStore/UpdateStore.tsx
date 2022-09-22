import { Box, Button, Group, Modal, TextInput, Title, useMantineTheme } from '@mantine/core';
import { useForm } from '@mantine/form';
import { useEffect } from 'react';
import { useAppDispatch } from '../../../../app/hooks';
import { Store } from '../models';
import { updateStore } from './UpdateStoreAPI';

interface ModalStateType {
  openUpdateStoreForm: boolean,
  setOpenUpdateStoreForm: (boolean) => void,
  store: Store
}

interface FormValues {
  storeName: string,
  street: string,
  city: string,
  state: string,
  country: string,
  pinCode: number
}
const UpdateStore = ({ openUpdateStoreForm, setOpenUpdateStoreForm, store }: ModalStateType) => {
  const dispatch = useAppDispatch();
  const theme = useMantineTheme();
  const form = useForm<FormValues>({
    initialValues: {
      storeName: store.storeName,
      street: store.address.street,
      state: store.address.state,
      city: store.address.city,
      country: store.address.country,
      pinCode: store.address.pinCode
    },

    validate: {
      storeName: (value) => (value.length === 0 ? 'store name cannot be empty' : null),
      street: (value) => (value.length === 0 ? 'street name cannot be empty' : null),
      city: (value) => (value.length === 0 ? 'city name cannot be empty' : null),
      state: (value) => (value.length === 0 ? 'state name cannot be empty' : null),
      country: (value) => (value.length === 0 ? 'country name cannot be empty' : null),
      pinCode: (value) => (value.toString().length <= 5 ? 'invalid pin code' : null),
    },
  });



  const handleSubmit = (values: any) => {
    const data: Store = {
      address: {
        addressId: store.address.addressId,
        city: values.city,
        country: values.country,
        pinCode: values.pinCode,
        state: values.state,
        street: values.street
      },
      createdDate: store.createdDate,
      manager: store.manager,
      revenue: store.revenue,
      storeId: store.storeId,
      storeName: values.storeName
    };
    dispatch(updateStore(data));
    closeModal();
  }
  const closeModal = () => {
    setOpenUpdateStoreForm(false);
    form.setValues({
      storeName: store.storeName,
      street: store.address.street,
      city: store.address.city,
      state: store.address.state,
      country: store.address.country,
      pinCode: store.address.pinCode
    })

  }
  useEffect(() => {
    form.setValues({
      storeName: store.storeName,
      street: store.address.street,
      city: store.address.city,
      state: store.address.state,
      country: store.address.country,
      pinCode: store.address.pinCode
    })

    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [store])

  return (

    <>
      <Modal
        opened={openUpdateStoreForm}
        onClose={closeModal}
        title="Update Store"
        overlayColor={theme.colorScheme === 'dark' ? theme.colors.dark[9] : theme.colors.gray[2]}
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
              withAsterisk
              label="Street"
              placeholder="street"
              {...form.getInputProps('street')}
              mt={4}
            />
            <TextInput
              withAsterisk
              label="City"
              placeholder="city"
              {...form.getInputProps('city')}
              mt={4}
            />
            <TextInput
              withAsterisk
              label="State"
              placeholder="state"
              {...form.getInputProps('state')}
              mt={4}
            />
            <TextInput
              withAsterisk
              label="Country"
              placeholder="country"
              {...form.getInputProps('country')}
              mt={4}
            />
            <TextInput
              withAsterisk
              label="PIN Code"
              placeholder="******"
              {...form.getInputProps('pinCode')}
              mt={4}
            />
            <Group position="right" mt="md">
              <Button fullWidth type="submit">Update</Button>
            </Group>
          </form>
        </Box>
      </Modal>
    </>
  )
}

export default UpdateStore