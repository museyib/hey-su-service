package az.inci.heysu.model;

import lombok.Data;

@Data
public class OrderDetails
{
    private String trxNo;
    private String trxDate;
    private String invCode;
    private String invName;
    private String bpCode;
    private String bpName;
    private String sbeCode;
    private String sbeName;
    private String recStatus;
    private String pickStatus;
    private Integer quantity;
    private Integer exchangeLimit;
    private Integer exchangeQuantity;
    private Integer validDays;
    private Integer daysFromLastSale;
}
