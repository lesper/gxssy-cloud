package top.latke.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import top.latke.entity.EcommerceLogistics;

/**
 * EcommerceLogistics Dao 接口定义
 */
public interface EcommerceLogisticsDao extends JpaRepository<EcommerceLogistics,Long> {


}
