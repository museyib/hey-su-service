package az.inci.heysu.service;

import az.inci.heysu.model.DeliverDetails;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DeliverDetailsService
{
    private EntityManager entityManager;

    @Autowired
    public void setEntityManager(EntityManager entityManager)
    {
        this.entityManager = entityManager;
    }

    @SuppressWarnings("unchecked")
    public List<DeliverDetails> getDeliverDetailsList()
    {
        List<DeliverDetails> result = new ArrayList<>();


        Query query = entityManager.createNativeQuery("""
                SELECT IT.TRX_NO,
                    CAST(IT.TRX_DATE as DATE) AS TRX_DATE,
                    IT.INV_CODE,
                    IT.INV_NAME,
                    AD.BP_CODE,
                    AD.BP_NAME,
                    AD.SBE_CODE,
                    AD.SBE_NAME,
                    IT.QTY
                FROM INV_TRX IT
                JOIN ACC_DOC AD ON IT.TRX_NO = AD.TRX_NO
                JOIN INV_MASTER IM ON IT.INV_CODE = IM.INV_CODE AND IM.RELATED_INV_CODE != ''
                LEFT JOIN BP_EXCH BE ON AD.BP_CODE = BE.BP_CODE
                                        AND IM.RELATED_INV_CODE = BE.INV_CODE
                WHERE IT.TRX_TYPE_ID = 25
                        AND AD.REC_STATUS = 3
                ORDER BY IT.TRX_NO DESC""");

        List<Object[]> resultList = query.getResultList();

        for (Object[] item : resultList)
        {
            DeliverDetails details = new DeliverDetails();
            details.setTrxNo(String.valueOf(item[0]));
            details.setTrxDate(String.valueOf(item[1]));
            details.setInvCode(String.valueOf(item[2]));
            details.setInvName(String.valueOf(item[3]));
            details.setBpCode(String.valueOf(item[4]));
            details.setBpName(String.valueOf(item[5]));
            details.setSbeCode(String.valueOf(item[6]));
            details.setSbeName(String.valueOf(item[7]));
            details.setQuantity((int) Double.parseDouble(String.valueOf(item[8])));

            result.add(details);
        }

        return result;
    }
}
