import { useForm } from "@mantine/form";
import { useNavigate } from "react-router-dom";

import { Button, Card, Container, TextInput, Title } from "@mantine/core";
import { showNotification } from "@mantine/notifications";
import AuthNavbar from "../../../ui/AuthNavbar/AuthNavbar";
import { sendPasswordResetLink } from "./ForgotPasswordAPI";

export default function ForgotPassword() {
  const navigate = useNavigate();
  // const email = useState("");

  interface FormValues {
    email: string;
  }

  const form = useForm({
    initialValues: { email: "" },
    validate: {
      email: (value) => (/^\S+@\S+$/.test(value) ? null : 'Invalid email')
    },
  });

  const handleSubmit = (data: FormValues) => {

    sendPasswordResetLink(data.email)
      .then((response) => {
        console.log(response);
        showNotification({
          message: "Password reset link has been sent!!",
          color: "green",
        });
        navigate("/login");
      })
      .catch((error) => {
        console.log(error);
        showNotification({
          message: error.response.data,
          color: "red",
        });
        form.resetDirty();
      });
  };

  return (
    <>
      <AuthNavbar /> <br /> <br /> <br />
      <Title order={2} align="center">
        Enter your email to get password reset link!!
      </Title>
      <Container size={420} my={40}>
        <Card shadow="sm" p="lg" radius="md" withBorder>
          <form onSubmit={form.onSubmit(handleSubmit)}>
            <TextInput
              withAsterisk
              label="Email"
              placeholder="your@email.com"
              {...form.getInputProps('email')}
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
