package com.rob.bertbuster.repository;

import com.rob.bertbuster.domain.entity.Rental;
import com.rob.bertbuster.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface RentalRepository extends JpaRepository<Rental, UUID> {

    boolean existsByUserAndReturnedAtIsNull(User user);

    List<Rental> findByUserAndReturnedAtIsNull(User user);


}
