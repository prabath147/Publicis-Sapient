import {
  Button,
  Card,
  Center,
  Container,
  Grid,
  Loader,
  Pagination,
  Space,
  Table,
  TextInput
} from "@mantine/core";
import { useForm } from "@mantine/form";
import { showNotification } from "@mantine/notifications";
import { useEffect, useState } from "react";
import {
  createCategory,
  deleteCategory,
  getCategoryList
} from "./CategoryListAPI";
import { Category } from "./models";

export default function CategoryList() {
  const [activePage, setPage] = useState(1);
  const [pageCount, setPageCount] = useState(1);
  const [loading, setLoading] = useState(true);
  const [categories, setCategories] = useState<Category[]>([]);

  const limit = 10;

  function getCategories() {
    setLoading(true);
    getCategoryList(activePage - 1, limit)
      .then((response) => {
        setPageCount(response.data.totalPages);
        setCategories(response.data.data);
        setLoading(false);

      })
      .catch(() => {
        showNotification({
          color: "red",
          message: "oops, something went wrong",
        });
        setCategories([]);
        setLoading(false);

      })

  }

  useEffect(() => {
    getCategories();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [activePage]);

  const form = useForm<Category>({
    validateInputOnChange: true,
    initialValues: {
      categoryId: -1,
      categoryName: "",
    },
    validate: {
      categoryName: (value: string) =>
        value.length < 2 ? "it should be have a length of 2" : null,
    },
  });

  if (loading)
    return (
      <Center style={{ height: "80%" }}>
        <Loader data-testid="Loading" />
      </Center>
    );

  return (
    <Container>
      <Card>
        <form>
          <Grid>
            <Grid.Col>
              <TextInput
                data-testid="CategoryInput"
                mt={"md"}
                label="Category Name"
                withAsterisk
                {...form.getInputProps("categoryName")}
              />
            </Grid.Col>
            <Grid.Col>
              <Center>
                <Button
                  data-testid="Create-Category-Button"
                  onClick={() => {
                    createCategory(form.values)
                      .then(() => {
                        showNotification({
                          color: "green",
                          message: "Category created successfully.",
                        });
                        getCategories();
                        form.setValues({
                          categoryName: "",
                          categoryId: -1,
                        });
                      })
                      .catch(() => {
                        showNotification({
                          color: "red",
                          message: "Oops, something went wrong.",
                        });
                      });
                  }}
                >
                  Create
                </Button>
              </Center>
            </Grid.Col>
          </Grid>
        </form>
      </Card>
      <Table>
        <thead>
          <tr>
            <th>Id</th>
            <th>Name</th>
            <th>Operations</th>
          </tr>
        </thead>
        <tbody>
          {categories.map((category) => (
            <tr key={category.categoryId}>
              <td>{category.categoryId}</td>
              <td>{category.categoryName}</td>
              <td>
                <Button
                  size="sm"
                  color="red"
                  data-testid={"category-list-" + category.categoryId}
                  onClick={() => {
                    deleteCategory(category.categoryId)
                      .then(() => {
                        getCategories();
                      })
                      .catch(() => {
                        showNotification({
                          color: "red",
                          message: "Oops! Something went wrong",
                        });
                      });
                  }}
                >
                  Delete
                </Button>
              </td>
            </tr>
          ))}
        </tbody>
      </Table>
      <Space h="xl" />

      <Pagination
        position="center"
        page={activePage}
        onChange={setPage}
        total={pageCount}
      />
    </Container>
  );
}
