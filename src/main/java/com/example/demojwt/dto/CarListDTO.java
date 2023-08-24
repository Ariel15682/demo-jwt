package com.example.demojwt.dto;

import com.example.demojwt.domain.Car;
import java.util.List;


//Devuelve datos al FrontEnd
public class CarListDTO {

    private List<Car> cars;

    public CarListDTO(){}

    public List<Car> getCars() { return cars; }

    public void setCars(List<Car> cars){ this. cars = cars; }

}
