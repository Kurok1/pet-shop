package indi.pet.consumer.service;

import indi.pet.consumer.repository.ShockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

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
}
