package com.sapient.employee.entity;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "role")
public class Role {
    @Id
    @Column(name = "role_id")
    private long roleId;

    @Column(name = "role_desc")
    private String roleDesc;

//    @OneToMany(mappedBy="roleId")
//    private Set<User> user;

    @Override
    public String toString() {
        return "Role{" +
                "roleId=" + roleId +
                ", roleDesc='" + roleDesc + '\'' +
                
                '}';
    }

    public Role() {
    }

    public long getRoleId() {
        return roleId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }

    public String getRoleDesc() {
        return roleDesc;
    }

    public void setRoleDesc(String roleDesc) {
        this.roleDesc = roleDesc;
    }

    
}
