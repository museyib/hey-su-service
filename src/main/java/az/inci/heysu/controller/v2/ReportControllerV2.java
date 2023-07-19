package az.inci.heysu.controller.v2;

import az.inci.heysu.model.MonthlySaleReport;
import az.inci.heysu.model.Response;
import az.inci.heysu.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v2/report")
public class ReportControllerV2
{

    private ReportService service;

    @Autowired
    public void setService(ReportService service) {
        this.service = service;
    }

    @GetMapping("/monthly-sale")
    public ResponseEntity<Response> getSaleReport()
    {
        try
        {
            List<MonthlySaleReport> data = service.getSaleReport();
            return ResponseEntity.ok(Response.builder()
                    .statusCode(0)
                    .data(data)
                    .build());
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return ResponseEntity.ok(Response.builder()
                    .statusCode(1)
                    .systemMessage(e.getMessage())
                    .developerMessage("Server xətası")
                    .build());
        }
    }

    @GetMapping("/sale-from-date-interval")
    public ResponseEntity<Response> getSaleReportFromDateInterval(@RequestParam("start-date") String startDate,
                                                                  @RequestParam("end-date") String endDate)
    {
        try
        {
            List<MonthlySaleReport> data = service.getSaleReportFromDateInterval(startDate, endDate);
            return ResponseEntity.ok(Response.builder()
                    .statusCode(0)
                    .data(data)
                    .build());
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return ResponseEntity.ok(Response.builder()
                    .statusCode(1)
                    .systemMessage(e.getMessage())
                    .developerMessage("Server xətası")
                    .build());
        }
    }
}
