package kz.amihady.app.handler;

import java.util.Map;

public record ErrorResponse(
        Map<String,String> errors
) {
}
