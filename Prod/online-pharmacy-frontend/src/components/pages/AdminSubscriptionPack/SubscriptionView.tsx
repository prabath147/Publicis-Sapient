import { Badge, Button, Center, Container, Loader, Pagination, Switch, Table } from "@mantine/core";
import { Dispatch } from "@reduxjs/toolkit";
import { useEffect, useState } from "react";
import { useAppDispatch, useAppSelector } from "../../../app/hooks";
import AddSubscription from "./AddSubscription";
import { Subscriptionv } from "./models";
import { ExpSub, getSubscriptionvAPI, sDelete } from "./SubscriptionAPI";
import * as Const from './SubscriptionvConst';
import { loadData, selectSubscriptionv } from "./SubscriptionvSlice";
export default function SubscriptionView() {

  const SubscripList = useAppSelector(selectSubscriptionv);
  const dispatch = useAppDispatch();
  const [open, setOpen] = useState(false);
  const [check, setCheck] = useState(false);
  const [loading_1, setLoading_1] = useState(true);


  const [activePage, setPage] = useState(1);
  const [pageCount, setPageCount] = useState(1);

  const limit = 5;
  // const [itemsPerPage] = useState(5);
  const [onadd, setAdd] = useState(false);

  // const indexOfLastItem = activePage * itemsPerPage;
  // const indexOfFirstItem = indexOfLastItem - itemsPerPage;
  // const currItems = SubscripList.slice(indexOfFirstItem, indexOfLastItem);
  // const totalPages = Math.ceil(SubscripList.length / itemsPerPage);

  useEffect(() => {
    // console.log(check);
    setLoading_1(true);
    dispatch(loadDataAction());
    
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [activePage, check, open]
  );

  

  const loadDataAction = () => async (dispatch: Dispatch) => {
    try {
      let payload: any;
      if (check) {
        payload = await ExpSub(activePage - 1, limit);
      }
      else {
        payload = await getSubscriptionvAPI(activePage - 1, limit);
      }

      const subscriptionv: Subscriptionv[] = [];
      console.log("next page data");
      console.log(payload.data);
      setPageCount(payload.data.totalPages);
      console.log("next page data updated");
      payload.data.data.forEach((element: Subscriptionv) => {
        subscriptionv.push(element)
      });
      dispatch({
        type: loadData,
        payload: subscriptionv
      });
      setLoading_1(false);
    }
    catch (error) {
      //showNotification({ message: "Sorry! Somthing went wrong", color: "red" })
      // alert(error)
      console.error(error);
      setLoading_1(false);
    }
  };

  const handelDel = (id:number) =>{
    sDelete(id);
    setLoading_1(true);
    dispatch(loadDataAction());
  }

  if(onadd)
  {
    setLoading_1(true);
    dispatch(loadDataAction());
    setAdd(false);
  }
  

  const rows = SubscripList.map((row: Subscriptionv) => (
    <tr key={row.subscriptionId}>
      <td>{row.subscriptionId}</td>
      <td>{row.subscriptionName}</td>
      <td>{row.description}</td>
      <td>{row.startDate.toString()}</td>
      <td>{row.endDate.toString()}</td>
      <td>{row.benefits.delivery_discount}</td>
      <td>{row.benefits.one_day_delivery === true ? "yes" : "no"}</td>
      <td>{row.subscriptionCost}</td>
      <td>{row.period}</td>
      <td><Badge color={row.status === 'ACTIVE' ? Const.ACTIVE_STAT : Const.INACTIVE_STAT}>
        {row.status}</Badge>
      </td>

      <td>
        <Button onClick={() => { handelDel(row.subscriptionId) }} style={{ backgroundColor: 'red' }} disabled={row.status === 'ACTIVE' ? true : false}>Delete</Button>
      </td>
    </tr>
  ));

  if(loading_1)
  {
    return (
      <Center style={{ height: "80%" }}>
        <Loader data-testid="Loading" />
      </Center>
    );
  }

  return (
    <div>
      <Container>
        <AddSubscription onadd={onadd} setAdd={setAdd} open={open} setOpen={setOpen}></AddSubscription>
        <h1>Subscription Package</h1>
        <div style={{ float: 'right', display: "flex" }}>
        <Switch style={{ margin: 5 }}
            checked={check}
            label="Expired"
            onChange={(event) => {
              setCheck(event.currentTarget.checked);
              setPage(1);
            }}
          />
          <Button onClick={() => setOpen(true)} style={{ margin: 5 }}>ADD</Button>
        </div>

        <Table style={{ border: 'inset', textAlign: 'center' }}>
          <thead>
            <tr style={{ textAlignLast: 'center' }}>
              <th> ID </th>
              <th> Name </th>
              <th> Description </th>
              <th> Start Date </th>
              <th> End Date </th>
              <th> DD </th>
              <th> ODD </th>
              <th> Cost </th>
              <th> Period </th>
              <th> Status </th>
              <th> Action </th>
            </tr>
          </thead>
          <tbody>{rows}</tbody>
        </Table>
        <Pagination page={activePage}
          onChange={setPage}
          total={pageCount} style={{ float: 'right', margin: 10, padding: 10 }} />
      </Container>
    </div>
  );
}
