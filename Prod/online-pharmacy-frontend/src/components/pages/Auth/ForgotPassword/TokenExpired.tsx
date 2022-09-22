import { Image, Text } from "@mantine/core";
import { Link } from "react-router-dom";
import image from "../../../../images/expired.jpg";

export default function TokenExpired() {
  return (
    <>
      <div
        style={{
          width: 240,
          marginLeft: "auto",
          marginRight: "auto",
          marginTop: "4rem",
        }}
      >
        <Image height={250} width={250} fit="contain" src={image} />
      </div>
      <div>
        <Text  size="lg" align="center" mt={5}>
          Your token has expired!! <br />
          Click on the given link to get link to reset your password
          <Link to="/forgot-password-email"> Forgot Password?</Link>
        </Text>
      </div>
    </>
  );
}
