package top.latke.transactional;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * JpaSpringBootUser Dao 接口定义
 * */
public interface SpringBootUserRepository extends JpaRepository<JpaSpringBootUser, Integer> {
}
