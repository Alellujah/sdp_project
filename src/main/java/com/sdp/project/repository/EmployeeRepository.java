package com.sdp.project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sdp.project.model.Employee;
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long>{

}