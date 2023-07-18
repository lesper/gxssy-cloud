package top.latke.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import top.latke.goods.GoodsInfo;
import top.latke.service.async.AsyncTaskManager;
import top.latke.vo.AsyncTaskInfo;

import java.util.List;

/**
 * 异步任务服务，对外提供的 API
 */
@Api(tags = "商品异步入库服务")
@Slf4j
@RestController
@RequestMapping("/async-goods")
public class AsyncGoodsController {

    private final AsyncTaskManager asyncTaskManager;

    public AsyncGoodsController(AsyncTaskManager asyncTaskManager) {
        this.asyncTaskManager = asyncTaskManager;
    }

    @ApiOperation(value = "导入商品", notes = "导入商品进入到商品表", httpMethod = "POST")
    @PostMapping("/import-goods")
    public AsyncTaskInfo importGoods(@RequestBody List<GoodsInfo> goodsInfos) {
        return asyncTaskManager.submit(goodsInfos);
    }

    @ApiOperation(value = "查询状态", notes = "查询异步任务的执行状态",httpMethod = "GET")
    @GetMapping("/task-info")
    public AsyncTaskInfo importGoods(@RequestParam String taskId) {
        return asyncTaskManager.getTaskInfo(taskId);
    }

}
