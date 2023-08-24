package com.example.demojwt.repository;

import com.example.demojwt.domain.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {

    //Estos metodos estan en el repositorio para que Spring los lea y permita hacer busquedas sobre la tabla "car" asociada
    //Se declaran y Spring proporciona la implementacion
    List<Car> findByDoors(Integer doors);

    List<Car> findByManufacturerAndModel(String manufacturer, String model);

    List<Car> findByDoorsGreaterThanEqual(Integer doors);

    List<Car> findByModelContaining(String model);

    List<Car> findByYearIn(List<Integer> years);

    List<Car> findByYearBetween(Integer startYear, Integer endYear);

    List<Car> findByReleaseDateBetween(LocalDate startDate, LocalDate endDate);

    List<Car> findByAvailableTrue();

    Long deleteAllByAvailableFalse();

}