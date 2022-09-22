import { Provider } from 'react-redux';
import renderer from 'react-test-renderer';
import { store } from '../../app/store';
import { Store } from '../../components/pages/Admin/storeList/models';
import StoreList from '../../components/pages/Admin/storeList/StoreList';
import UserReducer, { loadStoreData, StoresState } from "../../components/pages/Admin/storeList/StoreSlice";

it('component created', () => {
    const component = renderer.create(
        <Provider store={store}>
            <StoreList />
        </Provider>
    )
    const tree = component.toJSON();
    expect(tree).toMatchSnapshot();
})

it('component data loaded', () => {
    const component = renderer.create(
        <Provider store={store}>
            <StoreList />
        </Provider>
    )
    const tree = component.toJSON();
    expect(tree).toMatchSnapshot();
})

it('test fetch', async () => {
    const data = {
        data: [
            {
                id: 1,
                store_manger_id: 101,
                store_name: "pharma",
                address: 'xyz..',
                reg_date: new Date("2022-08-16"),
                store_desc: "abc...",
            },
        ],
    };
});

const initialState: StoresState = {
    stores: [],
};

it("should handle initial state", () => {
    expect(UserReducer(undefined, { type: "unknown" })).toEqual({
        stores: [],
    });
});


it("should handle data load", () => {
    const data: Store[] = [
        {
            storeId: 1,
            managerId: 101,
            storeName: "pharma",
            address: "xyz..",
            createdDate: new Date("2022-08-16"),
            revenue: 2000
        },
    ];
    const actual = UserReducer(initialState, loadStoreData(data));
    expect(actual.stores).toEqual(data);
});
