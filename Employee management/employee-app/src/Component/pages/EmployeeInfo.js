import React, { useState } from 'react';
import Navbar from '../element/Navbar';
import { useDispatch } from "react-redux";
import { createUserDetail } from '../actions/postAction';
import './EmployeeInfo.css';
const EmployeeInfo = () => {
  const dispatch = useDispatch();
  const [data, setData] = useState({});
  const [address,setAddress]= useState([]);

  if(sessionStorage.getItem("role")!=4){
    return (
        <div>
        <Navbar/>
    <h1>UNAUTHORIZED</h1></div>
    );
}

  const handleChange= e=>{
    e.persist();
    console.log(address);
    console.log([e.target.name], e.target.value);
        setAddress((address)=>(
            {
                ...address,
                [e.target.name]: e.target.value
            }
        ));  
}
 
  const submitForm = (e) => {
    e.preventDefault();
     const formData = new FormData(e.target);
     
     var user={};
     var manager={};
    var dept={};
    var grade={};
    var role={};
    
     for (let [key, value] of formData.entries()) {

      if(key==='userId'){
        user[key]=value;
      }
      else if(key==='managerId'){
        manager['userId']=value;
      }
      else if(key==='roleId'){
        role[key]=value;
      }
      else if(key==='deptId'){
        dept[key]=value;
      }
      else if(key==='gradeCode'){
        grade['id']=value;
      }
      else{
      data[key]=value;
      }
    }
    user['roleId']=role;
    data['userId']=user;
    data['managerId']=manager;
    data['deptId']=dept;
    data['gradeId']=grade;
    data['userAddressId']=address;
    console.log(data);
    
    dispatch(createUserDetail(data));
    
  };

    return (
        <div>
          <Navbar/>
    <form onSubmit={submitForm}>

           <div className="personal">
                Personal Details<br/><br/>
                
                <label htmlFor="userid">User ID : </label><br/>
                <input type="text" name="userId" id="userid" pattern="[0-9]{6}" required/>
                <br/><br/>

                <label htmlFor="fname">First Name : </label><br/>
                <input type="text" name="userFirstname" id="fname" pattern="[a-zA-Z]+" required/>
                <br/><br/>

                <label htmlFor="lname">Last Name : </label><br/>
                <input type="text" name="userLastname" id="lname" pattern="[a-zA-Z]+" required/>
                <br/><br/>
                <label htmlFor="dob">Date of Birth:</label><br/>
                <input type="date" id="dob" name="userDob" required/><br/><br/>

                <label htmlFor="doj">Date of Joining:</label><br/>
                <input type="date" id="doj" name="userDoj" required/><br/><br/>

                <label htmlFor="email">Email Id : </label><br/>
                <input type="email"  id="email" name="userEmail" required/>
                <br/><br/>

                <label htmlFor="tel1"> Mobile Number : </label><br/>
                <input type="tel" id="tel1" name="userPhoneno" placeholder="123456789" pattern="[0-9]{10}" required/>
                <br/><br/>
                
                <label htmlFor="managerid">Manager ID : </label><br/>
                <input type="text" name="managerId" id="managerid" pattern="[0-9]{6}"/><br/><br/>

                <label htmlFor="deptid">Department ID : </label><br/>
                
                <select name="deptId" id="deptid"  required>
                  <option value="0">select</option>
                <option value="1">Development</option>
                <option value="2">Production</option>
                <option value="3">Hr</option>                
                </select><br/><br/>

                <label htmlFor="marital">Choose Marital status:</label><br/>
                <select id="marital" name="maritalStatus" required>
                <option value="select">select</option>
                <option value="SINGLE">Single</option>
                <option value="MARRIED">Married</option>
                </select><br/><br/>
                <p>Gender:</p>
                <input type="radio" id="male" name="userGender" value="M"/>
                <label htmlFor="male">Male</label>&emsp;
                <input type="radio" id="female" name="userGender" value="F"/>
                <label htmlFor="female">Female</label><br/><br/>

                <label htmlFor="designation">Designation: </label><br/>
                <input type="text"  id="designation" name="userDesignation"  required />
                <br/><br/>
        
     
                <label htmlFor="city">city : </label><br/>
                <input type="text" name="city" onChange={handleChange} id="city"/><br/><br/>

                <label htmlFor="state">State : </label><br/>
                <input type="text" name="state" onChange={handleChange} id="state"/><br/><br/>
            
                <label htmlFor="area">Area : </label><br/>
                <textarea rows="4" cols="50" onChange={handleChange} name="area" id="area">
                </textarea><br/><br/>

                <label htmlFor="pincode">Pincode : </label><br/>
                <input type="text" name="pincode" onChange={handleChange} id="pincode" pattern="[0-9]{6}"/><br/><br/>

              </div><br/><br/>
              <div className="role">
                Role Details<br/><br/>
                <label htmlFor="roleid">Role Id: </label><br/>
                <select name="roleId" id="roleid"  required>
                  <option value="1">select</option>
                <option value="2">Employee</option>
                <option value="3">Manager</option>
                <option value="4">Admin</option>                
                </select><br/><br/>
              </div><br/><br/>
              <div className="grade">
                Grade Details<br/><br/>
                <label htmlFor="gradeCode">Choose code:</label><br/>
                <select id="gradeCode" name="gradeCode" required>
                <option value="M1">M1</option>
                <option value="M2">M2</option>
                <option value="M3">M3</option>
                <option value="M4">M4</option>
                <option value="M5">M5</option>
                <option value="M6">M6</option>
                <option value="M7">M7</option>
                </select><br/><br/>
              </div>
   
        
        <br/><br/><br/><br/>
        <center>
        <input type="submit" value="submit" />
        </center>
		
    
	</form>
        </div>
    );
};

export default EmployeeInfo;