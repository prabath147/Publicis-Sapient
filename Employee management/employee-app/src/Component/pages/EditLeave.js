import React, { useState, useEffect } from 'react';
import Navbar from '../element/Navbar';
import { useDispatch, useSelector } from "react-redux";
import { useParams} from "react-router-dom";
import { getLeave, updateLeave } from '../actions/postAction';
const EditLeave = () => {

  
  const dispatch = useDispatch();
  const leave = useSelector((state) => state.post.leave);
  const { id } = useParams();
  const [leaveId, setLeaveId] = useState("");
  const [lsd, setLsd] = useState("");
  const [led, setLed] = useState("");
  const [totalDays, setTotalDays] = useState("");
  const [status, setStatus] = useState("");

  

  useEffect(() => {
    loadPost();
  }, []);
  useEffect(() => {
    if (leave) {
      setLeaveId(leave.leaveId);
      setLsd(leave.leaveStartDate);
      setLed(leave.leaveEndDate);
      setTotalDays(leave.leaveNoOfDaysApplied);
      setStatus(leave.leaveStatus);
      
    }
  }, [leave]);
  const loadPost = () => {
   // const urlId=id.slice(1);
    dispatch(getLeave(id));
  };
  if(sessionStorage.getItem("role")!=3){
    return (
        <div>
        <Navbar/>
    <h1>UNAUTHORIZED</h1></div>
    );
}
  const submitForm = (e) => {
    e.preventDefault();
    const update_leave = {
      id: leaveId,
      leaveStatus:status
    };
    console.log(update_leave);
    dispatch(updateLeave(update_leave));
    
  };

    
    return (
        <div>
             <Navbar/>
            
            <div className="leave">
        <center>
      <form onSubmit={submitForm}>
        <h2>Leave Form</h2>
         <label htmlFor="leaveid">Leave ID : </label><br/>
        <input type="text" name="leaveid"  value={leaveId} required/>
        <br/><br/><br/><br/> 

        <label htmlFor="lsd">Leave Start Date:</label><br/>
        <input type="date" id="lsd" name="lsd" value={lsd} required/><br/><br/> <br/><br/>

        <label htmlFor="led">Leave End Date:</label><br/>
        <input type="date" id="led" name="led" value={led} required/><br/><br/><br/><br/>

        <label htmlFor="totalDays">No. of Days Applied : </label><br/>
        <input type="number" name="totalDays" id="totalDays" value={totalDays}/><br/><br/><br/><br/>

        <label htmlFor="status">Choose status:</label><br/>
                <select id="status" name="status" value={status} onChange={(e) => setStatus(e.target.value)} required>
                <option value="select">select</option>
                <option value="APPROVED">APPROVED</option>
                <option value="REJECTED">REJECTED</option>
                <option value="PENDING">PENDING</option>
                </select><br/><br/>

        <input type="submit" value="submit" /><br/><br/>

        
      </form>
    </center>
    </div>
        </div>
    );
};

export default EditLeave;