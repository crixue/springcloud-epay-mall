package org.micro.epay.mall.payB.pojo;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

import lombok.Data;

@Table(name = "mmall_user")
@Data
public class User {
    /**
     * 用户表id
     */
    @Id
    private Integer id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 用户密码，MD5加密
     */
    private String password;

    private String email;

    private String phone;

    /**
     * 找回密码问题
     */
    private String question;

    /**
     * 找回密码答案
     */
    private String answer;

    /**
     * 角色0-管理员,1-普通用户
     */
    private Integer role;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 最后一次更新时间
     */
    @Column(name = "update_time")
    private Date updateTime;

    @Column(name = "current_balance")
    private BigDecimal currentBalance;

    private Integer version;

}