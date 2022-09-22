import { useForm } from "@mantine/form";
import { useNavigate } from "react-router-dom";
import {
  checkPassword,
  validateEmail,
  validatePhoneNumber
} from "../RegistrationUtil";
import { ManagerRegistrationForm, ManagerRegistrationRequest } from "./models";

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
import { showNotification } from "@mantine/notifications";
import AuthNavbar from "../../../../ui/AuthNavbar/AuthNavbar";
import { userRegister } from "./ManagerRegistrationAPI";

export default function ManagerRegistration() {
  const navigate = useNavigate();
  const form = useForm<ManagerRegistrationForm>({
    // validateInputOnChange: true,

    initialValues: {
      name: "",
      username: "",
      password: "",
      confirmPassword: "",

      phoneNo: 0,
      email: "",
      licenseNo: 0,
    },

    validate: {
      name: (value) =>
        value.length < 2 ? "Must have at least 2 letters" : null,
      username: (value) =>
        value.length < 2 ? "Must have at least 2 letters" : null,
      password: (value) =>
        !checkPassword(value) ? "Invalid Password, use strong password" : null,
      confirmPassword: (value) =>
        !checkPassword(value) ? "Invalid Password, use strong password" : null,

      phoneNo: (value) =>
        !validatePhoneNumber(value)
          ? "Invalid phone number, use valid phone number"
          : null,
      email: (value) =>
        !validateEmail(value) ? "Invalid email, use valid email address" : null,
      licenseNo: (value) =>
        value < 10000 || value > 1000000 ? "Invalid License Number, must have length of 5" : null,
    },
  });

  return (
    <>
      <AuthNavbar />
      <Container>
        <div className="user">
          <form
            onSubmit={form.onSubmit((values) => {
              const ManagerData: ManagerRegistrationRequest = {
                username: values.username,
                email: values.email,
                password: values.password,
                role: ["manager"],

                detailObject: {
                  phoneNo: values.phoneNo,
                  name: values.name,
                  licenseNo: values.licenseNo,
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

              // return;

              userRegister(ManagerData)
                .then((response) => {
                  showNotification({
                    message: "User Created Successfully",
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
              <Title>Manager Registration</Title>
              <Divider my={"sm"} />
              <>
                <TextInput
                  mt={"md"}
                  label="User Name"
                  withAsterisk
                  {...form.getInputProps("username")}
                />

                <TextInput
                  data-testid="namet"
                  mt={"md"}
                  label="Name"
                  withAsterisk
                  {...form.getInputProps("name")}
                />

                <PasswordInput
                  data-testid="mPT"
                  mt={"md"}
                  label="Password"
                  withAsterisk
                  {...form.getInputProps("password")}
                />

                <PasswordInput
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
                  {...form.getInputProps("phoneNo")}
                />

                <TextInput
                  mt={"md"}
                  label="Email"
                  withAsterisk
                  {...form.getInputProps("email")}
                />

                <NumberInput
                  mt={"md"}
                  label="License Number"
                  withAsterisk
                  {...form.getInputProps("licenseNo")}
                />
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

// export default function ManagerRegistration() {

//     const navigate = useNavigate();

//     const { register, handleSubmit, formState: { errors } } = useForm<ManagerRegistrationForm>();

//     async function ManagerRegistration(data: ManagerRegistrationForm) {
//         // request data preparation
//         const ManagerData: ManagerRegistrationRequest = {
//             username: data.username,
//             email: data.email,
//             password: data.password,
//             role: ["manager"],

//             detailObject: {
//                 phoneNo: data.phoneNo,
//                 name: data.name,
//                 licenseNo: data.licenseNo,
//             }
//         }

//         if (data.password !== data.confirmPassword) {
//             showNotification({ message: "password confirmation failed, please provide same password", color: "red" })
//             // alert("password confirmation failed, please provide same password")
//             return
//         }

//         userRegister(ManagerData)
//             .then(response => {
//                 showNotification({ message: "User Created Successfully", color: "green" })
//                 navigate("/login")
//             })
//             .catch(error => {
//                 console.log(error);
//                 showNotification({ message: error.response.data.message, color: "red" })
//             })

//         // try {
//         //     const response: AxiosResponse = await userRegister(ManagerData);
//         //     if (response.status === 200) {
//         //         showNotification({ message: "User Created Successfully", color: "green" })
//         //         navigate("/login")
//         //     } else {
//         //         showNotification({ message: response.statusText, color: "red" })
//         //         // alert(response.statusText)
//         //     }
//         // } catch (error) {
//         //     console.log(error);
//         //     showNotification({ message: "Oops! Something went wrong" + error.response., color: "red" })
//         //     // alert("Something wen't wrong")
//         // }
//     }

//     return (
//         <>
//             <AuthNavbar />
//             <Container>
//                 <div className="user">
//                     <form onSubmit={handleSubmit(ManagerRegistration)}>

//                         <Paper withBorder shadow="md" p={30} mt={30} radius="md" style={{ width: '80%', marginLeft: 'auto', marginRight: 'auto' }}>
//                             <TextInput label="Full Name" placeholder="Your Full Name" required {...register("name", {
//                                 required: { value: true, message: "this field is required" }
//                             })} /><br />
//                             {errors?.name && <Alert color="red">{errors.name.message}</Alert>}

//                             <TextInput label="Username" placeholder="Your Username" required {...register("username", {
//                                 required: { value: true, message: "this field is required" },
//                                 minLength: { value: 4, message: "username should have length of 4 charectors" },
//                             })} /><br />
//                             {errors?.username && <Alert color="red">{errors.username.message}</Alert>}

//                             <TextInput label="Email" placeholder="Your Email" type={"email"} {...register("email", {
//                                 required: { value: true, message: "this field is required" },
//                             })} /><br />
//                             {errors?.email && <Alert color="red">{errors.email.message}</Alert>}

//                             <TextInput label="Mobile Number" placeholder="Your Mobile Number" required type={"number"} {...register("phoneNo", {
//                                 required: { value: true, message: "this field is required" },
//                                 min: { value: 1000, message: "Invalid Mobile Number" },
//                                 max: { value: 1000000000000, message: "Invalid Mobile Number" }
//                             })} /><br />
//                             {errors?.phoneNo && <Alert color="red">{errors.phoneNo.message}</Alert>}

//                             <PasswordInput label="Password" placeholder="Your Password" required {...register("password", {
//                                 required: { value: true, message: "this field is required" },
//                                 pattern: { value: /(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{8,}/i, message: "Must contain at least one number and one uppercase and lowercase letter, and at least 8 or more characters" },
//                             })} /><br />
//                             {errors?.password && <Alert color="red">{errors.password.message}</Alert>}

//                             <PasswordInput label="Confirm Password" placeholder="Confirm Password" required {...register("confirmPassword", {
//                                 required: { value: true, message: "this field is required" },
//                                 pattern: { value: /(?=.*\d)(?=.*[a-z])(?=.*[A-Z]).{8,}/i, message: "Must contain at least one number and one uppercase and lowercase letter, and at least 8 or more characters" },
//                             })} /><br />
//                             {errors?.confirmPassword && <Alert color="red">{errors.confirmPassword.message}</Alert>}

//                             <TextInput label="License No" placeholder="Your License No" required type={"number"} {...register("licenseNo", {
//                                 required: { value: true, message: "this field is required" },
//                             })} />
//                             {errors?.licenseNo && <Alert color="red">{errors.licenseNo.message}</Alert>}

//                             <Button fullWidth mt="xl" type='submit'>
//                                 Register
//                             </Button>
//                         </Paper>

//                     </form>
//                 </div>
//             </Container>
//         </>
//     );
// }
