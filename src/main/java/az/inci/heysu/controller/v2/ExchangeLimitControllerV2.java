package az.inci.heysu.controller.v2;

import az.inci.heysu.model.ExchangeInfo;
import az.inci.heysu.model.ExchangeUpdateRequest;
import az.inci.heysu.model.Response;
import az.inci.heysu.service.ExchangeLimitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v2/exchange")
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
            return ResponseEntity.ok(Response.builder()
                                             .statusCode(0)
                                             .developerMessage("Məlumat uğurla yeniləndi")
                                             .build());
        }
        catch (Exception e)
        {
            return ResponseEntity.ok(Response.builder()
                                             .statusCode(1)
                                             .systemMessage(e.getMessage())
                                             .developerMessage("Server xətası")
                                             .build());
        }
    }

    @PostMapping("/insert")
    public ResponseEntity<Response> insertExchangeLimit(@RequestBody ExchangeUpdateRequest request)
    {
        try
        {
            service.insertExchangeLimit(request);
            return ResponseEntity.ok(Response.builder()
                                             .statusCode(0)
                                             .developerMessage("Məlumat uğurla daxil edildi")
                                             .build());
        }
        catch (Exception e)
        {
            return ResponseEntity.ok(Response.builder()
                                             .statusCode(1)
                                             .systemMessage(e.getMessage())
                                             .developerMessage("Server xətası")
                                             .build());
        }
    }
    @GetMapping("/info")
    public ResponseEntity<Response> getInfo()
    {
        try
        {
            List<ExchangeInfo> data = service.getExchangeInfoList();
            return ResponseEntity.ok(Response.builder()
                                             .statusCode(0)
                                             .data(data)
                                             .build());
        }
        catch (Exception e)
        {
            return ResponseEntity.ok(Response.builder()
                                             .statusCode(1)
                                             .systemMessage(e.getMessage())
                                             .developerMessage("Server xətası")
                                             .build());
        }
    }
}
