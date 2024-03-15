package az.inci.heysu.model;

import lombok.Data;

@Data
public class PrevTrxForReturnResponse {
    private int trxId;
    private String trxNo;
    private String trxDate;
    private double qty;
    private double price;
    private double discountRatio;
    private int trxTypeId;
    private String taxCode;
    private String uom;
    private double uomFactor;

}
