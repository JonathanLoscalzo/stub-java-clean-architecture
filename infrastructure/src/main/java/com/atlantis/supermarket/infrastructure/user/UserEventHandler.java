package com.atlantis.supermarket.infrastructure.user;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;

import com.atlantis.supermarket.core.user.event.UserCreatedEvent;

@Service
public class UserEventHandler {

    @Async
    @TransactionalEventListener
    public void handleBankTransferCompletedEvent(UserCreatedEvent event) {
	System.out.println("aloha: " + event);
	//PODRÍA PERSISTIR EL EVENTO.
    }
}