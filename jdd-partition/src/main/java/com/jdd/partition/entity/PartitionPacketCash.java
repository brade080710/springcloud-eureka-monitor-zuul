package com.jdd.partition.entity;

    import java.io.Serializable;
    import java.util.Date;

    import com.fasterxml.jackson.annotation.JsonFormat;
    import io.swagger.annotations.ApiModel;
    import io.swagger.annotations.ApiModelProperty;
    import org.springframework.format.annotation.DateTimeFormat;

/**
* <p>
    * 瓜分红包提现表
    * </p>
*
* @author pengbaoning
* @since 2019-12-12
*/
    @ApiModel(value="PartitionPacketCash对象", description="瓜分红包提现表")
    public class PartitionPacketCash implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;

            @ApiModelProperty(value = "瓜分id")
    private String partitionId;

            @ApiModelProperty(value = "用户id")
    private String cardcustId;

            @ApiModelProperty(value = "用户手机号")
    private String custTel;

            @ApiModelProperty(value = "用户姓名")
    private String custName;

    @ApiModelProperty(value = "用户头像")
    private String iconPic;

            @ApiModelProperty(value = "提现金额")
    private Integer amount;

            @ApiModelProperty(value = "工行联名卡号")
    private String mediumId;

            @ApiModelProperty(value = "审核意见")
    private String auditRemark;

            @ApiModelProperty(value = "状态：(0：待审核，1：业务审核通过，2：财务审核通过，3：拒绝)")
    private Integer status;

            @ApiModelProperty(value = "创建时间")
            @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
            @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;

            @ApiModelProperty(value = "修改时间")
            @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
            @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date modifyTime;

            @ApiModelProperty(value = "版本号")
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
        public String getAuditRemark() {
        return auditRemark;
        }

            public void setAuditRemark(String auditRemark) {
        this.auditRemark = auditRemark;
        }
        public Integer getStatus() {
        return status;
        }

            public void setStatus(Integer status) {
        this.status = status;
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
    return "PartitionPacketCash{" +
            "id=" + id +
            ", partitionId=" + partitionId +
            ", cardcustId=" + cardcustId +
            ", custTel=" + custTel +
            ", custName=" + custName +
            ", amount=" + amount +
            ", mediumId=" + mediumId +
            ", auditRemark=" + auditRemark +
            ", status=" + status +
            ", createTime=" + createTime +
            ", modifyTime=" + modifyTime +
            ", version=" + version +
            ", remark=" + remark +
            ", terminal=" + terminal +
    "}";
    }
}
