package com.example.demojwt.rest;

import com.example.demojwt.domain.Car;
import com.example.demojwt.dto.CarListDTO;
import com.example.demojwt.dto.CountDTO;
import com.example.demojwt.dto.MessageDTO;
import com.example.demojwt.services.CarService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api")
public class CarController {

    private final Logger log = LoggerFactory.getLogger(CarController.class);

    // Dependencia
    private final CarService carService;

    public CarController(CarService carService) { this.carService = carService; }


    /* ================ SPRING CRUD METHODS =============== */

    /**
     * http://localhost:8080/api/cars/1
     */
    @GetMapping("/cars/{id}")
    public ResponseEntity<Car> findById(@PathVariable Long id) {

        log.info("REST request to find one car");

        Optional<Car> carOpt = this.carService.findById(id);

        // Opcion 1
        return carOpt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());

        // Opcion 2
//        if (carOpt.isPresent())
//            return ResponseEntity.ok(carOpt.get());
//
//        return ResponseEntity.notFound().build();

        // Opcion 3
//        return carOpt
//                .map(
//                        car -> ResponseEntity.ok(car))
//                .orElseGet(
//                        () -> ResponseEntity.notFound().build()
//                );
    }

    /**
     * http://localhost:8080/api/cars
     */
    @GetMapping("/cars")
    public List<Car> findAll(){

        log.info("REST request to find all cars");
        return this.carService.findAll();
    }

    // Create one
    @PostMapping("/cars")
    public ResponseEntity<Car> create(@RequestBody Car car){

        log.info("REST request to create new car");

        if(car.getId() != null){ // Hay Id, el coche ya existe no puedo crearlo de nuevo
            log.warn("Trying to create a new car with existent id");
            return ResponseEntity.badRequest().build();
        }
        return  ResponseEntity.ok(this.carService.save(car));
    }

    // Update
    @PutMapping("/cars")
    public ResponseEntity<Car> update(@RequestBody Car car){

        log.info("REST request to update an existing car");
        if(car.getId() == null){ // No hay Id - Por tanto no existe el coche a actualizar
            log.warn("Trying to update an existing car without id");
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(this.carService.save(car));
    }

    // Delete All
    @DeleteMapping("/cars")
    public ResponseEntity<Car> deleteAll(){

        log.info("REST request to delete all cars");
        this.carService.deleteAll();
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/cars/count")
    public ResponseEntity<CountDTO> count(){

        log.info("REST request to count all cars");
        Long count = this.carService.count();
        CountDTO dto = new CountDTO(count);
        dto.setMessage("Que  tenga usted un feliz dia :)");
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/cars/hello")
    public  ResponseEntity<String> hello(){ return ResponseEntity.ok("Hello");}

    @GetMapping("/cars/hello2")
    public  ResponseEntity<MessageDTO> hello2(){ return ResponseEntity.ok(new MessageDTO("Hello"));}

    @DeleteMapping("/cars/deletemany")
    public ResponseEntity<Car> deleteMany(@RequestBody CarListDTO carListDto){

        this.carService.deleteAll(carListDto.getCars());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/cars/deletemany/{ids}")
    public ResponseEntity<Car> deleteMany(@PathVariable List<Long> ids){

        this.carService.deleteAllById(ids);
        return ResponseEntity.noContent().build();
    }

    /* =============== CUSTOM CRUD METHODS ================ */

    @GetMapping("/cars/manufacturer/{manufacturer}/model/{model}")
    public List<Car> findByManufacturerAndModel(@PathVariable Integer doors){

        log.info("REST request to find cars by num doors");
        return this.carService.findByDoors(doors);
    }

    @GetMapping("/cars/doors-gte/{doors}")
    public List<Car> findByDoorsGreaterThanEqual(@PathVariable Integer doors){

        return this.carService.findByDoorsGreaterThanEqual(doors);
    }

}