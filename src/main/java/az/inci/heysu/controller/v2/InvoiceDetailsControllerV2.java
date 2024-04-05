package az.inci.heysu.controller.v2;

import az.inci.heysu.model.InvoiceDetails;
import az.inci.heysu.model.Response;
import az.inci.heysu.service.InvoiceDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static az.inci.heysu.Utilities.getMessage;

@RestController
@RequestMapping("/v2/invoice")
@Slf4j
public class InvoiceDetailsControllerV2
{
    private InvoiceDetailsService service;

    @Autowired
    public void setService(InvoiceDetailsService service)
    {
        this.service = service;
    }

    @GetMapping("/get-details")
    public ResponseEntity<Response> getOrders()
    {
        try
        {
            List<InvoiceDetails> result = service.getInvoiceDetailsList();
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
