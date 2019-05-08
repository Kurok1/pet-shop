package indi.pet.producer.service;

import indi.pet.producer.domain.Pet;
import indi.pet.producer.repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import java.util.List;

/**
 * @author <a href="maimengzzz@gmail.com">韩超</a>
 * @since 2018.11.05
 */
@Service("petService")
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

    public void delete(List<String> ids){
        getPetRepository().deletePetsByIdIn(ids);
    }



    public Pet findById(String id){
        return getPetRepository().findOne(id);
    }
}
