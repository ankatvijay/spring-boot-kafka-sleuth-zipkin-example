package com.ankat.topology;

import com.ankat.config.TopicProperties;
import com.ankat.suplier.MessageTransformerSupplier;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Produced;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Slf4j
@RequiredArgsConstructor
@EnableConfigurationProperties(TopicProperties.class)
@Component
public class Scenario1StreamsTopology {

    private final StreamsBuilder scenario1StreamsBuilderFactoryBean;
    private final TopicProperties topicProperties;

    @PostConstruct
    public void runStreams() {
        var streams = scenario1StreamsBuilderFactoryBean.stream(topicProperties.getScenarios().get(1).getScenario().get(0).getName(), Consumed.with(Serdes.String(), Serdes.String()));
        streams.transform(new MessageTransformerSupplier());
        streams.to(topicProperties.getScenarios().get(1).getScenario().get(1).getName(), Produced.with(Serdes.String(), Serdes.String()));
    }
}
