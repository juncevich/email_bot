package com.example.bot.config;

import enums.RoutingKey;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    @Bean
    public DirectExchange direct() {
        return new DirectExchange("bot.direct");
    }

    @Bean
    public Queue messageCandidateQueue() {
        return new Queue("message_candidate_queue");
    }

    @Bean
    public Queue sendEmailQueue() {
        return new Queue("send_email");
    }

    @Bean
    public Queue registrationQueue() {
        return new Queue("registration_queue");
    }

    @Bean
    public Binding bindingSendEmailQueue(DirectExchange direct,
                                         Queue sendEmailQueue) {
        return BindingBuilder.bind(sendEmailQueue)
                .to(direct)
                .with(RoutingKey.SEND_EMAIL.getValue());
    }

    @Bean
    public Binding bindingRegistrationQueue(DirectExchange direct,
                                            Queue registrationQueue) {
        return BindingBuilder.bind(registrationQueue)
                .to(direct)
                .with(RoutingKey.REGISTRATION.getValue());
    }

    @Bean
    public Binding bindingMessageCandidate(DirectExchange direct,
                                           Queue messageCandidateQueue) {
        return BindingBuilder.bind(messageCandidateQueue)
                .to(direct)
                .with(RoutingKey.MESSAGE_CANDIDATE.getValue());
    }
}
