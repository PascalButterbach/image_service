package de.chronies.image_service.exceptions;

import de.chronies.image_service.dto.responses.ApiResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Instant;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiResponseDto> handleException(ApiException e, HttpServletResponse response, HttpServletRequest request) {

        var apiException = ApiResponseDto.builder()
                .status(e.getStatus())
                .message(e.getMessage())
                .time(Instant.now())
                .path(request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE).toString())
                .build();

        return new ResponseEntity<>(apiException, apiException.getStatus());
    }

}
