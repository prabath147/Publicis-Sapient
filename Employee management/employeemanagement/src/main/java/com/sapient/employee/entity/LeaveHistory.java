package com.sapient.employee.entity;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "leave_history")
public class LeaveHistory {
    @Id
    @SequenceGenerator(name = "myLeaveGen", sequenceName = "leaveSeq", initialValue = 105601)
	@GeneratedValue(generator = "myLeaveGen")
    @Column(name = "leave_id")
    private long leaveId;

    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private User userLeaveId;

    @Column(name = "leave_start_date")
    private Date leaveStartDate;

    @Column(name = "leave_end_date")
    private Date leaveEndDate;

    @Column(name = "leave_no_of_days_applied")
    private Integer leaveNoOfDaysApplied;


    @Column(name = "leave_status")
    @Enumerated(EnumType.STRING)
    private LeaveStatus leaveStatus;

    public LeaveHistory() {
    }

    @Override
    public String toString() {
        return "LeaveHistory{" +
                "leaveId=" + leaveId +
                ", userLeaveId=" + userLeaveId +
            
                ", leaveStartDate=" + leaveStartDate +
                ", leaveEndDate=" + leaveEndDate +
                ", leaveNoOfDaysApplied=" + leaveNoOfDaysApplied +
                
                ", leaveStatus=" + leaveStatus +
                '}';
    }

    public LeaveStatus getLeaveStatus() {
        return leaveStatus;
    }

    public void setLeaveStatus(LeaveStatus leaveStatus) {
        this.leaveStatus = leaveStatus;
    }

    public long getLeaveId() {
        return leaveId;
    }

    public void setLeaveId(long leaveId) {
        this.leaveId = leaveId;
    }

    public User getUserLeaveId() {
        return userLeaveId;
    }

    public void setUserLeaveId(User userLeaveId) {
        this.userLeaveId = userLeaveId;
    }

    

    public Date getLeaveStartDate() {
        return leaveStartDate;
    }

    public void setLeaveStartDate(Date leaveStartDate) {
        this.leaveStartDate = leaveStartDate;
    }

    public Date getLeaveEndDate() {
        return leaveEndDate;
    }

    public void setLeaveEndDate(Date leaveEndDate) {
        this.leaveEndDate = leaveEndDate;
    }

    public Integer getLeaveNoOfDaysApplied() {
        return leaveNoOfDaysApplied;
    }

    public void setLeaveNoOfDaysApplied(Integer leaveNoOfDaysApplied) {
        this.leaveNoOfDaysApplied = leaveNoOfDaysApplied;
    }

   
}
