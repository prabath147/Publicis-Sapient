import {
  Button,
  Container,
  createStyles,
  Group,
  Image,
  List,
  Text,
  ThemeIcon,
  Title
} from "@mantine/core";
import { IconCheck } from "@tabler/icons";
import { Navigate, useNavigate } from "react-router-dom";
import { useAppSelector } from "../../../../app/hooks";
import image from "../../../../images/8000.jpg";
import AuthNavbar from "../../../ui/AuthNavbar/AuthNavbar";
import { isAdmin, isCustomer, isLoggedIn, isManager } from "../../Auth/Login/UserSlice";

const useStyles = createStyles((theme) => ({
  inner: {
    display: "flex",
    justifyContent: "space-between",
    paddingTop: theme.spacing.xl * 2,
  },

  content: {
    maxWidth: 480,
    marginRight: theme.spacing.xl * 3,

    [theme.fn.smallerThan("md")]: {
      maxWidth: "100%",
      marginRight: 0,
    },
  },

  title: {
    color: theme.colorScheme === "dark" ? theme.white : theme.black,
    fontFamily: `Greycliff CF, ${theme.fontFamily}`,
    fontSize: 35,
    lineHeight: 1.4,
    fontWeight: 900,

    [theme.fn.smallerThan("xs")]: {
      fontSize: 32,
    },
  },

  control: {
    [theme.fn.smallerThan("xs")]: {
      flex: 1,
    },
  },

  highlight: {
    position: "relative",
    backgroundColor: theme.fn.variant({
      variant: "light",
      color: theme.primaryColor,
    }).background,
    borderRadius: theme.radius.sm,
    padding: "4px 12px",
    color: "#0c7ec9",
  },
}));

export default function LandingPage() {
  const { classes } = useStyles();
  const navigate = useNavigate();

  const isUserAdmin = useAppSelector(isAdmin);
  const isUserManager = useAppSelector(isManager);
  const isUserCustomer = useAppSelector(isCustomer);
  const isUserLoggedIN = useAppSelector(isLoggedIn);

  if (isUserAdmin) return <Navigate to="/admin-dashboard" />;
  else if (isUserManager) return <Navigate to="/manager-dashboard" />;
  else if (isUserCustomer) return <Navigate to="/search" />
  else if (isUserLoggedIN) return <Navigate to="/waiting" />



  const Login = () => {
    navigate("/login");
  };

  const Signup = () => {
    navigate('/register');
  };

  return (
    <>
      <AuthNavbar />
      <div>
        <Container>
          <div className={classes.inner}>
            <div className={classes.content}>
              <Title className={classes.title}>
                <span className={classes.highlight}>PillZone</span>- An Online
                Marketplace for Pharmaceutical Products!
              </Title>
              <Text color="dimmed" mt="md">
                We offer a huge catalog of medicines and health care products,
                sourced from various offline pharmacies and medicine companies.
              </Text>

              <List
                mt={30}
                spacing="sm"
                size="sm"
                icon={
                  <ThemeIcon size={20} radius="xl">
                    <IconCheck size={12} stroke={1.5} />
                  </ThemeIcon>
                }
              >
                <List.Item>
                  <b>Tactically Optimized Inventory</b>
                </List.Item>
                <List.Item>
                  <b>Least Price Centered Design</b>
                </List.Item>
                <List.Item>
                  <b>Automatic Reading of Uploaded Prescriptions</b>
                </List.Item>
                <List.Item>
                  <b>Reminders to our Opted-In Customers </b>
                </List.Item>
              </List>

              <Group mt={30}>
                <Button onClick={Login} size="sm">
                  Login
                </Button>
                <Button onClick={Signup} variant="outline" size="sm">
                  Register
                </Button>
              </Group>
            </div>
            <Image
              className="animate__animated animate__backInRight"
              height={500}
              fit="contain"
              src={image}
            />
          </div>
        </Container>
      </div>
    </>
  );
}
