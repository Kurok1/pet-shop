package indi.pet.consumer.repository;

import indi.pet.consumer.domain.Shock;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * @author <a href="maimengzzz@gmail.com">韩超</a>
 * @since 2019.02.28
 */
@Repository("shockRepository")
public interface ShockRepository extends ElasticsearchRepository<Shock,String> {
}
