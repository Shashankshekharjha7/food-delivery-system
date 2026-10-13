package com.foodtracker.kitchenservice.repository;

import com.foodtracker.kitchenservice.entity.kitchenTicket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface kitchenTicketRepository extends JpaRepository<kitchenTicket, Long> {

}
