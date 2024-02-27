package az.inci.heysu.controller.v2;

import az.inci.heysu.model.Response;
import az.inci.heysu.model.Whs;
import az.inci.heysu.service.WhsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v2/whs")
public class WhsControllerV2
{
    private WhsService service;

    @Autowired
    public void setService(WhsService service)
    {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<Response> getWhsList()
    {
        try
        {
            List<Whs> data = service.getWhsListForIsmi();
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

    @GetMapping("/for-heybe")
    public ResponseEntity<Response> getWhsListForHeybe()
    {
        try
        {
            List<Whs> data = service.getWhsListForHeybe();
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
