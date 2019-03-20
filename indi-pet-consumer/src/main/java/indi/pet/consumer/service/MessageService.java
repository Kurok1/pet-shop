package indi.pet.consumer.service;

import indi.pet.consumer.domain.Message;
import indi.pet.consumer.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * @author <a href="maimengzzz@gmail.com">韩超</a>
 * @since 2019.02.28
 */
@Service("messageService")
public class MessageService {

    private MessageRepository messageRepository;

    public MessageRepository getMessageRepository() {
        return messageRepository;
    }

    @Autowired
    public void setMessageRepository(@Qualifier("messageRepository") MessageRepository messageRepository) {
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

    /**
     * to do:根据地理位置获取附件消息列表
     */
}
