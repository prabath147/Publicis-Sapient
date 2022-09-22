import { Autocomplete, CloseButton, Modal, useMantineTheme } from '@mantine/core';
import { useForm } from '@mantine/form';
import { showNotification } from '@mantine/notifications';
import { IconSearch } from '@tabler/icons';
import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAppSelector } from '../../../../app/hooks';
import { getManagerStoreData } from '../ManagerSlice';
import { getItemAPI, getSuggestions } from './InventoryAPI';


interface ModalStateType {
  openSearchBar: boolean,
  setOpenSearchBar: (boolean) => void
}
// interface Product {
//   productId: number;
//   productName: string;
//   proprietaryName: string;
//   description: string;
//   dosageForm: string;
//   categorySet: Array<string>;
//   productType: boolean;
//   imageUrl: string;
// }
function InventorySearchBar({ openSearchBar, setOpenSearchBar }: ModalStateType) {
  const theme = useMantineTheme();
  const navigate = useNavigate();
  const { store } = useAppSelector(getManagerStoreData);
  const [productDataArray, setProductDataArray] = useState<string[]>([]);
  const form = useForm({
    initialValues: {
      productName: ''
    }

  });

  //   const fetchProductByName = (productName) => {
  //     getItemAPI(productName).then(response => {
  //         console.log(response.data.content[0]);



  //     }).catch(error => {
  //         console.log(error);

  //     })
  // }


  const handleProductSearch = (values) => {
    getItemAPI(values.productName, store.storeId + "").then(response => {
      const itemList = response.data.content;
      console.log(response.data.content);
      if (itemList.length === 1) {
        navigate(`/manager/store/${store.storeId}/item/${itemList[0].itemId}`);
      }
      else if (itemList.length > 1) {
        setProductDataArray(itemList.map(pdt => pdt.productName));
      } else {
        showNotification({
          message: "Item not found in store!",
          color: "red"
        });
      }
      // setProductData(response.data.content[0]); 
      // console.log(productData); 
      // if (itemData.product.productName !== values.productName) {
      //   showNotification({
      //     message: "Item not found!",
      //     color: "red"
      //   });
      //   closeModal();
      // }


    }).catch(error => {
      console.log(error);

    })

    // navigate(`/manager/store/${store.storeId}/item/${65}`);
  }
  const handleKeys = (event) => {
    if (
      (event.keyCode >= 65 && event.keyCode <= 90) ||
      (event.keyCode >= 97 && event.keyCode <= 122)
    ) {
      console.log(event.key);
      console.log(form.values.productName + event.key);
      getSuggestions(form.values.productName + event.key).then(response => {
        setProductDataArray(response.data.map(pdtName => pdtName));
        // setOpenList(true);
      }).catch(error => {
        console.log(error);

      })
      // getSuggestions(form.values.productName + event.key).then(response => {
      //     setProductDataArray(response.data);
      //     // setOpenList(true);
      // }).catch(error => {
      //     console.log(error);

      // })

    }



  }
  const closeModal = () => {
    setOpenSearchBar(false);
    setProductDataArray([]);
    form.reset();
  }
  return (
    <div>
      <Modal
        withCloseButton={false}
        opened={openSearchBar}
        onClose={closeModal}
        overlayColor={theme.colorScheme === 'dark' ? theme.colors.dark[9] : theme.colors.gray[2]}
        overlayOpacity={0.55}
        overlayBlur={3}
        padding='xs'
      >
        <form onSubmit={form.onSubmit(handleProductSearch)}>
          {/* <TextInput
            icon={<IconSearch />}
            placeholder="product name"
            variant='unstyled'
            {...form.getInputProps('productName')}
            onKeyDownCapture={(e) => handleKeys(e)}
          /> */}
          <Autocomplete
            icon={<IconSearch />}
            placeholder="product name"
            variant='unstyled'
            {...form.getInputProps('productName')}
            onKeyDownCapture={(e) => handleKeys(e)}
            data={productDataArray}
            rightSection={<CloseButton aria-label="Close modal" onClick={closeModal} />}
          />
        </form>
        {/* <Menu shadow="md" width={300} closeOnClickOutside={true} opened={openList} onChange={setOpenList}>

          {productDataArray?.length > 0 && (<Menu.Dropdown>
            {productDataArray?.map(pdtName => (
              <Menu.Item key={pdtName} onClick={() => {
                handleProductSearch(pdtName);
                // setProductSearchValues(pdt);
                setOpenList(false);
              }}>{pdtName}</Menu.Item>
            ))}
          </Menu.Dropdown>)}
        </Menu> */}
      </Modal>
    </div>
  )
}

export default InventorySearchBar