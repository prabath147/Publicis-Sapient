import React , {  useEffect } from 'react';
import './Login.css';
import { useDispatch, useSelector } from "react-redux";
import { getUserAuth } from '../actions/postAction';
import { useNavigate } from "react-router-dom";
const Login = () => {

  const dispatch = useDispatch();
  const user = useSelector((state) => state.post.user);
  let navigate = useNavigate();

  useEffect(() => {
    
    if (user) {
      console.log(user);
      sessionStorage.setItem("role", user.roleId.roleId);
      navigate('/home');
      
    }
    
  }, [user]);

    const submitForm = (e) => {
      e.preventDefault();
        var data={};
        const formData = new FormData(e.target);

        for (let [key, value] of formData.entries()) {
            data[key]=value;
        }
        console.log(data);
        
        dispatch(getUserAuth(data));
      
        
      };
     
    return (
        <div>
            <div className="login-wrapper">
      <form  className="loginform" onSubmit={submitForm}>
        <h2>Login</h2>
        <div className="input-group">
          <input type="text" name="username" placeholder="Username" id="loginUser" required />
          
        
          <input type="password" name="password" id="loginPassword" placeholder="Password" required/>
          
        </div>
        <input type="submit" value="Login" className="submit-btn" /><br/>
        <a href="/signup"><input type="button" className="submit-btn" value="SignUp"/></a>
      </form>
    </div>
        </div>
    );
};

export default Login;