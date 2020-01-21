package com.jdd.partition.entity;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * <p>
 * 瓜分红包助力明细表
 * </p>
 *
 * @author pengbaoning
 * @since 2019-12-12
 */
@ApiModel(value = "PartitionAssistDetail对象", description = "瓜分红包助力明细表")
public class PartitionAssistDetail implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

    @ApiModelProperty(value = "瓜分id")
    private String partitionId;

    @ApiModelProperty(value = "用户id")
    private String cardcustId;

    @ApiModelProperty(value = "用户openid")
    private String openId;

    @ApiModelProperty(value = "用户头像")
    private String iconPic;

    @ApiModelProperty(value = "被邀请人id")
    private String invitedCardcustId;

    @ApiModelProperty(value = "用户手机号")
    private String custTel;

    @ApiModelProperty(value = "用户姓名")
    private String custName;

    @ApiModelProperty(value = "助力金额")
    private Integer amount;

    @ApiModelProperty(value = "工行联名卡号")
    private String mediumId;

    @ApiModelProperty(value = "助力类型：1.开户，2：注册，3：助力")
    private Integer assistType;

    @ApiModelProperty(value = "开户银行类型：0:其他银行,1.工行")
    private Integer bankType;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @ApiModelProperty(value = "修改时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date modifyTime;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "终端（1，Android， 2，IOS）")
    private Integer terminal;

    @ApiModelProperty(value = "被助力红包用户手机号")
    private String priCustTel;

    @ApiModelProperty(value = "被助力红包用户姓名")
    private String priCustName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPartitionId() {
        return partitionId;
    }

    public void setPartitionId(String partitionId) {
        this.partitionId = partitionId;
    }

    public String getCardcustId() {
        return cardcustId;
    }

    public void setCardcustId(String cardcustId) {
        this.cardcustId = cardcustId;
    }

    public String getInvitedCardcustId() {
        return invitedCardcustId;
    }

    public void setInvitedCardcustId(String invitedCardcustId) {
        this.invitedCardcustId = invitedCardcustId;
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

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getMediumId() {
        return mediumId;
    }

    public void setMediumId(String mediumId) {
        this.mediumId = mediumId;
    }

    public Integer getAssistType() {
        return assistType;
    }

    public void setAssistType(Integer assistType) {
        this.assistType = assistType;
    }

    public Integer getBankType() {
        return bankType;
    }

    public void setBankType(Integer bankType) {
        this.bankType = bankType;
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

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getIconPic() {
        return iconPic;
    }

    public void setIconPic(String iconPic) {
        this.iconPic = iconPic;
    }

    public String getPriCustTel() {
        return priCustTel;
    }

    public void setPriCustTel(String priCustTel) {
        this.priCustTel = priCustTel;
    }

    public String getPriCustName() {
        return priCustName;
    }

    public void setPriCustName(String priCustName) {
        this.priCustName = priCustName;
    }

    @Override
    public String toString() {
        return "PartitionAssistDetail{" +
                "id=" + id +
                ", partitionId=" + partitionId +
                ", cardcustId=" + cardcustId +
                ", invitedCardcustId=" + invitedCardcustId +
                ", custTel=" + custTel +
                ", custName=" + custName +
                ", amount=" + amount +
                ", mediumId=" + mediumId +
                ", assistType=" + assistType +
                ", bankType=" + bankType +
                ", createTime=" + createTime +
                ", modifyTime=" + modifyTime +
                ", remark=" + remark +
                ", terminal=" + terminal +
                "}";
    }
}
