import {
  Box,
  Button,
  Center,
  Container,
  Divider,
  FileButton,
  Group,
  Loader,
  Table,
  Text,
  Title
} from "@mantine/core";
import { showNotification } from "@mantine/notifications";
import { Buffer } from "buffer";

import { useRef, useState } from "react";
import { useNavigate } from "react-router-dom";
import { createWorker } from "tesseract.js";
import { useAppDispatch, useAppSelector } from "../../../app/hooks";
import { getUserData } from "../Auth/Login/UserSlice";
import { PrescriptionItem } from "./models";
import { AddToPrescriptionCart, searchItemsAPI } from "./PrescriptionAPI";

// @ts-ignore
window.Buffer = Buffer;

export default function Prescription() {
  const [file, setFile] = useState("");
  const resetRef = useRef<() => void>(null);
  const [isLoading, setLoading] = useState(false);
  const [items, setItems] = useState<Array<PrescriptionItem>>([]);
  const navigate = useNavigate();
  const dispatch = useAppDispatch();
  const uid = useAppSelector(getUserData).id;

  const clearFile = () => {
    setFile("");
    resetRef.current?.();
  };

  const sleep = (milliseconds) => {
    return new Promise((resolve) => setTimeout(resolve, milliseconds));
  };

  const PrescriptionToCart = () => {
    AddToPrescriptionCart(uid, items, dispatch);
    showNotification({
      message: "Prescription added successfully",
      color: "green",
    });
    navigate("/cart");
  };

  const handleSubmit = () => {
    setLoading(true);

    const worker = createWorker({
      // langPath: process.env.PUBLIC_URL + "/lang-data",
      logger: (m) => console.log(m),
    });

    (async () => {
      await worker.load();
      await worker.loadLanguage("eng");
      await worker.initialize("eng");
      const {
        data: { text },
      } = await worker.recognize(file);
      // console.log(text);
      let item_array = text.split("\n");

      const output = await searchItemsAPI(item_array);

      setItems(output);
      await sleep(1000);
      setLoading(false);

      await worker.terminate();
    })();

    // console.log(output);
  };

  return (
    <>
      <Container>
        {!isLoading && items.length === 0 && (
          <>
            <Title>Upload your prescription</Title>
            <Divider my="sm" />
            <Center>
              <Box
                my={30}
                sx={(theme) => ({
                  backgroundColor:
                    theme.colorScheme === "dark"
                      ? theme.colors.dark[6]
                      : theme.colors.gray[0],
                  textAlign: "center",
                  padding: theme.spacing.lg,
                  borderRadius: theme.radius.md,
                  height: "20%",
                  width: "90%",
                })}
              >
                <Group position="center" mx={30}>
                  <FileButton
                    resetRef={resetRef}
                    onChange={(payload: File) =>
                      setFile(URL.createObjectURL(payload))
                    }
                    accept="image/png,image/jpeg"
                  >
                    {(props) => <Button {...props}>Upload image</Button>}
                  </FileButton>
                  <Button disabled={!file} color="red" onClick={clearFile}>
                    Reset
                  </Button>
                </Group>

                {file && (
                  <Text size="sm" align="center" mt="sm">
                    File has been Selected
                  </Text>
                )}
                <Button mt="lg" onClick={handleSubmit}>
                  Submit
                </Button>
              </Box>
            </Center>
          </>
        )}
        {isLoading && (
          <Center>
            <Loader />
          </Center>
        )}
        {!isLoading && file !== "" && items.length !== 0 && (
          <>
            <Button
              mt="lg"
              onClick={() => {
                setFile("");
                setItems([]);
              }}
            >
              Retry
            </Button>
            <Table>
              <thead>
                <tr>
                  <th>Medicine Name</th>
                  <th>Quantity</th>
                  <th>Price</th>
                </tr>
              </thead>
              <tbody>
                {items.map((item: PrescriptionItem) => {
                  if (item.name !== "") {
                    return (
                      <tr key={item.id}>
                        <td>{item.name}</td>
                        <td>{item.quantity}</td>
                        <td>{item.price}₹</td>
                      </tr>
                    );
                  } else return " ";
                })}
              </tbody>
            </Table>

            <Center>
              <Button mt="lg" onClick={() => PrescriptionToCart()} color="lime">
                Add To Cart
              </Button>
            </Center>
          </>
        )}
      </Container>
    </>
  );
}
