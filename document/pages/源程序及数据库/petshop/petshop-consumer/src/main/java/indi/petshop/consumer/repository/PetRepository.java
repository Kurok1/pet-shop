package indi.petshop.consumer.repository;

import indi.petshop.consumer.domain.Pet;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author <a href="maimengzzz@gmail.com">韩超</a>
 * @since 2019.02.28
 */
@Repository("consumerPetRepository")
public interface PetRepository extends ElasticsearchRepository<Pet,String> {

    List<Pet> findByUserId(String userId);

}
