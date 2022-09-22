import React, { useState } from "react";
import { useSelector, useDispatch } from "react-redux";
import { getLeaveByManager } from '../actions/postAction';
import Navbar from '../element/Navbar';
import './LeaveApproval.css';
import { Link } from "react-router-dom";

const LeaveApproval = () => {

    const dispatch = useDispatch();
    const leaves = useSelector((state) => state.post.leaves);
    const [userid, setUserid] = useState("");
    // useEffect(() => {
    //     //dispatch(getLeaveByManager(id));
        
        
    // }, []);
    if(sessionStorage.getItem("role")!=3){
        return (
            <div>
            <Navbar/>
        <h1>UNAUTHORIZED</h1></div>
        );
    }
    const submitForm = (e) => {
        e.preventDefault();
        console.log(userid);
    
        dispatch(getLeaveByManager(userid));
        // history.push("/");
      };

    return (
        <div>
            <Navbar/>
             
      <form onSubmit={submitForm} >

               <br/><br/>
                
                <label htmlFor="managerId">Manager ID : </label><br/>
                <input type="text" name="managerId" pattern="[0-9]{6}" onChange={(e) => setUserid(e.target.value)} required/>

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
                    <th>Edit Status</th>
                    
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
                    <td><Link to={`/leave/edit/${leavesItem.leaveId}`}>edit
                    </Link></td>
                </tr>
            ))}
        </tbody>
        </table>
    </div>
        </div>
    );
};

export default LeaveApproval;