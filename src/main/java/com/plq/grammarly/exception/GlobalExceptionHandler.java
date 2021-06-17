package com.plq.grammarly.exception;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;

import com.plq.grammarly.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author plq
 * 全局异常处理 无法处理filter抛出的异常
 * 且只处理请求中，对于类似task后台任务发生的异常不捕捉
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {



    /**
     * 验证不通过的时候一般使用全局异常进行处理。设计到三个类：
     * ConstraintViolationException（方法参数校验异常）如实体类中的@Size注解配置和数据库中该字段的长度不统一等问题
     * MethodArgumentNotValidException（Bean 校验异常）
     * BindException （参数绑定异常）
     * 请求时候不加任何参数：BindException
     *
     * 作者：一行代码一首诗
     * 链接：https://www.jianshu.com/p/578b2d1800e4
     * 来源：简书
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     */

    /**
     * 方法参数校验异常 Validate
     * @param request 请求
     * @param constraintViolationException 约束异常异常
     * @return Result code 400
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    public Result handleValidationException(HttpServletRequest request, ConstraintViolationException constraintViolationException) {
        log.error("异常:" + request.getRequestURI(), constraintViolationException);
        String collect = constraintViolationException.getConstraintViolations().stream().filter(Objects::nonNull)
                .map(cv -> cv.getPropertyPath() + ": " + cv.getMessage())
                .collect(Collectors.joining(", "));
        log.error("请求参数异常！{}", collect);
        return Result.builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .msg(constraintViolationException.getMessage())
                .build();
    }

    /**
     * Bean 校验异常 Validate
     * @param request 请求
     * @param methodArgumentNotValidException Valid注释的参数的验证失败
     * @return Result code 400
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseBody
    public Result methodArgumentValidationHandler(HttpServletRequest request, MethodArgumentNotValidException methodArgumentNotValidException){
        log.error("异常:" + request.getRequestURI(), methodArgumentNotValidException);
//        log.error("请求参数错误！{} ，参数数据：{}", getExceptionDetail(methodArgumentNotValidException), showParams(request));
        Result result = new Result();
        result.setCode(HttpStatus.BAD_REQUEST.value());
        if (!CollectionUtils.isEmpty(methodArgumentNotValidException.getBindingResult().getAllErrors())) {
            result.setMsg(methodArgumentNotValidException.getBindingResult().getAllErrors().get(0).getDefaultMessage());
        } else {
            result.setMsg(methodArgumentNotValidException.getMessage());
        }
        return result;
    }

    /**
     * 绑定异常
     * @param request 请求
     * @param bindException 绑定异常
     * @return Result code 400
     */
    @ExceptionHandler(BindException.class)
    @ResponseBody
    public Result bindException(HttpServletRequest request, BindException bindException) {
        log.error("异常:" + request.getRequestURI(), bindException);
        Map<String, Object> map = new HashMap<>(16);
        List<ObjectError> allErrors = bindException.getBindingResult().getAllErrors();
        allErrors.stream().filter(Objects::nonNull)
                .forEach(objectError -> map.put("请求路径："+request.getRequestURI()+"--请求参数："+(((FieldError) allErrors.get(0)).getField()),objectError.getDefaultMessage()));
        return Result.builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .msg("请求参数绑定失败")
                .data(map)
                .build();
    }


    /**
     * 访问接口参数不全
     * @param request 请求
     * @param missingServletRequestParameterException 丢失参数
     * @return Result code 400
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseBody
    public Result missingServletRequestParameterException(HttpServletRequest request, MissingServletRequestParameterException missingServletRequestParameterException) {
        log.error("异常:" + request.getRequestURI(), missingServletRequestParameterException);
        return Result.builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .msg("请求参数不全")
                .build();
    }

    /**
     * 请求方法方式不支持异常
     * @param request 请求
     * @param httpRequestMethodNotSupportedException 请求方法不支持异常
     * @return Result code 400
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseBody
    public Result httpRequestMethodNotSupportedException(HttpServletRequest request, HttpRequestMethodNotSupportedException httpRequestMethodNotSupportedException) {
        log.error("异常:" + request.getRequestURI(), httpRequestMethodNotSupportedException);
        return Result.builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .msg("请求方式不正确")
                .build();
    }

    /**
     * 登录异常
     * @param request 请求
     * @param badCredentialsException 异常
     * @return Result code 500
     */
    @ExceptionHandler(BadCredentialsException.class)
    @ResponseBody
    public Result otherException(HttpServletRequest request, BadCredentialsException badCredentialsException) {
        log.error("异常:" + request.getRequestURI(), badCredentialsException);
        return Result.builder()
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .msg("用户名或者密码错误")
                .data(getExceptionDetail(badCredentialsException))
                .build();
    }


    /**
     * 其他异常
     * @param request 请求
     * @param exception 异常
     * @return Result code 500
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result otherException(HttpServletRequest request, Exception exception) {
        log.error("异常:" + request.getRequestURI(), exception);
        return Result.builder()
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .msg("系统异常，请联系管理员")
                .data(getExceptionDetail(exception))
                .build();
    }

    /**
     * 异常详情
     * @param e Exception
     * @return string 异常详情
     */
    private String getExceptionDetail(Exception e) {
        StringBuilder stringBuffer = new StringBuilder(e.toString() + "\n");
        StackTraceElement[] messages = e.getStackTrace();
        Arrays.stream(messages).filter(Objects::nonNull)
                .forEach(stackTraceElement -> stringBuffer.append(stackTraceElement.toString() + "\n"));
        return stringBuffer.toString();
    }

    /**
     * 请求参数
     * @param request 请求
     * @return string params
     */
    public String showParams(HttpServletRequest request) {
        StringBuilder stringBuilder=new StringBuilder();
        Enumeration<String> paramNames = request.getParameterNames();
        stringBuilder.append("----------------参数开始-------------------");
        stringBuilder.append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        if(Objects.nonNull(paramNames)){
            while (paramNames.hasMoreElements()) {
                String paramName = paramNames.nextElement();
                String[] paramValues = request.getParameterValues(paramName);
                if (paramValues.length >0) {
                    String paramValue = paramValues[0];
                    if (paramValue.length() != 0) {
                        stringBuilder.append("参数名:").append(paramName).append("参数值:").append(paramValue);
                    }
                }
            }
        }
        stringBuilder.append("----------------参数结束-------------------");
        return stringBuilder.toString();
    }
}
