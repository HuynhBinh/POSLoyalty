package greendao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table VOUCHER.
 */
public class Voucher {

    private Long id;
    private String voucherCode;
    private Double voucherAmount;
    private String voucherGeneratedTime;
    private Boolean isApplied;
    private Boolean isCustomerApplied;
    private String appliedTime;
    private Long appliedOutletID;
    private String appliedOutletName;
    private Long appliedCompanyID;
    private String appliedCompanyName;
    private Long appliedUserID;
    private String appliedUserName;
    private String appliedBillCode;
    private Long customerID;

    public Voucher() {
    }

    public Voucher(Long id) {
        this.id = id;
    }

    public Voucher(Long id, String voucherCode, Double voucherAmount, String voucherGeneratedTime, Boolean isApplied, Boolean isCustomerApplied, String appliedTime, Long appliedOutletID, String appliedOutletName, Long appliedCompanyID, String appliedCompanyName, Long appliedUserID, String appliedUserName, String appliedBillCode, Long customerID) {
        this.id = id;
        this.voucherCode = voucherCode;
        this.voucherAmount = voucherAmount;
        this.voucherGeneratedTime = voucherGeneratedTime;
        this.isApplied = isApplied;
        this.isCustomerApplied = isCustomerApplied;
        this.appliedTime = appliedTime;
        this.appliedOutletID = appliedOutletID;
        this.appliedOutletName = appliedOutletName;
        this.appliedCompanyID = appliedCompanyID;
        this.appliedCompanyName = appliedCompanyName;
        this.appliedUserID = appliedUserID;
        this.appliedUserName = appliedUserName;
        this.appliedBillCode = appliedBillCode;
        this.customerID = customerID;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getVoucherCode() {
        return voucherCode;
    }

    public void setVoucherCode(String voucherCode) {
        this.voucherCode = voucherCode;
    }

    public Double getVoucherAmount() {
        return voucherAmount;
    }

    public void setVoucherAmount(Double voucherAmount) {
        this.voucherAmount = voucherAmount;
    }

    public String getVoucherGeneratedTime() {
        return voucherGeneratedTime;
    }

    public void setVoucherGeneratedTime(String voucherGeneratedTime) {
        this.voucherGeneratedTime = voucherGeneratedTime;
    }

    public Boolean getIsApplied() {
        return isApplied;
    }

    public void setIsApplied(Boolean isApplied) {
        this.isApplied = isApplied;
    }

    public Boolean getIsCustomerApplied() {
        return isCustomerApplied;
    }

    public void setIsCustomerApplied(Boolean isCustomerApplied) {
        this.isCustomerApplied = isCustomerApplied;
    }

    public String getAppliedTime() {
        return appliedTime;
    }

    public void setAppliedTime(String appliedTime) {
        this.appliedTime = appliedTime;
    }

    public Long getAppliedOutletID() {
        return appliedOutletID;
    }

    public void setAppliedOutletID(Long appliedOutletID) {
        this.appliedOutletID = appliedOutletID;
    }

    public String getAppliedOutletName() {
        return appliedOutletName;
    }

    public void setAppliedOutletName(String appliedOutletName) {
        this.appliedOutletName = appliedOutletName;
    }

    public Long getAppliedCompanyID() {
        return appliedCompanyID;
    }

    public void setAppliedCompanyID(Long appliedCompanyID) {
        this.appliedCompanyID = appliedCompanyID;
    }

    public String getAppliedCompanyName() {
        return appliedCompanyName;
    }

    public void setAppliedCompanyName(String appliedCompanyName) {
        this.appliedCompanyName = appliedCompanyName;
    }

    public Long getAppliedUserID() {
        return appliedUserID;
    }

    public void setAppliedUserID(Long appliedUserID) {
        this.appliedUserID = appliedUserID;
    }

    public String getAppliedUserName() {
        return appliedUserName;
    }

    public void setAppliedUserName(String appliedUserName) {
        this.appliedUserName = appliedUserName;
    }

    public String getAppliedBillCode() {
        return appliedBillCode;
    }

    public void setAppliedBillCode(String appliedBillCode) {
        this.appliedBillCode = appliedBillCode;
    }

    public Long getCustomerID() {
        return customerID;
    }

    public void setCustomerID(Long customerID) {
        this.customerID = customerID;
    }

}
