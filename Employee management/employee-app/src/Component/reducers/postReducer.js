import * as All from '../actions/types'


const initialState = {
  posts: [],
  post: null,
  user: null,
  userDetails:[],
  userDetail: null,
  leave: null,
  leaves:[]
};


export default (state = initialState, { type, payload }) => {
  switch (type) {
    case All.USER_CREATE_POST:
      console.log(payload);
      return {
        ...state,
        user: payload,
      };
      case All.USER_AUTHENTICATE_POST:
      console.log(payload);
      return {
        ...state,
        user: payload,
      };
      case All.USER_DETAIL_GET_DETAILS:
      return {
        ...state,
        userDetails: payload,
      };
      case All.USER_DETAIL_GET_DETAIL:
      return {
        ...state,
        userDetail: payload,
      };
    case All.USER_DETAIL_CREATE_POST:
      console.log(payload);
      return {
        ...state,
        userDetails: [payload, ...state.userDetails],
      };
      case All.USER_DETAIL_UPDATE_POST:
      return {
        ...state,
        userDetails: state.userDetails.map((postItem) =>
          postItem.id === payload.id ? payload : postItem
        ),
      };
      case All.LEAVE_GET_DETAIL:
      return {
        ...state,
        leave: payload,
      };
      case All.USER_LEAVE_GET_DETAILS:
      return {
        ...state,
        leaves: payload,
      };
      case All.MANAGER_LEAVE_GET_DETAILS:
      return {
        ...state,
        leaves: payload,
      };
      case All.USER_LEAVE_CREATE_POST:
      console.log(payload);
      return {
        ...state,
        leaves: [payload, ...state.leaves],
      };
      case All.USER_LEAVE_UPDATE_POST:
      return {
        ...state,
        leaves: state.leaves.map((postItem) =>
          postItem.id === payload.id ? payload : postItem
        ),
      };

    
    default:
      return state;
  }
};



