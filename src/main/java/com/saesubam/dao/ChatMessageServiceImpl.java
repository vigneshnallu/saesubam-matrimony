package com.saesubam.dao;

import com.saesubam.model.ChatMessage;
import com.saesubam.model.Users;
import com.saesubam.repositories.ChatMessageRepository;
import com.saesubam.service.ChatMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ChatMessageServiceImpl implements ChatMessageService {

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @Override
    @Transactional
    public ChatMessage sendMessage(Users sender, Users receiver, String content) {
        if (sender == null || receiver == null || content == null || content.trim().isEmpty()) {
            throw new IllegalArgumentException("Invalid message parameters");
        }
        ChatMessage msg = new ChatMessage(sender, receiver, content.trim());
        return chatMessageRepository.save(msg);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChatMessage> getChatHistory(Users user1, Users user2) {
        if (user1 == null || user2 == null) {
            return List.of();
        }
        return chatMessageRepository.findChatHistory(user1, user2);
    }

    @Override
    @Transactional(readOnly = true)
    public long countUnreadMessages(Users user) {
        if (user == null) return 0;
        return chatMessageRepository.countByReceiverAndIsReadFalse(user);
    }
}
