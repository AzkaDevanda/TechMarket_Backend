package com.backendtechmarket.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.backendtechmarket.demo.entity.Order;
import com.backendtechmarket.demo.entity.User;

@Repository
public interface OrderRepository extends JpaRepository<Order,Integer>{
    List<Order> findAllByUserOrderByCreatedDateDesc(User user);
}
