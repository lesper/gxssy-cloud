package top.latke.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.latke.common.TableId;
import top.latke.dao.EcommerceGoodsDao;
import top.latke.goods.DeductGoodsInventory;
import top.latke.goods.GoodsInfo;
import top.latke.goods.SimpleGoodsInfo;
import top.latke.service.IGoodsService;
import top.latke.vo.PageSimpleGoodsInfo;

import java.util.List;

/**
 * 商品微服务相关服务接口实现
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class GoodsServiceImpl implements IGoodsService {

    private final StringRedisTemplate stringRedisTemplate;
    private final EcommerceGoodsDao ecommerceGoodsDao;

    public GoodsServiceImpl(StringRedisTemplate stringRedisTemplate, EcommerceGoodsDao ecommerceGoodsDao) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.ecommerceGoodsDao = ecommerceGoodsDao;
    }

    @Override
    public List<GoodsInfo> getGoodsInfoByTableId(TableId tableId) {
        return null;
    }

    @Override
    public PageSimpleGoodsInfo getSimpleGoodsInfoByPage(int page) {
        return null;
    }

    @Override
    public List<SimpleGoodsInfo> getSimpleGoodsInfoByTableId(TableId tableId) {
        return null;
    }

    @Override
    public Boolean deductGoodsInventory(List<DeductGoodsInventory> deductGoodsInventories) {
        return null;
    }
}
