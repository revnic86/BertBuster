package com.rob.bertbuster.service.impl;

import com.rob.bertbuster.domain.entity.*;
import com.rob.bertbuster.domain.entity.dto.RentalResponseDto;
import com.rob.bertbuster.mapper.RentalMapper;
import com.rob.bertbuster.repository.DVDRepository;
import com.rob.bertbuster.repository.MovieRepository;
import com.rob.bertbuster.repository.RentalRepository;
import com.rob.bertbuster.repository.UserRepository;
import com.rob.bertbuster.service.RentalService;
import jakarta.transaction.Transactional;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Service
public class RentalServiceImpl implements RentalService {

    private final RentalRepository rentalRepository;
    private final DVDRepository dvdRepository;
    private final UserRepository userRepository;
    private final RentalMapper rentalMapper;
    private final MovieRepository movieRepository;

    public RentalServiceImpl(RentalRepository rentalRepository, DVDRepository dvdRepository, UserRepository userRepository, RentalMapper rentalMapper, MovieRepository movieRepository){
        this .rentalRepository = rentalRepository;
        this.dvdRepository = dvdRepository;
        this.userRepository = userRepository;
        this.rentalMapper = rentalMapper;
        this.movieRepository = movieRepository;
    }

    @Transactional
    @Override
    public RentalResponseDto borrowDVD(UUID movieId) {
        //GET logged in user from security context.
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        if ("anonymousUser".equals(currentUsername)) {
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.UNAUTHORIZED, "You must be logged in to rent"
            );
        }

        User user = userRepository.findByUsername(currentUsername) //select statement populates user object
                .orElseThrow(() -> new RuntimeException("User not found"));



        //Check if the user has a current rental (max 1)
        boolean hasActiveRental = rentalRepository.existsByUserAndReturnedAtIsNull(user);

        if (hasActiveRental){
            throw new RuntimeException("You already have a rental. Return it first");
        }

        //Find DVD availability


        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new RuntimeException("Movie not found"));

        DVD dvd = dvdRepository.findFirstByMovieAndDvdStatus(movie, DVDStatus.AVAILABLE) //Select statement
                .orElseThrow(() -> new RuntimeException("No available DVDs for this movie"));

        if (dvd.getDvdStatus() != DVDStatus.AVAILABLE){
            throw new RuntimeException("DVD is not available");
        }

        //Create rental
        Rental rental = new Rental();
        rental.setUser(user);
        rental.setDvd(dvd);
        rental.setBorrowedAt(LocalDate.now());
        rental.setDvdBarcodeAtRental(dvd.getBarcode());
        rental.setUsernameAtRental(user.getUsername());


        //Update DVD Status
        dvd.setDvdStatus(DVDStatus.BORROWED);

        //Save both
        rentalRepository.save(rental);
        dvdRepository.save(dvd);


        RentalResponseDto rentalResponseDto = rentalMapper.rentalToDto(rental);


        return rentalResponseDto;


    }

    @Override
    public RentalResponseDto returnDVD(UUID rentalId) {
        Rental rental = rentalRepository.findById(rentalId).orElseThrow(() -> new RuntimeException("Rental not found"));

        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        if ("anonymousUser".equals(currentUsername)) {
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.UNAUTHORIZED, "You must be logged in to rent"
            );
        }

        //Mark as returned
        rental.setReturnedAt(LocalDate.now());

        //Free the DVD
        DVD dvd = rental.getDvd();
        dvd.setDvdStatus(DVDStatus.AVAILABLE);

        rentalRepository.save(rental);
        dvdRepository.save(dvd);

        RentalResponseDto rentalResponseDto = rentalMapper.rentalToDto(rental);

        return rentalResponseDto;
    }
}
