package indi.pet.producer.service;

import indi.pet.producer.domain.Pet;
import indi.pet.producer.repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.awt.print.Pageable;
import java.util.List;

/**
 * @author <a href="maimengzzz@gmail.com">韩超</a>
 * @since 2018.11.05
 */
@Service
public class PetService {

    private PetRepository petRepository;

    public PetRepository getPetRepository() {
        return petRepository;
    }

    @Autowired
    public void setPetRepository(@Qualifier("petRepository") PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    public Pet save(Pet entity){
        return getPetRepository().save(entity);
    }

    public void delete(Pet entity){
        getPetRepository().delete(entity);
    }

    public List<Pet> search(String name,String lower,String upper){
        Pageable
    }
}
