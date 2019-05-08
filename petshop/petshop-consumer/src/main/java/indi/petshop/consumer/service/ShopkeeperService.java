package indi.petshop.consumer.service;

import indi.petshop.consumer.domain.Shopkeeper;
import indi.petshop.consumer.repository.ShopkeeperRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author <a href="maimengzzz@gmail.com">韩超</a>
 * @since 2019.02.28
 */
@Service("consumerShopkeeperService")
@Transactional
public class ShopkeeperService {

    private ShopkeeperRepository shopkeeperRepository;


    public ShopkeeperRepository getShopkeeperRepository() {
        return shopkeeperRepository;
    }

    @Autowired
    public void setShopkeeperRepository(@Qualifier("consumerShopkeeperRepository") ShopkeeperRepository shopkeeperRepository) {
        this.shopkeeperRepository = shopkeeperRepository;
    }

    public Shopkeeper getOne(String id){
        return getShopkeeperRepository().findOne(id);
    }

    public Iterable<Shopkeeper> getByIds(Iterable<String> ids){
        return getShopkeeperRepository().findAll(ids);
    }

    public List<Shopkeeper> findByLocationInfo(double minLat, double maxLat, double minLng, double maxLng,int size){
        Pageable pageable=new PageRequest(0,size);
        return getShopkeeperRepository().findByLatitudeBetweenAndLongitudeBetween(minLat,maxLat,minLng,maxLng,pageable).getContent();
    }
}
