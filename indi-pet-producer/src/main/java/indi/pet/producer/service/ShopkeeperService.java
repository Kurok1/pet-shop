package indi.pet.producer.service;

import indi.pet.producer.domain.Shopkeeper;
import indi.pet.producer.repository.ShopkeeperRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author <a href="maimengzzz@gmail.com">韩超</a>
 * @since 2018.11.05
 */
@Service("shopkeeperService")
public class ShopkeeperService {

    private ShopkeeperRepository repository;

    public ShopkeeperRepository getRepository() {
        return repository;
    }

    @Autowired
    public void setRepository(@Qualifier("shopkeeperRepository") ShopkeeperRepository repository) {
        this.repository = repository;
    }

    public Shopkeeper save(Shopkeeper entity){
        return getRepository().save(entity);
    }

    public List<Shopkeeper> searchByName(String name,int page){
        Pageable pageable=new PageRequest(page,10);
        return getRepository().findShopkeepersByNameLike(name,pageable);
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
}