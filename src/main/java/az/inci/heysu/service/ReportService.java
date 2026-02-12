package az.inci.heysu.service;

import az.inci.heysu.model.MonthlySaleReport;
import az.inci.heysu.model.SaleStockReport;
import az.inci.heysu.model.StockReport;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReportService {
    private EntityManager entityManager;

    @Autowired
    public void setEntityManager(EntityManager entityManager)
    {
        this.entityManager = entityManager;
    }

    public List<MonthlySaleReport> getSaleReport()
    {
        List<MonthlySaleReport> result = new ArrayList<>();

        Query query = entityManager.createNativeQuery(
                "SELECT IM.INV_CODE,  " +
                        " IM.INV_NAME,  " +
                        " IM.INV_BRAND_CODE,  " +
                        " ISNULL(IT.QTY, 0) AS QTY,  " +
                        " ISNULL(WS.WHS_QTY, 0) AS WHS_QTY  " +
                        "FROM INV_MASTER IM " +
                        "     LEFT JOIN " +
                        "( " +
                        "    SELECT INV_CODE,  " +
                        "     -SUM(QTY * UOM_FACTOR * DBT_CRD) QTY " +
                        "    FROM IVC_TRX " +
                        "    WHERE TRX_TYPE_ID IN(17, 27) " +
                        "    AND TRX_DATE BETWEEN DATEADD(MONTH, -1, GETDATE()) AND GETDATE() " +
                        "    GROUP BY INV_CODE " +
                        ") IT ON IM.INV_CODE = IT.INV_CODE " +
                        "     LEFT JOIN " +
                        "( " +
                        "    SELECT INV_CODE,  " +
                        "     SUM(WHS_QTY) WHS_QTY " +
                        "    FROM WHS_SUM " +
                        "    GROUP BY INV_CODE " +
                        ") WS ON IM.INV_CODE = WS.INV_CODE " +
                        "WHERE IM.INV_BRAND_CODE IN('SILSIL', 'ION') " +
                        "OR IM.INV_CODE IN('a035398', 'a035338', 'a035335') " +
                        "ORDER BY IM.INV_BRAND_CODE,  " +
                        "   IM.INV_CODE;");

        return getMonthlySaleReports(result, query);
    }

    public List<MonthlySaleReport> getSaleReportFromDateInterval(String startDate, String endDate)
    {
        List<MonthlySaleReport> result = new ArrayList<>();

        Query query = entityManager.createNativeQuery("""
                        SELECT IM.INV_CODE,
                        IM.INV_NAME,
                        IM.INV_BRAND_CODE,
                        ISNULL(IT.QTY, 0) AS QTY,
                        ISNULL(WS_G.WHS_QTY - WS_G.RZV_QTY, 0) AS WHS_QTY_G,
                        ISNULL(WS_T20.WHS_QTY - WS_T20.RZV_QTY, 0) AS WHS_QTY_T20,
                        ISNULL(WS_T29.WHS_QTY - WS_T29.RZV_QTY, 0) AS WHS_QTY_T29,
                        PL_L.PRICE AS PRICE_LST,
                        PL_S.PRICE AS PRICE_STD,
                        PL_P.PRICE AS PRICE_P01,
                        PL_V.PRICE AS PRICE_VP4,
                        ISNULL(WS_G.RZV_QTY, 0) AS RZV_QTY_G,
                        ISNULL(WS_T20.RZV_QTY, 0) AS RZV_QTY_T20,
                        ISNULL(WS_T29.RZV_QTY, 0) AS RZV_QTY_T29,
                        PL_P.MAX_DISCOUNT_RATIO
                        FROM INV_MASTER IM
                            LEFT JOIN
                        (
                           SELECT INV_CODE,
                            -SUM(QTY * UOM_FACTOR * DBT_CRD) QTY
                           FROM IVC_TRX
                           WHERE TRX_TYPE_ID IN(17, 27)
                           AND TRX_DATE BETWEEN :START_DATE AND :END_DATE
                           GROUP BY INV_CODE
                        ) IT ON IM.INV_CODE = IT.INV_CODE
                            LEFT JOIN
                        (
                           SELECT INV_CODE,
                            SUM(WHS_QTY) WHS_QTY,
                            SUM(RZV_QTY) RZV_QTY
                           FROM WHS_SUM
                           WHERE WHS_CODE IN ('01', '11', '13', '15', '20')
                           GROUP BY INV_CODE
                        ) WS_G ON IM.INV_CODE = WS_G.INV_CODE
                            LEFT JOIN
                        (
                           SELECT INV_CODE,
                            SUM(WHS_QTY) WHS_QTY,
                            SUM(RZV_QTY) RZV_QTY
                           FROM WHS_SUM
                           WHERE WHS_CODE IN ('T20')
                           GROUP BY INV_CODE
                        ) WS_T20 ON IM.INV_CODE = WS_T20.INV_CODE
                            LEFT JOIN
                        (
                           SELECT INV_CODE,
                            SUM(WHS_QTY) WHS_QTY,
                            SUM(RZV_QTY) RZV_QTY
                           FROM WHS_SUM
                           WHERE WHS_CODE IN ('T29')
                           GROUP BY INV_CODE
                        ) WS_T29 ON IM.INV_CODE = WS_T29.INV_CODE
                            JOIN PRICE_LIST PL_L ON IM.INV_CODE = PL_L.INV_CODE
                                                        AND PL_L.PRICE_CODE = 'LST'
                            JOIN PRICE_LIST PL_S ON IM.INV_CODE = PL_S.INV_CODE
                                                        AND PL_S.PRICE_CODE = 'STD'
                            JOIN PRICE_LIST PL_P ON IM.INV_CODE = PL_P.INV_CODE
                                                        AND PL_P.PRICE_CODE = 'P01'
                            JOIN PRICE_LIST PL_V ON IM.INV_CODE = PL_V.INV_CODE
                                                        AND PL_V.PRICE_CODE = 'VP4'
                        WHERE IM.PRODUCER_CODE IN('1239')
                        OR IM.INV_CODE IN('a035398', 'a035338', 'a035335', 'a063989',
                        'm001772', 'm001773', 'a063987', 'm001755', 'm001781', 'm001706', 'a063988', 'm001708',
                        'm001676', 'm001677', 'm001678', 'm001679', 'm001680', 'm001681', 'm001682', 'm001683',
                        'm001684', 'm001685', 'm001686', 'm001687', 'm001688', 'm001689', 'm001690', 'm001691')
                        ORDER BY IM.INV_BRAND_CODE,IM.INV_CODE;""");
        query.setParameter("START_DATE", startDate);
        query.setParameter("END_DATE", endDate);

        return getMonthlySaleReports(result, query);
    }

    private List<MonthlySaleReport> getMonthlySaleReports(List<MonthlySaleReport> result, Query query)
    {
        List<Object[]> resultList = query.getResultList();

        for (Object[] item : resultList)
        {
            MonthlySaleReport reportItem = new MonthlySaleReport();
            reportItem.setInvCode((String) item[0]);
            reportItem.setInvName((String) item[1]);
            reportItem.setBrandCode((String) item[2]);
            reportItem.setSaleQty(Double.parseDouble(String.valueOf(item[3])));
            reportItem.setMainWhsQty(Double.parseDouble(String.valueOf(item[4])));
            reportItem.setT20WhsQty(Double.parseDouble(String.valueOf(item[5])));
            reportItem.setT29WhsQty(Double.parseDouble(String.valueOf(item[6])));
            reportItem.setPriceLst(Double.parseDouble(String.valueOf(item[7])));
            reportItem.setPriceStd(Double.parseDouble(String.valueOf(item[8])));
            reportItem.setPriceP01(Double.parseDouble(String.valueOf(item[9])));
            reportItem.setPriceVp4(Double.parseDouble(String.valueOf(item[10])));
            reportItem.setMainRzvQty(Double.parseDouble(String.valueOf(item[11])));
            reportItem.setT20RzvQty(Double.parseDouble(String.valueOf(item[12])));
            reportItem.setT29RzvQty(Double.parseDouble(String.valueOf(item[13])));
            reportItem.setMaxDiscountRatio(Double.parseDouble(String.valueOf(item[14])));

            result.add(reportItem);
        }

        return result;
    }

    public List<StockReport> getIsmiStockReport(String whsCode)
    {
        List<StockReport> result = new ArrayList<>();

        Query query = entityManager.createNativeQuery("""
                        SELECT IM.INV_CODE,
                        IM.INV_NAME,
                        IM.INV_BRAND_CODE,
                        WS.WHS_QTY - WS.RZV_QTY AS WHS_QTY,
                        WS.RZV_QTY,
                        PL_L.PRICE AS PRICE_LST,
                        PL_S.PRICE AS PRICE_STD,
                        PL_P.PRICE AS PRICE_P01,
                        PL_V.PRICE AS PRICE_VP4,
                        PL_P.MAX_DISCOUNT_RATIO
                        FROM INV_MASTER IM
                            JOIN
                        (
                           SELECT INV_CODE,
                            SUM(WHS_QTY) WHS_QTY,
                            SUM(RZV_QTY) RZV_QTY
                           FROM WHS_SUM
                           WHERE WHS_CODE = :WHS_CODE
                           GROUP BY INV_CODE
                        ) WS ON IM.INV_CODE = WS.INV_CODE
                            JOIN PRICE_LIST PL_L ON IM.INV_CODE = PL_L.INV_CODE
                                                        AND PL_L.PRICE_CODE = 'LST'
                            JOIN PRICE_LIST PL_S ON IM.INV_CODE = PL_S.INV_CODE
                                                        AND PL_S.PRICE_CODE = 'STD'
                            JOIN PRICE_LIST PL_P ON IM.INV_CODE = PL_P.INV_CODE
                                                        AND PL_P.PRICE_CODE = 'P01'
                            JOIN PRICE_LIST PL_V ON IM.INV_CODE = PL_V.INV_CODE
                                                        AND PL_V.PRICE_CODE = 'VP4'
                        WHERE IM.PRODUCER_CODE IN('1239')
                        ORDER BY IM.INV_BRAND_CODE,IM.INV_CODE;""");
        return getStockReport(whsCode, result, query);
    }

    public List<StockReport> getHeybeStockReport(String whsCode)
    {
        List<StockReport> result = new ArrayList<>();

        Query query = entityManager.createNativeQuery("""
                        SELECT IM.INV_CODE,
                        IM.INV_NAME,
                        IM.INV_BRAND_CODE,
                        WS.WHS_QTY,
                        WS.RZV_QTY,
                        PL_S.PRICE AS PRICE_STD,
                        PL_P.PRICE AS PRICE_P01,
                        PL_V.PRICE AS PRICE_VP4,
                        PL_P.MAX_DISCOUNT_RATIO
                        FROM INV_MASTER IM
                            JOIN
                        (
                           SELECT INV_CODE,
                            SUM(WHS_QTY) WHS_QTY,
                            SUM(RZV_QTY) RZV_QTY
                           FROM WHS_SUM
                           WHERE WHS_CODE = :WHS_CODE
                           GROUP BY INV_CODE
                        ) WS ON IM.INV_CODE = WS.INV_CODE
                            JOIN PRICE_LIST PL_S ON IM.INV_CODE = PL_S.INV_CODE
                                                        AND PL_S.PRICE_CODE = 'STD'
                            JOIN PRICE_LIST PL_P ON IM.INV_CODE = PL_P.INV_CODE
                                                        AND PL_P.PRICE_CODE = 'P01'
                            JOIN PRICE_LIST PL_V ON IM.INV_CODE = PL_V.INV_CODE
                                                        AND PL_V.PRICE_CODE = 'VP4'
                        WHERE IM.INV_BRAND_CODE IN(N'HEYBƏ')
                        ORDER BY IM.INV_CODE;""");
        return getStockReport(whsCode, result, query);
    }

    private List<StockReport> getStockReport(String whsCode, List<StockReport> result, Query query)
    {
        query.setParameter("WHS_CODE", whsCode);


        List<Object[]> resultList = query.getResultList();

        for (Object[] item : resultList)
        {
            StockReport reportItem = new StockReport();
            reportItem.setInvCode((String) item[0]);
            reportItem.setInvName((String) item[1]);
            reportItem.setBrandCode((String) item[2]);
            reportItem.setWhsQty(Double.parseDouble(String.valueOf(item[3])));
            reportItem.setRzvQty(Double.parseDouble(String.valueOf(item[4])));
            reportItem.setPriceLst(Double.parseDouble(String.valueOf(item[5])));
            reportItem.setPriceStd(Double.parseDouble(String.valueOf(item[6])));
            reportItem.setPriceP01(Double.parseDouble(String.valueOf(item[7])));
            reportItem.setPriceVp4(Double.parseDouble(String.valueOf(item[8])));

            result.add(reportItem);
        }

        return result;
    }

    public List<SaleStockReport> getHeybeSaleStockReport(String whsCode, String startDate, String endDate)
    {
        List<SaleStockReport> result = new ArrayList<>();

        Query query = entityManager.createNativeQuery("""
                        SELECT IM.INV_CODE,
                        IM.INV_NAME,
                        WS.WHS_QTY,
                        WS.RZV_QTY,
                        ISNULL(IT.QTY, 0) SALE_QTY
                        FROM INV_MASTER IM
                            LEFT JOIN
                        (
                           SELECT INV_CODE,
                            -SUM(QTY * UOM_FACTOR * DBT_CRD) QTY
                           FROM IVC_TRX
                           WHERE TRX_TYPE_ID IN(17, 27)
                           AND WHS_CODE = :WHS_CODE
                           AND TRX_DATE BETWEEN :START_DATE AND :END_DATE
                           GROUP BY INV_CODE
                        ) IT ON IM.INV_CODE = IT.INV_CODE
                            JOIN
                        (
                           SELECT INV_CODE,
                            SUM(WHS_QTY) WHS_QTY,
                            SUM(RZV_QTY) RZV_QTY
                           FROM WHS_SUM
                           WHERE WHS_CODE = :WHS_CODE
                           GROUP BY INV_CODE
                        ) WS ON IM.INV_CODE = WS.INV_CODE
                        WHERE IM.INV_BRAND_CODE IN(N'HEYBƏ')
                        ORDER BY IM.INV_CODE;""");
        query.setParameter("WHS_CODE", whsCode);
        query.setParameter("START_DATE", startDate);
        query.setParameter("END_DATE", endDate);
        List<Object[]> resultList = query.getResultList();

        for (Object[] item : resultList)
        {
            SaleStockReport reportItem = new SaleStockReport();
            reportItem.setInvCode((String) item[0]);
            reportItem.setInvName((String) item[1]);
            reportItem.setWhsQty(Double.parseDouble(String.valueOf(item[2])));
            reportItem.setRzvQty(Double.parseDouble(String.valueOf(item[3])));
            reportItem.setSaleQty(Double.parseDouble(String.valueOf(item[4])));

            result.add(reportItem);
        }

        return result;
    }
}
