// import logo from './logo.svg';
import './App.css';
import EmployeeInfo from './Component/pages/EmployeeInfo';

import EmployeeRecords from './Component/pages/EmployeeRecords';
import Leave from './Component/pages/Leave';
import LeaveApproval from './Component/pages/LeaveApproval';
import Login from './Component/pages/Login';
import SignUp from './Component/pages/SignUp';
import { BrowserRouter, Route, Routes } from "react-router-dom";
import { Provider } from "react-redux";
import store from "./store";
import EditLeave from './Component/pages/EditLeave';
import LeaveStatus from './Component/pages/LeaveStatus';
import EditEmployee from './Component/pages/EditEmployee';
import Home from './Component/pages/Home';


function App() {
  return (
    <div className="App">
      <Provider store={store}>
      {/* <EmployeeRecord1/> */}
      <BrowserRouter>
        
        <br />
        <div className="container">
          <Routes>
          <Route path="/" element={<Login />} />
            <Route path="/login" element={<Login />} />
            <Route path="/signup" element={<SignUp/>}/>
            <Route path="/home" element={<Home />} />
            <Route path="/logout" element={<Login />} />
            <Route path="/employee/records" element={<EmployeeRecords/>}/>
              <Route path="/employee/info" element={<EmployeeInfo />} />
              <Route path="/leave" element={<Leave />} />
              <Route path="/leave/edit/:id" element={<EditLeave/>} />
            <Route path="/leave/approval" element={<LeaveApproval />} />
            <Route path="/leave/status" element={<LeaveStatus />} />
            <Route path="/employee/edit" element={<EditEmployee />} />
            
          </Routes>
        </div>
      </BrowserRouter>
      </Provider>
    </div>
  );
}

export default App;
