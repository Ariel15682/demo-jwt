package com.example.demojwt.services;

import com.example.demojwt.domain.Car;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


// Los servicios estan situados entre un controlador y un repositorio con el objetivo de liberar carga al controlador
// ademas asegura cumplir la regla de una sola funcionalidad y el controlador se encarga unicamente a gestionar peticiones.
public interface CarService {

    // Spring Repository methods

    List<Car> findAll();

    Optional<Car> findById(Long id);

    Long count();

    Car save(Car car);

    void deleteByid(Long id);

    void deleteAll();

    void deleteAll(List<Car> cars);

    void deleteAllById(List<Long> ids);


    //Custom methods

    List<Car> findByDoors(Integer doors);

    List<Car> findByManufacturerAndModel(String manufacturer, String model);

    List<Car> findByDoorsGreaterThanEqual(Integer doors);

    List<Car> findByModelContaining(String model);

    List<Car> findByYearIn(List<Integer> years);

    List<Car> findByYearBetween(Integer startYear, Integer endYear);

    List<Car> findByReleaseDateBetween(LocalDate startDate, LocalDate endDate);

    List<Car> findByAvailableTrue();

    Long deleteAllByAvailableFalse() ;

}
