package com.rob.bertbuster.repository;

import com.rob.bertbuster.domain.entity.DVD;
import com.rob.bertbuster.domain.entity.DVDStatus;
import com.rob.bertbuster.domain.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;


public interface DVDRepository extends JpaRepository<DVD, UUID> {

    Optional<DVD> findFirstByMovieAndDvdStatus(Movie movie, DVDStatus status);


}
