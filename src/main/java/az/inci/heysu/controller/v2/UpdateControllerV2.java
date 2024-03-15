package az.inci.heysu.controller.v2;

import az.inci.heysu.model.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static az.inci.heysu.Utilities.getMessage;

@RequestMapping("/v2")
@RestController
@Slf4j
public class UpdateControllerV2
{

    @GetMapping("/download")
    @ResponseBody
    public ResponseEntity<Response> download(@RequestParam("file-name") String fileName)
    {
        try
        {
            File file = new File(fileName.concat(".apk"));
            Path path = Paths.get(file.getPath());
            byte[] result = Files.readAllBytes(path);
            return ResponseEntity.ok(Response.getResultResponse(result));
        }
        catch(IOException e)
        {
            String message = getMessage(e);
            log.error(message);
            return ResponseEntity.ok(Response.getServerErrorResponse(message));
        }
    }
}
