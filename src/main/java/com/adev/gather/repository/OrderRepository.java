package com.adev.gather.repository;

import com.adev.gather.domain.CurrencyPair;
import com.adev.gather.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, String>, JpaSpecificationExecutor {

}
