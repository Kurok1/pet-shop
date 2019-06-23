package indi.petshop.consumer.controller;

import indi.petshop.consumer.domain.Shock;
import indi.petshop.consumer.domain.Shopkeeper;
import indi.petshop.consumer.exception.TokenExpiredException;
import indi.petshop.consumer.service.ShockService;
import indi.petshop.consumer.service.ShopkeeperService;
import indi.petshop.consumer.util.TokenUtil;
import indi.petshop.consumer.wrapper.LocationInfoWrapper;
import indi.petshop.consumer.wrapper.ShockWrapper;
import indi.petshop.consumer.wrapper.ShocksApiWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author <a href="chan@ittx.com.cn">韩超</a>
 * @version 2019.04.14
 */
@RestController("consumerShockController")
@RequestMapping("/consumer/shock")
public class ShockController {

    private ShockService shockService;

    private ShopkeeperService shopkeeperService;

    public ShockService getShockService() {
        return shockService;
    }

    @Autowired
    public void setShockService(@Qualifier("consumerShockService") ShockService shockService) {
        this.shockService = shockService;
    }

    public ShopkeeperService getShopkeeperService() {
        return shopkeeperService;
    }

    @Autowired
    public void setShopkeeperService(@Qualifier("consumerShopkeeperService") ShopkeeperService shopkeeperService) {
        this.shopkeeperService = shopkeeperService;
    }

    @PostMapping("/get")
    public ShocksApiWrapper get(@RequestBody LocationInfoWrapper locationInfoWrapper, @RequestParam("token")String token,@RequestParam("size")int size){
        if(!TokenUtil.validate(token))
            throw new TokenExpiredException();
        ShocksApiWrapper shocksApiWrapper=new ShocksApiWrapper();

        double maxLat=locationInfoWrapper.getLatitude()+locationInfoWrapper.getAccurate();
        double minLat=locationInfoWrapper.getLatitude()-locationInfoWrapper.getAccurate();
        double maxLng=locationInfoWrapper.getLongitude()+locationInfoWrapper.getAccurate();
        double minLng=locationInfoWrapper.getLongitude()-locationInfoWrapper.getAccurate();
        List<Shopkeeper> shopkeepers=getShopkeeperService().findByLocationInfo(minLat,maxLat,minLng,maxLng,size);
        //生成Shock数据
        if(shopkeepers!=null && shopkeepers.size()!=0){
            Stream<Map<String,Object>> infos=shopkeepers.stream().map(
                    shopkeeper -> {
                        Map<String,Object> map=new HashMap<>();
                        map.put("id",shopkeeper.getId());
                        map.put("name",shopkeeper.getName());
                        map.put("logo",shopkeeper.getLogo());
                        map.put("latitude",shopkeeper.getLatitude());
                        map.put("longitude",shopkeeper.getLongitude());
                        return map;
                    }
            ).limit(size);
            Stream<Shock> shockStream=getShockService().findByShopkeeper(infos.map(
                    item->item.get("id").toString()
            ).collect(Collectors.toList()));
            List<Map<String,Object>> newInfos=shopkeepers.stream().map(
                shopkeeper -> {
                    Map<String,Object> map=new HashMap<>();
                    map.put("id",shopkeeper.getId());
                    map.put("name",shopkeeper.getName());
                    map.put("logo",shopkeeper.getLogo());
                    map.put("latitude",shopkeeper.getLatitude());
                    map.put("longitude",shopkeeper.getLongitude());
                    return map;
                }
            ).limit(size).collect(Collectors.toList());
            //根据商家号进行分组
            Map<String, List<Shock>> collect = shockStream.collect(Collectors.groupingBy(Shock::getShopkeeperId));
            List<ShockWrapper> shockWrappers=new LinkedList<>();
            collect.forEach(
                (key,value)-> {
                    ShockWrapper wrapper=new ShockWrapper();
                    wrapper.setShopkeeperId(key);
                    final Optional<Map<String, Object>> keeper = newInfos.stream().filter(
                        item -> key.equals(item.get("id"))
                    ).findFirst();
                    if(keeper.isPresent()){
                        wrapper.setShopkeeperName(keeper.get().get("name").toString());
                        wrapper.setShopkeeperLogo(keeper.get().get("logo").toString());
                        wrapper.setLatitude(Double.parseDouble(keeper.get().get("latitude").toString()));
                        wrapper.setLongitude(Double.parseDouble(keeper.get().get("longitude").toString()));
                        Optional<Shock> shock=value.stream().min(
                            (t1,t2)->{
                                if(t1.getTimestamp()<t2.getTimestamp())
                                    return 1;
                                else if(t1.getTimestamp()==t2.getTimestamp())
                                    return 0;
                                else return -1;
                            }
                        );
                        shock.ifPresent(
                            wrapper::setShock
                        );
                        shockWrappers.add(wrapper);
                    }

                }
            );
            shocksApiWrapper.setShocks(shockWrappers);
            shockStream.close();
            infos.close();
        }
        shocksApiWrapper.setSize(size);
        shocksApiWrapper.setLongitude(locationInfoWrapper.getLongitude());
        shocksApiWrapper.setAccurate(locationInfoWrapper.getAccurate());
        shocksApiWrapper.setLatitude(locationInfoWrapper.getLatitude());
        return shocksApiWrapper;
    }

    @GetMapping("/{id}")
    public ShockWrapper getById(@PathVariable("id")String id,@RequestParam("token")String token){
        if(!TokenUtil.validate(token))
            throw new TokenExpiredException();
        Shock shock=getShockService().findOne(id);
        Shopkeeper shopkeeper=getShopkeeperService().getOne(shock.getShopkeeperId());
        ShockWrapper shockWrapper=new ShockWrapper();
        shockWrapper.setShock(shock);
        shockWrapper.setShopkeeperId(shopkeeper.getId());
        shockWrapper.setShopkeeperName(shopkeeper.getName());
        shockWrapper.setShopkeeperLogo(shopkeeper.getLogo());
        shockWrapper.setLatitude(shopkeeper.getLatitude());
        shockWrapper.setLongitude(shopkeeper.getLongitude());
        return shockWrapper;
    }

    @GetMapping("/list/{shopkeeperId}")
    public ShocksApiWrapper getByShopkeeper(@RequestParam("token")String token,@PathVariable("shopkeeperId")String shopkeeperId){
        if(!TokenUtil.validate(token))
            throw new TokenExpiredException();
        List<String> shopkeeperIds = new LinkedList<>();
        shopkeeperIds.add(shopkeeperId);
        Shopkeeper shopkeeper = getShopkeeperService().getOne(shopkeeperId);
        Stream<Shock> shockStream = getShockService().findByShopkeeper(shopkeeperIds);
        List<ShockWrapper> list = new LinkedList<>();
        shockStream.forEach(
            item->{
                ShockWrapper wrapper = new ShockWrapper();

                wrapper.setShock(item);
                wrapper.setShopkeeperId(shopkeeperId);
                wrapper.setShopkeeperLogo(shopkeeper.getLogo());
                wrapper.setShopkeeperName(shopkeeper.getName());
                wrapper.setLatitude(shopkeeper.getLatitude());
                wrapper.setLongitude(shopkeeper.getLongitude());

                list.add(wrapper);
            }
        );

        ShocksApiWrapper shocksApiWrapper = new ShocksApiWrapper();
        shocksApiWrapper.setShocks(list);
        shocksApiWrapper.setSize(list.size());

        return shocksApiWrapper;
    }
}
