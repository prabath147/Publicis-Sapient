import { Button, Container, Divider, NumberInput, TextInput, Title } from '@mantine/core';
import { DatePicker } from '@mantine/dates';
import { useForm } from '@mantine/form';
import AddressForm, { addressValidation } from '../../../ui/forms/AddressForm';
// import { Controller, useForm } from 'react-hook-form';
import dayjs from 'dayjs';
import { ReactNode } from 'react';
import { OptInType } from '../OptinList/models';




interface OptInDefaultType {
    defaults: OptInType,
    onSubmitCallback: (data: OptInType) => void,
    children: ReactNode
}
export default function OptInForm({ defaults, onSubmitCallback, children }: OptInDefaultType) {
    // console.log(defaults);

    const form = useForm<OptInType>({
        validateInputOnChange: true,

        initialValues: {
            ...defaults,
            deliveryDate: dayjs(new Date(defaults.deliveryDate.toString())).toDate(),
        },

        validate: {
            name: value => value.length < 2 ? 'Name must have at least 2 letters' : null,
            intervalInDays: value => value < 2 ? 'the intervals must be greater than 1 day' : null,
            numberOfDeliveries: value => value < 2 ? 'there must be at least 2 deliveries' : null,
            deliveryDate: value => value === null ? 'Delivery date is required' : null,

            address: addressValidation(),
        }
    })

    return (<Container>
        <form onSubmit={form.onSubmit((values) => {
            // let values2 = values
            // if (typeof values.deliveryDate !== "string")
            //     values = { ...values, deliveryDate: values.deliveryDate.toISOString().split('T')[0] }

            // console.log(values2);

            onSubmitCallback(values)
            // console.log(values);
        })}>
            <Container>
                <Title>Opt In Form</Title>
                <Divider my={"sm"} />
                <>
                    <TextInput
                        mt={"md"}
                        label="Name"
                        withAsterisk
                        placeholder="Something to remember, like fever"
                        {...form.getInputProps('name')} />

                    <NumberInput
                        mt={"md"}
                        label="Interval in days"
                        withAsterisk
                        {...form.getInputProps('intervalInDays')} />

                    <NumberInput
                        mt={"md"}
                        label="Number of Deliveries"
                        withAsterisk
                        {...form.getInputProps('numberOfDeliveries')} />

                    <DatePicker
                        placeholder="Pick date"
                        label="Delivery Date"
                        withAsterisk
                        minDate={dayjs(new Date()).add(3, 'days').toDate()}
                        {...form.getInputProps("deliveryDate")} />

                    <AddressForm form={form} />
                </>

                <Button type='submit' my={10}>Place Order</Button>

                {children}
            </Container>



        </form >
    </Container >)
}