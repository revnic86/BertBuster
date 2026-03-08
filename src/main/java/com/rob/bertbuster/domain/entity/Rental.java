package com.rob.bertbuster.domain.entity;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
public class Rental {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;


    @ManyToOne
    @JoinColumn(name = "dvd_id", nullable = true)
    private DVD dvd;

    private LocalDate borrowedAt;

    private LocalDate returnedAt;

    @Column(name = "username_at_rental")
    private String usernameAtRental;

    @Column(name = "dvd_barcode_at_rental")
    private String dvdBarcodeAtRental;

    public Rental(){

    }

    public Rental(User user, DVD dvd, LocalDate borrowedAt){
        this.user = user;
        this.dvd = dvd;
        this.borrowedAt = borrowedAt;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public DVD getDvd() {
        return dvd;
    }

    public void setDvd(DVD dvd) {
        this.dvd = dvd;
    }

    public LocalDate getBorrowedAt() {
        return borrowedAt;
    }

    public void setBorrowedAt(LocalDate borrowedAt) {
        this.borrowedAt = borrowedAt;
    }

    public LocalDate getReturnedAt() {
        return returnedAt;
    }

    public void setReturnedAt(LocalDate returnedAt) {
        this.returnedAt = returnedAt;
    }

    public String getUsernameAtRental() {
        return usernameAtRental;
    }

    public void setUsernameAtRental(String usernameAtRental) {
        this.usernameAtRental = usernameAtRental;
    }

    public String getDvdBarcodeAtRental() {
        return dvdBarcodeAtRental;
    }

    public void setDvdBarcodeAtRental(String dvdBarcodeAtRental) {
        this.dvdBarcodeAtRental = dvdBarcodeAtRental;
    }

    @Override
    public String toString() {
        return "Rental{" +
                "user=" + user +
                ", dvd=" + dvd +
                ", borrowedAt=" + borrowedAt +
                '}';
    }
}
