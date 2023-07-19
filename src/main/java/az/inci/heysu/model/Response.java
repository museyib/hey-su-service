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
}
