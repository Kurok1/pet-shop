package indi.petshop.consumer.repository;

import indi.petshop.consumer.domain.Shopkeeper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * @author <a href="maimengzzz@gmail.com">韩超</a>
 * @since 2019.02.28
 */
@Repository("consumerShopkeeperRepository")
public interface ShopkeeperRepository extends ElasticsearchRepository<Shopkeeper,String> {

//    @Query("{\"bool\":{\"must\":{\"range\":{\"latitude\" :{\"gte\":?,\"lte\":?},\"longitude\" :{\"gte\":?,\"lte\":?}}}}}")
    Page<Shopkeeper> findByLatitudeBetweenAndLongitudeBetween(double minLat, double maxLat, double minLng, double maxLng, Pageable pageable);

}
