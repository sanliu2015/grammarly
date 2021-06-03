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
     * @param exception
     * @return
     */


    /**
     * 方法参数校验异常 Validate
     * @param request
     * @param ex
     * @return
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseBody
    public Result handleValidationException(HttpServletRequest request, ConstraintViolationException ex) {
        log.error("异常:" + request.getRequestURI(), ex);
        String collect = ex.getConstraintViolations().stream().filter(Objects::nonNull)
                .map(cv -> cv == null ? "null" : cv.getPropertyPath() + ": " + cv.getMessage())
                .collect(Collectors.joining(", "));
        Result<String> Result = new Result();
        log.info("请求参数异常", collect);
        Result.setCode(HttpStatus.BAD_REQUEST.value());
        Result.setMsg(ex.getMessage());
        return Result;
    }

    /**
     * Bean 校验异常 Validate
     * @param request
     * @param exception
     * @return 400
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseBody
    public Result methodArgumentValidationHandler(HttpServletRequest request, MethodArgumentNotValidException exception){
        log.info("异常:" + request.getRequestURI(), exception);
        log.info("请求参数错误！{}",getExceptionDetail(exception),"参数数据：" + showParams(request));
        Result<String> Result = new Result();
        Result.setCode(HttpStatus.BAD_REQUEST.value());
        if (exception.getBindingResult() != null && !CollectionUtils.isEmpty(exception.getBindingResult().getAllErrors())) {
            Result.setMsg(exception.getBindingResult().getAllErrors().get(0).getDefaultMessage());
        } else {
            Result.setMsg(exception.getMessage());
        }
        return Result;
    }

    /**
     * 绑定异常
     * @param request
     * @param pe
     * @return
     */
    @ExceptionHandler(BindException.class)
    @ResponseBody
    public Result bindException(HttpServletRequest request, BindException pe) {
        log.error("异常:" + request.getRequestURI(), pe);
        Result<String> Result = new Result();
        Map map = new HashMap();
        if(pe.getBindingResult()!=null){
            List<ObjectError> allErrors = pe.getBindingResult().getAllErrors();
            allErrors.stream().filter(Objects::nonNull).forEach(objectError -> {
                map.put("请求路径："+request.getRequestURI()+"--请求参数："+(((FieldError) ((FieldError) allErrors.get(0))).getField().toString()),objectError.getDefaultMessage());
            });
        }
        Result.setCode(HttpStatus.BAD_REQUEST.value());
        Result.setMsg("请求参数绑定失败");
        Result.setData(map.toString());
        return Result;
    }


    /**
     * 访问接口参数不全
     * @param request
     * @param pe
     * @return
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseBody
    public Result missingServletRequestParameterException(HttpServletRequest request, MissingServletRequestParameterException pe) {
        log.error("异常:" + request.getRequestURI(), pe);
        Result<String> Result = new Result();
        Result.setCode(HttpStatus.BAD_REQUEST.value());
        Result.setMsg("该请求路径：" + request.getRequestURI() + "下的请求参数不全：" + pe.getMessage());
        return Result;
    }

    /**
     * HttpRequestMethodNotSupportedException
     * @param request
     * @param pe
     * @return
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseBody
    public Result httpRequestMethodNotSupportedException(HttpServletRequest request, HttpRequestMethodNotSupportedException pe) {
        log.error("异常:" + request.getRequestURI(), pe);
        Result<String> Result = new Result();
        Result.setCode(HttpStatus.BAD_REQUEST.value());
        Result.setMsg("请求方式不正确");
        return Result;
    }


    /**
     * 其他异常
     * @param request
     * @param pe
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result otherException(HttpServletRequest request, Exception pe) {
        log.error("异常:" + request.getRequestURI(), pe);
        Result<String> Result = new Result();
        Result.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        Result.setMsg(getExceptionDetail(pe));
        return Result;
    }

    /**
     * 异常详情
     * @param e
     * @return
     */
    private String getExceptionDetail(Exception e) {
        StringBuilder stringBuffer = new StringBuilder(e.toString() + "\n");
        StackTraceElement[] messages = e.getStackTrace();
        Arrays.stream(messages).filter(Objects::nonNull).forEach(stackTraceElement -> {
            stringBuffer.append(stackTraceElement.toString() + "\n");
        });
        return stringBuffer.toString();
    }

    /**
     * 请求参数
     * @param request
     * @return
     */
    public  String showParams(HttpServletRequest request) {
        Map<String,Object> map = new HashMap<String,Object>();
        StringBuilder stringBuilder=new StringBuilder();
        Enumeration paramNames = request.getParameterNames();
        stringBuilder.append("----------------参数开始-------------------");
        stringBuilder.append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        if(Objects.nonNull(paramNames)){
            while (paramNames.hasMoreElements()) {
                String paramName = (String) paramNames.nextElement();
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
