package com.rob.bertbuster.repository;

import com.rob.bertbuster.domain.entity.DVD;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;


public interface DVDRepository extends JpaRepository<DVD, UUID> {

}
