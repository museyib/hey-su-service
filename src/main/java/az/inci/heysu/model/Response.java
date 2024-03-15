package az.inci.heysu.model;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Response
{
    private int statusCode;
    private String systemMessage;
    private String developerMessage;
    private Object data;


    public static Response getResultResponse(Object result)
    {
        return Response.builder()
                .statusCode(0)
                .data(result)
                .build();
    }

    public static Response getSuccessResponse()
    {
        return Response.builder()
                .statusCode(0)
                .developerMessage("Uğurlu əməliyyat")
                .build();
    }

    public static Response getServerErrorResponse(String message)
    {
        return Response.builder()
                .statusCode(1)
                .systemMessage(message)
                .developerMessage("Server xətası")
                .build();
    }

    public static Response getUserErrorResponse(String message)
    {
        return Response.builder()
                .statusCode(2)
                .developerMessage(message)
                .build();
    }
}
