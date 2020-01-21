package com.jdd.partition.util;

/**
 * @author Administrator
 *
 */
public class JwtUser {
	
	public String getHeadImg() {
		return headImg;
	}

	public void setHeadImg(String headImg) {
		this.headImg = headImg;
	}

	public String getPartnerOrgName() {
		return partnerOrgName;
	}

	public void setPartnerOrgName(String partnerOrgName) {
		this.partnerOrgName = partnerOrgName;
	}

	public int getAudit() {
		return audit;
	}

	public void setAudit(int audit) {
		this.audit = audit;
	}

	public String getPartnerNickName() {
		return partnerNickName;
	}

	public void setPartnerNickName(String partnerNickName) {
		this.partnerNickName = partnerNickName;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(String partnerId) {
		this.partnerId = partnerId;
	}

	public String getCardUserId() {
		return cardUserId;
	}

	public void setCardUserId(String cardUserId) {
		this.cardUserId = cardUserId;
	}

	public String getPartnerUserName() {
		return partnerUserName;
	}

	public void setPartnerUserName(String partnerUserName) {
		this.partnerUserName = partnerUserName;
	}

	private static final long serialVersionUID = 1L;
    private String partnerId;
    private String cardUserId;
    private String partnerUserName;
    private String token;
    private String partnerNickName;
    private int audit;
    private String partnerOrgName;
    private String headImg;
	private String mobile;
	
	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public JwtUser() {
		super();
	}
	
}
