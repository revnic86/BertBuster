package com.rob.bertbuster.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "movies")
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;

    @NotBlank(message = "You must enter a title")
    @Size(min = 1, max = 100, message="Title must be between 1 and 100 characters")
    private String title;

    @NotNull
    @Enumerated(EnumType.STRING)
    private MovieGenre movieGenre;

    @NotNull
    @Enumerated(EnumType.STRING)
    private MovieRating movieRating;

    @NotNull(message = "You must enter a year")
    @Min(1888)
    @Max(2026)
    private Integer releaseYear;

    @NotNull(message = "You must enter a runtime")
    @Min(1)
    @Max(300)
    private Integer runtime;

    @Version
    private Integer version;

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DVD> copies = new ArrayList<>();



    public Movie(){

    }

    public Movie(String title, MovieGenre movieGenre, MovieRating movieRating, Integer releaseYear, Integer runtime) {
        this.title = title;
        this.movieGenre = movieGenre;
        this.movieRating = movieRating;
        this.releaseYear = releaseYear;
        this.runtime = runtime;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public MovieGenre getMovieGenre() {
        return movieGenre;
    }

    public void setMovieGenre(MovieGenre movieGenre) {
        this.movieGenre = movieGenre;
    }

    public MovieRating getMovieRating() {
        return movieRating;
    }

    public void setMovieRating(MovieRating movieRating) {
        this.movieRating = movieRating;
    }

    public Integer getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(Integer releaseYear) {
        this.releaseYear = releaseYear;
    }

    public Integer getRuntime() {
        return runtime;
    }

    public void setRuntime(Integer runtime) {
        this.runtime = runtime;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public List<DVD> getCopies() {
        return copies;
    }

    public void setCopies(List<DVD> copies) {
        this.copies = copies;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "uuid=" + uuid +
                ", title='" + title + '\'' +
                ", movieGenre=" + movieGenre +
                ", movieRating=" + movieRating +
                ", releaseYear=" + releaseYear +
                ", runtime=" + runtime +
                ", version=" + version +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Movie movie = (Movie) o;
        return Objects.equals(uuid, movie.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(uuid);
    }



    public void addDvd(DVD dvd){
        this.copies.add(dvd);
        dvd.setMovie(this);
    }



}
