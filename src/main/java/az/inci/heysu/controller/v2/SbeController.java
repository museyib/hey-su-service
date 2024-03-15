package az.inci.heysu.controller.v2;

import az.inci.heysu.model.Response;
import az.inci.heysu.model.Sbe;
import az.inci.heysu.service.SbeService;
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
@RequestMapping("/v2/sbe")
@Slf4j
public class SbeController {
    private SbeService service;

    @Autowired
    public void setService(SbeService service) {
        this.service = service;
    }

    @GetMapping("/list")
    public ResponseEntity<Response> getSbeList(@RequestParam("bp-code") String bpCode,
                                               @RequestParam("inv-code") String invCode)
    {
        try
        {
            List<Sbe> result = service.getSbeList(bpCode, invCode);
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
