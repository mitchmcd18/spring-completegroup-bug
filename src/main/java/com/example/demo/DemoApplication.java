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
		messageStore.addMessagesToGroup("test", MessageBuilder.withPayload("1").build());
		messageStore.addMessagesToGroup("test", MessageBuilder.withPayload("2").build());

		return f -> f.aggregate((a) -> a
						.messageStore(messageStore)
						.expireTimeout(10)
						.expireGroupsUponTimeout(true)
						.expireGroupsUponCompletion(true)
						.releaseStrategy(new MessageCountReleaseStrategy(2)))
				.handle(
						Message.class, (m, h)  -> m).channel(new DirectChannel());
	}

}
