import { Burger, Container, createStyles, Group, Header, Image, Paper, Space, Transition } from '@mantine/core';
import { useDisclosure } from '@mantine/hooks';
import { useNavigate } from 'react-router-dom';
import logo from '../../../images/logo.png';
import UserBadge from '../UserBadge/UserBadge';

const HEADER_HEIGHT = 60;

const useStyles = createStyles((theme) => ({
  root: {
    position: 'relative',
    zIndex: 3,
    // width: '100%',
    display: "block"
  },

  dropdown: {
    position: 'absolute',
    top: HEADER_HEIGHT,
    left: 0,
    right: 0,
    zIndex: 0,
    borderTopRightRadius: 0,
    borderTopLeftRadius: 0,
    borderTopWidth: 0,
    overflow: 'hidden',

    [theme.fn.largerThan('sm')]: {
      display: 'none',
    },
  },

  header: {
    display: 'flex',
    justifyContent: 'space-between',
    alignItems: 'center',
    height: '100%',
    // width: "100%"
  },

  links: {
    [theme.fn.smallerThan('sm')]: {
      display: 'none',
    },
  },

  burger: {
    [theme.fn.largerThan('sm')]: {
      display: 'none',
    },
  },

  link: {
    display: 'block',
    lineHeight: 1,
    padding: '8px 12px',
    borderRadius: theme.radius.sm,
    textDecoration: 'none',
    color: theme.colors.gray[7],
    fontSize: theme.fontSizes.sm,
    fontWeight: 500,

    '&:hover': {
      backgroundColor: theme.colors.gray[0],
    },

    [theme.fn.smallerThan('sm')]: {
      borderRadius: 0,
      padding: theme.spacing.md,
    },
  },

  linkActive: {
    '&, &:hover': {
      backgroundColor: theme.fn.variant({ variant: 'light', color: theme.primaryColor }).background,
      color: theme.fn.variant({ variant: 'light', color: theme.primaryColor }).color,
    },
  },
}));


const links = [
  { label: "Search", link: "/search" },
  { label: "Subscriptions", link: "/register/subscription" },
  { label: "Cart", link: "/cart" },
]

export default function CustomerNavbar() {
  const [opened, { toggle }] = useDisclosure(false);
  const { classes, cx } = useStyles();
  const navigate = useNavigate()

  const items = links.map((link) => (
    <a
      key={link.label}
      href={link.link}
      // className={cx(classes.link, { [classes.linkActive]: active === link.link })}
      className={cx(classes.link)}
      onClick={(event) => {
        event.preventDefault();
        navigate(link.link)
        // close();
      }}
    >
      {link.label}
    </a>
  ));

  return (
    <Container>
      <Header height={HEADER_HEIGHT} className={classes.root}>
        <Container className={classes.header}>
          <Image src={logo} width={120} height={20} fit="contain" />
          <Group spacing={5} className={classes.links}>
            {items}
            {/* <ActionIcon onClick={() => { navigate("/notify"); }}>
              <FontAwesomeIcon icon={faBell} />
            </ActionIcon> */}
            <UserBadge />
          </Group>

          <Burger opened={opened} onClick={toggle} className={classes.burger} size="sm" data-testid="burger-btn" />

          <Transition transition="pop-top-right" duration={200} mounted={opened}>
            {(styles) => (
              <Paper className={classes.dropdown} withBorder style={styles}>
                {items}
                {/* <ActionIcon onClick={() => { navigate("/notify"); }}>
                  <FontAwesomeIcon icon={faBell} />
                </ActionIcon> */}
                <UserBadge />


              </Paper>
            )}
          </Transition>
        </Container>
      </Header>
      <Space h="md" />
    </Container>
  );
}