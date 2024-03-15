package az.inci.heysu.service;

import az.inci.heysu.model.PrevTrxForReturnRequest;
import az.inci.heysu.model.PrevTrxForReturnResponse;
import az.inci.heysu.model.SaleCreditMemoRequest;
import az.inci.heysu.model.SaleCreditMemoRequestItem;
import jakarta.persistence.Query;
import jakarta.persistence.StoredProcedureQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.ParameterMode.IN;

@Service
public class SaleCreditMemoService extends AbstractService {

    @SuppressWarnings("SqlInsertValues")
    @Transactional
    public void createDoc(SaleCreditMemoRequest request)
    {
        Query query1 = em.createNativeQuery("""
              DELETE FROM TERMINAL_REQ WHERE USER_ID = :USER_ID
                    """);
        query1.setParameter("USER_ID", request.getUserId());
        query1.executeUpdate();

        for (SaleCreditMemoRequestItem item : request.getRequestItems()) {
            Query query2 = em.createNativeQuery("""
                    INSERT INTO TERMINAL_REQ(
                            SYSTEM_NO,
                            SYSTEM_DATE,
                            USER_ID,
                            CC,
                            BRANCH_CODE,
                            WHS_CODE,
                            RETURN_WHS_CODE,
                            SBE_CODE,
                            BP_CODE,
                            PAY_METHOD_CODE,
                            NOTES,
                            TERMINAL,
                            REQ_TYPE,
                            INV_CODE,
                            INV_QTY,
                            PRICE,
                            DISCOUNT,
                            TAX_CODE,
                            PREV_TRX_ID,
                            PREV_TRX_NO)
                            VALUES (
                            :SYSTEM_NO,
                            :SYSTEM_DATE,
                            :USER_ID,
                            :CC,
                            :BRANCH_CODE,
                            :WHS_CODE,
                            :RETURN_WHS_CODE,
                            :SBE_CODE,
                            :BP_CODE,
                            :PAY_METHOD_CODE,
                            :NOTES,
                            :TERMINAL,
                            :REQ_TYPE,
                            :INV_CODE,
                            :INV_QTY,
                            :PRICE,
                            :DISCOUNT,
                            :TAX_CODE,
                            :PREV_TRX_ID,
                            :PREV_TRX_NO)""");
            query2.setParameter("SYSTEM_NO", request.getTerminalTrxNo());
            query2.setParameter("SYSTEM_DATE", request.getTrxDate());
            query2.setParameter("USER_ID", request.getUserId());
            query2.setParameter("CC", request.getCc());
            query2.setParameter("BRANCH_CODE", request.getBranch());
            query2.setParameter("WHS_CODE", request.getWhsCode());
            query2.setParameter("RETURN_WHS_CODE", request.getWhsCode());
            query2.setParameter("SBE_CODE", request.getSbeCode());
            query2.setParameter("BP_CODE", request.getBpCode());
            query2.setParameter("PAY_METHOD_CODE", request.getPayMethodCode());
            query2.setParameter("NOTES", request.getNotes());
            query2.setParameter("TERMINAL", request.getTerminalId());
            query2.setParameter("REQ_TYPE", 5);
            query2.setParameter("INV_CODE", item.getInvCode());
            query2.setParameter("INV_QTY", item.getQty());
            query2.setParameter("PRICE", item.getQty() * item.getPrice());
            query2.setParameter("DISCOUNT", item.getDiscountRatio());
            query2.setParameter("TAX_CODE", item.getTaxCode());
            query2.setParameter("PREV_TRX_ID", item.getPrevTrxId());
            query2.setParameter("PREV_TRX_NO", item.getPrevTrxNo());
            query2.executeUpdate();
        }

        StoredProcedureQuery query3 = em.createStoredProcedureQuery("SP_CREATE_REQUEST_DOC");
        query3.registerStoredProcedureParameter("USER_ID", String.class, IN);
        query3.registerStoredProcedureParameter("REQ_TYPE", Integer.class, IN);
        query3.setParameter("USER_ID", request.getUserId());
        query3.setParameter("REQ_TYPE", 5);
        query3.executeUpdate();

        em.close();
    }

    public List<PrevTrxForReturnResponse> getPrevTrxDataForReturn(PrevTrxForReturnRequest request)
    {
        List<PrevTrxForReturnResponse> response = new ArrayList<>();

        Query q = em.createNativeQuery("EXEC DBO.SP_TRX_FOR_RETURN :BP_CODE,:INV_CODE,:QTY");
        q.setParameter("BP_CODE", request.getBpCode());
        q.setParameter("INV_CODE", request.getInvCode());
        q.setParameter("QTY", request.getQty());
        List<Object[]> resultList;
        try
        {
            resultList = q.getResultList();
        }
        catch(Exception e)
        {
            return new ArrayList<>();
        }

        resultList.stream().map((result)->
        {
            PrevTrxForReturnResponse responseItem = new PrevTrxForReturnResponse();
            responseItem.setTrxId(Integer.parseInt(String.valueOf(result[1])));
            responseItem.setTrxNo(String.valueOf(result[2]));
            responseItem.setTrxDate(String.valueOf(result[3]));
            responseItem.setQty(Double.parseDouble(String.valueOf(result[4])));
            responseItem.setPrice(Double.parseDouble(String.valueOf(result[5])));
            responseItem.setDiscountRatio(Double.parseDouble(String.valueOf(result[6])));
            responseItem.setTrxTypeId(Integer.parseInt(String.valueOf(result[7])));
            responseItem.setTaxCode(String.valueOf(result[8]));
            responseItem.setUom(String.valueOf(result[9]));
            responseItem.setUomFactor(Double.parseDouble(String.valueOf(result[10])));
            return responseItem;
        }).forEachOrdered(response::add);

        em.close();

        return response;
    }
}
