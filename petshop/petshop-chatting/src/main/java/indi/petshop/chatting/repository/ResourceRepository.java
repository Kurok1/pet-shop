package indi.petshop.chatting.repository;

import indi.petshop.chatting.entity.Resource;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * @author <a href="chan@ittx.com.cn">韩超</a>
 * @version 2019.05.02
 */
@Repository
public interface ResourceRepository extends ElasticsearchRepository<Resource,String> {
}
