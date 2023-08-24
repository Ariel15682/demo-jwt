package com.example.demojwt.domain;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "cars")
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String manufacturer;
    private String model; //CONTAINS
    private double cc;
    private Integer doors;

    @Column(name = "año")
    private Integer year; //IN
    private LocalDate releaseDate; //BETWEEN
    private Boolean available; //True or False

    public Car() { }

    public Car(Long id, String manufacturer, String model, double cc, Integer doors, Integer year, LocalDate releaseDate, Boolean available) {
        this.id = id;
        this.manufacturer = manufacturer;
        this.model = model;
        this.cc = cc;
        this.doors = doors;
        this.year = year;
        this.releaseDate = releaseDate;
        this.available = available;
    }

    public Long getId() { return id; }

    //public void setId(Long id) { this.id = id; }

    public String getManufacturer() { return manufacturer; }

    public void setManufacturer(String manufacturer) { this.manufacturer = manufacturer; }

    public String getModel() { return model; }

    public void setModel(String model) { this.model = model; }

    public double getCc() { return cc; }

    public void setCc(double cc) { this.cc = cc; }

    public Integer getDoors() { return doors; }

    public void setDoors(Integer doors) { this.doors = doors; }

    public Integer getYear() { return year; }

    public void setYear(Integer year) { this.year = year; }

    public LocalDate getReleaseDate() { return releaseDate; }

    public void setReleaseDate(LocalDate releaseDate) { this.releaseDate = releaseDate; }

    public Boolean getAvailable() { return available; }

    public void setAvailable(Boolean available) { this.available = available; }

    @Override
    public String toString() {
        return "Car{" +
                "id=" + id +
                ", manufacturer='" + manufacturer + '\'' +
                ", model='" + model + '\'' +
                ", cc=" + cc +
                ", doors=" + doors +
                ", year=" + year +
                ", releaseDate=" + releaseDate +
                ", available=" + available +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Car car = (Car) o;
        return Double.compare(car.cc, cc) == 0 && Objects.equals(id, car.id) && Objects.equals(manufacturer, car.manufacturer) && Objects.equals(model, car.model) && Objects.equals(doors, car.doors) && Objects.equals(year, car.year) && Objects.equals(releaseDate, car.releaseDate) && Objects.equals(available, car.available);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, manufacturer, model, cc, doors, year, releaseDate, available);
    }
}