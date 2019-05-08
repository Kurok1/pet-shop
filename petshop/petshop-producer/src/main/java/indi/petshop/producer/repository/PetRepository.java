package indi.pet.producer.repository;

import indi.pet.producer.domain.Pet;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author <a href="maimengzzz@gmail.com">韩超</a>
 * @since 2018.11.05
 */
@Repository("petRepository")
public interface PetRepository extends ElasticsearchRepository<Pet,String> {

    public void deletePetsByIdIn(Iterable<String> ids);

}
