package az.inci.heysu.service;

import az.inci.heysu.model.ExchangeInfo;
import az.inci.heysu.model.ExchangeUpdateRequest;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ExchangeLimitService
{
    private EntityManager entityManager;

    @Autowired
    public void setEntityManager(EntityManager entityManager)
    {
        this.entityManager = entityManager;
    }

    @Transactional
    public void updateExchangeLimit(ExchangeUpdateRequest request)
    {
        Query query = entityManager.createNativeQuery("""
                UPDATE BP_EXCH SET LIMIT = :LIMIT,
                                    VALID_DAYS = :VALID_DAYS
                WHERE BP_CODE = :BP_CODE
                    AND INV_CODE IN (dbo.FN_GET_RELATED_INV_CODE(:INV_CODE), :INV_CODE)""");
        query.setParameter("LIMIT", request.getExchangeLimit());
        query.setParameter("VALID_DAYS", request.getValidDays());
        query.setParameter("BP_CODE", request.getBpCode());
        query.setParameter("INV_CODE", request.getInvCode());
        query.executeUpdate();
    }

    @Transactional
    public void insertExchangeLimit(ExchangeUpdateRequest request)
    {
        Query query = entityManager.createNativeQuery("""
                                  INSERT INTO BP_EXCH(
                                  LIMIT,
                                  VALID_DAYS,
                                  BP_CODE,
                                  INV_CODE)
                                  VALUES (
                                  :LIMIT,
                                  :VALID_DAYS,
                                  :BP_CODE,
                                  dbo.FN_GET_RELATED_INV_CODE(:INV_CODE))""");
        query.setParameter("LIMIT", request.getExchangeLimit());
        query.setParameter("VALID_DAYS", request.getValidDays());
        query.setParameter("BP_CODE", request.getBpCode());
        query.setParameter("INV_CODE", request.getInvCode());
        query.executeUpdate();
    }

    @SuppressWarnings("unchecked")
    public List<ExchangeInfo> getExchangeInfoList()
    {
        List<ExchangeInfo> result = new ArrayList<>();

        Query query = entityManager.createNativeQuery("""
                SELECT BE.INV_CODE,
                    IM.INV_NAME,
                    BE.BP_CODE,
                    BM.BP_NAME,
                    BE.LIMIT,
                    BE.QTY,
                    BE.VALID_DAYS,
                    CASE
                        WHEN BE.QTY > 0
                        THEN ISNULL(DATEDIFF(DAY, dbo.FN_GET_LAST_SALE_DATE(BE.INV_CODE,BE.BP_CODE), GETDATE()), 0)
                        ELSE 0
                    END AS [DAYS_FROM_LAST_SALE],
                    ISNULL(BM.PHONE1, '') AS PHONE,
                    ISNULL(BM.ADDRESS1, '') AS ADDRESS
                FROM BP_EXCH BE
                JOIN INV_MASTER IM ON BE.INV_CODE = IM.INV_CODE
                JOIN BP_MASTER BM ON BE.BP_CODE = BM.BP_CODE
                ORDER BY BE.LIMIT - BE.QTY, [DAYS_FROM_LAST_SALE]""");

        List<Object[]> resultList = query.getResultList();

        for (Object[] item : resultList)
        {
            ExchangeInfo exchangeInfo = new ExchangeInfo();
            exchangeInfo.setInvCode(String.valueOf(item[0]));
            exchangeInfo.setInvName(String.valueOf(item[1]));
            exchangeInfo.setBpCode(String.valueOf(item[2]));
            exchangeInfo.setBpName(String.valueOf(item[3]));
            exchangeInfo.setExchangeLimit((int) Double.parseDouble((String.valueOf(item[4]))));
            exchangeInfo.setExchangeQuantity((int) Double.parseDouble((String.valueOf(item[5]))));
            exchangeInfo.setValidDays((int) Double.parseDouble((String.valueOf(item[6]))));
            exchangeInfo.setDaysFromLastSale((int) Double.parseDouble((String.valueOf(item[7]))));
            exchangeInfo.setPhone(String.valueOf(item[8]));
            exchangeInfo.setAddress(String.valueOf(item[9]));

            result.add(exchangeInfo);
        }

        return result;
    }
}
