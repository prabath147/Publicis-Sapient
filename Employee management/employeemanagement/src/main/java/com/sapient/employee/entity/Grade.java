package com.sapient.employee.entity;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Entity
@Table(name = "grade")
public class Grade {
    @Id
    @Column(name = "grade_code")
    private String id;

    @Column(name = "grade_min_sal")
    private Integer minSalary;

    @Column(name = "grade_max_sal")
    private Integer maxSalary;

    @Column(name = "grade_desc")
    @NotEmpty(message = "Description may not be empty")
    private String gradeDescription;
    
//    @OneToOne(fetch = FetchType.LAZY, cascade =  CascadeType.ALL, mappedBy = "gradeId")
//    private UserDetails userDetails;

    public Grade() {
    }

  
//    public UserDetails getUserDetails() {
//		return userDetails;
//	}
//
//
//	public void setUserDetails(UserDetails userDetails) {
//		this.userDetails = userDetails;
//	}


	@Override
	public String toString() {
		return "Grade [gradeId=" + id + ", minSalary=" + minSalary + ", maxSalary=" + maxSalary
				+ ", gradeDescription=" + gradeDescription + "]";
	}


	public String getId() {
		return id;
	}


	public void setId(String gradeId) {
		this.id = gradeId;
	}


	public Integer getMinSalary() {
        return minSalary;
    }

    public void setMinSalary(Integer minSalary) {
        this.minSalary = minSalary;
    }

    public Integer getMaxSalary() {
        return maxSalary;
    }

    public void setMaxSalary(Integer maxSalary) {
        this.maxSalary = maxSalary;
    }

    public String getGradeDescription() {
        return gradeDescription;
    }

    public void setGradeDescription(String gradeDescription) {
        this.gradeDescription = gradeDescription;
    }

    
}
