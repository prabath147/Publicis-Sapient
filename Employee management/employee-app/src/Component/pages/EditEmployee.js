import React, { useState } from 'react';
import Navbar from '../element/Navbar';
import { useDispatch } from "react-redux";
import { updateUserDetail } from '../actions/postAction';
import './Leave.css';

const EditEmployee = () => {

  const dispatch = useDispatch();
    
  const [userid, setUserid] = useState("");
  const [roleid, setRoleid] = useState("");
  const [gradeCode, setGradeCode] = useState("");
  
  if(sessionStorage.getItem("role")!=4){
    return (
        <div>
        <Navbar/>
    <h1>UNAUTHORIZED</h1></div>
    );
}
  
  const submitForm = (e) => {
    e.preventDefault();
    console.log(roleid);
    const update_details = {
      userId:{
        userId:userid,
        roleId:{
          roleId:roleid
        }
      },
      gradeId:{
        id:gradeCode
      }
      
    };
    console.log(update_details);
    dispatch(updateUserDetail(update_details));
    
  };
    return (
        <div>
             <Navbar/>
    <form onSubmit={submitForm} >

           <div className="leave">
                Personal Details<br/><br/>
                
                <label htmlFor="userid">User ID : </label><br/>
                <input type="text" name="userid" id="userid" pattern="[0-9]{6}" onChange={(e) => setUserid(e.target.value)} required/>
                <br/><br/>

              <br/><br/>
              
                Role Details<br/><br/>
                <label htmlFor="roleid">Role Id: </label><br/>
                <select name="roleid" id="roleid" onChange={(e) => setRoleid(e.target.value)} required>
                  <option value="1">select</option>
                <option value="2">Employee</option>
                <option value="3">Manager</option>
                <option value="4">Admin</option>                
                </select><br/><br/>

              <br/><br/>
              
                Grade Details<br/><br/>
                <label htmlFor="gradeCode">Choose code:</label><br/>
                <select id="gradeCode" name="gradeCode" onChange={(e) => setGradeCode(e.target.value)}  required>
                <option value="select">select</option>
                <option value="M1">M1</option>
                <option value="M2">M2</option>
                <option value="M3">M3</option>
                <option value="M4">M4</option>
                <option value="M5">M5</option>
                <option value="M6">M6</option>
                <option value="M7">M7</option>
                </select><br/><br/>

        <center>
        <input type="submit" value="submit" />
        </center>
		</div>
    
	</form>
        </div>
    );
};

export default EditEmployee;