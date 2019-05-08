package indi.pet.producer.repository;

import indi.pet.producer.domain.Resource;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author <a href="maimengzzz@gmail.com">韩超</a>
 * @since 2018.11.05
 */
@Repository("resourceRepository")
public interface ResourceRepository extends ElasticsearchRepository<Resource,String> {

        List<Resource> findResourcesByHasUsed(boolean used);
}
