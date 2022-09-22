import { Button, Checkbox, Container, Divider, Select, Space, Table, TextInput, Title } from "@mantine/core";
import { DatePicker } from "@mantine/dates";
import { useForm } from "@mantine/form";
import { showNotification } from "@mantine/notifications";
import dayjs from "dayjs";
import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { useAppSelector } from "../../../app/hooks";
import AddressForm, { addressInitialValues, addressValidation } from "../../ui/forms/AddressForm";
import LoadingComponent from "../../ui/LoadingComponent/LoadingComponent";
import { getUserData } from "../Auth/Login/UserSlice";
import CartCard from "../Cart/CartCard";
import { getSelectedCartItems } from "../Cart/CartSlice";
import { ItemMiniDTO } from "../Cart/models";
import { usersubscriptionAPI } from "../Customer/RegisterSubscriptionPage/SubscriptionAPI";
import { DeliveryDelay } from "./Constants";
import { OrderDetail } from "./models";
import { orderCreateAPI, orderSetAPI } from "./OrderAPI";



const getPrice = (items: ItemMiniDTO[]) => {
    let price = 0;
    items.forEach(element => {
        price += element.price * element.itemQuantity
    });
    return price
}

// TODO check if correct
const getQuantity = (items: ItemMiniDTO[]) => {
    let quantity = 0;
    items.forEach(element => {
        quantity += element.itemQuantity
    });
    return quantity
}

export default function OrderCreatePage() {
    const user = useAppSelector(getUserData)
    const navigate = useNavigate();
    const cartSelectedItems = useAppSelector(getSelectedCartItems)
    const [selectedSub, setSelectedSub] = useState<string | null>(null)
    const [subscriptions, setSubscriptions] = useState<any[] | null>(null)
    const [saveData, setSaveData] = useState(false)



    // console.log(subscriptions)

    if (cartSelectedItems.length === 0) {
        showNotification({ color: 'red', message: 'No items selected.' })
        navigate("/cart")
    }

    useEffect(() => {
        usersubscriptionAPI(user.id)
            .then((response) => {
                console.log(response.data)
                const useAbleSubs: any[] = response.data.userSubsSet
                    .filter((subscription) => { return new Date(subscription.subEndDate.toString()) >= new Date() })

                console.log(useAbleSubs)

                setSubscriptions(useAbleSubs.map(sub => sub.subscriptions.benefits))
                console.log("done")
            })
            .catch(error => {
                console.log(error)
                setSubscriptions([]);
                // console.log(error);
                // showNotification({ color: "red", message: "oops, something went wrong," })
            })


    }, [user.id])

    function setSubScriptionBenifits() {

        let item = (subscriptions !== null) ? subscriptions[parseInt(selectedSub || "-1")] : {}
        item = { ...{ one_day_delivery: false, delivery_discount: 0 }, ...item }
        const days = item.one_day_delivery ? 1 : DeliveryDelay
        const discount = item.delivery_discount
        // console.log(discount)
        const delivery_date = dayjs(new Date()).add(days, 'days').toDate()
        form.setValues({
            ...form.values,
            deliveryDate: delivery_date,
            price: getPrice(cartSelectedItems) + 100 - discount,
        })
    }

    // change with subscription selection
    useEffect(() => {

        setSubScriptionBenifits()

        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [selectedSub])

    // change with save data 
    // useEffect(() => {
    //     form.setValues({
    //         ...form.values,
    //         optionalOrderDetails: saveData,
    //     })
    //     // eslint-disable-next-line react-hooks/exhaustive-deps
    // }, [saveData])


    var defaults: OrderDetail = {
        userId: user.id,
        items: cartSelectedItems,
        optionalOrderDetails: false,
        orderDetails: {},
        orderAddress: addressInitialValues(),
        orderDate: dayjs(new Date()).toDate(),
        deliveryDate: dayjs(new Date()).add(3, 'days').toDate(),
        price: getPrice(cartSelectedItems) + 100,
        quantity: getQuantity(cartSelectedItems),
    }

    const form = useForm<OrderDetail>({
        validateInputOnChange: true,
        initialValues: defaults,
        validate: {
            orderAddress: addressValidation(),
        }
    })

    function createOrder(data: OrderDetail) {
        orderSetAPI(user.id, data)
            .then(response => {
                console.log("item set");

                const data = {
                    ...response.data,
                    items: [
                        ...response.data.items.map(item => ({ ...item, itemId: undefined }))
                    ]
                }

                orderCreateAPI(data)
                    .then(response => {
                        showNotification({ color: "green", message: "Order Placed successfully" })
                        navigate("/orders/list")
                    })
                    .catch(error => {
                        showNotification({ color: "red", message: "OOps, Something went wrong" })
                        // log error
                    })
            })
            .catch(error => {
                showNotification({ color: "red", message: "OOps, Something went wrong" })

            })

    }

    if (subscriptions === null) return <LoadingComponent />


    // console.log(subscriptions)
    const selectOption = subscriptions.map((subscription, index) => ({
        value: index.toString(),
        label: "Delivery Discount: " + subscription.delivery_discount + "% OFF"
            + (subscription.one_day_delivery ? ", One Day Delivery" : "")
    }))

    // applyBenifits()

    return (
        <Container>
            <form onSubmit={(e) => {
                e.preventDefault()
                // console.log(form.validate())
                setSubScriptionBenifits()
                // console.log(form.values)

                if (!form.validate().hasErrors) {
                    console.log(form.values)
                    createOrder(form.values)
                }
                // console.log(form.values)
                // form.onSubmit((values) => {
                //     console.log(values)
                //     // createOrder(values)
                // })
            }}>
                <Container>
                    <Title>Place Order</Title>
                    <Divider my={"sm"} />
                    <>
                        <AddressForm form={form} formFieldName="orderAddress" />
                    </>

                    <Space h="md" />

                    <Checkbox
                        checked={saveData}
                        onChange={() => { setSaveData(!saveData) }}
                        label="Save My data for future use orders" />

                    <Select
                        label="Choose your subscriptions"
                        placeholder="Pick one"
                        value={selectedSub}
                        onChange={setSelectedSub}

                        data={selectOption}

                    />

                    <Button type='submit' my={10}>Place Order</Button>

                    <Table>
                        <tbody>
                            <tr>
                                <td>Order Date</td>
                                <td>
                                    <DatePicker
                                        disabled
                                        {...form.getInputProps('orderDate')}
                                    />
                                </td>
                            </tr>
                            <tr>
                                <td>Delivery Date</td>
                                <td>
                                    <DatePicker
                                        disabled
                                        {...form.getInputProps('deliveryDate')}
                                    />
                                </td>
                            </tr>
                            <tr>
                                <td>Total Payable</td>
                                <td>
                                    <TextInput
                                        disabled
                                        {...form.getInputProps('price')}
                                    />
                                </td>
                            </tr>
                        </tbody>
                    </Table>

                    <CartCard itemCart={cartSelectedItems} />
                </Container>
            </form >
        </Container >
    )
}
