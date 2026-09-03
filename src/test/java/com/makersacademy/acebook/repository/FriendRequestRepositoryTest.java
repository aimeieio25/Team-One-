package com.makersacademy.acebook.repository;

import com.makersacademy.acebook.model.FriendRequest;
import com.makersacademy.acebook.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class FriendRequestRepositoryTest {

    @Autowired
    private FriendRequestRepository friendRequestRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void canFindPendingRequestsForReceiver() {
        User sender = new User("sender@example.com", "Sender");
        User receiver = new User("receiver@example.com", "Receiver");

        userRepository.save(sender);
        userRepository.save(receiver);

        FriendRequest request = new FriendRequest(sender, receiver);
        friendRequestRepository.save(request);

        List<FriendRequest> requests =
                friendRequestRepository.findByReceiverAndStatus(
                        receiver,
                        "pending"
                );

        assertEquals(1, requests.size());
        assertEquals(sender.getId(), requests.get(0).getSender().getId());
    }

    @Test
    public void canDetectExistingFriendRequestInEitherDirection() {
        User sender = new User("sender@example.com", "Sender");
        User receiver = new User("receiver@example.com", "Receiver");

        userRepository.save(sender);
        userRepository.save(receiver);

        FriendRequest request = new FriendRequest(sender, receiver);
        friendRequestRepository.save(request);

        boolean exists =
                friendRequestRepository.existsBySenderAndReceiverOrSenderAndReceiver(
                        receiver,
                        sender,
                        sender,
                        receiver
                );

        assertEquals(true, exists);
    }

    @Test
    public void canFindAcceptedFriendship() {
        User sender = new User("sender@example.com", "Sender");
        User receiver = new User("receiver@example.com", "Receiver");

        userRepository.save(sender);
        userRepository.save(receiver);

        FriendRequest request = new FriendRequest(sender, receiver);
        request.setStatus("accepted");
        friendRequestRepository.save(request);

        List<FriendRequest> acceptedRequests =
                friendRequestRepository.findBySenderAndStatusOrReceiverAndStatus(
                        receiver,
                        "accepted",
                        receiver,
                        "accepted"
                );

        assertEquals(1, acceptedRequests.size());
        assertEquals(sender.getId(), acceptedRequests.get(0).getSender().getId());
    }

    @Test
    public void existingFriendCanBeDetectedForSearchFiltering() {
        User currentUser = new User("current@example.com", "Current User");
        User friend = new User("friend@example.com", "Friend User");

        userRepository.save(currentUser);
        userRepository.save(friend);

        FriendRequest request = new FriendRequest(currentUser, friend);
        request.setStatus("accepted");
        friendRequestRepository.save(request);

        boolean relationshipExists =
                friendRequestRepository.existsBySenderAndReceiverOrSenderAndReceiver(
                        currentUser,
                        friend,
                        friend,
                        currentUser
                );

        assertEquals(true, relationshipExists);
    }
}