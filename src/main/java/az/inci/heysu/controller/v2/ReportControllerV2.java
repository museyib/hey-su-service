package az.inci.heysu.controller.v2;

import az.inci.heysu.model.MonthlySaleReport;
import az.inci.heysu.model.Response;
import az.inci.heysu.model.SaleStockReport;
import az.inci.heysu.model.StockReport;
import az.inci.heysu.service.ReportService;
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
@RequestMapping("/v2/report")
@Slf4j
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
            List<MonthlySaleReport> result = service.getSaleReport();
            return ResponseEntity.ok(Response.getResultResponse(result));
        }
        catch (Exception e)
        {
            String message = getMessage(e);
            log.error(message);
            return ResponseEntity.ok(Response.getServerErrorResponse(message));
        }
    }

    @GetMapping("/sale-from-date-interval")
    public ResponseEntity<Response> getSaleReportFromDateInterval(@RequestParam("start-date") String startDate,
                                                                  @RequestParam("end-date") String endDate)
    {
        try
        {
            List<MonthlySaleReport> result = service.getSaleReportFromDateInterval(startDate, endDate);
            return ResponseEntity.ok(Response.getResultResponse(result));
        }
        catch (Exception e)
        {
            String message = getMessage(e);
            log.error(message);
            return ResponseEntity.ok(Response.getServerErrorResponse(message));
        }
    }

    @GetMapping("/stock")
    public ResponseEntity<Response> getStockReport(@RequestParam("whs-code") String whsCode)
    {
        try
        {
            List<StockReport> result = service.getIsmiStockReport(whsCode);
            return ResponseEntity.ok(Response.getResultResponse(result));
        }
        catch (Exception e)
        {
            String message = getMessage(e);
            log.error(message);
            return ResponseEntity.ok(Response.getServerErrorResponse(message));
        }
    }

    @GetMapping("/stock-heybe")
    public ResponseEntity<Response> getHeybeStockReport(@RequestParam("whs-code") String whsCode)
    {
        try
        {
            List<StockReport> result = service.getHeybeStockReport(whsCode);
            return ResponseEntity.ok(Response.getResultResponse(result));
        }
        catch (Exception e)
        {
            String message = getMessage(e);
            log.error(message);
            return ResponseEntity.ok(Response.getServerErrorResponse(message));
        }
    }

    @GetMapping("/sale-stock-heybe")
    public ResponseEntity<Response> getHeybeSaleStockReport(@RequestParam("whs-code") String whsCode,
                                                            @RequestParam("start-date") String startDate,
                                                            @RequestParam("end-date") String endDate)
    {
        try
        {
            List<SaleStockReport> result = service.getHeybeSaleStockReport(whsCode, startDate, endDate);
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
