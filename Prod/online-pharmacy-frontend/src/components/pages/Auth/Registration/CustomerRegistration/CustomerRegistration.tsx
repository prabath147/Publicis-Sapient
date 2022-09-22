import {
  Button,
  Container,
  Divider,
  NumberInput,
  Paper,
  PasswordInput,
  TextInput,
  Title
} from "@mantine/core";
import { useForm } from "@mantine/form";
import { showNotification } from "@mantine/notifications";
import { useNavigate } from "react-router-dom";
import AuthNavbar from "../../../../ui/AuthNavbar/AuthNavbar";
import AddressForm, {
  addressInitialValues,
  addressValidation
} from "../../../../ui/forms/AddressForm";
import {
  checkPassword,
  validateEmail,
  validatePhoneNumber
} from "../RegistrationUtil";
import { userRegister } from "./customerRegistrationAPI";
import {
  CustomerRegistrationForm,
  CustomerRegistrationRequest
} from "./models";

export default function CustomerRegistration() {
  const navigate = useNavigate();

  const form = useForm<CustomerRegistrationForm>({
    // validateInputOnChange: true,

    initialValues: {
      fullName: "",
      username: "",
      password: "",
      confirmPassword: "",
      mobileNumber: 0,
      email: "",
      address: addressInitialValues(),
    },

    validate: {
      fullName: (value) =>
        value.length < 2 ? "Must have at least 2 letters" : null,
      username: (value) =>
        value.length < 2 ? "Must have at least 2 letters" : null,
      password: (value) =>
        !checkPassword(value) ? "Invalid Password, use strong password" : null,
      confirmPassword: (value) =>
        !checkPassword(value) ? "Invalid Password, use strong password" : null,

      mobileNumber: (value) =>
        !validatePhoneNumber(value)
          ? "Invalid phone number, use valid phone number"
          : null,
      email: (value) =>
        !validateEmail(value) ? "Invalid email, use valid email address" : null,
      address: addressValidation(),
    },
  });

  return (
    <>
      <AuthNavbar />
      <Container>
        <div className="user">
          <form
            onSubmit={form.onSubmit((values) =>
           {

              // request data preparation
              const customerData: CustomerRegistrationRequest = {
                username: values.username,
                email: values.email,
                password: values.password,
                role: ["customer"],
                detailObject: {
                  fullName: values.fullName,
                  mobileNumber: values.mobileNumber,
                  address: values.address,
                },
              };

              if (values.password !== values.confirmPassword) {
                showNotification({
                  message:
                    "password confirmation failed, please provide same password",
                  color: "red",
                });
                return;
              }

              // alert("password confirmation failed, please provide same password")

              userRegister(customerData)
                .then((response) => {
                  console.log(response);
                  showNotification({
                    message: "User Created Successfully",
                    color: "green",
                  });
                  navigate("/login");
                })
                .catch((error) => {
                  showNotification({
                    message: "Oops! Something went wrong",
                    color: "red",
                  });
                  showNotification({
                    message: error.response.data.message,
                    color: "red",
                  });
                });
            })}
          >
            <Paper
              withBorder
              shadow="md"
              p={30}
              mt={30}
              radius="md"
              style={{ width: "80%", marginLeft: "auto", marginRight: "auto" }}
            >
              <Title>Customer Registration</Title>
              <Divider my={"sm"} />
              <>
                <TextInput
                  data-testid="uT"
                  mt={"md"}
                  label="User Name"
                  withAsterisk
                  {...form.getInputProps("username")}
                />

                <TextInput
                  data-testid="fT"
                  mt={"md"}
                  label="Full Name"
                  withAsterisk
                  {...form.getInputProps("fullName")}
                />

                <PasswordInput
                  data-testid="pT"
                  mt={"md"}
                  label="Password"
                  withAsterisk
                  {...form.getInputProps("password")}
                />

                <PasswordInput
                  data-testid="cpT"
                  mt={"md"}
                  label="Confirm Password"
                  withAsterisk
                  {...form.getInputProps("confirmPassword")}
                />

                <NumberInput
                  data-testid="phone"
                  mt={"md"}
                  label="Phone Number"
                  withAsterisk
                  {...form.getInputProps("mobileNumber")}
                />

                <TextInput
                  mt={"md"}
                  label="Email"
                  withAsterisk
                  {...form.getInputProps("email")}
                />

                <AddressForm form={form} />
              </>

              <Button type="submit" my={10}>
                Register
              </Button>
            </Paper>
          </form>
        </div>
      </Container>
    </>
  );
}
