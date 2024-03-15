package az.inci.heysu.controller.v2;

import az.inci.heysu.model.Response;
import az.inci.heysu.model.Whs;
import az.inci.heysu.service.WhsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static az.inci.heysu.Utilities.getMessage;

@RestController
@RequestMapping("/v2/whs")
@Slf4j
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
            return ResponseEntity.ok(Response.getResultResponse(data));
        }
        catch (Exception e)
        {
            String message = getMessage(e);
            log.error(message);
            return ResponseEntity.ok(Response.getServerErrorResponse(message));
        }
    }

    @GetMapping("/for-heybe")
    public ResponseEntity<Response> getWhsListForHeybe()
    {
        try
        {
            List<Whs> data = service.getWhsListForHeybe();
            return ResponseEntity.ok(Response.getResultResponse(data));
        }
        catch (Exception e)
        {
            String message = getMessage(e);
            log.error(message);
            return ResponseEntity.ok(Response.getServerErrorResponse(message));
        }
    }

    @GetMapping("/for-user")
    public ResponseEntity<Response> getWhsListForUser(@RequestParam("user-id") String userId)
    {
        try
        {
            List<Whs> data = service.getWhsListForUser(userId);
            return ResponseEntity.ok(Response.getResultResponse(data));
        }
        catch (Exception e)
        {
            String message = getMessage(e);
            log.error(message);
            return ResponseEntity.ok(Response.getServerErrorResponse(message));
        }
    }
}
