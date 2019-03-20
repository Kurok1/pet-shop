package indi.pet.consumer.repository;

import indi.pet.consumer.domain.Resource;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * @author <a href="maimengzzz@gmail.com">韩超</a>
 * @since 2019.02.24
 */
@Repository("resourceRepository")
public interface ResourceRepository extends ElasticsearchRepository<Resource,String> {
    public void deleteResourcesByIdIn(Iterable<String> ids);
}
