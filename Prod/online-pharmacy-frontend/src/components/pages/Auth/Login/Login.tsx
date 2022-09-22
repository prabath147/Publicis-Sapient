import {
  Button,
  Container,
  Paper,
  PasswordInput,
  Text,
  TextInput,
  Title,
} from "@mantine/core";
import { useForm } from "@mantine/form";
import { showNotification } from "@mantine/notifications";
import { Link, useNavigate } from "react-router-dom";
import { useAppDispatch } from "../../../../app/hooks";
import AuthNavbar from "../../../ui/AuthNavbar/AuthNavbar";
import { loginAPI } from "./LoginAPI";
import { LoginRequest } from "./models";
import { LoginAction } from "./UserSlice";

export interface LoginFormProps {
  onSubmit?: (loginRequest: LoginRequest) => void;
}
export default function Login({ onSubmit }: LoginFormProps) {
  const navigate = useNavigate();
  const dispatch = useAppDispatch();

  const form = useForm<LoginRequest>({
    validateInputOnChange: true,

    initialValues: {
      username: "",
      password: "",
    },

    validate: {
      username: (value) =>
        value.length < 2 ? "Must have at least 2 letters" : null,
      password: (value) =>
        value.length < 2 ? "Must have at least 2 letters" : null,
    },
  });

  return (
    <>
      <AuthNavbar />
      <Container size={420} my={40}>
        <form
          onSubmit={form.onSubmit((values) => {
            if (onSubmit) {
              onSubmit(values);
            }

            loginAPI(values)
              .then((response) => {
                dispatch(LoginAction(response.data));
                showNotification({
                  message: "Logged in Successfully!",
                  color: "green",
                });
                navigate("/");
              })
              .catch((error) => {
                showNotification({
                  message: "Invalid Username Or Password",
                  color: "red",
                });
              });
          })}
        >
          <Title
            className="animate__animated animate__pulse"
            align="center"
            sx={() => ({ fontWeight: 900 })}
          >
            Log In
          </Title>
          <Text color="dimmed" size="sm" align="center" mt={5}>
            Do not have an account yet?{" "}
            <Link to="/register">Create account </Link>
          </Text>

          <Paper withBorder shadow="md" p={30} mt={30} radius="md">
            <TextInput
              mt={"md"}
              label="User Name"
              withAsterisk
              {...form.getInputProps("username")}
            />

            <PasswordInput
              mt={"md"}
              label="Password"
              withAsterisk
              {...form.getInputProps("password")}
            />

            <Text color="dimmed" size="sm" align="center">
              <Link to="/forgot-password-email">Forgot Password?</Link>
            </Text>
            <Button fullWidth mt="xl" type="submit">
              Sign in
            </Button>
          </Paper>
        </form>
      </Container>
    </>
  );
}
