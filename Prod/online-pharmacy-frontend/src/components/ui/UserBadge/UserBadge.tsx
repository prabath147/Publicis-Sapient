import { Avatar, Badge, Menu, UnstyledButton } from '@mantine/core';
import { showNotification } from '@mantine/notifications';
import { IconLock, IconRotateClockwise, IconTicket, IconTrash, IconTruckDelivery } from '@tabler/icons';
import { useNavigate } from 'react-router-dom';
import { useAppDispatch, useAppSelector } from '../../../app/hooks';
import { getUserData, isCustomer, LogoutAction } from '../../pages/Auth/Login/UserSlice';
import { clearCart } from '../../pages/Cart/CartSlice';
import { logOutAPI } from './UserBadgeAPI';

export default function UserBadge() {
  const user = useAppSelector(getUserData);

  const dispatch = useAppDispatch();
  const navigate = useNavigate();

  const isUserCustomer = useAppSelector(isCustomer)



  return (
    <Menu shadow="md" width={250}>
      <Menu.Target>
        <UnstyledButton p="md" data-testid="btn-badge">
          <Avatar size={30} radius={30} ml="xs" color="blue" />
        </UnstyledButton>
      </Menu.Target>

      <Menu.Dropdown>
        <Menu.Label>
          <Badge variant="filled" fullWidth size="sm">
            {user.username}
          </Badge>
        </Menu.Label>

        <Menu.Label>{user.email}</Menu.Label>

        <Menu.Divider />

        {isUserCustomer && (
          <>
            <Menu.Item
              color="blue"
              icon={<IconTicket size={14} />}
              data-testid="btn-subs"
              onClick={() => {
                navigate("/subscriptions")
              }}
            >
              My Subscription
            </Menu.Item>

            <Menu.Item
              color="blue"
              data-testid="btn-orders"
              icon={<IconTruckDelivery size={14} />}
              onClick={() => {
                navigate("/orders/list")
              }}
            >
              My Orders
            </Menu.Item>

            <Menu.Item
              color="blue"
              data-testid="btn-optins"
              icon={<IconRotateClockwise size={14} />}
              onClick={() => {
                navigate("/optin/list")
              }}
            >
              My Opt-ins
            </Menu.Item>

            <Menu.Divider />
          </>

        )}


        <UnstyledButton
          style={{ width: "100%" }}
          data-testid="btn-pwd-reset"
          onClick={() => {
            navigate("/reset-password")
          }}>
          <Menu.Item
            icon={<IconLock size={14} />}
          >Reset Password</Menu.Item>
        </UnstyledButton>

        <Menu.Item
          color="red"
          icon={<IconTrash size={14} />}
          data-testid="btn-logout"
          onClick={() => {
            logOutAPI()
              .then(() => {
                dispatch(LogoutAction());
                showNotification({ message: "Logged Out", color: "green" });
              })
              .catch(() => {
                dispatch(LogoutAction());
                showNotification({ message: "Logging out Locally", color: "red" });
              })
              .then(() => {
                dispatch(clearCart())
              })
          }}>
          Logout
        </Menu.Item>

      </Menu.Dropdown>
    </Menu >
  );
}
