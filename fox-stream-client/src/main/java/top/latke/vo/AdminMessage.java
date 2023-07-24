package top.latke.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 消息传递对象: SpringCloud Strean + Kafka/RocketMQ
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminMessage {

    private Integer id;
    private String projectName;
    private String org;
    private String author;
    private String version;

    /**
     * 定义一个默认的消息，方便使用
     * @return
     */
    public static AdminMessage defaultMessage(){
        return new AdminMessage(1,
                "fox-stream-client",
                "top.latke",
                "admin",
                "1.0");
    }
}
