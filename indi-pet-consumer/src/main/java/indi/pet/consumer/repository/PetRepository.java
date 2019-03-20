package indi.pet.consumer.repository;

import indi.pet.consumer.domain.Pet;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author <a href="maimengzzz@gmail.com">韩超</a>
 * @since 2019.02.28
 */
@Repository("petRepository")
public interface PetRepository extends ElasticsearchRepository<Pet,String> {

    List<Pet> findByUserId(String userId);

}
