import dayjs from 'dayjs';
import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAppSelector } from '../../../../app/hooks';
import { addressInitialValues } from '../../../ui/forms/AddressForm';
import LoadingComponent from '../../../ui/LoadingComponent/LoadingComponent';
import { getUserData } from '../../Auth/Login/UserSlice';
import CartCard from '../../Cart/CartCard';
import { getSelectedCartItems } from '../../Cart/CartSlice';
import { ProductMiniDTO } from '../../Cart/models';
// import { convertCartItemToOrderItem } from '../../OrderCreatePage/OrderCreatePage';
import { OptInType } from '../OptinList/models';
import { optInCreateAPI } from '../OptinList/OptinAPI';
import OptInForm from './OptInForm';
import { ItemMiniDTOToProductMiniDTO } from './OptInUtils';

export default function OptInCreate() {
    const user = useAppSelector(getUserData)
    const cart = useAppSelector(getSelectedCartItems)
    const [loading, setLoading] = useState(true)
    const navigate = useNavigate();
    const [defaults, setDefaults] = useState<OptInType | null>(null)

    async function getProperDTOs() {
        let items: ProductMiniDTO[] = []

        for (let element of cart) {
            const item_data = await ItemMiniDTOToProductMiniDTO(element)
            console.log(item_data);
            items.push(item_data)
        }

        setDefaults({
            intervalInDays: 7,
            name: "",
            deliveryDate: dayjs(new Date()).add(3, 'days').toDate(),
            numberOfDeliveries: 3,
            address: addressInitialValues(),
            id: -1,
            repeatOrderItems: items,
            userId: user.id
        })

        setLoading(false)
        console.log(items, cart);

    }

    useEffect(() => {
        getProperDTOs()
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [cart])



    function createOptIn(data: OptInType) {
        optInCreateAPI(data)
            .then(response => {
                // add data to list
                navigate("/optin/list")
            })
            .catch(error => {
                // log error
            })
    }



    // const defaults: OptInType = {
    //     intervalInDays: 7,
    //     name: "",
    //     deliveryDate: dayjs(new Date()).add(3, 'days').toDate(),
    //     numberOfDeliveries: 3,
    //     address: { ...addressInitialValues(), addressId: -1 },
    //     id: -1,
    //     repeatOrderItems: cart.map(item => await ItemMiniDTOToProductMiniDTO(item)),
    //     userId: user.id
    // }

    if (loading || defaults === null) return <LoadingComponent />

    console.log(defaults);

    return (
        <OptInForm onSubmitCallback={createOptIn} defaults={defaults} >
            <CartCard productCart={defaults.repeatOrderItems} />
        </OptInForm>
    )
}
