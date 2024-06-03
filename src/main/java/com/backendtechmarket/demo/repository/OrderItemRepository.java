package com.backendtechmarket.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backendtechmarket.demo.entity.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Integer>{

}
