package indi.pet.producer.controller;

import indi.pet.producer.domain.Pet;
import indi.pet.producer.service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author <a href="maimengzzz@gmail.com">韩超</a>
 * @since 2018.11.05
 */
@RestController
@RequestMapping("/pet")
public class PetController {

    private PetService petService;

    public PetService getPetService() {
        return petService;
    }

    @Autowired
    public void setPetService(@Qualifier("petService") PetService petService) {
        this.petService = petService;
    }

    @PostMapping("/save")
    public Pet save(@RequestBody Pet pet){
        return getPetService().save(pet);
    }

    @PutMapping("/update")
    public Pet update(@RequestBody Pet pet){
        return getPetService().save(pet);
    }

    @DeleteMapping("/")
    public boolean delete(@RequestBody List<String> ids){
        getPetService().delete(ids);
        return true;
    }

    @GetMapping("/{id}")
    public Pet getById(@PathVariable("id") String id){
        return getPetService().findById(id);
    }



}
