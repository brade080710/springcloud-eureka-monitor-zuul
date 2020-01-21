package com.jdd.partition.config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;

import com.jdd.partition.common.JddException;
import com.jdd.partition.common.Result;
import com.jdd.partition.common.ResultCode;



    /**
     * @ClassName: AppErrorController
     * @Description: TODO(应用错误处理类)
     * @author 刘军伟
     * @date 2019年4月26日
     *
     */
    
@Controller
public class AppErrorController implements ErrorController {
	private static final Logger logger = LoggerFactory.getLogger(AppErrorController.class);
    private static final String ERROR_PATH = "/error";
    @Override
    public String getErrorPath() {
        return ERROR_PATH;
    }

    /**
     * Web页面错误处理
     */
    @RequestMapping(value = ERROR_PATH, produces = "text/html")
    public String errorPageHandler(HttpServletRequest request, HttpServletResponse response) {
        int status = response.getStatus();
        switch (status) {
            case 403:
                return "403";
            case 404:
                return "404";
            case 500:
                return "500";
        }
        return "index";
    }

    /**
     * 除Web页面外的错误处理，比如Json/XML等
     */
    @RequestMapping(value = ERROR_PATH)
    @ResponseBody
    @ExceptionHandler(value = {Exception.class})
    public Result<String> errorApiHandler(HttpServletRequest request, final Exception ex, final WebRequest req) {
    	logger.error(ex.getMessage());
    	if (ex instanceof JddException) {
    		JddException exception = (JddException)ex;
    		return Result.genFailResult(exception.getMessage(), exception.getCode());
		}
    	
        return Result.genFailResult(ex.getMessage(), ResultCode.NOT_FOUND);
    }

   
}