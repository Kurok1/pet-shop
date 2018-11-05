package indi.pet.producer.repository;

import indi.pet.producer.domain.Pet;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author <a href="maimengzzz@gmail.com">韩超</a>
 * @since 2018.11.05
 */
@Repository("petRepository")
public interface PetRepository extends ElasticsearchRepository<Pet,String> {


    /**
     * 根据名称和价格查找宠物
     * @param name 宠物名称,required
     * @param lower 价格最低值,默认为0
     * @param upper 价格最大值,默认为Float.MAX_VALUE
     * @param pageable 分页信息
     * @return 返回符合条件的宠物信息
     */
   public List<Pet> findByNameEqualsAndPriceBetween(String name,String lower,String upper,Pageable pageable);
}
