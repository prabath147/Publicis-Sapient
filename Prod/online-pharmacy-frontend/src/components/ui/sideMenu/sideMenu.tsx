import { faBell } from "@fortawesome/free-regular-svg-icons";
import {
  faHome,
  faPills,
  faStore,
  faTag,
  faTicket,
  faUser,
  faXmark,
} from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
  ActionIcon,
  Container,
  Divider,
  Grid,
  Image,
  MediaQuery,
  Navbar,
} from "@mantine/core";
import { Dispatch, SetStateAction } from "react";
import { Link } from "react-router-dom";
import logo from "../../../images/logo_white.png";
import UserBadge from "../UserBadge/UserBadge";

interface Role {
  role: string;
  opened: boolean;
  setOpened: Dispatch<SetStateAction<boolean>>;
}

export default function SideMenu(props: Role) {
  const LinkStyles = {
    height: "5vh",
    borderRadius: "10px",
    "&:hover": { backgroundColor: "#92c6fc" },
    color: "#FFF",
    padding: 4,
    width: "90%",
  };

  const manager_links = [
    { label: "Manager Dashboard", link: "/manager-dashboard", faIcon: faHome },
    // { label: "Stores", link: "/manager/store", faIcon: faStore },
    { label: "Notifications", link: "/manager/notifications", faIcon: faBell },
  ];

  const admin_links = [
    { label: "Admin Dashboard", link: "/admin-dashboard", faIcon: faHome },
    { label: "Stores", link: "/store", faIcon: faStore },
    { label: "Products", link: "/products-page", faIcon: faPills },
    { label: "Managers", link: "/manager", faIcon: faUser },
    {
      label: "Subscriptions",
      link: "/AdminSubscriptionPack",
      faIcon: faTicket,
    },
    { label: "Notifications", link: "/admin/notifications", faIcon: faBell },
    { label: "Category", link: "/admin/category", faIcon: faTag },
  ];

  const links = props.role === "manager" ? manager_links : admin_links;

  return (
    <>
      <Navbar
        p="xs"
        sx={{ backgroundColor: "#5F97FF", color: "#FFF" }}
        hiddenBreakpoint="sm"
        hidden={!props.opened}
        width={{ sm: 200, lg: 300 }}
      >
        <Navbar.Section sx={{ display: "flex", alignItems: "center" }}>
          <MediaQuery largerThan="sm" styles={{ display: "none" }}>
            <ActionIcon
              onClick={() => props.setOpened((o) => !o)}
              sx={{
                color: "white",
                width: 35,
                height: 35,
                border: "1px solid white",
                marginRight: 10,
              }}
              data-testid="burger-btn-sidebar"
            >
              <FontAwesomeIcon icon={faXmark} size="lg" />
            </ActionIcon>
          </MediaQuery>

          <Image src={logo} sx={{ width: "90%" }} fit="contain" />
        </Navbar.Section>

        <Divider />
        <Navbar.Section grow mt={20}>
          <Grid columns={300}>
            {links.map((link) => (
              <Grid.Col span={300} key={link.link}>
                <Link to={link.link} style={{ textDecoration: "none" }}>
                  <Container sx={LinkStyles}>
                    <FontAwesomeIcon
                      icon={link.faIcon}
                      style={{ marginLeft: 10, marginRight: 10 }}
                      size="lg"
                    />
                    {link.label}
                  </Container>
                </Link>
              </Grid.Col>
            ))}
          </Grid>
        </Navbar.Section>

        <Navbar.Section>
          <UserBadge />
        </Navbar.Section>

        {/* <Navbar.Section>
            <Link to="/*" style={{ textDecoration: "none" }}>
              <Container sx={LinkStyles}>Settings</Container>
            </Link>
          </Navbar.Section> */}
      </Navbar>
    </>
  );

  // }
  //  else {

  //   return (
  //     <Navbar
  //       p="xs"
  //       sx={{ backgroundColor: "#5F97FF", color: "#FFF" }}
  //       hiddenBreakpoint="sm"
  //       hidden={!props.opened}
  //       width={{ sm: 200, lg: 300 }}
  //     >
  //       <Navbar.Section sx={{ display: "flex", alignItems: "center" }}>
  //         <MediaQuery largerThan="sm" styles={{ display: "none" }}>
  //           <ActionIcon
  //             onClick={() => props.setOpened((o) => !o)}
  //             sx={{
  //               color: "white",
  //               width: 35,
  //               height: 35,
  //               border: "1px solid white",
  //               marginRight: 10,
  //             }}
  //           >
  //             <FontAwesomeIcon icon={faXmark} size="lg" />
  //           </ActionIcon>
  //         </MediaQuery>
  //         <h1> PharmaOnline</h1>
  //       </Navbar.Section>

  //       <Divider></Divider>

  //       <Navbar.Section grow mt={20}>
  //         <Grid columns={300}>
  //           <Grid.Col span={300}>
  //             <Link to="/store" style={{ textDecoration: "none" }}>
  //               <Container sx={LinkStyles}>
  //                 <FontAwesomeIcon
  //                   icon={faStore}
  //                   style={{ marginLeft: 10, marginRight: 10 }}
  //                   size="lg"
  //                 />
  //                 Stores
  //               </Container>
  //             </Link>
  //           </Grid.Col>
  //           <Grid.Col span={300}>
  //             <Link to="/manager" style={{ textDecoration: "none" }}>
  //               <Container sx={LinkStyles}>
  //                 <FontAwesomeIcon
  //                   icon={faStore}
  //                   style={{ marginLeft: 10, marginRight: 10 }}
  //                   size="lg"
  //                 />
  //                 Managers
  //               </Container>
  //             </Link>
  //           </Grid.Col>
  //         </Grid>
  //       </Navbar.Section>

  //       <Navbar.Section>
  //         <UserBadge />
  //       </Navbar.Section>
  //       <Navbar.Section>
  //         <Link to="/*" style={{ textDecoration: "none" }}>
  //           <Container sx={LinkStyles}>Settings</Container>
  //         </Link>
  //       </Navbar.Section>
  //     </Navbar>
  //   );
  // }
}
