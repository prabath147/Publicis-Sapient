import { ActionIcon, Button, Card, Center, Container, Grid, Loader, Pagination, Table } from "@mantine/core";
import { showNotification } from "@mantine/notifications";
import { IconChevronRight } from "@tabler/icons";
import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import AddProduct from "./AddProduct";
import { Product } from "./models";
import { getProductList } from "./ProductListAPI";


const ProductPage = () => {

  const [productList, setProductList] = useState<Product[]>([]);
  const [loading, setLoading] = useState(true);
  const [activePage, setPage] = useState(1);
  const [pageCount, setPageCount] = useState(1);
  const [opened, setOpened] = useState(false);


  const limit = 10;
  useEffect(() => {
    loadProductList();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [activePage]);

  const loadProductList = () => {
    setLoading(true);
    getProductList(
      (activePage - 1),
      limit
    ).then((payload) => {
      const managers: Product[] = [];
      // console.log(payload.data);
      setPageCount(payload.data.totalPages);
      payload.data.data.forEach((element: Product) => {
        managers.push(element);
      });
      setProductList(managers);
      setLoading(false);
    })
      .catch((err) => {
        showNotification({
          color: "red",
          message: "Oops! Something went wrong",
        });
        setLoading(false);
      });;
  }

  if (loading)
    return (
      <Center style={{ height: "80%" }}>
        <Loader data-testid="Loading" />
      </Center>
    );

  const rows = productList.map((row: Product) => (
    <tr key={row.productId}>
      <td>{row.productId}</td>
      <td>{row.productName}</td>
      <td>{row.productType ? "Generic" : "Non-Generic"}</td>
      <td>{row.proprietaryName}</td>

      <td>{row.dosageForm}</td>
      <td>
        <Link to={"/products-details/" + row.productId} style={{ textDecoration: 'none' }}>

          <ActionIcon variant='subtle' color={"blue"}>
            <IconChevronRight size={26} />
          </ActionIcon>
        </Link>
      </td>
    </tr>

  ))

  return (
    <div>
      <AddProduct opened={opened} setOpened={setOpened}></AddProduct>
      <Card shadow="sm" p="lg" radius="md" withBorder>
        <Container>
          <Grid justify="space-between" >
            <Grid.Col span={4}>
              <h1>List of Products</h1>
            </Grid.Col>
            <Grid.Col span={2}>

              <Button onClick={() => { setOpened(true) }} size="sm" >Add Products</Button>

            </Grid.Col>
          </Grid>
          <Table highlightOnHover>
            <thead>
              <tr>
                <th>ProductId</th>
                <th>ProductName</th>
                <th>ProductType</th>
                <th>ProprietaryName</th>
                <th>DosageForm</th>
                <th>View</th>
              </tr>
            </thead>
            <tbody>{rows}</tbody>
          </Table>
          <Pagination
            position="center"
            page={activePage}
            onChange={setPage}
            total={pageCount}
          />
        </Container>
      </Card>

    </div>
  )
}

export default ProductPage;