package az.inci.heysu.controller.v2;

import az.inci.heysu.model.OrderDetails;
import az.inci.heysu.model.Response;
import az.inci.heysu.service.OrderDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v2/order")
public class OrderDetailsControllerV2
{
    private OrderDetailsService service;

    @Autowired
    public void setService(OrderDetailsService service)
    {
        this.service = service;
    }

    @GetMapping("/get-details")
    public ResponseEntity<Response> getOrders()
    {
        try
        {
            List<OrderDetails> data = service.getOrderDetailsList();
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
