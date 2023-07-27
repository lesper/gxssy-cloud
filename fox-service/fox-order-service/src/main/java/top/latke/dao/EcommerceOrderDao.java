package top.latke.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import top.latke.entity.EcommerceOrder;

/**
 * EcommerceOrder Dao 接口定义
 */
public interface EcommerceOrderDao extends PagingAndSortingRepository<EcommerceOrder,Long> {

    /**
     * 根据 userId 查询分页订单
     * @param userId
     * @param pageable
     * @return
     */
    Page<EcommerceOrder> findAllByUserId(Long userId, Pageable pageable);


}
