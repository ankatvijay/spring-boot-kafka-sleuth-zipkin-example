package com.ankat.util;

import com.ankat.config.TopicProperties;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class TopicUtil {
    public static String getScenarioTopicName(TopicProperties topicProperties,int scenarioIndex, int topicIndex){
        List<TopicProperties.Scenario> scenarios = topicProperties.getScenarios().stream().toList();
        String topicName = scenarios.get(scenarioIndex).getScenario().get(topicIndex).getName();
        log.info(topicName);
        return topicName;
    }
}
