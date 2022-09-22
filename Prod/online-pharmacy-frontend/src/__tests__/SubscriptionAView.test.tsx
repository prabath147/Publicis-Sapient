import { render, screen } from '@testing-library/react';
import { Provider } from 'react-redux';
import { BrowserRouter } from 'react-router-dom';
import { store } from '../app/store';
import { Subscriptionv } from '../components/pages/AdminSubscriptionPack/models';
import SubscriptionView from '../components/pages/AdminSubscriptionPack/SubscriptionView';
import UserReducer, { loadData, SubscriptionState } from '../components/pages/AdminSubscriptionPack/SubscriptionvSlice';


describe('SubscriptionView', () => {

    it('component created', () => {
        const component = render(
            <Provider store={store}>
                <BrowserRouter>
                    <SubscriptionView />
                </BrowserRouter>
            </Provider>
        )
        expect(screen).toMatchSnapshot();
    })

    it('component data loaded', () => {
        const component = render(
            <Provider store={store}>
                <BrowserRouter>
                    <SubscriptionView />
                </BrowserRouter>
            </Provider>
        )
        expect(screen).toMatchSnapshot();
    })

    it('test fetch', async () => {
        const data = {
            data: [
                {
                    subscriptionId: 1,

                    subscriptionName: "seventh offer",

                    description: "seventh subscription",

                    startDate: new Date(),

                    endDate: new Date(),

                    subscriptionCost: 999.0,

                    benefits: {

                        "delivery_discount": 50.0,

                        "one_day_delivery": false

                    },

                    period: 10,

                    status: "ACTIVE",
                },
            ],
        };
    });

    const initialState: SubscriptionState = {
        subscriptionv: [],
    };

    it("should handle initial state", () => {
        expect(UserReducer(undefined, { type: "unknown" })).toEqual({
            subscriptionv: [],
        });
    });

    it("should handle data load", () => {
        const data: Subscriptionv[] = [
            {
                subscriptionId: 1,

                subscriptionName: "seventh offer",

                description: "seventh subscription",

                startDate: new Date(),

                endDate: new Date(),

                subscriptionCost: 999.0,

                benefits: {

                    "delivery_discount": 50.0,

                    "one_day_delivery": false

                },

                period: 10,

                status: "ACTIVE",
            },
        ];
        const actual = UserReducer(initialState, loadData(data));
        expect(actual.subscriptionv).toEqual(data);
    });


})