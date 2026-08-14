package com.saesubam.service;

import com.saesubam.model.ChatMessage;
import com.saesubam.model.Users;
import java.util.List;

public interface ChatMessageService {

    ChatMessage sendMessage(Users sender, Users receiver, String content);

    List<ChatMessage> getChatHistory(Users user1, Users user2);

    long countUnreadMessages(Users user);
}
