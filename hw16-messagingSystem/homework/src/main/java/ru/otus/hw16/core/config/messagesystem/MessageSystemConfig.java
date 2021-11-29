package ru.otus.hw16.core.config.messagesystem;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.hw16.messagesystem.MessageSystem;
import ru.otus.hw16.messagesystem.MessageSystemImpl;

@Configuration
public class MessageSystemConfig {

    @Bean(destroyMethod = "dispose")
    public MessageSystem messageSystem() {
        MessageSystemImpl messageSystem = new MessageSystemImpl();
        messageSystem.start();
        return messageSystem;
    }
}
