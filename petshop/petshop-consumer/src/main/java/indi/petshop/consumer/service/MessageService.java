package indi.petshop.consumer.service;

import indi.petshop.consumer.domain.Message;
import indi.petshop.consumer.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author <a href="maimengzzz@gmail.com">韩超</a>
 * @since 2019.02.28
 */
@Service("consumerMessageService")
@Transactional
public class MessageService {

    private MessageRepository messageRepository;

    public MessageRepository getMessageRepository() {
        return messageRepository;
    }

    @Autowired
    public void setMessageRepository(@Qualifier("consumerMessageRepository") MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public Message getOne(String id){
        return getMessageRepository().findOne(id);
    }

    public Message save(Message message){
        return getMessageRepository().save(message);
    }

    public void delete(String id){
        getMessageRepository().delete(id);
    }

    public List<Message> findByUserIn(Iterable<String> users,int currentPage){
        Pageable pageable=new PageRequest(currentPage,10);
        Page<Message> messages=getMessageRepository().findByUserInOrderByTimestampDesc(users,pageable);
        return messages.getContent();
    }

    public Long countByUserIn(Iterable<String> users){
        return getMessageRepository().countByUserIn(users);
    }
}
