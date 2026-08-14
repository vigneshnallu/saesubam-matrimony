package com.saesubam.dao;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.saesubam.model.UserInterest;
import com.saesubam.model.UserInterest.InterestStatus;
import com.saesubam.model.Users;
import com.saesubam.repositories.UserInterestRepository;
import com.saesubam.service.UserInterestService;

@Service
public class UserInterestServiceImpl implements UserInterestService {

    private final UserInterestRepository userInterestRepository;

    @Autowired(required = false)
    private JavaMailSender mailSender;

    @Autowired
    public UserInterestServiceImpl(UserInterestRepository userInterestRepository) {
        this.userInterestRepository = userInterestRepository;
    }

    @Override
    @Transactional
    public UserInterest sendInterest(Users sender, Users receiver) {
        if (sender == null || receiver == null || sender.getId() == null || receiver.getId() == null) {
            return null;
        }

        if (sender.getId().equals(receiver.getId())) {
            throw new IllegalArgumentException("Cannot send interest to yourself");
        }

        List<UserInterest> existingList = userInterestRepository.findBySenderAndReceiver(sender, receiver);
        if (existingList != null && !existingList.isEmpty()) {
            if (existingList.size() > 1) {
                for (int i = 1; i < existingList.size(); i++) {
                    try {
                        userInterestRepository.delete(existingList.get(i));
                    } catch (Exception ignored) {}
                }
            }
            return existingList.get(0);
        }

        UserInterest savedInterest = userInterestRepository.save(new UserInterest(sender, receiver));

        // Trigger Instant Email & WhatsApp Intimation Notifications
        sendInterestEmailNotification(sender, receiver);
        sendInterestWhatsAppNotification(sender, receiver);

        return savedInterest;
    }

    private void sendInterestEmailNotification(Users sender, Users receiver) {
        System.out.println("=================================================");
        System.out.println("📧 [EMAIL INTITATION NOTIFICATION] Sent to " + receiver.getEmail() + " for interest proposal from " + sender.getName());
        System.out.println("=================================================");

        if (mailSender != null && receiver.getEmail() != null) {
            try {
                SimpleMailMessage message = new SimpleMailMessage();
                message.setFrom("saesubam.matrimony@gmail.com");
                message.setTo(receiver.getEmail());
                message.setSubject("💖 New Express Interest: " + sender.getName() + " expressed interest in your profile!");
                message.setText("Dear " + receiver.getName() + ",\n\n"
                        + "Great news! " + sender.getName() + " (" + (sender.getCaste() != null ? sender.getCaste() : "Community") + ") has expressed interest in connecting with your matrimony profile on SaeSubam Matrimony.\n\n"
                        + "To view their full profile and accept or decline this match proposal, please visit:\n"
                        + "http://localhost:8080/interests\n\n"
                        + "Wishing you success in finding your life partner,\n"
                        + "SaeSubam Matrimony Team");
                mailSender.send(message);
                System.out.println("✅ Interest Email Intimation successfully sent to " + receiver.getEmail());
            } catch (Throwable t) {
                System.err.println("⚠️ Could not send Interest Email to " + receiver.getEmail() + ": " + t.getMessage());
            }
        }
    }

    private void sendInterestWhatsAppNotification(Users sender, Users receiver) {
        String waMobile = receiver.getMobile() != null ? receiver.getMobile() : "6369541046";
        String messageText = "Hi " + receiver.getName() + ", " + sender.getName() + " expressed interest in your profile on SaeSubam Matrimony! View proposal: http://localhost:8080/interests";
        
        String waDeepLink = "https://api.whatsapp.com/send?phone=91" + waMobile + "&text=" + java.net.URLEncoder.encode(messageText, java.nio.charset.StandardCharsets.UTF_8);

        System.out.println("=================================================");
        System.out.println("💬 [WHATSAPP INTIMATION NOTIFICATION] Generated for " + waMobile);
        System.out.println("📲 WhatsApp Click-to-Send Link (100% Free): " + waDeepLink);
        System.out.println("=================================================");
    }

    @Override
    @Transactional
    public UserInterest acceptInterest(Long interestId, Users user) {
        UserInterest interest = userInterestRepository.findById(interestId)
                .orElseThrow(() -> new RuntimeException("Interest request not found"));

        if (!interest.getReceiver().getId().equals(user.getId())) {
            throw new IllegalStateException("Unauthorized to accept this interest request");
        }

        interest.setStatus(InterestStatus.ACCEPTED);
        interest.setRespondedAt(LocalDateTime.now());
        UserInterest saved = userInterestRepository.save(interest);

        // Notify Sender that interest was accepted!
        sendAcceptanceNotification(interest.getSender(), user);

        return saved;
    }

    private void sendAcceptanceNotification(Users sender, Users receiver) {
        System.out.println("🎉 [MATCH ACCEPTED NOTIFICATION] " + receiver.getName() + " accepted " + sender.getName() + "'s interest!");
        if (mailSender != null && sender.getEmail() != null) {
            try {
                SimpleMailMessage message = new SimpleMailMessage();
                message.setFrom("saesubam.matrimony@gmail.com");
                message.setTo(sender.getEmail());
                message.setSubject("🎉 Match Accepted! " + receiver.getName() + " accepted your interest proposal!");
                message.setText("Dear " + sender.getName() + ",\n\n"
                        + "Wonderful news! " + receiver.getName() + " has ACCEPTED your interest proposal on SaeSubam Matrimony!\n\n"
                        + "You can now view their contact details and connect directly:\n"
                        + "http://localhost:8080/interests\n\n"
                        + "Best regards,\nSaeSubam Matrimony Team");
                mailSender.send(message);
            } catch (Throwable t) {
                System.err.println("⚠️ Could not send Acceptance Email: " + t.getMessage());
            }
        }
    }

    @Override
    @Transactional
    public UserInterest declineInterest(Long interestId, Users user) {
        UserInterest interest = userInterestRepository.findById(interestId)
                .orElseThrow(() -> new RuntimeException("Interest request not found"));

        if (!interest.getReceiver().getId().equals(user.getId())) {
            throw new IllegalStateException("Unauthorized to decline this interest request");
        }

        interest.setStatus(InterestStatus.DECLINED);
        interest.setRespondedAt(LocalDateTime.now());
        return userInterestRepository.save(interest);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserInterest> getReceivedInterests(Users receiver) {
        List<UserInterest> rawList = userInterestRepository.findByReceiver(receiver);
        List<UserInterest> uniqueList = new ArrayList<>();
        java.util.Set<Long> seenSenderIds = new java.util.HashSet<>();

        for (UserInterest ui : rawList) {
            if (ui != null && ui.getSender() != null) {
                Long sId = ui.getSender().getId();
                if (!seenSenderIds.contains(sId)) {
                    seenSenderIds.add(sId);
                    uniqueList.add(ui);
                }
            }
        }
        return uniqueList;
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserInterest> getSentInterests(Users sender) {
        List<UserInterest> rawList = userInterestRepository.findBySender(sender);
        List<UserInterest> uniqueList = new ArrayList<>();
        java.util.Set<Long> seenReceiverIds = new java.util.HashSet<>();

        for (UserInterest ui : rawList) {
            if (ui != null && ui.getReceiver() != null) {
                Long rId = ui.getReceiver().getId();
                if (!seenReceiverIds.contains(rId)) {
                    seenReceiverIds.add(rId);
                    uniqueList.add(ui);
                }
            }
        }
        return uniqueList;
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserInterest> getAcceptedMatches(Users user) {
        if (user == null || user.getId() == null) return List.of();

        List<UserInterest> accepted = new ArrayList<>();
        java.util.Set<Long> seenPartnerIds = new java.util.HashSet<>();

        // 1. Matches where user is receiver and status is ACCEPTED
        List<UserInterest> received = userInterestRepository.findByReceiverAndStatus(user, InterestStatus.ACCEPTED);
        for (UserInterest ui : received) {
            if (ui != null && ui.getSender() != null && ui.getSender().getId() != null) {
                Long partnerId = ui.getSender().getId();
                if (!seenPartnerIds.contains(partnerId)) {
                    seenPartnerIds.add(partnerId);
                    accepted.add(ui);
                }
            }
        }

        // 2. Matches where user is sender and status is ACCEPTED
        List<UserInterest> sent = userInterestRepository.findBySender(user);
        for (UserInterest ui : sent) {
            if (ui != null && ui.getReceiver() != null && ui.getReceiver().getId() != null && ui.getStatus() == InterestStatus.ACCEPTED) {
                Long partnerId = ui.getReceiver().getId();
                if (!seenPartnerIds.contains(partnerId)) {
                    seenPartnerIds.add(partnerId);
                    accepted.add(ui);
                }
            }
        }

        return accepted;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean hasSentInterest(Users sender, Users receiver) {
        return userInterestRepository.existsBySenderAndReceiver(sender, receiver);
    }

    @Override
    @Transactional(readOnly = true)
    public long countPendingReceivedInterests(Users receiver) {
        return userInterestRepository.countByReceiverAndStatus(receiver, InterestStatus.PENDING);
    }
}
