package top.latke.partition;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.binder.PartitionSelectorStrategy;
import org.springframework.stereotype.Component;

/**
 * 决定 message 发送到哪个分区的策略
 */
@Slf4j
@Component
public class AdminPartitionSelectorStrategy implements PartitionSelectorStrategy {

    /**
     * 选择分区的策略
     * @param key the key
     * @param partitionCount the number of partitions
     * @return
     */
    @Override
    public int selectPartition(Object key, int partitionCount) {
        int partition = key.toString().hashCode() & partitionCount;
        log.info("SpringCloud Stream Admin Selector info: [{}], [{}], [{}]", key.toString(), partitionCount, partition);
        return partition;
    }

}
