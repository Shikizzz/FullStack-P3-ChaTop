package com.P3.ChaTop.service;

import com.P3.ChaTop.model.DTO.message.PutMessage;
import com.P3.ChaTop.model.Message;
import com.P3.ChaTop.repository.MessageRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;

@Service
public class MessageService {
    private MessageRepository messageRepository;
    private ModelMapper modelMapper;

    public MessageService(MessageRepository messageRepository, ModelMapper modelMapper) {
        this.messageRepository = messageRepository;
        this.modelMapper = modelMapper;
    }

    public void postMessage(PutMessage putMessage) {
        Message message = modelMapper.map(putMessage, Message.class);
        Timestamp now = new Timestamp(new Date().getTime());
        message.setCreated_at(now);
        message.setUpdated_at(now);
        messageRepository.save(message);
    }
}
