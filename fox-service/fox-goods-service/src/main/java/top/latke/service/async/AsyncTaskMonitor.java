package top.latke.service.async;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import top.latke.constant.AsyncTaskStatusEnum;
import top.latke.vo.AsyncTaskInfo;

import java.util.Date;

/**
 * 异步任务执行监控切面
 */
@Slf4j
@Aspect
@Component
public class AsyncTaskMonitor {

    /**
     * 注入异步任务执行管理器
     */
    private final AsyncTaskManager asyncTaskManager;

    public AsyncTaskMonitor(AsyncTaskManager asyncTaskManager) {
        this.asyncTaskManager = asyncTaskManager;
    }

    /**
     * 异步任务执行的环绕切面
     *
     * @param proceedingJoinPoint
     * @return
     */
    @Around("execution(* top.latke.service.async.AsyncServiceImpl.*(..))")
    public Object taskHandle(ProceedingJoinPoint proceedingJoinPoint) {
        //获取 taskId，调用异步任务传入的第二个参数
        String taskId = proceedingJoinPoint.getArgs()[1].toString();
        //获取任务信息，在 taskManager（submit时存在）
        AsyncTaskInfo taskInfo = asyncTaskManager.getTaskInfo(taskId);
        log.info("AsyncTaskManager is monitoring async task: [{}]", taskId);
        //设置为运行状态并重新放入容器
        taskInfo.setStatus(AsyncTaskStatusEnum.RUNNING);
        asyncTaskManager.setTaskInfo(taskInfo);
        AsyncTaskStatusEnum asyncTaskStatusEnum;
        Object result;

        try {
            result = proceedingJoinPoint.proceed();
            asyncTaskStatusEnum = AsyncTaskStatusEnum.SUCCESS;
        } catch (Throwable ex) {
            result = null;
            asyncTaskStatusEnum = AsyncTaskStatusEnum.FAILED;
            log.info("AsyncTaskManager async task [{}] is failed,Error info: [{}]", taskId, ex.getMessage(), ex);
        }

        //设置异步任务其他的信息，再次重新放入容器中
        taskInfo.setEndTime(new Date());
        taskInfo.setStatus(asyncTaskStatusEnum);
        taskInfo.setTotalTime(String.valueOf(taskInfo.getEndTime().getTime() - taskInfo.getStartTime().getTime()));
        asyncTaskManager.setTaskInfo(taskInfo);
        return result;
    }

}
