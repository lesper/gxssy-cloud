package top.latke.service.async;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.IterableUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import top.latke.constant.GoodsConstant;
import top.latke.dao.EcommerceGoodsDao;
import top.latke.entity.EcommerceGoods;
import top.latke.goods.GoodsInfo;
import top.latke.goods.SimpleGoodsInfo;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 异步服务接口实现
 */
@Slf4j
@Service
@Transactional
public class AsyncServiceImpl implements IAsyncService{

    private final EcommerceGoodsDao ecommerceGoodsDao;
    private final StringRedisTemplate stringRedisTemplate;

    public AsyncServiceImpl(EcommerceGoodsDao ecommerceGoodsDao, StringRedisTemplate stringRedisTemplate) {
        this.ecommerceGoodsDao = ecommerceGoodsDao;
        this.stringRedisTemplate = stringRedisTemplate;
    }

    /**
     * 异步任务处理
     * 1.将商品信息保存至数据表
     * 2.更新商品缓存
     * @param goodsInfos
     * @param taskId
     */
    @Async("getAsyncExecutor")
    @Override
    public void asyncImportGoods(List<GoodsInfo> goodsInfos, String taskId) {
        log.info("async task running taskId: [{}]",taskId);
        StopWatch watch = StopWatch.createStarted();

        //1.如果是 GoodsInfo 存在重复商品，不保存，直接返回，记录错误日志
        //请求数据是否合法的标记
        boolean isIllegal = false;

        //查询是否重复
        Set<String> goodsJointInfos = new HashSet<>(goodsInfos.size());
        //过滤数据
        List<GoodsInfo> filteredGoodsInfo = new ArrayList<>(goodsInfos.size());
        //过滤非法参数与判定当前请求是否合法
        for (GoodsInfo goodsInfo : goodsInfos) {
            if (goodsInfo.getPrice() <= 0 || goodsInfo.getSupply() <= 0) {
                log.info("goods info is invalid: [{}]", JSON.toJSONString(goodsInfo));
                continue;
            }

            //组合商品信息
            String jointInfo = String.format("%s,%s,%s",goodsInfo.getGoodsCategory(),goodsInfo.getBrandCategory(),goodsInfo.getGoodsName());

            if (goodsJointInfos.contains(jointInfo)) {
                isIllegal = true;
            }

            //加入到两个容器中
            goodsJointInfos.add(jointInfo);
            filteredGoodsInfo.add(goodsInfo);
        }

        //如果存在重复商品或没有需要入库的商品
        if (isIllegal || CollectionUtils.isEmpty(filteredGoodsInfo)) {
            watch.stop();
            log.warn("import nothing: [{}]",JSON.toJSONString(filteredGoodsInfo));
            log.info("check and import goods done: [{}ms]",watch.getTime(TimeUnit.MILLISECONDS));
            return;
        }

        List<EcommerceGoods> ecommerceGoods = filteredGoodsInfo.stream()
                .map(EcommerceGoods::to)
                .collect(Collectors.toList());

        List<EcommerceGoods> targetGoods = new ArrayList<>(ecommerceGoods.size());

        //保存 goodsInfo 前，先判断是否存在重复商品
        ecommerceGoods.forEach(ecommerceGoods1 ->  {
            if (null != ecommerceGoodsDao.
                    findFirst1ByGoodsCategoryAndBrandCategoryAndGoodsName(
                            ecommerceGoods1.getGoodsCategory(),
                            ecommerceGoods1.getBrandCategory(),
                            ecommerceGoods1.getGoodsName()).orElse(null)
            ) {
                return;
            }
            targetGoods.add(ecommerceGoods1);
        });

        //商品信息入库
        List<EcommerceGoods> saveGoods = IterableUtils.toList(ecommerceGoodsDao.saveAll(targetGoods));

        //将信息同步至 Redis 中
        sabeNewGoodsInfoToRedis(saveGoods);
        log.info("save goods info to db and redis: [{}]",saveGoods.size());
        watch.stop();
        log.info("check and import goods success: [{}ms]",watch.getTime(TimeUnit.MILLISECONDS));
    }

    /**
     * 将保存到数据表中的数据缓存至 Redis 中
     * @param savedGoods
     */
    private void sabeNewGoodsInfoToRedis(List<EcommerceGoods> savedGoods) {
        //只存储简单商品信息
        List<SimpleGoodsInfo> simpleGoodsInfos = savedGoods.stream()
                .map(EcommerceGoods::toSimple)
                .collect(Collectors.toList());

        Map<String,String> id2JsonObject = new HashMap<>(simpleGoodsInfos.size());
        simpleGoodsInfos.forEach(simpleGoodsInfo -> {
            id2JsonObject.put(simpleGoodsInfo.getId().toString(),JSON.toJSONString(simpleGoodsInfo));
        });

        //保存 Redis
        stringRedisTemplate.opsForHash().putAll(GoodsConstant.ECOMMERCE_GOODS_DICT_KEY,id2JsonObject);
    }
}
