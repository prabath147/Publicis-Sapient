import { Box, Button, Group, Modal, TextInput, Title, useMantineTheme } from '@mantine/core';
import { useForm } from '@mantine/form';
import { useAppDispatch, useAppSelector } from '../../../../app/hooks';
import { getUserData } from '../../Auth/Login/UserSlice';
import { createNewStore } from '../ManagerStoreList/ManagerStoreListAPI';

interface ModalStateType {
  open: boolean,
  setOpen: (boolean) => void
}

export default function CreateStore({ open, setOpen }: ModalStateType) {
  // const user = LocalUserService.getLocalUser();
  const user = useAppSelector(getUserData);
  const dispatch = useAppDispatch();
  const theme = useMantineTheme();
  const form = useForm({
    initialValues: {
      storeName: '',
      street: '',
      city: '',
      state: '',
      country: '',
      pinCode: 0
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
    console.log(values);
    const data = {
      address: {
        city: values.city,
        country: values.country,
        pinCode: values.pinCode,
        state: values.state,
        street: values.street
      },
      createdDate: (new Date()).toISOString().slice(0, 10),
      manager: {
        managerId: user.id
      },
      revenue: 0,
      storeName: values.storeName
    };
    dispatch(createNewStore(data));
    closeModal();
  }
  const closeModal = () => {
    setOpen(false);
    form.reset();
  }
  return (
    <Modal
      opened={open}
      onClose={closeModal}
      title="Create Store"
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
            <Button fullWidth type="submit">Create</Button>
          </Group>
        </form>
      </Box>
    </Modal>
  );
}
