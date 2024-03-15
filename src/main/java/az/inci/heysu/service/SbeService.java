package az.inci.heysu.service;

import az.inci.heysu.model.Sbe;
import jakarta.persistence.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SbeService extends AbstractService {
    public List<Sbe> getSbeList(String bpCode, String invCode)
    {
        List<Sbe> result = new ArrayList<>();
        Query query = em.createNativeQuery("""
                EXEC DBO.SP_SBE_FOR_BP_AND_INV_CODE :BP_CODE, :INV_CODE
                """);
        query.setParameter("BP_CODE", bpCode);
        query.setParameter("INV_CODE", invCode);

        List<Object[]> resultList = query.getResultList();
        for (Object[] item : resultList)
        {
            Sbe sbe = new Sbe();
            sbe.setSbeCode((String) item[0]);
            sbe.setSbeName((String) item[1]);
            sbe.setBranch((String) item[2]);
            sbe.setCc((String) item[3]);
            sbe.setPayMethodCode((String) item[4]);
            result.add(sbe);
        }

        return result;
    }
}
