package top.latke.notifier;

import de.codecentric.boot.admin.server.domain.entities.Instance;
import de.codecentric.boot.admin.server.domain.entities.InstanceRepository;
import de.codecentric.boot.admin.server.domain.events.InstanceEvent;
import de.codecentric.boot.admin.server.domain.events.InstanceStatusChangedEvent;
import de.codecentric.boot.admin.server.notify.AbstractEventNotifier;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

/**
 * AbstractEventNotifier 事件通知器,实现它可以实现发送定制化的邮件告警,类似spring-boot-starter-mail
 * 自定义告警
 */

@Slf4j
@Component
public class FoxNotifier extends AbstractEventNotifier {

    protected FoxNotifier(InstanceRepository instanceRepository) {
        super(instanceRepository);
    }

    /**
     * 实现对事件的通知
     * @param event
     * @param instance
     * @return
     */
    @Override
    protected Mono<Void> doNotify(InstanceEvent event, Instance instance) {
        return Mono.fromRunnable(() -> {
            //当状态发生改变
            if (event instanceof InstanceStatusChangedEvent) {
                log.info("Instance Status Change: [{}],[{}],[{}]",instance.getRegistration().getName(),event.getInstance(),((InstanceStatusChangedEvent) event).getStatusInfo().getStatus());
            } else {
                log.info("Instance Info: [{}],[{}].[{}]",instance.getRegistration().getName(),event.getInstance(),event.getType());
            }
        });
    }
}
