package az.inci.heysu.controller.v2;

import az.inci.heysu.model.PrevTrxForReturnRequest;
import az.inci.heysu.model.PrevTrxForReturnResponse;
import az.inci.heysu.model.Response;
import az.inci.heysu.model.SaleCreditMemoRequest;
import az.inci.heysu.service.SaleCreditMemoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static az.inci.heysu.Utilities.getMessage;

@RequestMapping("/v2/sale-credit-memo")
@RestController
@Slf4j
public class SaleCreditMemoControllerV2 {
    private SaleCreditMemoService service;

    @Autowired
    public void setService(SaleCreditMemoService service) {
        this.service = service;
    }

    @PostMapping(value = "/prev-trx", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResponseEntity<Response> getPrevTrxDataForReturn(@RequestBody PrevTrxForReturnRequest request)
    {
        try
        {
            List<PrevTrxForReturnResponse> result = service.getPrevTrxDataForReturn(request);
            return ResponseEntity.ok(Response.getResultResponse(result));
        }
        catch (Exception e)
        {
            String message = getMessage(e);
            log.error(message);
            return ResponseEntity.ok(Response.getServerErrorResponse(message));
        }
    }

    @PostMapping("/create")
    @ResponseBody
    public ResponseEntity<Response> createDoc(@RequestBody SaleCreditMemoRequest request)
    {
        try
        {
            service.createDoc(request);
            return ResponseEntity.ok(Response.getSuccessResponse());
        }
        catch (Exception e)
        {
            String message = getMessage(e);
            log.error(message);
            return ResponseEntity.ok(Response.getServerErrorResponse(message));
        }
    }
}
