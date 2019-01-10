package org.micro.epay.mall.order.pojo;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

@Table(name = "mmall_refund_info")
public class RefundInfo {
    @Id
    private Integer id;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "order_no")
    private Long orderNo;

    /**
     * 支付平台，1为ali，2为微信
     */
    @Column(name = "pay_platform")
    private Integer payPlatform;

    /**
     * 各个平台的交易号，支付宝参数为trade_no，微信参数为transaction_id
     */
    @Column(name = "platform_number")
    private String platformNumber;

    /**
     * 商户在各个平台的商户号，支付宝参数为buyer_user_id，微信参数为mch_id
     */
    @Column(name = "platform_user_id")
    private String platformUserId;

    /**
     * 用户的登录id，支付宝参数为buyer_logon_id，微信无此参数
     */
    @Column(name = "buy_login_id")
    private String buyLoginId;

    /**
     * 退货的状态
     */
    @Column(name = "refund_status")
    private String refundStatus;

    /**
     * 退货金额
     */
    @Column(name = "refund_fee")
    private BigDecimal refundFee;

    /**
     * 微信的公众账号ID，支付宝无此参数
     */
    @Column(name = "app_id")
    private String appId;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;

    @Column(name = "refund_reason")
    private String refundReason;

    /**
     * 退货细节描述封装成接送、，支付宝回调参数refund_detail_item_list，微信为其他拼装的参数
     */
    @Column(name = "refund_detail_desc")
    private String refundDetailDesc;

    /**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return user_id
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * @param userId
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * @return order_no
     */
    public Long getOrderNo() {
        return orderNo;
    }

    /**
     * @param orderNo
     */
    public void setOrderNo(Long orderNo) {
        this.orderNo = orderNo;
    }

    /**
     * 获取支付平台，1为ali，2为微信
     *
     * @return pay_platform - 支付平台，1为ali，2为微信
     */
    public Integer getPayPlatform() {
        return payPlatform;
    }

    /**
     * 设置支付平台，1为ali，2为微信
     *
     * @param payPlatform 支付平台，1为ali，2为微信
     */
    public void setPayPlatform(Integer payPlatform) {
        this.payPlatform = payPlatform;
    }

    /**
     * 获取各个平台的交易号，支付宝参数为trade_no，微信参数为transaction_id
     *
     * @return platform_number - 各个平台的交易号，支付宝参数为trade_no，微信参数为transaction_id
     */
    public String getPlatformNumber() {
        return platformNumber;
    }

    /**
     * 设置各个平台的交易号，支付宝参数为trade_no，微信参数为transaction_id
     *
     * @param platformNumber 各个平台的交易号，支付宝参数为trade_no，微信参数为transaction_id
     */
    public void setPlatformNumber(String platformNumber) {
        this.platformNumber = platformNumber;
    }

    /**
     * 获取商户在各个平台的商户号，支付宝参数为buyer_user_id，微信参数为mch_id
     *
     * @return platform_user_id - 商户在各个平台的商户号，支付宝参数为buyer_user_id，微信参数为mch_id
     */
    public String getPlatformUserId() {
        return platformUserId;
    }

    /**
     * 设置商户在各个平台的商户号，支付宝参数为buyer_user_id，微信参数为mch_id
     *
     * @param platformUserId 商户在各个平台的商户号，支付宝参数为buyer_user_id，微信参数为mch_id
     */
    public void setPlatformUserId(String platformUserId) {
        this.platformUserId = platformUserId;
    }

    /**
     * 获取用户的登录id，支付宝参数为buyer_logon_id，微信无此参数
     *
     * @return buy_login_id - 用户的登录id，支付宝参数为buyer_logon_id，微信无此参数
     */
    public String getBuyLoginId() {
        return buyLoginId;
    }

    /**
     * 设置用户的登录id，支付宝参数为buyer_logon_id，微信无此参数
     *
     * @param buyLoginId 用户的登录id，支付宝参数为buyer_logon_id，微信无此参数
     */
    public void setBuyLoginId(String buyLoginId) {
        this.buyLoginId = buyLoginId;
    }

    /**
     * 获取退货的状态
     *
     * @return refund_status - 退货的状态
     */
    public String getRefundStatus() {
        return refundStatus;
    }

    /**
     * 设置退货的状态
     *
     * @param refundStatus 退货的状态
     */
    public void setRefundStatus(String refundStatus) {
        this.refundStatus = refundStatus;
    }

    /**
     * 获取退货金额
     *
     * @return refund_fee - 退货金额
     */
    public BigDecimal getRefundFee() {
        return refundFee;
    }

    /**
     * 设置退货金额
     *
     * @param refundFee 退货金额
     */
    public void setRefundFee(BigDecimal refundFee) {
        this.refundFee = refundFee;
    }

    /**
     * 获取微信的公众账号ID，支付宝无此参数
     *
     * @return app_id - 微信的公众账号ID，支付宝无此参数
     */
    public String getAppId() {
        return appId;
    }

    /**
     * 设置微信的公众账号ID，支付宝无此参数
     *
     * @param appId 微信的公众账号ID，支付宝无此参数
     */
    public void setAppId(String appId) {
        this.appId = appId;
    }

    /**
     * @return create_time
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * @param createTime
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * @return update_time
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * @param updateTime
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * @return refund_reason
     */
    public String getRefundReason() {
        return refundReason;
    }

    /**
     * @param refundReason
     */
    public void setRefundReason(String refundReason) {
        this.refundReason = refundReason;
    }

    /**
     * 获取退货细节描述封装成接送、，支付宝回调参数refund_detail_item_list，微信为其他拼装的参数
     *
     * @return refund_detail_desc - 退货细节描述封装成接送、，支付宝回调参数refund_detail_item_list，微信为其他拼装的参数
     */
    public String getRefundDetailDesc() {
        return refundDetailDesc;
    }

    /**
     * 设置退货细节描述封装成接送、，支付宝回调参数refund_detail_item_list，微信为其他拼装的参数
     *
     * @param refundDetailDesc 退货细节描述封装成接送、，支付宝回调参数refund_detail_item_list，微信为其他拼装的参数
     */
    public void setRefundDetailDesc(String refundDetailDesc) {
        this.refundDetailDesc = refundDetailDesc;
    }
}