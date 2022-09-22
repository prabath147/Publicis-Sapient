import React from "react";


const Navbar = () => {
  return (
      <div className="container">
        {/* <Link class="navbar-brand" to="/">
          Redux Posts
        </Link> */}

        
        <div className="topnav">
      <a href="/home">Home</a>
      <a href="/employee/records">Employee Records</a>
      <a href="/employee/info">Add Employee Details</a>
      <a href="/leave">Apply Leave</a>
      <a href="/leave/status">Leave status</a>
      <a href="/employee/edit">Edit Employee</a>
      <a href="/leave/approval">Approve Leave</a>
      <a href="/login">Logout</a>
    </div><br/><br/>
          
        </div>
      
  );
};

export default Navbar;
