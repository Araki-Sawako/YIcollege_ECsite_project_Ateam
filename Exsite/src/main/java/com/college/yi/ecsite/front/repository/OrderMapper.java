package com.college.yi.ecsite.front.repository;

import org.apache.ibatis.annotations.Mapper;

import com.college.yi.ecsite.entity.OderItem;
import com.college.yi.ecsite.entity.Order;

@Mapper
public interface OrderMapper {
	void insertOrder(Order order);
	void insertOrderDetail(OderItem item);

}
