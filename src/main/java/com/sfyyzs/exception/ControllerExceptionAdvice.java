package com.sfyyzs.exception;

import com.sfyyzs.model.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * 全局异常处理
 */
@ResponseBody
@ControllerAdvice
public class ControllerExceptionAdvice {
    private Logger log= LoggerFactory.getLogger(ControllerExceptionAdvice.class);
    @ExceptionHandler(value = Exception.class)
    public Result handleException(Exception e){
        Result result=new Result();
        result.setCode(500);
        result.setMessage("系统异常！");
        result.setSuccess(false);
        log.error("全局异常捕获：{}",e);
        return result;
    }
    @ExceptionHandler(value = BindException.class)
    public Result handleException(BindException e){
        Result result=new Result();
        result.setCode(500);
        result.setSuccess(false);

        BindingResult bindingResult = e.getBindingResult();
        String errorMesssage = "";
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            errorMesssage += fieldError.getDefaultMessage() + "!";
        }
        result.setMessage(errorMesssage);
        log.error("参数验证异常：{}",errorMesssage);
        return result;
    }
}
