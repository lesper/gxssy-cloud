package top.latke.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import top.latke.entity.EcommerceAddress;

import java.util.List;

/**
 * EcommerceAddress Dao 接口定义
 */
public interface EcommerceAddressDao extends JpaRepository<EcommerceAddress,Long> {

    List<EcommerceAddress> findAllByUserId(Long userId);

}
