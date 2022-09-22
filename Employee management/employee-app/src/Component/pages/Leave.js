import React, { useState } from 'react';
import Navbar from '../element/Navbar';
import { useDispatch } from "react-redux";
import { createUserLeave } from '../actions/postAction';
import './Leave.css';

const Leave = () => {

    
    const dispatch = useDispatch();
    
    const [userid, setUserid] = useState("");
    const [lsd, setLsd] = useState("");
    const [led, setLed] = useState("");
    const [totalDays, setTotalDays] = useState();
    

    
    if(sessionStorage.getItem("role")==1){
      return (
          <div>
          <Navbar/>
      <h1>UNAUTHORIZED</h1></div>
      );
  }
  
  function validateData(lsd,led,totalDays){
    lsd=new Date(lsd);
    led=new Date(led);
    
    const diffTime = Math.abs(led - lsd);
    const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24)); 
    
    if(lsd>led){
        return false;
    }
    else if(diffDays!=totalDays){
        return false;
    }
}

function validateForm(){
    
    var lsd = document.getElementById("lsd").value;
    var led = document.getElementById("led").value;
    var totalDays = document.getElementById("totalDays").value;
    
    if(validateData(lsd,led,totalDays)==false){
        alert("Please check leave start date , end date and number of days");
        return false;
    }
}

    const submitForm = (e) => {
        
      if(validateForm()==false){
        
        e.preventDefault();
      }
      else{
      const create_leave = {
        userLeaveId:{
            userId:userid
        },
        leaveStartDate:lsd,
        leaveEndDate:led,
        leaveNoOfDaysApplied:totalDays
      };
      console.log(create_leave);
      dispatch(createUserLeave(create_leave));
    }
    };

    return (
        <div>
            <Navbar/>
            
            <div className="leave">
        <center>
      <form onSubmit={submitForm} >
        <h2>Leave Form</h2>
         <label htmlFor="userid">User ID : </label><br/>
        <input type="text" name="userid" id="userid" pattern="[0-9]{6}" onChange={(e) => setUserid(e.target.value)} required/>
        <br/><br/><br/><br/> 

        <label htmlFor="lsd">Leave Start Date:</label><br/>
        <input type="date" id="lsd" name="lsd" onChange={(e) => setLsd(e.target.value)} required/><br/><br/> <br/><br/>

        <label htmlFor="led">Leave End Date:</label><br/>
        <input type="date" id="led" name="led" onChange={(e) => setLed(e.target.value)} required/><br/><br/><br/><br/>

        <label htmlFor="totalDays">No. of Days Applied : </label><br/>
        <input type="number" name="totalDays" id="totalDays" onChange={(e) => setTotalDays(e.target.value)} required/><br/><br/><br/><br/>

        <input type="submit" value="submit" /><br/><br/>

        
      </form>
    </center>
    </div>
    <div className='blockoutput' id="blockoutput">
      <table>
          <thead>
              <tr>
                  <th>Leave ID</th>
                  <th>User ID</th>
                  <th>Leave Balance</th>
                  <th>Leave Start Date</th>
                  <th>Leave End Date</th>
                  <th>No. of Days Applied</th>
                  <th>Approval Status</th>
              </tr>
          </thead>
          <tbody id="bodyoutput">
         
      </tbody>
      </table>
  </div>
        </div>
    );
};

export default Leave;