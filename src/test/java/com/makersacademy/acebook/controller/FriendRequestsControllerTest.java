package com.makersacademy.acebook.controller;

import com.makersacademy.acebook.model.FriendRequest;
import com.makersacademy.acebook.model.User;
import com.makersacademy.acebook.repository.FriendRequestRepository;
import com.makersacademy.acebook.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class FriendRequestsControllerTest {

    @Autowired
    FriendRequestRepository friendRequestRepository;

    @Autowired
    UserRepository userRepository;

    @Test
    public void canAcceptFriendRequest() {
        User sender = new User("sender@example.com", "Sender");
        User receiver = new User("receiver@example.com", "Receiver");

        userRepository.save(sender);
        userRepository.save(receiver);

        FriendRequest request = new FriendRequest(sender, receiver);
        friendRequestRepository.save(request);

        FriendRequestsController controller = new FriendRequestsController();
        controller.friendRequestRepository = friendRequestRepository;

        controller.accept(request.getId());

        FriendRequest updatedRequest =
                friendRequestRepository.findById(request.getId()).orElseThrow();

        assertEquals("accepted", updatedRequest.getStatus());
    }

    @Test
    public void canDenyFriendRequest() {
        User sender = new User("sender2@example.com", "Sender");
        User receiver = new User("receiver2@example.com", "Receiver");

        userRepository.save(sender);
        userRepository.save(receiver);

        FriendRequest request = new FriendRequest(sender, receiver);
        friendRequestRepository.save(request);

        FriendRequestsController controller = new FriendRequestsController();
        controller.friendRequestRepository = friendRequestRepository;

        controller.deny(request.getId());

        boolean requestStillExists =
                friendRequestRepository.existsById(request.getId());

        assertEquals(false, requestStillExists);
    }

    @Test
    public void canRemoveFriend() {
        User sender = new User("sender3@example.com", "Sender");
        User receiver = new User("receiver3@example.com", "Receiver");

        userRepository.save(sender);
        userRepository.save(receiver);

        FriendRequest request = new FriendRequest(sender, receiver);
        request.setStatus("accepted");
        friendRequestRepository.save(request);

        FriendRequestsController controller = new FriendRequestsController();
        controller.friendRequestRepository = friendRequestRepository;

        controller.deleteFriend(request.getId());

        boolean requestStillExists =
                friendRequestRepository.existsById(request.getId());

        assertEquals(false, requestStillExists);
    }



}