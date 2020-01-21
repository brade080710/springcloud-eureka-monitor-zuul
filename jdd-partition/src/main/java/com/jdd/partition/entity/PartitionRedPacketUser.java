package com.jdd.partition.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.Version;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * <p>
 * 助力红包用户基本信息表
 * </p>
 *
 * @author pengbaoning
 * @since 2019-12-12
 */
@ApiModel(value = "PartitionRedPacketUser对象", description = "助力红包用户基本信息表")
public class PartitionRedPacketUser implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    @ApiModelProperty(value = "用户id")
    private String cardcustId;

    @ApiModelProperty(value = "用户手机号")
    private String custTel;

    @ApiModelProperty(value = "用户姓名")
    private String custName;

    @ApiModelProperty(value = "用户头像")
    private String iconPic;

    @ApiModelProperty(value = "初始金额")
    private Integer initAmout;

    @ApiModelProperty(value = "当前总金额")
    private Integer totalAmount;

    @ApiModelProperty(value = "工行联名卡号")
    private String mediumId;

    @ApiModelProperty(value = "瓜分专属码")
    private String partitionCode;

    @ApiModelProperty(value = "瓜分分享链接")
    private String partitionUrl;

    @ApiModelProperty(value = "瓜分分享短链接")
    private String partitionShortUrl;

    @ApiModelProperty(value = "失效时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date expireTime;

    @ApiModelProperty(value = "状态（-1：不可用(过期)，0：待兑换，1：可用，2：审核失败，3：兑换完成）")
    private Integer status;

    @ApiModelProperty(value = "链接点击次数")
    private Integer clickNum;

    @ApiModelProperty(value = "二维码扫码次数")
    private Integer scanNum;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date modifyTime;

    @ApiModelProperty(value = "版本号")
    @Version
    private Integer version;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "终端（1，Android， 2，IOS）")
    private Integer terminal;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCardcustId() {
        return cardcustId;
    }

    public void setCardcustId(String cardcustId) {
        this.cardcustId = cardcustId;
    }

    public String getCustTel() {
        return custTel;
    }

    public void setCustTel(String custTel) {
        this.custTel = custTel;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public Integer getInitAmout() {
        return initAmout;
    }

    public void setInitAmout(Integer initAmout) {
        this.initAmout = initAmout;
    }

    public Integer getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Integer totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getMediumId() {
        return mediumId;
    }

    public void setMediumId(String mediumId) {
        this.mediumId = mediumId;
    }

    public String getPartitionCode() {
        return partitionCode;
    }

    public void setPartitionCode(String partitionCode) {
        this.partitionCode = partitionCode;
    }

    public String getPartitionUrl() {
        return partitionUrl;
    }

    public void setPartitionUrl(String partitionUrl) {
        this.partitionUrl = partitionUrl;
    }

    public String getPartitionShortUrl() {
        return partitionShortUrl;
    }

    public void setPartitionShortUrl(String partitionShortUrl) {
        this.partitionShortUrl = partitionShortUrl;
    }

    public Date getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getClickNum() {
        return clickNum;
    }

    public void setClickNum(Integer clickNum) {
        this.clickNum = clickNum;
    }

    public Integer getScanNum() {
        return scanNum;
    }

    public void setScanNum(Integer scanNum) {
        this.scanNum = scanNum;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getTerminal() {
        return terminal;
    }

    public void setTerminal(Integer terminal) {
        this.terminal = terminal;
    }

    public String getIconPic() {
        return iconPic;
    }

    public void setIconPic(String iconPic) {
        this.iconPic = iconPic;
    }

    @Override
    public String toString() {
        return "PartitionRedPacketUser{" +
                "id=" + id +
                ", cardcustId=" + cardcustId +
                ", custTel=" + custTel +
                ", custName=" + custName +
                ", initAmout=" + initAmout +
                ", totalAmount=" + totalAmount +
                ", mediumId=" + mediumId +
                ", partitionCode=" + partitionCode +
                ", partitionUrl=" + partitionUrl +
                ", partitionShortUrl=" + partitionShortUrl +
                ", expireTime=" + expireTime +
                ", status=" + status +
                ", clickNum=" + clickNum +
                ", scanNum=" + scanNum +
                ", createTime=" + createTime +
                ", modifyTime=" + modifyTime +
                ", version=" + version +
                ", remark=" + remark +
                ", terminal=" + terminal +
                "}";
    }
}
