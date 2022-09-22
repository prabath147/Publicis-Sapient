import { Space, Text, TextInput } from "@mantine/core";
import { UseFormReturnType } from "@mantine/form";

interface addressFormPropType {
    form: UseFormReturnType<any>,
    formFieldName?: string
}
export default function AddressForm({ form, formFieldName = "address" }: addressFormPropType) {
    return (
        <div>
            <Space />
            <Text >Address</Text>
            <TextInput label="Street" placeholder='Street' withAsterisk {...form.getInputProps(formFieldName + '.street')} />
            <TextInput label="City" placeholder='City' withAsterisk {...form.getInputProps(formFieldName + '.city')} />
            <TextInput label="State" placeholder='State' withAsterisk {...form.getInputProps(formFieldName + '.state')} />
            <TextInput label="Country" placeholder='Country' withAsterisk {...form.getInputProps(formFieldName + '.country')} />
            <TextInput type="number" label="Pincode" placeholder="PinCode" withAsterisk {...form.getInputProps(formFieldName + '.pinCode')} />
        </div>
    )
}

export const addressValidation = (): any => {
    return {
        city: value => value.length < 2 ? "This field is required!" : null,
        country: value => value.length < 2 ? "This field is required!" : null,
        pinCode: value => Math.floor(value) < 99999 ? "Valid Pincode is required!" : null,
        state: value => value.length < 2 ? "This field is required!" : null,
        street: value => value.length < 2 ? "This field is required!" : null,
    }
}

export const addressInitialValues = (): AddressInterface => {
    return {
        city: '',
        country: '',
        pinCode: '',
        state: '',
        street: ''
    }
}

export interface AddressInterface {
    addressId?: number;
    city: string,
    country: string,
    pinCode: string,
    state: string,
    street: string,
}

