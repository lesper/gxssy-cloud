package top.latke.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 用户表实体类定义
 */
@Data
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "t_user")
public class EcommerceUser implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",nullable = false)
    private Long id;

    @Column(name = "username",nullable = false)
    private String username;

    @Column(name = "password",nullable = false)
    private String password;

    @Column(name = "extra_info",nullable = false)
    private String extraInfo;

    @CreatedDate
    @Column(name = "create_time",nullable = false)
    private Date createTime;

    @LastModifiedDate
    @Column(name = "update_time",nullable = false)
    private Date updateTime;

}
