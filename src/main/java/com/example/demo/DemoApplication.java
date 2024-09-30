package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.aggregator.MessageCountReleaseStrategy;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.store.MessageGroupStore;
import org.springframework.integration.store.SimpleMessageStore;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}


	@Bean
	IntegrationFlow testFlow() {
		MessageGroupStore messageStore = new SimpleMessageStore();
		// Add two messages that 'complete' the aggregation immediately on startup
		messageStore.addMessagesToGroup("test", MessageBuilder.withPayload("1").build());
		messageStore.addMessagesToGroup("test", MessageBuilder.withPayload("2").build());

		return f -> f.aggregate((a) -> a
						.messageStore(messageStore)
						.expireTimeout(1) // Expire immediately
						.releaseStrategy(new MessageCountReleaseStrategy(2))) // The group above is actually 'complete' on startup
				.handle(System.out::println);
	}

}
