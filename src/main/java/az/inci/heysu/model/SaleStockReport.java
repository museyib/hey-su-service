package az.inci.heysu.model;

import lombok.Data;

@Data
public class SaleStockReport
{
    private String invCode;
    private String invName;
    private double whsQty;
    private double rzvQty;
    private double saleQty;
}
