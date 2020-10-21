package com.sfyyzs.exception;

import com.sfyyzs.model.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
        log.error("全局异常捕获：{}",e.getMessage(),e.getStackTrace());
        return result;
    }
}
