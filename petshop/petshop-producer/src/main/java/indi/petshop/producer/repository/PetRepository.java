package indi.petshop.producer.repository;

import indi.petshop.producer.domain.Pet;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * @author <a href="maimengzzz@gmail.com">韩超</a>
 * @since 2018.11.05
 */
@Repository("producerPetRepository")
public interface PetRepository extends ElasticsearchRepository<Pet,String> {

    public void deletePetsByIdIn(Iterable<String> ids);

}
