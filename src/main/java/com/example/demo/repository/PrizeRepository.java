package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Prize;

public interface PrizeRepository  extends JpaRepository<Prize, Long>{

}
