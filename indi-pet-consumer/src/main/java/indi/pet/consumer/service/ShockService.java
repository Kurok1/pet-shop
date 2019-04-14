package indi.pet.consumer.service;

import indi.pet.consumer.domain.Shock;
import indi.pet.consumer.repository.ShockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Stream;

/**
 * @author <a href="maimengzzz@gmail.com">韩超</a>
 * @since 2019.02.28
 */
@Service("shockService")
public class ShockService {

    private ShockRepository shockRepository;

    public ShockRepository getShockRepository() {
        return shockRepository;
    }

    @Autowired
    public void setShockRepository(@Qualifier("shockRepository") ShockRepository shockRepository) {
        this.shockRepository = shockRepository;
    }

    public Stream<Shock> findByShopkeeper(Collection<String> ids){
        return getShockRepository().findByShopkeeperIdIn(ids);
    }

    public Shock findOne(String id){
        return getShockRepository().findOne(id);
    }
}
