package com.sapient.employee.repository;

import com.sapient.employee.entity.Grade;

import org.springframework.data.jpa.repository.JpaRepository;

public interface GradeRepository extends JpaRepository<Grade, Long> {

	Grade findById(String gradeId);

}
