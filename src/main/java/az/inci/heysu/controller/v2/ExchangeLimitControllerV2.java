package az.inci.heysu.controller.v2;

import az.inci.heysu.model.ExchangeInfo;
import az.inci.heysu.model.ExchangeUpdateRequest;
import az.inci.heysu.model.Response;
import az.inci.heysu.service.ExchangeLimitService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static az.inci.heysu.Utilities.getMessage;

@RestController
@RequestMapping("/v2/exchange")
@Slf4j
public class ExchangeLimitControllerV2
{
    private ExchangeLimitService service;

    @Autowired
    public void setService(ExchangeLimitService service)
    {
        this.service = service;
    }

    @PostMapping("/update")
    public ResponseEntity<Response> updateExchangeLimit(@RequestBody ExchangeUpdateRequest request)
    {
        try
        {
            service.updateExchangeLimit(request);
            return ResponseEntity.ok(Response.getSuccessResponse());
        }
        catch (Exception e)
        {
            String message = getMessage(e);
            log.error(message);
            return ResponseEntity.ok(Response.getServerErrorResponse(message));
        }
    }

    @PostMapping("/insert")
    public ResponseEntity<Response> insertExchangeLimit(@RequestBody ExchangeUpdateRequest request)
    {
        try
        {
            service.insertExchangeLimit(request);
            return ResponseEntity.ok(Response.getSuccessResponse());
        }
        catch (Exception e)
        {
            String message = getMessage(e);
            log.error(message);
            return ResponseEntity.ok(Response.getServerErrorResponse(message));
        }
    }
    @GetMapping("/info")
    public ResponseEntity<Response> getInfo()
    {
        try
        {
            List<ExchangeInfo> result = service.getExchangeInfoList();
            return ResponseEntity.ok(Response.getResultResponse(result));
        }
        catch (Exception e)
        {
            String message = getMessage(e);
            log.error(message);
            return ResponseEntity.ok(Response.getServerErrorResponse(message));
        }
    }
}
