package com.rob.bertbuster.service.impl;

import com.rob.bertbuster.domain.entity.*;
import com.rob.bertbuster.domain.entity.dto.RentalResponseDto;
import com.rob.bertbuster.repository.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class RentalServiceImplTest {

    @Autowired
    private RentalServiceImpl service;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private MovieRepository movieRepo;

    @Autowired
    private DVDRepository dvdRepo;

    @Autowired
    private RentalRepository rentalRepo;

    private void loginAs(String username) {
        var auth = org.mockito.Mockito.mock(org.springframework.security.core.Authentication.class);
        org.mockito.Mockito.when(auth.getName()).thenReturn(username);
        var ctx = org.mockito.Mockito.mock(org.springframework.security.core.context.SecurityContext.class);
        org.mockito.Mockito.when(ctx.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(ctx);
    }

    @Test
    void borrow_createsRental_and_marksDvdBorrowed() {
        loginAs("testuser");

        User user = userRepo.save(new User("testuser", "pass", "USER"));
        Movie movie = movieRepo.save(new Movie("The Matrix", MovieGenre.ACTION, MovieRating.PG13, 1999, 120));

        DVD dvd = new DVD();
        dvd.setMovie(movie);
        dvd.setBarcode("The Matrix - 1999 COPY - 2");
        dvd.setDvdStatus(DVDStatus.AVAILABLE);
        dvd = dvdRepo.save(dvd);

        RentalResponseDto result = service.borrowDVD(movie.getUuid());

        assertThat(result).isNotNull();
        assertThat(result.rentalId()).isNotNull();

        DVD updatedDvd = dvdRepo.findById(dvd.getUuid()).orElseThrow();
        assertThat(updatedDvd.getDvdStatus()).isEqualTo(DVDStatus.BORROWED);
    }

    @Test
    void borrow_throwsException_whenUserAlreadyHasRental() {
        loginAs("testuser");

        User user = userRepo.save(new User("testuser", "pass1234", "USER"));

        Rental existing = new Rental();
        existing.setUser(user);
        existing.setBorrowedAt(LocalDate.now().minusDays(1));
        rentalRepo.save(existing);

        Movie movie = movieRepo.save(new Movie("District 9", MovieGenre.ACTION, MovieRating.PG13, 2009, 120));

        assertThatThrownBy(() -> service.borrowDVD(movie.getUuid()))
                .hasMessageContaining("You already have a rental");
    }

    @Test
    void return_marksRentalReturned_andDvdAvailable() {
        loginAs("testuser");

        User user = userRepo.save(new User("testuser", "pass", "USER"));
        Movie movie = movieRepo.save(new Movie("Arrival", MovieGenre.ACTION, MovieRating.PG13, 2017, 120));

        DVD dvd = new DVD();
        dvd.setMovie(movie);
        dvd.setDvdStatus(DVDStatus.BORROWED);
        dvd = dvdRepo.save(dvd);

        Rental rental = new Rental();
        rental.setUser(user);
        rental.setDvd(dvd);
        rental.setBorrowedAt(LocalDate.now().minusDays(3));
        rental = rentalRepo.save(rental);

        RentalResponseDto result = service.returnDVD(rental.getUuid());

        assertThat(result).isNotNull();

        Rental updated = rentalRepo.findById(rental.getUuid()).orElseThrow();
        assertThat(updated.getReturnedAt()).isEqualTo(LocalDate.now());

        DVD updatedDvd = dvdRepo.findById(dvd.getUuid()).orElseThrow();
        assertThat(updatedDvd.getDvdStatus()).isEqualTo(DVDStatus.AVAILABLE);
    }
}