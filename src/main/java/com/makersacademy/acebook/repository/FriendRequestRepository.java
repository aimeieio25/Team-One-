package com.makersacademy.acebook.repository;

import com.makersacademy.acebook.model.FriendRequest;
import com.makersacademy.acebook.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface FriendRequestRepository extends CrudRepository<FriendRequest, Long> {

    List<FriendRequest> findByReceiverAndStatus(User receiver, String status);

    boolean existsBySenderAndReceiverOrSenderAndReceiver(
            User sender1,
            User receiver1,
            User sender2,
            User receiver2
    );

    List<FriendRequest> findBySenderAndStatusOrReceiverAndStatus(
            User sender,
            String senderStatus,
            User receiver,
            String receiverStatus
    );
}
