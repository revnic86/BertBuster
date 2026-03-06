package com.rob.bertbuster.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "dvds")
public class DVD {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;

    @NotBlank(message = "DVD barcode cannot be blank")
    @Column(unique = true)
    private String barcode; // representation of the DVD as a string. Each DVD should have a unique barcode

    @NotNull(message = "DVD must be associated with a movie")
    @ManyToOne
    @JoinColumn(name = "movie_id")
    private Movie movie;

    @NotNull
    @Enumerated(EnumType.STRING)
    private DVDStatus dvdStatus = DVDStatus.AVAILABLE;

    @OneToMany(mappedBy = "dvd") //no cascade - rental records retained
    private List<Rental> rentals = new ArrayList<>();

    public DVD(){

    }

    public DVD(String barcode) {
        this.barcode = barcode;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public DVDStatus getDvdStatus() {
        return dvdStatus;
    }

    public void setDvdStatus(DVDStatus dvdStatus) {
        this.dvdStatus = dvdStatus;
    }

    @Override
    public String toString() {
        return "DVD{" +
                "uuid=" + uuid +
                ", barcode='" + barcode + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        DVD dvd = (DVD) o;
        return Objects.equals(uuid, dvd.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(uuid);
    }
}
