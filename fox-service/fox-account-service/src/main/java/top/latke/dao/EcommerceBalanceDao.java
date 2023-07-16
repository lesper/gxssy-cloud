package top.latke.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import top.latke.entity.EcommerceBalance;

/**
 * EcommerceBalance Dao 接口定义
 */
public interface EcommerceBalanceDao extends JpaRepository<EcommerceBalance,Long> {

    EcommerceBalance findByUserId(String userId);

}
