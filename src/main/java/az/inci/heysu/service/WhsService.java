package az.inci.heysu.service;

import az.inci.heysu.model.Whs;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unchecked")
@Service
public class WhsService
{
    private EntityManager entityManager;

    @Autowired
    public void setEntityManager(EntityManager entityManager)
    {
        this.entityManager = entityManager;
    }

    public List<Whs> getWhsListForIsmi()
    {
        List<Whs> result = new ArrayList<>();
        Query query = entityManager.createNativeQuery("""
                 SELECT WHS_CODE, WHS_NAME FROM WHS_MASTER
                 WHERE WHS_CODE LIKE 'N%' OR WHS_CODE LIKE 'S%' AND INACTIVE_FLAG = 0""");

        return getWhsList(result, query);
    }
    public List<Whs> getWhsListForHeybe()
    {
        List<Whs> result = new ArrayList<>();
        Query query = entityManager.createNativeQuery("""
                 SELECT WHS_CODE, WHS_NAME FROM WHS_MASTER
                 WHERE WHS_CODE IN ('N001', 'N002', 'S033', 'S015', 'S038')""");

        return getWhsList(result, query);
    }

    private List<Whs> getWhsList(List<Whs> result, Query query)
    {
        List<Object[]> resultList = query.getResultList();

        for (Object[] item : resultList)
        {
            Whs whs = new Whs();
            whs.setWhsCode((String) item[0]);
            whs.setWhsName((String) item[1]);
            result.add(whs);
        }

        return result;
    }
}
