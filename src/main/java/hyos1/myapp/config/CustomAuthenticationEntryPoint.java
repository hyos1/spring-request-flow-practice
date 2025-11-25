package hyos1.myapp.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import hyos1.myapp.common.exception.constant.ErrorCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {

        // 토큰이 필요한 기능에 토큰 없이 요청시 응답 메세지 설정
        ErrorCode errorCode = ErrorCode.UNAUTHORIZED;

        response.setStatus(errorCode.getHttpStatus().value());
        response.setContentType("application/json;charset=UTF-8");

        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("errorName", errorCode.getHttpStatus().name());
        errorResponse.put("status", errorCode.getHttpStatus().value());
        errorResponse.put("message", errorCode.getMessage());

        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));

    }
}
