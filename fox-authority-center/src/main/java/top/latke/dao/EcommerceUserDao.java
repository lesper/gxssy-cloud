package top.latke.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import top.latke.entity.EcommerceUser;

/**
 * EcommerceUser Dao 接口定义
 */
public interface EcommerceUserDao extends JpaRepository<EcommerceUser, Long> {

    EcommerceUser findByUsername(String username);

    EcommerceUser findByUsernameAndPassword(String username, String password);

}
