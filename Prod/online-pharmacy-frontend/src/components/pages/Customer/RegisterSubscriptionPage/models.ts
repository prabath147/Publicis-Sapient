
export interface RegisterSubscription {
    subscriptionId: number,
    subscriptionName: string,
    description: string,
    startDate: Date,
    endDate: Date,
    benefits: {
        delivery_discount: number,
        one_day_delivery: boolean, //enable-disable
    },
    subscriptionCost: number,
    period: number,
    status: string //active-expired
}

export interface Unsubscribe {
    user_id: number,
    subscription_id: string
}

export interface Subscriber {
    userId: number,
    userSubsSet: UserSubs[]
}

export interface UserSubs {
    userSubId: number,
    userId: number,
    subscriptions: RegisterSubscription,
    lastPaidDate: Date,
    subEndDate: Date,
    status: string
}