package top.latke.annotation.advice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import top.latke.vo.CommonResponse;

import javax.servlet.http.HttpServletRequest;

/**
 * 全局异常捕获处理
 */

@Slf4j
@RestControllerAdvice
public class GlobalExceptionAdvice {

    @ExceptionHandler(value = Exception.class)
    public CommonResponse<String> handlerFoxException(HttpServletRequest request, Exception exception){
        CommonResponse<String> response = new CommonResponse<>(-1,"service error");
        response.setData(exception.getMessage());
        log.error("fox service has error: [{}]",exception.getMessage(), exception);
        return response;
    }

}
