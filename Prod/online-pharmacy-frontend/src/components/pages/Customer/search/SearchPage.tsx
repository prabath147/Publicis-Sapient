import { faArrowsLeftRight, faFilter } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import {
  ActionIcon,
  Autocomplete,
  Button,
  Container,
  Drawer,
  Grid,
  NumberInput,
  Pagination,
  Radio,
  Space,
  Switch,
  Text
} from "@mantine/core";
import { showNotification } from "@mantine/notifications";
import { IconArrowRight, IconSearch } from "@tabler/icons";
import { useEffect, useState } from "react";
import { useAppSelector } from "../../../../app/hooks";
import LoadingComponent from "../../../ui/LoadingComponent/LoadingComponent";
import { getCartItems } from "../../Cart/CartSlice";
import { Item } from "./Itemobjs";
import ProductCard from "./ProductCard";
import { getAllItems, getItemsAPI, getSuggestions } from "./SearchAPI";

export default function SearchPage() {
  const [itemL, setitemL] = useState([]);

  // const itemL = useAppSelector(selectItems);
  // const dispatch = useAppDispatch();

  const cartItems = useAppSelector(getCartItems);

  // console.log(cartItems);

  const cartItemIds = cartItems.map((item) => item.itemIdFk || -1);

  // console.log(cartItems);

  const [keyword, setKeyword] = useState("");
  const [suggestions, setSuggestions] = useState([]);
  const [buttonkey, setButtonkey] = useState("");

  const [firstRender, setFirstRender] = useState(true);

  const [loading, setLoading] = useState(false); // for loading skeleton
  const [outerLoading, setOuterLoading] = useState(true); // for loading
  // const navigate = useNavigate();

  const [jumpPage, setJumpPage] = useState(1);

  const [headerStatus, setHeaderStatus] = useState(true);

  //For Pagination
  const [pageCount, setPageCount] = useState(1);
  const [activePage, setPage] = useState(1);

  const [opened, setOpened] = useState(false); //for the filters drawer

  const [checked, setChecked] = useState(true); // Toggle for Generic
  const [value, setValue] = useState("asc"); // value --> asc or desc
  const [fromToggle, setFromToggle] = useState(false);

  useEffect(() => {
    loadDataAction();
    setFirstRender(false);

    // console.log(firstRender, "done fro use Effect");
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [activePage, fromToggle]);

  const handleClick = () => {
    keyword
      ? activePage === 1
        ? loadDataAction()
        : setPage(1)
      : showNotification({
        message: "Type Something in the search Box to Search",
        color: "blue",
      });
  };

  // const handleToggleSwitch = () => {
  //   setChecked(!checked);
  //   console.log("inside switch", !checked);
  //   handleClick();
  // };

  const enterPressed = (event) => {
    // const code = event.keyCode || event.which;
    if (event.key === "Enter") {
      //13 is the enter keycode
      // setPage(1);

      handleClick();
    }
  };

  const handleKeys = (event) => {
    if (event.keyCode >= 65 && event.keyCode <= 90)
      loadSuggestions(keyword + event.key);

    // console.log(event.key);
    // console.log(keyword + event.key);
  };

  const loadSuggestions = (kword) => {
    getSuggestions(kword)
      .then((response) => {
        setSuggestions(response.data);
        console.log("Suggestions", response, kword);
      })
      .catch((error) => {
        // console.log(error);
        showNotification({
          message: "Oops, something went wrong!",
          color: "red",
        });
      });
  };

  const jumpTo = (val) => {
    setJumpPage(val);
  };
  const handleJump = () => {
    setPage(jumpPage);
  };

  const loadDataAction = async () => {
    try {
      // console.log(activePage, pageCount);
      setLoading(true);

      console.log(keyword);
      console.log(checked);

      const response = await (firstRender || keyword === ""
        ? getAllItems(checked, value, activePage - 1)
        : getItemsAPI(keyword, activePage - 1, value, checked));

      // const response = await getItemsAPI(
      //   keyword,
      //   activePage - 1,
      //   value,
      //   checked
      // );
      console.log(response);
      // if (response.data.pageable.pageNumber > response.data.totalPages)
      //   setPage(1);
      setitemL(response.data.content);
      setButtonkey(keyword);
      setPageCount(response.data.totalPages);
      if (keyword) setHeaderStatus(false);

      // console.log(pageCount);

      //  console.log(firstRender);
      setLoading(false);
      setOuterLoading(false);
    } catch (error) {
      showNotification({ message: "Oops! Something went wrong", color: "red" });
      // alert(error);
    }
  };

  return outerLoading ? (<Container data-testid="spinner">
    <Grid>
      <Grid.Col>
        <LoadingComponent />
      </Grid.Col>
    </Grid>
  </Container>) : (
    <Container data-testid="searchT">
      <Drawer
        data-testid="drawerT"
        opened={opened}
        onClose={() => setOpened(false)}
        title="Select Filters"
        padding="xl"
        size="sm"
        position="left"
      >
        <div>
          <Switch
            data-testid="toggleT"
            checked={checked}
            onChange={(event) => setChecked(event.currentTarget.checked)}
            onLabel="Yes"
            offLabel="No"
            size="lg"
            label="Generic"
            color="green"
          />
        </div>
        <br></br>
        <div>
          <Radio.Group
            data-testid="radioT"
            label="Sort by Price"
            spacing="lg"
            value={value}
            onChange={setValue}
          >
            <Radio value="asc" label="Low to High" />
            <Radio value="desc" label="High to Low" />
          </Radio.Group>
        </div>
        <br />
        <br />
        <div>
          <Button
            variant="light"
            fullWidth
            onClick={() => {
              setOpened(false);
              loadDataAction();
            }}
          >
            Apply Filters
          </Button>
        </div>
      </Drawer>
      {/* filter button and search bar */}
      <Grid>
        <Grid.Col span={2}>
          <ActionIcon
            mt={"2px"}
            sx={{ border: "solid #228be6 2px", borderRadius: "10px" }}
            title="filterButton"
            name="filterButton"
            color="blue"
            size="xl"
            radius="xs"
            variant="light"
            onClick={() => setOpened(true)}
          >
            <FontAwesomeIcon icon={faFilter} size="lg" />
          </ActionIcon>
        </Grid.Col>

        <Grid.Col span={8}>
          <Autocomplete
            data-testid="sinputT"
            value={keyword}
            sx={{ border: "solid #228be6 2px", borderRadius: "10px" }}
            icon={<IconSearch size={18} stroke={1.5} />}
            radius="md"
            size="sm"
            placeholder="Search..."
            rightSection={
              <ActionIcon
                title="SearchGoButton"
                name="SearchGoButton"
                size={33}
                radius="md"
                color={"green"}
                variant="filled"
                onClick={() => {
                  // setPage(1);
                  handleClick();
                }}
              >
                <IconArrowRight size={18} stroke={1.5} />
              </ActionIcon>
            }
            onChange={setKeyword}
            onKeyDown={enterPressed}
            onKeyDownCapture={(e) => handleKeys(e)}
            rightSectionWidth={36.5}
            data={suggestions}
          />

          {/* <TextInput
            sx={{ border: "solid #228be6 2px", borderRadius: "10px" }}
            icon={<IconSearch size={18} stroke={1.5} />}
            value={keyword}
            radius="md"
            size="md"
            rightSection={
              <ActionIcon
                title="SearchGoButton"
                name="SearchGoButton"
                size={41.5}
                radius="md"
                color={"green"}
                variant="filled"
                onClick={() => handleClick()}
              >
                {theme.dir === "ltr" ? (
                  <IconArrowRight size={18} stroke={1.5} />
                ) : (
                  <IconArrowLeft size={18} stroke={1.5} />
                )}
              </ActionIcon>
            }
            variant="default"
            placeholder="Search..."
            rightSectionWidth={42}
            onKeyDown={enterPressed}
            onChange={(e) => setKeyword(e.target.value)}
            {...props}
          /> */}
        </Grid.Col>
      </Grid>
      <Space h={"md"} />
      <div>
        {itemL.length ? (
          <Text data-testid="showR" size="lg" hidden={headerStatus}>
            Showing Results for &nbsp;: &nbsp; <strong>{buttonkey}</strong>{" "}
          </Text>
        ) : (
          // suggestions.includes(tempKeyword) ?
          <Text data-testid="showR" size="lg" hidden={headerStatus}>
            No Item Found for &nbsp;: &nbsp; <strong>{buttonkey}</strong>{" "}
            <Text
              data-testid="toggleLink"
              sx={{ float: "right", cursor: "pointer" }}
              variant="link"
              onClick={() => {
                // setOpened(true);
                // console.log(checked);
                setChecked(!checked);
                setFromToggle(!fromToggle);
                // console.log(checked);
                // loadDataAction();
              }}
            >
              Toggle {checked ? "Generic" : "Non-Generic"} Filters
            </Text>
          </Text>
        )}
      </div>

      <Space h={"md"} />

      <Grid gutter="xl" align={"center"}>
        {itemL.map((item: Item) => (
          <Grid.Col xs={6} key={item.itemId}>
            <ProductCard
              item={item}
              loading={loading}
              // cartItems={ }
              added={cartItemIds.includes(item.itemId)}
            />
          </Grid.Col>
        ))}
      </Grid>

      <br></br>
      <Container>
        <Grid>
          <Grid.Col xs={12}>
            <Pagination
              data-testid="paginationT"
              position="center"
              page={activePage}
              onChange={setPage}
              total={pageCount}
            />
          </Grid.Col>
        </Grid>

        <Grid>
          <Grid.Col span={5}></Grid.Col>
          <Grid.Col span={7}>
            <Container>
              <NumberInput
                hideControls
                placeholder="Jump To..."
                onChange={(e) => jumpTo(e)}
                max={pageCount}
                min={1}
                step={2}
                sx={{
                  input: { width: 130, textAlign: "center" },
                  float: "left",
                }}
                rightSection={
                  <ActionIcon
                    title="Jump"
                    name="jump"
                    radius="sm"
                    color={"blue"}
                    variant="filled"
                    onClick={handleJump}
                    sx={{ height: "32px", width: "37px" }}
                  >
                    <FontAwesomeIcon icon={faArrowsLeftRight} size="sm" />
                  </ActionIcon>
                }
                rightSectionWidth={40}
              />
            </Container>
          </Grid.Col>
        </Grid>
      </Container>
    </Container>

  );
}
