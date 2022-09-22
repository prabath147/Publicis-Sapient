import React, { useState} from "react";
import { useSelector, useDispatch } from "react-redux";
import {  getUserLeave} from '../actions/postAction';
import Navbar from '../element/Navbar';
import './LeaveApproval.css';
const LeaveStatus = () => {

    
    const dispatch = useDispatch();
    const leaves = useSelector((state) => state.post.leaves);
    const [userid, setUserid] = useState("");

    if(sessionStorage.getItem("role")==1){
        return (
            <div>
            <Navbar/>
        <h1>UNAUTHORIZED</h1></div>
        );
    }
    const submitForm = (e) => {
        e.preventDefault();
        console.log(userid);
    
        dispatch(getUserLeave(userid));
        // history.push("/");
      };

    return (
        <div>
            <Navbar/>
             
      <form onSubmit={submitForm} >

               <br/><br/>
                
                <label htmlFor="userId">User ID : </label><br/>
                <input type="text" name="userId" pattern="[0-9]{6}" onChange={(e) => setUserid(e.target.value)} required/>

                <input type="submit" value="submit"/>
                <br/><br/>
                </form><br/><br/>
    <div className="leave-approval">
        <table>
            <thead>
                <tr>
                    <th>Leave ID</th>
                    <th>User ID</th>
                    <th>Leave Start Date</th>
                    <th>Leave End Date</th>
                    <th>No. of Days Applied</th>
                    <th>Approval Status</th>
                    <th></th>
                    <th></th>
                </tr>
            </thead>
            <tbody id="bodyoutput">
            {leaves.map((leavesItem)=>(
                <tr key={leavesItem.leaveId}>
                  <td>{leavesItem.leaveId}</td>
                    <td>{leavesItem.userLeaveId.userId}</td>
                    <td>{leavesItem.leaveStartDate}</td>
                    <td>{leavesItem.leaveEndDate}</td>                    
                    
                    <td>{leavesItem.leaveNoOfDaysApplied}</td>
                    <td>{leavesItem.leaveStatus}</td>

                </tr>
            ))}
        </tbody>
        </table>
    </div>
        </div>
    );
};

export default LeaveStatus;