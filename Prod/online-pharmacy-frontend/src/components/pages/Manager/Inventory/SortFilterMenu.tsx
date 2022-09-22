import { ActionIcon, Menu } from '@mantine/core';
import { IconAdjustments, IconArrowNarrowDown, IconArrowNarrowUp, IconBan, IconBox, IconBuildingFactory2, IconCurrencyDollar } from '@tabler/icons';

export default function SortFilterMenu({ keyword, order, setOrder, setKeyword }) {


  const toggleOrder = () => {
    setOrder(o => !o);
  }

  const handleClick = (word) => {
    setKeyword(word);
    toggleOrder();
  }

  return (
    <Menu shadow="md" width={200} position="bottom">
      <Menu.Target>
        <ActionIcon variant='subtle' color={"blue"}>
          <IconAdjustments size={26} />
        </ActionIcon>
        {/* <Button compact radius={"xl"} variant='subtle' leftIcon={<IconAdjustments />}></Button> */}
      </Menu.Target>

      <Menu.Dropdown>
        <Menu.Label>Sort</Menu.Label>
        <Menu.Item data-testid="menu1" icon={<IconBuildingFactory2 size={14} />} onClick={() => handleClick("manufacturedDate")} rightSection={(keyword === "manufacturedDate") && (order ? <IconArrowNarrowUp size={13} /> : <IconArrowNarrowDown size={13} />)}>Manufacturing Date</Menu.Item>
        <Menu.Item data-testid="menu2" icon={<IconBan size={14} />} onClick={() => handleClick("expiryDate")} rightSection={(keyword === "expiryDate") && (order ? <IconArrowNarrowUp size={13} /> : <IconArrowNarrowDown size={13} />)}>Expiry Date</Menu.Item>
        <Menu.Item data-testid="menu3" icon={<IconBox size={14} />} onClick={() => handleClick("itemQuantity")} rightSection={(keyword === "itemQuantity") && (order ? <IconArrowNarrowUp size={13} /> : <IconArrowNarrowDown size={13} />)}>Quantity</Menu.Item>
        <Menu.Item data-testid="menu4" icon={<IconCurrencyDollar size={14} />} onClick={() => handleClick("price")} rightSection={(keyword === "price") && (order ? <IconArrowNarrowUp size={13} /> : <IconArrowNarrowDown size={13} />)}>Price</Menu.Item>


        {/* <Menu.Divider />

        <Menu.Label>Filter</Menu.Label>
        <Menu.Item icon={<IconShoppingCart size={14} />}>In Stock</Menu.Item>
        <Menu.Item color="red" icon={<IconShoppingCartOff size={14} />}>Out of Stock</Menu.Item> */}
      </Menu.Dropdown>
    </Menu>
  );
}