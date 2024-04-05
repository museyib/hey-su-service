package az.inci.heysu.service;

import az.inci.heysu.model.OrderDetails;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderDetailsService
{
    private EntityManager entityManager;

    @Autowired
    public void setEntityManager(EntityManager entityManager)
    {
        this.entityManager = entityManager;
    }

    @SuppressWarnings("unchecked")
    public List<OrderDetails> getOrderDetailsList()
    {
        List<OrderDetails> result = new ArrayList<>();


        Query query = entityManager.createNativeQuery("""
                SELECT OT.TRX_NO,
                    CAST(OT.TRX_DATE as DATE) AS TRX_DATE,
                    OT.INV_CODE,
                    OT.INV_NAME,
                    OD.BP_CODE,
                    OD.BP_NAME,
                    OD.SBE_CODE,
                    OD.SBE_NAME,
                    OT.QTY,
                    ISNULL(BE.LIMIT, 0) AS LIMIT,
                    ISNULL(BE.QTY, 0) AS EXCH_QTY,
                    ISNULL(BE.VALID_DAYS, 0) AS VALID_DAYS,
                    ISNULL(DATEDIFF(DAY, dbo.FN_GET_LAST_SALE_DATE(OT.INV_CODE,OD.BP_CODE), GETDATE()), 0) AS DAYS_FROM_LAST_SALE,
                    BRS.REC_STATUS_DESC AS REC_STATUS,
                    dbo.FN_GET_DOC_PICK_STATUS_DESC(3, OT.TRX_NO) AS PICK_STATUS
                FROM OINV_TRX OT
                JOIN ORD_DOC OD ON OT.TRX_NO = OD.TRX_NO
                JOIN BMS_REC_STATUS BRS ON BRS.REC_STATUS = OD.REC_STATUS
                JOIN INV_MASTER IM ON OT.INV_CODE = IM.INV_CODE AND IM.RELATED_INV_CODE != ''
                LEFT JOIN BP_EXCH BE ON OD.BP_CODE = BE.BP_CODE
                                        AND IM.RELATED_INV_CODE = BE.INV_CODE
                WHERE OT.TRX_TYPE_ID = 3
                        AND OD.REC_STATUS in (2, 3)
                ORDER BY OT.TRX_NO DESC""");

        List<Object[]> resultList = query.getResultList();

        for (Object[] item : resultList)
        {
            OrderDetails orderDetails = new OrderDetails();
            orderDetails.setTrxNo(String.valueOf(item[0]));
            orderDetails.setTrxDate(String.valueOf(item[1]));
            orderDetails.setInvCode(String.valueOf(item[2]));
            orderDetails.setInvName(String.valueOf(item[3]));
            orderDetails.setBpCode(String.valueOf(item[4]));
            orderDetails.setBpName(String.valueOf(item[5]));
            orderDetails.setSbeCode(String.valueOf(item[6]));
            orderDetails.setSbeName(String.valueOf(item[7]));
            orderDetails.setQuantity((int) Double.parseDouble(String.valueOf(item[8])));
            orderDetails.setExchangeLimit((int) Double.parseDouble((String.valueOf(item[9]))));
            orderDetails.setExchangeQuantity((int) Double.parseDouble((String.valueOf(item[10]))));
            orderDetails.setValidDays((int) Double.parseDouble((String.valueOf(item[11]))));
            orderDetails.setDaysFromLastSale((int) Double.parseDouble((String.valueOf(item[12]))));
            orderDetails.setRecStatus(String.valueOf(item[13]));
            orderDetails.setPickStatus(String.valueOf(item[14]));

            result.add(orderDetails);
        }

        return result;
    }
}
