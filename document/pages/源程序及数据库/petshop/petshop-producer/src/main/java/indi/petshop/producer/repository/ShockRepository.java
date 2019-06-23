package indi.petshop.producer.repository;

import indi.petshop.producer.domain.Shock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;


/**
 * @author <a href="maimengzzz@gmail.com">韩超</a>
 * @since 2018.11.05
 */
@Repository("producerShockRepository")
public interface ShockRepository extends ElasticsearchRepository<Shock,String> {

    Page<Shock> findShocksByTitleContainingAndShopkeeperId(String title, String id, Pageable pageable);

    Page<Shock> findShocksByShopkeeperId(String id, Pageable pageable);

    Long countByShopkeeperId(String shopkeeper);
}
