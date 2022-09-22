import './EmployeeRecords.css';
import React, { useEffect } from "react";
import { useSelector, useDispatch } from "react-redux";
import { getUserDetails } from '../actions/postAction';
import moment from 'moment';
import Navbar from '../element/Navbar';
const EmployeeRecords = () => {

    const dispatch = useDispatch();
    const userDetails = useSelector((state) => state.post.userDetails);
    const user = useSelector((state) => state.post.user);

    useEffect(() => {
        dispatch(getUserDetails());
        console.log(user);
    }, []);

    if(sessionStorage.getItem("role")==1){
        return (
            <div>
            <Navbar/>
        <h1>UNAUTHORIZED</h1></div>
        );
    }

    return (
        <div>
            <Navbar/>
            
    <div className="employee-record">
        <table>
            <thead>
                <tr>
                    
                    <th>User ID</th>
                    <th>First Name</th>
                    <th>Last Name</th>
                    <th>Email</th>
                    <th>Date Of Birth</th>
                    <th>Manager ID</th>
                    <th>Phone No.</th>
                    <th>Grade ID</th>
                </tr>
            </thead>
            <tbody id="bodyoutput">
                
            {userDetails.map((userDetailItem)=>(
                <tr key={userDetailItem.id}>
                  <td>{userDetailItem.userId.userId}</td>
                    <td>{userDetailItem.userFirstname}</td>
                    <td>{userDetailItem.userLastname}</td>
                    <td>{userDetailItem.userEmail}</td>
                    <td>{moment(userDetailItem.userDob).format('DD-MM-YYYY')}</td>
                    
                    <td>{userDetailItem.managerId.userId}</td>
                    <td>{userDetailItem.userPhoneno}</td>
                    <td>{userDetailItem.gradeId.id}</td>
                </tr>
            ))}
        </tbody>
        </table>
    </div>
        </div>
    );
};

export default EmployeeRecords;