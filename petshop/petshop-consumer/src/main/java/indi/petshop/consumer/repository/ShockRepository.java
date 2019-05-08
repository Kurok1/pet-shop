package indi.pet.consumer.repository;

import indi.pet.consumer.domain.Shock;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author <a href="maimengzzz@gmail.com">韩超</a>
 * @since 2019.02.28
 */
@Repository("shockRepository")
public interface ShockRepository extends ElasticsearchRepository<Shock,String> {
    Stream<Shock> findShocksByIdIn(Collection<String> ids);

    Stream<Shock> findByShopkeeperIdIn(Collection<String> shopkeeperIds);
}
