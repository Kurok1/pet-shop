package indi.petshop.producer.service;

import indi.petshop.producer.domain.Pet;
import indi.petshop.producer.repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author <a href="maimengzzz@gmail.com">韩超</a>
 * @since 2018.11.05
 */
@Service("producerPetService")
public class PetService {

    private PetRepository petRepository;

    public PetRepository getPetRepository() {
        return petRepository;
    }

    @Autowired
    public void setPetRepository(@Qualifier("producerPetRepository") PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    public Pet save(Pet entity){
        return getPetRepository().save(entity);
    }

    public void delete(List<String> ids){
        getPetRepository().deletePetsByIdIn(ids);
    }



    public Pet findById(String id){
        return getPetRepository().findOne(id);
    }
}
