import { Box, Button, Group, Modal, NumberInput, Space, useMantineTheme } from '@mantine/core';
import { useForm } from '@mantine/form';
import { useAppDispatch } from '../../../../app/hooks';
import { Item, Store } from '../models';
// import { updateStore } from './UpdateStoreAPI';
import { DatePicker } from '@mantine/dates';
import dayjs from 'dayjs';
import { useEffect } from 'react';
import { updateItem } from './InventoryAPI';

interface ModalStateType {
  open: boolean,
  setOpen: (boolean) => void,
  item: Item,
  store: Store
}

const UpdateItemForm = ({ open, setOpen, item, store }: ModalStateType) => {
  const dispatch = useAppDispatch();
  const theme = useMantineTheme();
  const form = useForm({
    initialValues: {
      itemQuantity: item.itemQuantity,
      price: item.price,
      manufacturedDate: new Date(item.manufacturedDate),
      expiryDate: new Date(item.expiryDate)
    },

    validate: {
      itemQuantity: (value) => ((!(value) || value <= 0) ? 'Atleast enter 1 item' : null),
      price: (value) => ((!(value) || value <= 0) ? 'Minimum Rs. 1' : null),
      expiryDate: (value) => (dayjs(value).diff(new Date(), 'month') < 1 ? 'Item should last atleast 1 month' : null),
    },
  });



  const handleSubmit = (values: any) => {
    console.log("b4 submit", values);
    const data = {
      itemId: item.itemId,
      itemQuantity: values.itemQuantity,
      price: values.price,
      manufacturedDate: dayjs(values.manufacturedDate).add(1, 'days').toDate(),
      expiryDate: dayjs(values.expiryDate).add(1, 'days').toDate(),
      product: item.product,
      store: store
    };
    console.log("after submit", data);

    dispatch(updateItem(store.storeId, data));
    closeModal();
  }
  const closeModal = () => {
    setOpen(false);
    form.setValues({
      itemQuantity: item.itemQuantity,
      price: item.price,
      manufacturedDate: new Date(item.manufacturedDate),
      expiryDate: new Date(item.expiryDate)
    })
    // form.getInputProps()
  }
  useEffect(() => {
    form.setValues({
      itemQuantity: item.itemQuantity,
      price: item.price,
      manufacturedDate: new Date(item.manufacturedDate),
      expiryDate: new Date(item.expiryDate)
    })

    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [item])

  return (

    <>
      <Modal
        opened={open}
        onClose={closeModal}
        title="Update Item Data"
        overlayColor={theme.colorScheme === 'dark' ? theme.colors.dark[9] : theme.colors.gray[2]}
        overlayOpacity={0.55}
        overlayBlur={3}
      >
        <Box >
          <form onSubmit={form.onSubmit(handleSubmit)}>
            <NumberInput
              withAsterisk
              label="Item Quantity"
              placeholder="10"
              {...form.getInputProps('itemQuantity')}
            />
            <Space h="md" />

            <NumberInput
              withAsterisk
              label="Price"
              placeholder="10"
              {...form.getInputProps('price')}
              mt={4}
            />
            <Space h="md" />
            <DatePicker
              withAsterisk
              label="Manufactured Date"
              placeholder="Pick date"
              {...form.getInputProps('manufacturedDate')}
              maxDate={new Date()}
              mt={4}
            />
            <Space h="md" />
            <DatePicker
              withAsterisk
              label="Expiry Date"
              placeholder="Pick date"
              {...form.getInputProps('expiryDate')}
              //   value={new Date(item.expiryDate)}
              mt={4}
            />
            <Space h="md" />
            <Group position="right" mt="md">
              <Button fullWidth type="submit">Update</Button>
            </Group>
          </form>
        </Box>
      </Modal>
    </>
  )
}

export default UpdateItemForm