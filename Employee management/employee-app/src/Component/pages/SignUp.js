
import React  from 'react';
import { useDispatch  } from "react-redux";
import { createUser } from '../actions/postAction';
import './SignUp.css';
const SignUp = () => {

    const dispatch = useDispatch();

   
    

    const submitForm = (e) => {
       
        var data={};
        const formData = new FormData(e.target);
        
        for (let [key, value] of formData.entries()) {
            data[key]=value;
        }
        console.log(data);
        dispatch(createUser(data));
        
      };
  

    return (
        <div>
            <div className="login-wrapper">
      <form onSubmit={submitForm} className="signupform">
        <h2>Sign Up Form</h2>
        <div className="input-group">

            
            <input type="text" name="username" placeholder="User Name" id="username" required />
      
            <input type="password" name="password" id="password" placeholder="Password" required/>
            
            <input type="password" name="confirmpassword" id="confirmpassword" placeholder="Confirm Password" required/>

            <input type="text" name="name" id="name" placeholder="Name" pattern="[a-zA-Z]+" required/>
            
            <input type="email"  id="email" placeholder="Email Id" name="email" required/>
          
        </div>
        <input type="submit" value="Signup" className="submit-btn" /><br/>
        <a href="/login"><input type="button" className="submit-btn" value="Login"/></a>
      </form>
      
    </div>
        </div>
    );
};

export default SignUp;