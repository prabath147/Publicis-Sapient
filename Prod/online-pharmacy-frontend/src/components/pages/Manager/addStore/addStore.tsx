import {
    Button,
    Center, Divider,
    FileInput, Group, TextInput
} from '@mantine/core';
import { useForm } from '@mantine/form';
import { useState } from 'react';

import { Link } from 'react-router-dom';
import { useAppSelector } from '../../../../app/hooks';
import { getUserData } from '../../Auth/Login/UserSlice';
import { AddStoreRequest } from './models';

export interface AddStoreFormProps {
    onSubmit?: (addStoreRequest: AddStoreRequest) => void;
}


export default function AddStore({ onSubmit }: AddStoreFormProps) {

    const user = useAppSelector(getUserData);

    const InputStyle = {
        width: '25vw',
        borderColor: '#5F97FF',
        height: '5vh',
        margin: '0.5rem',
        borderRadius: '5px'
    }

    const StreetStyle = {
        width: '60vw',
        borderColor: '#5F97FF',
        height: '5vh',
        margin: '0.5rem',
        borderRadius: '5px'
    }

    const form = useForm<AddStoreRequest>({
        validateInputOnChange: true,
        initialValues: {
            city: '',
            country: '',
            pincode: '',
            state: '',
            street: '',
            storeName: ''
        },

        validate: (values) => {

            // city:(value)=>value.trim().length<2? "This field is required!":null,
            // country:(value)=>value.trim().length <2? "This field is required!":null,
            // pincode:(value)=>value.toString().trim().length <2? "This field is required!":null,
            // state:(value)=>value.trim().length <2? "This field is required!":null,
            // street:(value)=>value.trim().length <2? "This field is required!":null,
            // storeName:(value)=>value.trim().length <2? "This field is required!":null,



            return {
                city: values.city.trim().length < 2 ? "This field is required!" : null,
                country: values.country.trim().length < 2 ? "This field is required!" : null,
                pincode: isNaN(Number(values.pincode.trim())) ? "We only accept numbers" : values.pincode.trim().length !== 6 ? "This field is required!" : null,
                state: values.state.trim().length < 2 ? "This field is required!" : null,
                street: values.street.trim().length < 2 ? "This field is required!" : null,
                storeName: values.storeName.trim().length < 2 ? "This field is required!" : null,
            };
        },
    });

    const handleForm = (e: { preventDefault: () => void; }) => {

        if (onSubmit) {
            onSubmit(form.values);
        }
        e.preventDefault();
        if (!form.validate().hasErrors) {
            const data = {

                address: {
                    addressId: 0,
                    city: form.values.city,
                    country: form.values.country,
                    pinCode: Number(form.values.pincode),
                    state: form.values.state,
                    street: form.values.street
                },
                createdDate: new Date(),
                manager: {
                    firstName: "string",
                    lastName: "string",
                    licenseNo: "string",
                    managerId: user.id,
                    phoneNo: "string",
                    registrationDate: "2022-08-18T07:22:15.343Z",
                    status: "APPROVED"
                },
                storeId: 0,
                storeName: form.values.storeName

            }


            console.log(data);
            // console.log(inventory);
        }
    };

    const [inventory, setInventory] = useState<File | null>(null);

    return (
        <div style={{ display: "flex", width: "100%", height: "100%", flexDirection: "row" }}>

            <div style={{ backgroundColor: "#EFF7FF", width: "100%", padding: "0.5rem" }}>
                <Link to="/"><h4><Button>{"<--"}Back</Button></h4></Link>

                <div><h2>Add Store</h2></div>

                <form onSubmit={handleForm}>
                    <div>
                        <div>
                            <h3 style={{ textDecoration: "underline" }}>Store Details</h3>
                            <TextInput label="Store Name" style={InputStyle} placeholder='Enter Store Name' {...form.getInputProps('storeName')} />
                        </div>

                        <h4 style={{ marginTop: '4rem' }}>Address</h4>
                        <div style={{ display: "flex", width: "90vw" }}>

                            <TextInput label="Street" style={StreetStyle} placeholder='Street' {...form.getInputProps('street')} />
                            <TextInput label="City" style={InputStyle} placeholder='City' {...form.getInputProps('city')} />
                            <TextInput label="State" style={InputStyle} placeholder='State' {...form.getInputProps('state')} />
                            <TextInput label="Country" style={InputStyle} placeholder='Country' {...form.getInputProps('country')} />
                            <TextInput label="Pincode" style={InputStyle} placeholder="PinCode" {...form.getInputProps('pincode')} />
                        </div>
                    </div>
                    <Divider mt={50}></Divider>
                    <div>
                        <h3 style={{ textDecoration: "underline" }}>Inventory Details</h3>

                        <Group grow position="center">
                            <div style={{ width: "30%", marginLeft: "5%" }}>
                                <Link to="/MedicineTemplate.xlsx" target="_blank" download><Button>
                                    <span>Download Template</span>
                                </Button> </Link>
                                <p style={{ fontSize: "1.2vw" }}>Fill Inventory Details according to the Excel Template and upload the same template to update the stores inventory.</p>
                            </div>
                            <div style={{ width: "30%", marginRight: "5%" }}>
                                <FileInput
                                    placeholder="Insert Inventory Sheet"
                                    label="Inventory"
                                    size="lg"
                                    description="Insert only .xlsx" accept="application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
                                    value={inventory} onChange={setInventory}
                                />
                            </div>
                        </Group>
                    </div>
                    <Divider mt={50}></Divider>
                    <Center mt="md"><Button type="submit">Submit</Button></Center>

                </form>


            </div>
        </div>
    );

}