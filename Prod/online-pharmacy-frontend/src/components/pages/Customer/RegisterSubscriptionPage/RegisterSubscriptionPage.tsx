import {
  Badge,
  Button,
  Card,
  Center,
  Container,
  Grid,
  Group,
  LoadingOverlay,
  Pagination,
  Text,
  Title
} from "@mantine/core";
import { showNotification } from "@mantine/notifications";
import { useEffect, useState } from "react";
import { useAppSelector } from "../../../../app/hooks";
import { getUserData } from "../../Auth/Login/UserSlice";
import { RegisterSubscription } from "./models";
import {
  getSubscription,
  subscribeAPI,
  usersubscriptionAPI
} from "./SubscriptionAPI";

export default function RegisterSubscriptionPage() {
  // const [selectedId, changeSelection] = useState(0);
  const [data, setData] = useState<RegisterSubscription[]>([]);
  const [loading, setLoading] = useState(true);
  const user = useAppSelector(getUserData);
  const [subscriptions, setSubscriptions] = useState([]);

  const [pageCount, setPageCount] = useState(1);
  const [activePage, setPage] = useState(1);

  useEffect(() => {
    getSubscription(activePage - 1, 9)
      .then((response) => {
        // console.log("output = " + response.data.data);
        setData(response.data.data);
        setPageCount(response.data.totalPages);
      })
      .catch((error) => {
        console.log("Error" + error.message);
      });

    usersubscriptionAPI(user.id)
      .then((response) => {
        // console.log(response.data.userSubsSet);
        let userSubscriptions = [];
        userSubscriptions = response.data.userSubsSet.map((item) => {
          return item.subscriptions.subscriptionId;
        });
        // console.log(userSubscriptions);
        setSubscriptions(userSubscriptions);
      })
      .catch((error) => {
        console.log("Error" + error.message);
      });
    setLoading(false);
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [activePage]);

  const handleClick = (id) => {
    // changeSelection(id);
    subscribeAPI(user.id, id)
      .then((response) => {
        showNotification({
          message: "Subscribed!",
          color: "green",
          autoClose: 500,
        });
        getSubscription(activePage - 1, 9)
          .then((response) => {
            console.log("output = " + response.data.data);
            setData(response.data.data);
          })
          .catch((error) => {
            console.log("Error" + error.message);
          });
        usersubscriptionAPI(user.id)
          .then((response) => {
            console.log(response.data.userSubsSet);
            let userSubscriptions = [];
            userSubscriptions = response.data.userSubsSet.map((item) => {
              return item.subscriptions.subscriptionId;
            });
            console.log(userSubscriptions);
            setSubscriptions(userSubscriptions);
          })
          .catch((error) => {
            console.log("Error" + error.message);
          });
      })
      .catch((error) => {
        console.log("Error" + error.message);
        showNotification({
          message: "Something went wrong, please try again!",
          color: "red",
        });
      });
  };

  const status = (item: RegisterSubscription) => {
    const arr: number[] = subscriptions;
    if (arr.includes(item.subscriptionId)) {
      return (
        <Button
          size="xs"
          variant="gradient"
          gradient={{ from: "teal", to: "lime" }}
          disabled
        >
          Subscribe
        </Button>
      );
    } else {
      return (
        <Button
          size="xs"
          variant="gradient"
          onClick={() => handleClick(item.subscriptionId)}
          gradient={{ from: "teal", to: "lime" }}
        >
          Subscribe
        </Button>
      );
    }
  };

  return (
    <>
      {" "}
      {loading ? (
        <LoadingOverlay
          data-testid="Loading"
          loaderProps={{ size: "md", color: "blue", variant: "oval" }}
          overlayOpacity={0.3}
          overlayColor="#c5c5c5"
          visible={loading}
        />
      ) : (
        <Container>
          <Title align="center" order={2}>
            Available Subscriptions
          </Title>
          <br />
          <Center>
            <Grid columns={90} sx={{ width: "100%" }}>
              {data.map((item) => {
                return (
                  <Grid.Col sm={60} md={30}>
                    <Card
                      shadow="sm"
                      p="md"
                      style={{
                        width: "100%",
                        height: 300,
                        marginTop: "10px",
                        marginBottom: "10px",
                      }}
                      radius="md"
                      withBorder
                    >
                      <Text
                        style={{ fontSize: 22, fontWeight: 700, marginTop: 10 }}
                      >
                        {item.subscriptionName}
                        <br />
                        {"₹" + item.subscriptionCost}
                      </Text>

                      <Group sx={{ width: "100%" }} position="apart">
                        <Text
                          size="md"
                          weight={700}
                          style={{ marginBottom: 10 }}
                        >
                          <Badge>{item.startDate + "-" + item.endDate}</Badge>
                        </Text>
                      </Group>

                      <div style={{ height: "70px" }}>
                        <Group style={{ marginBottom: 10 }} spacing="xs">
                          {item.benefits.one_day_delivery === true ? (
                            <Badge
                              variant="gradient"
                              gradient={{ from: "#bd0000", to: "red" }}
                            >
                              1 DAY DELIVERY
                            </Badge>
                          ) : (
                            ""
                          )}
                          {item.benefits.delivery_discount !== 0 ? (
                            <Badge
                              variant="gradient"
                              gradient={{ from: "#bd0000", to: "red" }}
                            >
                              {item.benefits.delivery_discount +
                                "% OFF ON DELIVERY"}
                            </Badge>
                          ) : (
                            ""
                          )}
                        </Group>
                      </div>
                      <Text lineClamp={2}>{item.description}</Text>
                      <Center mt="xs">{status(item)}</Center>
                    </Card>
                  </Grid.Col>
                );
              })}
            </Grid>
          </Center>
          <br />
          {/* <Center>
      <Button size="sm" onClick={() => console.log(selectedId)}>
        Submit
      </Button>
    </Center> */}
        </Container>
      )}
      <Pagination
        mb="lg"
        data-testid="paginationT"
        position="center"
        page={activePage}
        onChange={setPage}
        total={pageCount}
      />
    </>
  );
}
