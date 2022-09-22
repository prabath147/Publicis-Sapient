export interface Subscriptionv{
    subscriptionId:number,
    subscriptionName:string,
    description:string,
    startDate:Date,
    endDate:Date,
    benefits:Benefit
    subscriptionCost:number,
    period:number,
    status:string
}

export interface Benefit{
    delivery_discount:number,
    one_day_delivery:boolean
}
export interface SubDetails{
    subscriptionName:string,
    description:string,
    startDate:Date,
    endDate:Date,
    benefits:Benefit,
    subscriptionCost:number,
    period:number
}

export interface SubRequest{
    name:string,
    description:string,
    stDate:Date,
    edDate:Date,
    deliveryDiscount:number,
    oneDayDelivery:string,
    cost:number,
    period:number
}