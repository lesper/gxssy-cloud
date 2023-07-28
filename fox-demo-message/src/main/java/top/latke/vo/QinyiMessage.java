package top.latke.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 通过 Kafka 传递的消息对象
 * */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QinyiMessage {

    private Integer id;
    private String projectName;
}
