package indi.petshop.chatting.controller;

import indi.petshop.chatting.endpoint.ChatEndPoint;
import indi.petshop.chatting.entity.SessionEntity;
import indi.petshop.chatting.entity.Shopkeeper;
import indi.petshop.chatting.entity.Type;
import indi.petshop.chatting.entity.User;
import indi.petshop.chatting.repository.ShopkeeperRepository;
import indi.petshop.chatting.repository.UserRepository;
import indi.petshop.chatting.wrapper.InfoWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author <a href="chan@ittx.com.cn">韩超</a>
 * @version 2019.04.27
 */
@RestController
public class ChatController {

    private ChatEndPoint chatEndPoint;

    private UserRepository userRepository;

    private ShopkeeperRepository shopkeeperRepository;

    public UserRepository getUserRepository() {
        return userRepository;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public ShopkeeperRepository getShopkeeperRepository() {
        return shopkeeperRepository;
    }

    @Autowired
    public void setShopkeeperRepository(ShopkeeperRepository shopkeeperRepository) {
        this.shopkeeperRepository = shopkeeperRepository;
    }

    public ChatEndPoint getChatEndPoint() {
        return chatEndPoint;
    }

    @Autowired
    public void setChatEndPoint(ChatEndPoint chatEndPoint) {
        this.chatEndPoint = chatEndPoint;
    }

    @PostMapping("/chat/{connectType}/{fromId}/{toId}")
    public Map<String, Object> connected(@PathVariable("fromId") String fromId, @PathVariable("toId") String toId, @PathVariable("connectType") int type) {
        Map<String, Object> map = new HashMap<>();
        boolean flag = false;
        switch (type) {
            case 1: {//普通用户之间互连
                flag = getChatEndPoint().userConnectToUser(fromId, toId);
                map.put("flag", flag);
                String str = flag ? "连接成功" : "连接失败";
                map.put("message", str);
            }
            ;
            break;
            case 2: {//普通用户和商家之间互连
                flag = getChatEndPoint().userConnectToKeeper(fromId, toId);
                map.put("flag", flag);
                String str = flag ? "连接成功" : "连接失败";
                map.put("message", str);
            }
            ;
            break;
            case 3: {//商家和普通用户之间互连
                flag = getChatEndPoint().userConnectToKeeper(toId, fromId);
                map.put("flag", flag);
                String str = flag ? "连接成功" : "连接失败";
                map.put("message", str);
            }
            ;
            break;
            default: {
                map.put("flag", flag);
                map.put("message", "连接出错");
            }
        }
        return map;
    }

    @GetMapping("/list/{type}/{id}")
    public List<InfoWrapper> list(@PathVariable("type") int type, @PathVariable("id") String id) {
        List<SessionEntity> entities = getChatEndPoint().getList(type, id);
        Map<Integer,List<String>> typeIdsMap = new HashMap<>();
        if(entities==null)
            return new ArrayList<>();
        entities.forEach(
            sessionEntity -> {
                if(!typeIdsMap.containsKey(sessionEntity.getType()))
                    typeIdsMap.put(sessionEntity.getType(),new LinkedList<>());
                List<String> ids=typeIdsMap.get(sessionEntity.getType());
                ids.add(sessionEntity.getId());
                typeIdsMap.put(sessionEntity.getType(),ids);
            }
        );
        final Map<String,String> idTimeMap = new HashMap<>();
        entities.forEach(
            sessionEntity -> idTimeMap.put(sessionEntity.getId(),sessionEntity.getLastTimeStamp())
        );
        List<InfoWrapper> wrappers = new LinkedList<>();

        typeIdsMap.forEach(
            (key,value)->{
                if(key== Type.USER){
                    wrappers.addAll(getUserRepository().findByIdIn(value).map(
                        user->{
                            InfoWrapper wrapper = translate(user);
                            wrapper.setTimestamp(idTimeMap.get(user.getId()));
                            return wrapper;
                        }
                    ).collect(Collectors.toList()));
                }else if(key==Type.KEEPER){
                    wrappers.addAll(getShopkeeperRepository().findByIdIn(value).map(
                        user->{
                            InfoWrapper wrapper = translate(user);
                            wrapper.setTimestamp(idTimeMap.get(user.getId()));
                            return wrapper;
                        }
                    ).collect(Collectors.toList()));
                }
            }
        );

        return wrappers;
    }

    @GetMapping("/exist/{type}/{id}")
    public Map<String, Object> exist(@PathVariable("type") int type, @PathVariable("id") String id) {
        Map<String, Object> map = new HashMap<>();
        if (getChatEndPoint().exist(type, id) != null) {
            map.put("flag", true);
        } else {
            map.put("flag", false);
            map.put("message", "对方已下线");
        }
        return map;
    }


    @GetMapping("/info/{type}/{id}")
    public Map<String, Object> getInfo(@PathVariable("type") int type, @PathVariable("id") String id) {
        Map<String, Object> map = new HashMap<>();
        if (type == Type.USER) {
            map.put("info", translate(getUserRepository().findOne(id)));
            map.put("type", "user");
            map.put("flag", true);
        } else if (type == Type.KEEPER) {
            map.put("info", translate(getShopkeeperRepository().findOne(id)));
            map.put("type", "keeper");
            map.put("flag", true);
        } else {
            map.put("info", null);
            map.put("type", "");
            map.put("flag", false);
        }
        return map;
    }

    private InfoWrapper translate(User user) {
        InfoWrapper wrapper = new InfoWrapper();
        wrapper.setId(user.getId());
        wrapper.setLogo(user.getLogo());
        wrapper.setName(user.getUsername());
        wrapper.setType(Type.USER);
        return wrapper;
    }

    private InfoWrapper translate(Shopkeeper shopkeeper) {
        InfoWrapper wrapper = new InfoWrapper();
        wrapper.setId(shopkeeper.getId());
        wrapper.setLogo(shopkeeper.getLogo());
        wrapper.setName(shopkeeper.getName());
        wrapper.setType(Type.KEEPER);
        return wrapper;
    }
}
