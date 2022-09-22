import { faBars } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { ActionIcon, AppShell, MediaQuery } from '@mantine/core';
import { useState } from 'react';
import { Navigate, Outlet } from 'react-router-dom';
import { useAppSelector } from '../../../app/hooks';
import { getUserData } from '../../pages/Auth/Login/UserSlice';
import SideMenu from '../sideMenu/sideMenu';


export default function ManagerProtectedRoutes() {
    const user = useAppSelector(getUserData)
    const [opened, setOpened] = useState(false);


    const role = "ROLE_MANAGER"

    if (user.id !== -1 && user.role === role) {
        return <>
            <AppShell
                navbarOffsetBreakpoint="sm"
                navbar={<SideMenu role={"manager"} opened={opened} setOpened={setOpened} />}
                styles={{ main: { backgroundColor: "#fafafa" } }}
            >

                <div style={{ display: "flex", alignItems: "center", marginBottom: 20, marginTop: 15 }}>
                    <MediaQuery largerThan="sm" styles={{ display: 'none' }}>
                        <ActionIcon variant="outline" onClick={() => setOpened((o) => !o)} sx={{ color: 'black', width: 35, height: 35, marginRight: 10 }}>
                            <FontAwesomeIcon icon={faBars} size="lg" />
                        </ActionIcon>
                    </MediaQuery>
                    {/* <Title order={3}>Manager Dashboard</Title> */}
                </div>

                <Outlet />

            </AppShell>

        </>;
    } else if (user.id !== -1) {
        return <Navigate to="/not-allowed" />;
    } else {
        return <Navigate to="/" />
    }
}
