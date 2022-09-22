import { Container, Grid, Group, Image, MediaQuery } from "@mantine/core";
import Images from '../../../../images/16210.jpg';
import UserBadge from "../../../ui/UserBadge/UserBadge";


export default function CustomerLandingPage() {

  return (
    <>

      <div style={{ height: '100vh', width: '100vw' }}>
        <Group position="apart" sx={{ backgroundColor: "#5F97FF", height: "10%", width: '100vw' }}>
          <Container sx={{ color: "#FFF" }}>
            <h3 >PharmaOnline</h3>
          </Container>

          <Container sx={{ color: "#FFF" }} >
            <UserBadge />
          </Container>
        </Group>
        <Grid sx={{ margin: 0, height: "90%", width: '100vw' }} columns={20} >
          <MediaQuery largerThan="sm" styles={{ display: "flex" }}>
            <Grid.Col sx={{ backgroundColor: "#28b6f6" }} pt={10} md={14} sm={20}  >

              <Image height={600} fit="contain" src={Images} />

            </Grid.Col>
          </MediaQuery>
          <Grid.Col sx={{ display: 'flex', width: '100%', alignItems: 'flex-start' }} md={6} sm={20}>
            <Grid columns={8}>
              <Grid.Col sx={{ backgroundColor: "#E0E8F5", height: '30%' }} md={8} sm={8}>
                <h2 style={{ color: "#5F97FF" }}>Active Subscriptions</h2>
              </Grid.Col>
              <Grid.Col sx={{ backgroundColor: "#FFF", height: '70%' }} md={8} sm={8}>
                <h2 style={{ color: "#5F97FF" }}>Recent Orders</h2>
                <p>Lorem ipsum dolor sit amet consectetur adipisicing elit. Veniam facere perspiciatis, exercitationem itaque repudiandae sequi soluta culpa impedit libero nisi repellat tempore labore aperiam dignissimos autem fuga eos esse doloribus.</p>

              </Grid.Col>
            </Grid>
          </Grid.Col>
        </Grid>
      </div>
    </>
  );


}