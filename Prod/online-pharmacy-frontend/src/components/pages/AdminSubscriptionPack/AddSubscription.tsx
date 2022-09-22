
import { Box, Button, Modal, NumberInput, Radio, TextInput, useMantineTheme } from '@mantine/core';
import { DatePicker } from '@mantine/dates';
import { useForm } from '@mantine/form';
import { showNotification } from '@mantine/notifications';
import { SubDetails, SubRequest } from './models';
import { register } from './SubscriptionAPI';

export interface AddSubscriptionFormProps {
  onSubmit?: (addSubscriptionRequest: SubRequest) => void,
  open: boolean,
  setOpen: (boolean) => void,
  setAdd: (boolean) => void,
  onadd: boolean
}

export default function AddSubscription({ onSubmit, open, setOpen, setAdd, onadd }: AddSubscriptionFormProps) {


  const theme = useMantineTheme();

  const form = useForm<SubRequest>({
    validateInputOnChange: true,
    initialValues: {
      name: "",
      description: "",
      stDate: new Date(),
      edDate: new Date(),
      deliveryDiscount: 0,
      oneDayDelivery: "disable",
      cost: 0,
      period: 0
    },

    validate: {
      name: (value) => (value.length < 2 ? 'Name must have at least 2 letters' : null),
      description: (value) => (value.length < 11 ? 'Decription must have at least 11 letters' : null),
      deliveryDiscount: (value) => ((value > 100 || value < 0) ? "delivery discount should be with in 0 to 100" : null),
      oneDayDelivery: (value) => (value.length < 0 ? "value need to be selected" : null),
      edDate: (value, values) => ((value >= new Date(new Date(values.stDate).setDate(values.stDate.getDate() + values.period)))
        ? null : "End Date is not satisifying the period with repect to start date")
    }
  });

  const closeModal = () => {
    setOpen(false);
  }

  // const handleChange = (e) => (
  //   name = e.target
  //   setSubDetails(...subDetails,  )
  // ) 





  const handleForm = (e: { preventDefault: () => void; }) => {

    if (onSubmit) {
      onSubmit(form.values);
      return;
    }

    e.preventDefault();
    const data: SubDetails = {
      subscriptionName: form.values.name,
      description: form.values.description,
      startDate: form.values.stDate,
      endDate: form.values.edDate,
      benefits: {
        one_day_delivery: form.values.oneDayDelivery === "disable" ? false : true,
        delivery_discount: form.values.deliveryDiscount
      },
      subscriptionCost: form.values.cost,
      period: form.values.period
    };
    //  console.log(data);
    //console.log(new Date(form.values.stDate).setDate(form.values.stDate.getDate() + form.values.period));
    register(data).then((res) => {
      console.log(res);
      console.log('Success');
      showNotification({ message: "Submited Succesfully", color: "green" });
      setAdd(true); 
      console.log(onadd);

    }).catch((error) => {
      console.log('Error');
      showNotification({ message: "Something went wrong", color: "red" });
      setAdd(true);
    });
    
    closeModal();
    
  };
  // const handleError = (errors: typeof form.errors) => {
  //     if (errors.name) {
  //       showNotification({ message: 'Please fill name field', color: 'red' });
  //     } else if (errors.email) {
  //       showNotification({ message: 'Please provide a valid email', color: 'red' });
  //     }
  //   };


  return (
    <>

      <Modal
        opened={open}
        onClose={closeModal}
        title="Add Subscription"
        overlayColor={theme.colorScheme === 'dark' ? theme.colors.dark[9] : theme.colors.gray[2]}
        overlayOpacity={0.55}
        overlayBlur={3}
      >
        <Box >

          <form onSubmit={handleForm}>
            <TextInput
              pb={5} label="Name" placeholder='subscription name'
              {...form.getInputProps('name')}
              withAsterisk required></TextInput>
            <TextInput
              pb={5} label="Description" placeholder='information'
              {...form.getInputProps('description')}
              withAsterisk required></TextInput>
            <DatePicker
              pb={5} placeholder="Subscription start date" label="Start Date" minDate={new Date()}
              {...form.getInputProps('stDate')}
              withAsterisk required />
            <NumberInput
              description="in days"
              pb={5}
              hideControls
              defaultValue={0}
              placeholder="Period in days"
              label="Period"
              {...form.getInputProps('period')}
              withAsterisk required
            />
            <DatePicker
              pb={5} placeholder="Subscription end date" label="End Date" minDate={form.values.stDate}
              {...form.getInputProps('edDate')} withAsterisk required />
            <NumberInput

              pb={5}
              hideControls
              defaultValue={0}
              placeholder="Discount on Delivery"
              label="Delivery Discount"
              {...form.getInputProps('deliveryDiscount')}
              withAsterisk required
            />
            <Radio.Group
              pb={5}
              label="One Day Delivery"
              description="One day delivery subcription benifit"
              {...form.getInputProps('oneDayDelivery')}
              withAsterisk required
            >
              <Radio value="enable" label="Enable" />
              <Radio value="disable" label="Disable" checked={form.values.oneDayDelivery === 'disable'} />
            </Radio.Group>
            <NumberInput
              pb={5}
              hideControls
              defaultValue={0}
              placeholder="Cost Of Subscription"
              label="Cost"
              {...form.getInputProps('cost')}
              withAsterisk required
            />

            <Button
              pb={5}
              fullWidth mt="lg" type='submit'
              color="dark">
              Submit
            </Button>
          </form>

        </Box>
      </Modal>
    </>
  );
}