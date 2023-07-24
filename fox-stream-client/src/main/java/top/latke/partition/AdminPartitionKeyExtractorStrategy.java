package top.latke.partition;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.binder.PartitionKeyExtractorStrategy;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import top.latke.vo.AdminMessage;

/**
 * 自定义从 Message 中提取 partition key 策略
 */
@Slf4j
@Component
public class AdminPartitionKeyExtractorStrategy implements PartitionKeyExtractorStrategy {

    @Override
    public Object extractKey(Message<?> message) {
        AdminMessage adminMessage = JSON.parseObject(message.getPayload().toString(), AdminMessage.class);

        //自定义提取 key
        String key = adminMessage.getProjectName();
        log.info("SpringCloud Stream Admin Partition Key: [{}]",key);
        return key;
    }

}
