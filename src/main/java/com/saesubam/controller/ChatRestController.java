package com.saesubam.controller;

import com.saesubam.model.ChatMessage;
import com.saesubam.model.Users;
import com.saesubam.service.ChatMessageService;
import com.saesubam.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/chat")
public class ChatRestController {

    @Autowired
    private ChatMessageService chatMessageService;

    @Autowired
    private UserService userService;

    private Users getLoggedInUser(HttpSession session) {
        Users sessionUser = (Users) session.getAttribute("loggedInUser");
        if (sessionUser != null) {
            return userService.getUserById(sessionUser.getId());
        }
        return null;
    }

    @GetMapping("/messages")
    public ResponseEntity<?> getChatHistory(@RequestParam Long partnerId, HttpSession session) {
        Users currentUser = getLoggedInUser(session);
        if (currentUser == null) {
            return ResponseEntity.status(401).body(Map.of("error", "Unauthorized"));
        }

        Users partner = userService.getUserById(partnerId);
        if (partner == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Partner user not found"));
        }

        List<ChatMessage> history = chatMessageService.getChatHistory(currentUser, partner);
        List<Map<String, Object>> result = new ArrayList<>();
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("hh:mm a");

        for (ChatMessage msg : history) {
            Map<String, Object> item = new HashMap<>();
            item.put("id", msg.getId());
            item.put("senderId", msg.getSender().getId());
            item.put("senderName", msg.getSender().getName());
            item.put("content", msg.getContent());
            item.put("time", msg.getTimestamp() != null ? msg.getTimestamp().format(timeFormatter) : "");
            item.put("isMe", msg.getSender().getId().equals(currentUser.getId()));
            result.add(item);
        }

        return ResponseEntity.ok(result);
    }

    @PostMapping("/send")
    public ResponseEntity<?> sendMessage(@RequestParam Long partnerId, @RequestParam String content, HttpSession session) {
        Users currentUser = getLoggedInUser(session);
        if (currentUser == null) {
            return ResponseEntity.status(401).body(Map.of("error", "Unauthorized"));
        }

        Users partner = userService.getUserById(partnerId);
        if (partner == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Partner user not found"));
        }

        if (content == null || content.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Message content cannot be empty"));
        }

        ChatMessage saved = chatMessageService.sendMessage(currentUser, partner, content);

        Map<String, Object> resp = new HashMap<>();
        resp.put("success", true);
        resp.put("id", saved.getId());
        resp.put("content", saved.getContent());
        resp.put("time", saved.getTimestamp() != null ? saved.getTimestamp().format(DateTimeFormatter.ofPattern("hh:mm a")) : "");

        return ResponseEntity.ok(resp);
    }
}
