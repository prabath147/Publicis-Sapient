import { Badge, Button, Card, Center, Container, Grid, Group, LoadingOverlay, Text, Title } from "@mantine/core";
import { showNotification } from "@mantine/notifications";
import { useEffect, useState } from "react";
import { useAppSelector } from "../../../../app/hooks";
import { getUserData } from "../../Auth/Login/UserSlice";
import { RegisterSubscription } from "./models";
import { unsubscribeAPI, usersubscriptionAPI } from "./SubscriptionAPI";
// import { useNavigate } from "react-router-dom";

export default function Subscriptions() {

  const [subscriptions, setSubscriptions] = useState<RegisterSubscription[]>([]);
  const [loading, setLoading] = useState(true);
  const user = useAppSelector(getUserData);

  useEffect(() => {
    usersubscriptionAPI(user.id).then((response) => {
      // console.log(response.data.userSubsSet);
      let userSubscriptions = [];
      userSubscriptions = response.data.userSubsSet.map((item) => {
        return ((item.subscriptions));
      })
      // console.log(userSubscriptions);
      setSubscriptions(userSubscriptions);
    }).catch((error) => {
      console.log('Error' + error.message);
    });
    setLoading(false);
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [])

  const handleClick = (id) => {
    unsubscribeAPI(user.id, id).then((response) => {
      showNotification({ message: "Unsubscribed!", color: "green", autoClose: 500 });
      usersubscriptionAPI(user.id).then((response) => {
        console.log(response.data.userSubsSet);
        let userSubscriptions = [];
        userSubscriptions = response.data.userSubsSet.map((item) => {
          return ((item.subscriptions));
        })
        console.log(userSubscriptions);
        setSubscriptions(userSubscriptions);
      }).catch((error) => {
        console.log('Error' + error.message);
      });
    }).catch((error) => {
      console.log('Error' + error.message);
      showNotification({ message: "Something went wrong, please try again!", color: "red" });
    });
  }

  return <> {loading ? (<LoadingOverlay
    loaderProps={{ size: "md", color: "blue", variant: "oval" }}
    overlayOpacity={0.3}
    overlayColor="#c5c5c5"
    visible={loading}
  />) : (<Container>
    <Title align="center" order={2}>
      Your Subscriptions
    </Title><br />
    <Center>
      <Grid columns={90} sx={{ width: '100%' }}>

        {subscriptions.map((item) => {
          return (
            <Grid.Col sm={60} md={30}>
              <Card shadow="sm" p="md" style={{ width: '100%', height: 300, marginTop: '10px', marginBottom: '10px' }} radius="md" withBorder>
                <Group sx={{ width: '100%' }} position="apart">
                  <Text style={{ fontSize: 22, fontWeight: 700, marginTop: 10 }}>{item.subscriptionName}<br />{"₹" + item.subscriptionCost}</Text>
                  <Text size="md" weight={700} style={{ marginBottom: 10 }}>
                    <Badge>{item.startDate + "-" + item.endDate}</Badge>
                  </Text>
                </Group>

                <Group position="apart">

                  <Group style={{ marginBottom: 10 }} spacing="xs">
                    {item.benefits.one_day_delivery === true ? (<Badge variant="gradient" gradient={{ from: '#bd0000', to: 'red' }}>1 DAY DELIVERY</Badge>) : ""}
                    {item.benefits.delivery_discount !== 0 ? (<Badge variant="gradient" gradient={{ from: '#bd0000', to: 'red' }}>{item.benefits.delivery_discount + "% OFF ON DELIVERY"}</Badge>) : ""}
                  </Group>

                </Group>
                <Text lineClamp={2}>{item.description}</Text>
                <Center mt="xs">
                  {/* <Button disabled size="xs" color="green">Subscribed</Button> */}
                  <Button onClick={() => handleClick(item.subscriptionId)} style={{ marginLeft: 10 }} size="xs" variant="gradient">Unsubscribe</Button>
                </Center>

              </Card>
            </Grid.Col>
          );
        })}
      </Grid>
    </Center>
    <br />
    {/* <Center>
      <Button size="sm" onClick={RegisterSubscriptionPage}>
        Upgrade
      </Button>
    </Center> */}

  </Container>)}
  </>
}