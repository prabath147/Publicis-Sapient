import { Button, Container, createStyles, Group, Image, SimpleGrid, Title } from '@mantine/core';
import { Link } from 'react-router-dom';
import Images from '../../../../../images/hero-img.jpg';
import AuthNavbar from '../../../../ui/AuthNavbar/AuthNavbar';

const useStyles = createStyles((theme) => ({
  root: {
    paddingTop: 75,
  },

  title: {
    fontWeight: 800,
    fontSize: 40,
    marginTop: 50,
    fontFamily: `Greycliff CF, ${theme.fontFamily}`,
    [theme.fn.smallerThan('sm')]: {
      fontSize: 32,
    },
  },

  control: {
    [theme.fn.smallerThan('sm')]: {
      width: '100%',
    },
  },

  mobileImage: {
    [theme.fn.largerThan('sm')]: {
      display: 'none',
    },
  },

  desktopImage: {
    [theme.fn.smallerThan('sm')]: {
      display: 'none',
    },
  },
}));

export default function RegistrationOption() {
  const { classes } = useStyles();
  // const navigate = useNavigate();

  // const registerCustomer = () => {
  //   navigate('/register/customer');
  // };

  // const registerManager = () => {
  //   navigate('/register/manager');
  // };

  return (
    <>
      <AuthNavbar />
      <Container className={classes.root}>
        <SimpleGrid spacing={80} cols={2} breakpoints={[{ maxWidth: 'sm', cols: 1, spacing: 40 }]}>
          <Image src={Images} className={classes.mobileImage} />
          <div>
            <Title className={classes.title}>Welcome to PillZone! Register as</Title>

            <Group mt={30}>
              <Link to="/register/customer">
                <Button size="sm">Customer</Button>
              </Link>
              <Link to="/register/manager">
                <Button variant="outline" size="sm">Store Manager</Button>
              </Link>
            </Group>
          </div>
          <Image src={Images} className={classes.desktopImage} />
        </SimpleGrid>
      </Container>
    </>
  );
}