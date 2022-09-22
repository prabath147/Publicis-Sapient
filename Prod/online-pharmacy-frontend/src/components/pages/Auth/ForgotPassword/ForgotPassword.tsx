import { useForm } from "@mantine/form";
import { showNotification } from "@mantine/notifications";
import { useNavigate, useParams } from "react-router-dom";

import {
  Button,
  Card,
  Container,
  LoadingOverlay,
  PasswordInput,
  Title
} from "@mantine/core";
import { IconLock } from "@tabler/icons";
import { useEffect, useState } from "react";
import AuthNavbar from "../../../ui/AuthNavbar/AuthNavbar";
import { checkPassword } from "../Registration/RegistrationUtil";
import { forgotPassword, validToken } from "./ForgotPasswordAPI";
import { ForgotPasswordForm, ForgotPasswordRequest } from "./model";

export default function ForgotPassword() {
  const navigate = useNavigate();
  const [tokenValid, setTokenValid] = useState(false);
  const { code } = useParams();
  const code_ = code === undefined ? "" : code;

  const form = useForm({
    initialValues: { newPassword: "", confirmPassword: "" },
    validate: {
      newPassword: (value) => checkPassword(value) ? null : "Must contain at least one number and one uppercase and lowercase letter, and at least 8 or more characters",
      confirmPassword: (value) => checkPassword(value) ? null : "Must contain at least one number and one uppercase and lowercase letter, and at least 8 or more characters",
    },
  });

  useEffect(() => {
    validToken(code_)
      .then((response) => {
        console.log(response);
        if (response.status === 200) {
          setTokenValid(true);
        }
      })
      .catch((error) => {
        console.log(error);
        if (error.response.status === 404) {
          showNotification({
            message: error.response.data,
            color: "red",
          });
          navigate("/");
        }
        else {
          navigate("/expired");
        }
      });
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [tokenValid]);

  const handleSubmit = (data: ForgotPasswordForm) => {

    if (data.newPassword !== data.confirmPassword) {
      showNotification({
        message:
          "password confirmation failed, please provide same password",
        color: "red",
      });
      return;
    }


    const passwordRequest: ForgotPasswordRequest = {
      newPassword: data.newPassword,
    };

    forgotPassword(code_, passwordRequest)
      .then((response) => {
        console.log(response);
        showNotification({
          message: "Password changed successfully!!",
          color: "green",
        });
        navigate("/login");
      })
      .catch((error) => {
        console.log(error);
        showNotification({
          message: error.response.data.message,
          color: "red",
        });
      });
  };

  return (
    <>
      {!tokenValid ? (
        <LoadingOverlay
          loaderProps={{ size: "md", color: "blue", variant: "oval" }}
          overlayOpacity={0.3}
          overlayColor="#c5c5c5"
          visible={tokenValid}
        />
      ) : (
        <div>
          <AuthNavbar /> <br /> <br />
          <Title order={2} align="center" color="blue.6">
            Reset your Password!!
          </Title>
          <Container size={420} my={40}>
            <Card shadow="sm" p="lg" radius="md" withBorder>
              <form onSubmit={form.onSubmit(handleSubmit)}>
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
        </div>
      )}
    </>
  );
}
