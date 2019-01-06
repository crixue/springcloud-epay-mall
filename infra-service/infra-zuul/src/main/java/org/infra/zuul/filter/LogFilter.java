package org.infra.zuul.filter;

import java.io.IOException;
import java.nio.charset.Charset;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class LogFilter extends ZuulFilter {

    @Override
    public String filterType() {
        return "post";
    }

    @Override
    public int filterOrder() {
        return 100;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();



        try {
            if (log.isDebugEnabled()) {

                String reqString = StreamUtils.copyToString(request.getInputStream(), Charset.forName("UTF-8"));

                String respString = StreamUtils.copyToString(ctx.getResponseDataStream(), Charset.forName("UTF-8"));
                ctx.setResponseBody(respString);


                log.debug("接受到请求：{}, 参数： {}", request.getServletPath(), reqString);
                log.debug("返回：{}", respString);
            }


        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }

        return null;

    }
}
