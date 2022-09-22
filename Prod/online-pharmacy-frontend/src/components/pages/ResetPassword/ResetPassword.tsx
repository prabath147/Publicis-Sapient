import { useForm } from "@mantine/form";
import { useNavigate } from "react-router-dom";
import { useAppSelector } from "../../../app/hooks";
import { ResetPasswordForm, ResetPasswordRequest } from "./model";

import { Button, Card, Container, PasswordInput, Title } from "@mantine/core";

import { showNotification } from "@mantine/notifications";
import { IconLock } from "@tabler/icons";
import { useDispatch } from "react-redux";
import AuthNavbar from "../../ui/AuthNavbar/AuthNavbar";
import { logOutAPI } from "../../ui/UserBadge/UserBadgeAPI";
import { getUserData, LogoutAction } from "../Auth/Login/UserSlice";
import { checkPassword } from "../Auth/Registration/RegistrationUtil";
import { resetPasswordAPI } from "./ResetPasswordAPI";

export default function ResetPassword() {
  const navigate = useNavigate();
  const userId = useAppSelector(getUserData).id;
  const dispatch = useDispatch();

  const form = useForm({
    initialValues: { oldPassword: "", newPassword: "", confirmPassword: "" },
    validate: {
      newPassword: (value) =>
        checkPassword(value)
          ? null
          : "Must contain at least one number and one uppercase and lowercase letter, and at least 8 or more characters",
      confirmPassword: (value) =>
        checkPassword(value)
          ? null
          : "Must contain at least one number and one uppercase and lowercase letter, and at least 8 or more characters",
    },
  });

  async function resetPassword(data: ResetPasswordForm) {
    if (data.newPassword !== data.confirmPassword) {
      showNotification({
        message: "password confirmation failed, please provide same password",
        color: "red",
      });
      return;
    }

    const passwordRequest: ResetPasswordRequest = {
      oldPassword: data.oldPassword,
      newPassword: data.newPassword,
    };

    resetPasswordAPI(userId, passwordRequest)
      .then((response) => {
        showNotification({
          message: "Password Reset Successfully",
          color: "green",
        });
        logOutAPI()
          .then(() => {
            dispatch(LogoutAction());
          })
          .catch(() => {
            dispatch(LogoutAction());
          });
        navigate("/login");
      })
      .catch((error) => {
        console.log(error);
        showNotification({ message: error.response.data, color: "red" });
      });
  }

  return (
    <>
      <AuthNavbar />
      <br />
      <br />
      <Title order={2} align="center" color="blue.6">
        Reset your Password!!
      </Title>
      <Container size={420} my={40}>
        <Card shadow="sm" p="lg" radius="md" withBorder>
          <form onSubmit={form.onSubmit(resetPassword)}>
            <PasswordInput
              icon={<IconLock size={16} />}
              label="Old Password"
              placeholder="Old password"
              {...form.getInputProps("oldPassword")}
            />
            <PasswordInput
              icon={<IconLock size={16} />}
              mt="sm"
              label="New password"
              placeholder="New password"
              {...form.getInputProps("newPassword")}
            />
            <PasswordInput
              icon={<IconLock size={16} />}
              mt="sm"
              label="Confirm password"
              placeholder="Confirm password"
              {...form.getInputProps("confirmPassword")}
            />

            <Button type="submit" mt="sm" fullWidth>
              Submit
            </Button>
          </form>
        </Card>
      </Container>
    </>
  );
}
