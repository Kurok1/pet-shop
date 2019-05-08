package indi.petshop.consumer.service;

import indi.petshop.consumer.domain.Pet;
import indi.petshop.consumer.repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author <a href="maimengzzz@gmail.com">韩超</a>
 * @since 2019.02.28
 */
@Service("consumerPetService")
@Transactional
public class PetService {

    private PetRepository petRepository;

    public PetRepository getPetRepository() {
        return petRepository;
    }

    @Autowired
    public void setPetRepository(@Qualifier("consumerPetRepository") PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    public Pet save(Pet pet){
        return getPetRepository().save(pet);
    }

    public void delete(String id){
        getPetRepository().delete(id);
    }

    public List<Pet> getPetsByUserId(String user){
        return getPetRepository().findByUserId(user);
    }
}
