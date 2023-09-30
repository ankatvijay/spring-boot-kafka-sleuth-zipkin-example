package com.ankat.topology;

import com.ankat.config.TopicProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Printed;
import org.apache.kafka.streams.kstream.Produced;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Slf4j
@RequiredArgsConstructor
@EnableConfigurationProperties(TopicProperties.class)
@Component
public class StreamsTopology {

    @Value("${topic.source}")
    private String sourceTopic;

    @Value("${topic.destination}")
    private String destination;

    private final StreamsBuilder streamsBuilder;
    private final TopicProperties topicProperties;

    @PostConstruct
    public void runStreams() {
        var streams = streamsBuilder.stream(topicProperties.getSource(), Consumed.with(Serdes.String(), Serdes.String()));
        streams.print(Printed.<String, String>toSysOut().withLabel("Streamed Message: "));
        streams.foreach((key, value) -> {
            log.info("Streamed Message with key: {} & value: {}", key, value);
        });
        streams.to(topicProperties.getDestination(), Produced.with(Serdes.String(), Serdes.String()));
    }
}
