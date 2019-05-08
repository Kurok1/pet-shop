package indi.petshop.producer.repository;

import indi.petshop.producer.domain.Resource;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author <a href="maimengzzz@gmail.com">韩超</a>
 * @since 2018.11.05
 */
@Repository("producerResourceRepository")
public interface ResourceRepository extends ElasticsearchRepository<Resource,String> {

        List<Resource> findResourcesByHasUsed(boolean used);
}
