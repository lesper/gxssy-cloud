package top.latke.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import top.latke.vo.QinyiMessage;

import java.util.Optional;

/**
 * Kafka 消费者
 * */
@Slf4j
@Component
public class KafkaConsumer {

    private final ObjectMapper mapper;

    public KafkaConsumer(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    /**
     * 监听 Kafka 消息并消费
     * */
    @KafkaListener(topics = {"qinyi-springboot"}, groupId = "qinyi-springboot-kafka")
    public void listener01(ConsumerRecord<String, String> record) throws Exception {

        String key = record.key();
        String value = record.value();

        QinyiMessage kafkaMessage = mapper.readValue(value, QinyiMessage.class);
        log.info("in listener01 consume kafka message: [{}], [{}]",
                key, mapper.writeValueAsString(kafkaMessage));
    }

    /**
     * 监听 Kafka 消息并消费
     * */
    @KafkaListener(topics = {"qinyi-springboot"}, groupId = "qinyi-springboot-kafka-1")
    public void listener02(ConsumerRecord<?, ?> record) throws Exception {

        Optional<?> _kafkaMessage = Optional.ofNullable(record.value());
        if (_kafkaMessage.isPresent()) {
            Object message = _kafkaMessage.get();
            QinyiMessage kafkaMessage = mapper.readValue(message.toString(),
                    QinyiMessage.class);
            log.info("in listener02 consume kafka message: [{}]",
                    mapper.writeValueAsString(kafkaMessage));
        }
    }
}
