package com.sapient.employee.entity;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Entity
@Table(name = "department")
public class Department {
    @Id
    @Column(name = "dept_id")
    private long deptId;

    @Column(name = "dept_name")
    @NotEmpty(message = "Name may not be empty")
    private String deptName;

//    @OneToOne(fetch = FetchType.LAZY, cascade =  CascadeType.ALL, mappedBy = "deptId")
//    private UserDetails userDetails;

	public long getDeptId() {
		return deptId;
	}

	public void setDeptId(long deptId) {
		this.deptId = deptId;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	
	public Department(long deptId, String deptName) {
		super();
		this.deptId = deptId;
		this.deptName = deptName;
	}

	public Department() {
		super();
	}

    

}
