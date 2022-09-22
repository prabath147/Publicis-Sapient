import * as All from './types';
import axios from "axios";


// here we will be putting our base url for data
// get all posts
export const getUserAuth = (user) => async (dispatch) => {
  const result = await axios.get(
    `http://localhost:8081/user/authenticate/${user.username}/${user.password}`
    
  );
  
  dispatch({
    type: All.USER_AUTHENTICATE_POST,
    payload: result.data,
  });
};

export const createUser = (user) => async (dispatch) => {
  const result = await axios.post(
    "http://localhost:8081/user/user",
    user
  );
  dispatch({
    type: All.USER_CREATE_POST,
    payload: result.data,
  });
};

export const getUserDetails = () => async (dispatch) => {
  const result = await axios.get("http://localhost:8081/userdetails/data");
  dispatch({
    type: All.USER_DETAIL_GET_DETAILS,
    payload: result.data,
  });
};

// get a post
export const getUserDetail = (id) => async (dispatch) => {
  const result = await axios.get(
    `http://localhost:8081/userdetails/user/${id}`
  );
    console.log(result.data);
    console.log(id);
  dispatch({
    type: All.USER_DETAIL_GET_DETAIL,
    payload: result.data,
  });
};

export const createUserDetail = (userDetail) => async (dispatch) => {
  const result = await axios.post(
    "http://localhost:8081/userdetails/userdata",
    userDetail
  );
  dispatch({
    type: All.USER_DETAIL_CREATE_POST,
    payload: result.data,
  });
};

export const updateUserDetail = (userDetail) => async (dispatch) => {
  const result = await axios.put(
    `http://localhost:8081/userdetails/userData`,
    userDetail
  );
  dispatch({
    type: All.USER_DETAIL_UPDATE_POST,
    payload: result.data,
  });
};

export const getUserLeave = (id) => async (dispatch) => {
  const result = await axios.get(
    `http://localhost:8081/leave/user/${id}`
  );
    console.log(result.data);
    console.log(id);
  dispatch({
    type: All.USER_LEAVE_GET_DETAILS,
    payload: result.data,
  });
};

export const getLeave = (id) => async (dispatch) => {
  const result = await axios.get(
    `http://localhost:8081/leave/leave/${id}`
  );
    console.log(result.data);
    console.log(id);
  dispatch({
    type: All.LEAVE_GET_DETAIL,
    payload: result.data,
  });
};

export const createUserLeave = (leave) => async (dispatch) => {
  const result = await axios.post(
    "http://localhost:8081/leave/user",
    leave
  );
  dispatch({
    type: All.USER_LEAVE_CREATE_POST,
    payload: result.data,
  });
};

export const getLeaveByManager = (id) => async (dispatch) => {
  const result = await axios.get(
    `http://localhost:8081/leave/manager/${id}`
  );
    console.log(result.data);
    console.log(id);
  dispatch({
    type: All.MANAGER_LEAVE_GET_DETAILS,
    payload: result.data,
  });
};

export const updateLeave = (leave) => async (dispatch) => {
  const result = await axios.put(
    `http://localhost:8081/leave/leave/${leave.id}`,
    leave
  );
  dispatch({
    type: All.USER_LEAVE_UPDATE_POST,
    payload: result.data,
  });
};
// create a post

