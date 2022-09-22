import React, { Component } from 'react'
import EmployeeService from '../service/EmployeeService';
import Navbar from '../element/Navbar';

export default class EmployeeRecord1 extends Component {

    constructor(props){
        super(props)
        this.state = {
            users:[]
        }
    }

    componentDidMount(){
        EmployeeService.getUsers().then((response) => {
            this.setState({ users: response.data})
        });
    }


  render() {
    return (
        <div>
             <Navbar/>
        <div class="employee-record">
           
        <table>
            <thead>
                <tr>
                    
                    <th>User ID</th>
                    <th>First Name</th>
                    <th>Last Name</th>
                    <th>Email</th>
                    <th>Date Of Birth</th>
                    <th>Manager ID</th>
                    <th>Dept ID</th>
                    <th>Designation</th>
                </tr>
            </thead>
            <tbody id="bodyoutput">
            {
                            this.state.users.map(
                                user => 
                                <tr key = {user.userId}>
                                     <td> {user.userId}</td>   
                                     <td> {user.username}</td>   
                                     <td> {user.password}</td>   
                                     <td> {user.roleId.roleId}</td>
                                     <td> {user.roleId.roleDesc}</td>
                                </tr>
                            )
                        }
        </tbody>
        </table>
        </div>
    </div>
    )
  }
}
