package com.jdd.zuul.config;

import com.jdd.zuul.common.ParameterRequestWrapper;
import com.jdd.zuul.common.ResponseUtil;
import com.jdd.zuul.common.Result;
import com.jdd.zuul.common.ResultCode;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * filterType：返回一个字符串代表过滤器的类型，在zuul中定义了四种不同生命周期的过滤器类型，具体如下：
 * <p>
 * pre：可以在请求被路由之前调用
 * route：在路由请求时候被调用
 * post：在route和error过滤器之后被调用
 * error：处理请求时发生错误时被调用
 * <p>
 * filterOrder：通过int值来定义过滤器的执行顺序
 * <p>
 * shouldFilter：返回一个boolean类型来判断该过滤器是否要执行，所以通过此函数可实现过滤器的开关。返回true，该过滤器总是生效
 */
@Component
public class AccessFilter extends ZuulFilter {
    @Value("${server.port}")
    private String servrePort;

    @Override
    public String filterType() {
        return "pre"; // 前置过滤器
    }

    @Override
    public int filterOrder() {
        return 0;// 优先级为0，数字越大，优先级越低
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() throws ZuulException {

        // 1.获取上下文
        RequestContext context = RequestContext.getCurrentContext();
        // 2.获取 Request
        HttpServletRequest request = context.getRequest();
        ParameterRequestWrapper parameterRequestWrapper = new ParameterRequestWrapper(request);
        String accessToken = parameterRequestWrapper.getParameter("accessToken");
        if (StringUtils.isEmpty(accessToken)) {
            context.setSendZuulResponse(false);
//            context.setResponseBody("非法请求");
//            context.setResponseStatusCode(401);
//            context.getResponse().setHeader("Access-Control-Allow-Origin", "*");
//            context.getResponse().setCharacterEncoding("UTF-8");
//            context.getResponse().setContentType("application/json; charset=utf-8");
            ResponseUtil.returnJson(context.getResponse(), Result.genFailResult("非法请求", ResultCode.UNAUTHORIZED));
            return null;


        }
        System.err.println(servrePort);
        return null;
    }
}
