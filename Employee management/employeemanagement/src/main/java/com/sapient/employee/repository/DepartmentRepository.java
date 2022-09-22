package com.sapient.employee.repository;

import com.sapient.employee.entity.Department;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentRepository extends JpaRepository<Department, Long>{

}
