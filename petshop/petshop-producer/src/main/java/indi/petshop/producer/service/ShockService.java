package indi.petshop.producer.service;

import indi.petshop.producer.domain.Shock;
import indi.petshop.producer.repository.ShockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * @author <a href="maimengzzz@gmail.com">韩超</a>
 * @since 2018.11.05
 */
@Service("producerShockService")
@Transactional
public class ShockService {
    private ShockRepository shockRepository;

    public ShockRepository getShockRepository() {
        return shockRepository;
    }

    @Autowired
    public void setShockRepository(@Qualifier("producerShockRepository") ShockRepository shockRepository) {
        this.shockRepository = shockRepository;
    }

    public Shock save(Shock shock){
        return getShockRepository().save(shock);
    }

    public Page<Shock> searchByName(String name, String id, int page){
        Pageable pageable=new PageRequest(page,10);
        return getShockRepository().findShocksByTitleContainingAndShopkeeperId(name,id,pageable);
    }

    public void delete(String id){
        getShockRepository().delete(id);
    }

    public Page<Shock> getShocks(String id,int page){
        Pageable pageable=new PageRequest(page,10);
        return getShockRepository().findShocksByShopkeeperId(id,pageable);
    }

    public Long getCountByShopkeeper(String keeper){
        return getShockRepository().countByShopkeeperId(keeper);
    }

    public Shock findById(String id){
        return getShockRepository().findOne(id);
    }
}
