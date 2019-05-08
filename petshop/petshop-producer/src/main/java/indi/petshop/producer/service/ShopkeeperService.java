package indi.petshop.producer.service;

import indi.petshop.producer.domain.Shopkeeper;
import indi.petshop.producer.repository.ShopkeeperRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author <a href="maimengzzz@gmail.com">韩超</a>
 * @since 2018.11.05
 */
@Service("producerShopkeeperService")
@Transactional
public class ShopkeeperService {

    private ShopkeeperRepository repository;

    public ShopkeeperRepository getRepository() {
        return repository;
    }

    @Autowired
    public void setRepository(@Qualifier("producerShopkeeperRepository") ShopkeeperRepository repository) {
        this.repository = repository;
    }

    public Shopkeeper save(Shopkeeper entity){
        return getRepository().save(entity);
    }

    public boolean existsByEmail(String email){
        return getRepository().findByEmail(email)!=null;
    }

    public Shopkeeper getById(String id){
        return getRepository().findOne(id);
    }

    public void deleteById(String id){
        getRepository().delete(id);
    }

    public Shopkeeper login(String email,String password){
        return getRepository().findFirstByEmailEqualsAndPasswordEquals(email, password);
    }

    public Shopkeeper check(String id,String password){
        return getRepository().findByIdAndPassword(id,password);
    }
}
