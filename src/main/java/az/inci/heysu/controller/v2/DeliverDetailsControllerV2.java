package az.inci.heysu.controller.v2;

import az.inci.heysu.model.DeliverDetails;
import az.inci.heysu.model.Response;
import az.inci.heysu.service.DeliverDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static az.inci.heysu.Utilities.getMessage;

@RestController
@RequestMapping("/v2/deliver")
@Slf4j
public class DeliverDetailsControllerV2
{
    private DeliverDetailsService service;

    @Autowired
    public void setService(DeliverDetailsService service)
    {
        this.service = service;
    }

    @GetMapping("/get-details")
    public ResponseEntity<Response> getOrders()
    {
        try
        {
            List<DeliverDetails> result = service.getDeliverDetailsList();
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
