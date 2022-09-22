import { Text } from "@mantine/core";
import { showNotification } from "@mantine/notifications";
import { AxiosError, AxiosResponse } from "axios";
import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { useAppSelector } from "../../../../app/hooks";
import LoadingComponent from "../../../ui/LoadingComponent/LoadingComponent";
import { getUserData } from "../../Auth/Login/UserSlice";
import CartCard from "../../Cart/CartCard";
import OptInForm from "../OptInCreate/OptInForm";
import { OptInType } from "../OptinList/models";
import { getOptInByIdAPI, optInUpdateAPI } from "../OptinList/OptinAPI";

export default function OptInUpdate() {
  const [OptInData, setOptInData] = useState<OptInType | null>(null)
  const [loading, setLoading] = useState(true)
  // const [cart, setCart] = useState([])

  const user = useAppSelector(getUserData)
  const navigate = useNavigate();

  const { id } = useParams();
  console.log("parma", id);


  const notFond = <Text>Not Found !</Text>

  if (id === undefined) return notFond

  // eslint-disable-next-line react-hooks/rules-of-hooks
  useEffect(() => {
    setLoading(true);
    getOptInByIdAPI(parseInt(id))
      .then((res: AxiosResponse) => {
        setOptInData(res.data)
        // setCart(res.data.repeatOrderItems)
        setLoading(false)
      })
      .catch((err: AxiosError) => {
        showNotification({ color: "red", message: "oops, something went wrong" })
      })

  }, [id])

  // const cart: Cart = { orderItems: [] }
  // console.table(useAppSelector(getOptInList))
  // const originalData = useAppSelector(getOptInList).find(item => item.id === parseInt(id2))
  // console.log(originalData, id2);

  // if (originalData === undefined) return <Text>No Data Found</Text>

  // const cart: Cart = orignalData.cart;


  async function updateOptIn(data: OptInType) {
    const requestData: OptInType = {
      userId: user.id,
      name: data.name,
      deliveryDate: data.deliveryDate,
      numberOfDeliveries: data.numberOfDeliveries,
      intervalInDays: data.intervalInDays,
      address: data.address,
      id: data.id,
      repeatOrderItems: data.repeatOrderItems
    }
    // console.log(requestData);

    // TODO call to optin update api and checage it to sync type
    optInUpdateAPI(requestData)
      .then((response) => {
        navigate("/optin/list")
      })
      .catch((err) => {
        showNotification({ color: "red", message: "oops, something went wrong" })
      })
  }

  if (loading || OptInData === null) return <LoadingComponent />

  return (
    <OptInForm onSubmitCallback={updateOptIn} defaults={OptInData} >
      <CartCard productCart={OptInData.repeatOrderItems} />
    </OptInForm>
  )
}
