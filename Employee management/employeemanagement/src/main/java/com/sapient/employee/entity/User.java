package com.sapient.employee.entity;

import java.util.Set;

import javax.persistence.*;

@Entity
@Table(name = "user_credential")
public class User {
    @Id
    @SequenceGenerator(name = "myUserGen", sequenceName = "userSeq", initialValue = 102201)
	@GeneratedValue(generator = "myUserGen")
    @Column(name = "user_id")
    private long userId;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @ManyToOne
    @JoinColumn(name="role_id", nullable=false)
    private Role roleId;

//    @OneToOne(fetch = FetchType.LAZY, cascade =  CascadeType.ALL, mappedBy = "userId")
//    private UserDetails userDetails;
//
//    @OneToOne(fetch = FetchType.LAZY, cascade =  CascadeType.ALL, mappedBy = "managerId")
//    private UserDetails managerDetails;
//
//    @OneToMany(mappedBy="userLeaveId")
//    private Set<LeaveHistory> leaveHistory;

   
    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRoleId() {
        return roleId;
    }

    public void setRoleId(Role roleId) {
        this.roleId = roleId;
    }

    
    

    

	public User() {
    }
}
