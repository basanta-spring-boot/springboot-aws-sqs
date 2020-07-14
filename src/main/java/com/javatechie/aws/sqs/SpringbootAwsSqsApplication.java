package com.javatechie.aws.sqs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.aws.autoconfigure.context.ContextStackAutoConfiguration;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.*;


@SpringBootApplication (exclude = {ContextStackAutoConfiguration.class})
@RestController
public class SpringbootAwsSqsApplication {

    private static final Logger LOG = LoggerFactory.getLogger(SpringbootAwsSqsApplication.class);
    
    @Autowired
    private QueueMessagingTemplate queueMessagingTemplate;

    @Value("${cloud.aws.end-point.uri}")
    private String sqsEndPoint;

    @GetMapping("/send/{message}")
    public void sendMessage(@PathVariable String message) {
        queueMessagingTemplate.send(sqsEndPoint,
                MessageBuilder.withPayload(message)
                        .build());
    }

    @SqsListener("mynew_queue")
    public void loadMessage(String message){
           LOG.info("Message read from SQS {} ",message);
    }

    public static void main(String[] args) {
        SpringApplication.run(SpringbootAwsSqsApplication.class, args);
    }

}
