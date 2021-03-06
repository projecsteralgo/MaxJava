package com.maxtrain.prs.request;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestRepository extends JpaRepository<Request, Integer>{
	List<Request> findByUserIdNot(int userId);
	List<Request> findByStatusAndUserIdNot(String status, int userId);
}