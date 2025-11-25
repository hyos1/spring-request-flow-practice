//package hyos1.myapp.config;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import hyos1.myapp.common.exception.constant.ErrorCode;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.access.AccessDeniedException;
//import org.springframework.security.web.access.AccessDeniedHandler;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.Map;
//
//@Component
//@RequiredArgsConstructor
//public class CustomAccessDeniedHandler implements AccessDeniedHandler {
//    private final ObjectMapper objectMapper;
//    @Override
//    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
//        // 관리자 권한 기능을 일반 유저가 호출시 권한부족 메세지 응답
//        ErrorCode errorCode = ErrorCode.ACCESS_DENIED;
//
//        response.setStatus(errorCode.getHttpStatus().value());
//        response.setContentType("application/json;charset=UTF-8");
//
//        Map<String, Object> errorResponse = new HashMap<>();
//        errorResponse.put("errorName", errorCode.getHttpStatus().name());
//        errorResponse.put("status", errorCode.getHttpStatus().value());
//        errorResponse.put("message", errorCode.getMessage());
//
//        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
//    }
//}