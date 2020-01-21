package com.jdd.partition.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 用户二类账号信息表
 * </p>
 *
 * @author liujunwei
 * @since 2018-08-06
 */
public class IcbcUserAccount implements Serializable {

    @Override
    public String toString() {
        return "IcbcUserAccount [id=" + id + ", cardcustId=" + cardcustId + ", custName=" + custName + ", certNo="
                + certNo + ", validityPeriod=" + validityPeriod + ", bindMedium=" + bindMedium + ", mobileNo="
                + mobileNo + ", certFontImageUrl=" + certFontImageUrl + ", certReverseImageUrl=" + certReverseImageUrl
                + ", mediumId=" + mediumId + ", mediumIdTail=" + mediumIdTail + ", createTime=" + createTime
                + ", eventNo=" + eventNo + ", corpSernoOriginal=" + corpSernoOriginal + ", auditStatus=" + auditStatus
                + ", setPassword=" + setPassword + ", gestureCipher=" + gestureCipher + ", showTrack=" + showTrack
                + ", imageUploadIcbc=" + imageUploadIcbc + "]";
    }

    public String getCorpSernoOriginal() {
        return corpSernoOriginal;
    }

    public void setCorpSernoOriginal(String corpSernoOriginal) {
        this.corpSernoOriginal = corpSernoOriginal;
    }

    public Integer getShowTrack() {
        return showTrack;
    }

    public void setShowTrack(Integer showTrack) {
        this.showTrack = showTrack;
    }

    public Integer getSetPassword() {
        return setPassword;
    }

    public void setSetPassword(Integer setPassword) {
        this.setPassword = setPassword;
    }

    public String getGestureCipher() {
        return gestureCipher;
    }

    public void setGestureCipher(String gestureCipher) {
        this.gestureCipher = gestureCipher;
    }

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private String id;

    /**
     * 用户id
     */
    private String cardcustId;

    /**
     * 用户真实姓名
     */
    private String custName;

    /**
     * 身份证号
     */
    private String certNo;

    /**
     * 证件失效日期
     */
    private String validityPeriod;

    private String bindMedium;

    /**
     * 手机号，只支持11位，不加区号等
     */
    private String mobileNo;

    /**
     * 身份证正面url
     */
    private String certFontImageUrl;

    /**
     * 身份证反面url
     */
    private String certReverseImageUrl;

    /**
     * 工行联名卡号，只有开户需要返回(支持加密，Base64编码)
     */
    private String mediumId;

    /**
     * 工行联名卡号后4位，只有开户需要返回
     */
    private String mediumIdTail;

    /**
     * 创建日期
     */
    private Date createTime;

    /**
     * 工行交易订单号
     */
    private String eventNo;
    /**
     * 原合作方交易单号
     */
    private String corpSernoOriginal;

    /**
     * 审核状态，1：正在审核，2审核通过，3审核不通过
     */
    private Integer auditStatus;
    private Integer setPassword;
    private String gestureCipher;
    private Integer showTrack;
    private Integer imageUploadIcbc;

    public Integer getImageUploadIcbc() {
        return imageUploadIcbc;
    }

    public void setImageUploadIcbc(Integer imageUploadIcbc) {
        this.imageUploadIcbc = imageUploadIcbc;
    }

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
    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }
    public String getCertNo() {
        return certNo;
    }

    public void setCertNo(String certNo) {
        this.certNo = certNo;
    }
    public String getValidityPeriod() {
        return validityPeriod;
    }

    public void setValidityPeriod(String validityPeriod) {
        this.validityPeriod = validityPeriod;
    }
    public String getBindMedium() {
        return bindMedium;
    }

    public void setBindMedium(String bindMedium) {
        this.bindMedium = bindMedium;
    }
    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }
    public String getCertFontImageUrl() {
        return certFontImageUrl;
    }

    public void setCertFontImageUrl(String certFontImageUrl) {
        this.certFontImageUrl = certFontImageUrl;
    }
    public String getCertReverseImageUrl() {
        return certReverseImageUrl;
    }

    public void setCertReverseImageUrl(String certReverseImageUrl) {
        this.certReverseImageUrl = certReverseImageUrl;
    }
    public String getMediumId() {
        return mediumId;
    }

    public void setMediumId(String mediumId) {
        this.mediumId = mediumId;
    }
    public String getMediumIdTail() {
        return mediumIdTail;
    }

    public void setMediumIdTail(String mediumIdTail) {
        this.mediumIdTail = mediumIdTail;
    }
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    public String getEventNo() {
        return eventNo;
    }

    public void setEventNo(String eventNo) {
        this.eventNo = eventNo;
    }
    public Integer getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(Integer auditStatus) {
        this.auditStatus = auditStatus;
    }
}
