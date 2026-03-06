package com.rob.bertbuster.controller;

import com.rob.bertbuster.domain.entity.Rental;
import com.rob.bertbuster.domain.entity.User;
import com.rob.bertbuster.domain.entity.dto.RentalResponseDto;
import com.rob.bertbuster.mapper.RentalMapper;
import com.rob.bertbuster.repository.RentalRepository;
import com.rob.bertbuster.repository.UserRepository;
import com.rob.bertbuster.service.RentalService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class RentalController {

    private final RentalService rentalService;
    private final UserRepository userRepository;
    private final RentalRepository rentalRepository;
    private final RentalMapper rentalMapper;

    public RentalController(RentalService rentalService, UserRepository userRepository, RentalRepository rentalRepository, RentalMapper rentalMapper){
        this.rentalService = rentalService;
        this.userRepository = userRepository;
        this.rentalRepository = rentalRepository;
        this.rentalMapper = rentalMapper;

    }

    @PostMapping("/rent/{movieId}")  // ← Change {dvdId} to {movieId}
    public ResponseEntity<RentalResponseDto> borrowDVD(@PathVariable UUID movieId) {
        RentalResponseDto response = rentalService.borrowDVD(movieId);  // ← pass movieId
        return ResponseEntity.ok(response);
    }


    @PostMapping("/return/{rentalId}")
    public ResponseEntity<RentalResponseDto> returnDVD(@PathVariable UUID rentalId) {
        RentalResponseDto response = rentalService.returnDVD(rentalId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/rentals/my-active")
    public ResponseEntity<List<RentalResponseDto>> getMyActiveRentals() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if ("anonymousUser".equals(username)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Rental> active = rentalRepository.findByUserAndReturnedAtIsNull(user);
        List<RentalResponseDto> dtos = active.stream()
                .map(rentalMapper::rentalToDto)
                .toList();

        return ResponseEntity.ok(dtos);
    }
}
