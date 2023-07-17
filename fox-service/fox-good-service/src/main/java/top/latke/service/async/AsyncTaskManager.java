package top.latke.service.async;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import top.latke.constant.AsyncTaskStatusEnum;
import top.latke.goods.GoodsInfo;
import top.latke.vo.AsyncTaskInfo;

import java.util.*;

/**
 * 异步任务执行管理器
 * 对异步任务进行包装管理，记录并塞入异步任务的执行信息
 */
@Slf4j
@Component
public class AsyncTaskManager {

    /**
     * 异步任务执行信息容器
     */
    private final Map<String, AsyncTaskInfo> taskContainer = new HashMap<>(16);

    private final IAsyncService asyncService;

    public AsyncTaskManager(IAsyncService asyncService) {
        this.asyncService = asyncService;
    }

    /**
     * 初始化异步任务
     * @return
     */
    public AsyncTaskInfo initTask() {
        AsyncTaskInfo taskInfo = new AsyncTaskInfo();
        taskInfo.setTaskId(UUID.randomUUID().toString());
        taskInfo.setStatus(AsyncTaskStatusEnum.STARTED);
        taskInfo.setStartTime(new Date());
        taskContainer.put(taskInfo.getTaskId(),taskInfo);
        return taskInfo;
    }

    /**
     * 提交异步任务
     * @param goodsInfos
     * @return
     */
    public AsyncTaskInfo submit(List<GoodsInfo> goodsInfos) {
        //初始化一个异步任务的监控信息
        AsyncTaskInfo taskInfo = initTask();
        asyncService.asyncImportGoods(goodsInfos,taskInfo.getTaskId());
        return taskInfo;
    }

    /**
     * 设置异步任务执行状态信息
     * @param taskInfo
     */
    public void setTaskInfo(AsyncTaskInfo taskInfo) {
        taskContainer.put(taskInfo.getTaskId(),taskInfo);
    }

    /**
     * 获取异步任务执行状态信息
     * @param taskId
     */
    public AsyncTaskInfo getTaskInfo(String taskId) {
        return taskContainer.get(taskId);
    }
}
