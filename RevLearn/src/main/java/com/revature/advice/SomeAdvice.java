package com.revature.advice;

import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.nio.charset.StandardCharsets;

@ControllerAdvice
public class SomeAdvice {

    private static final Logger logger= LoggerFactory.getLogger(SomeAdvice.class);

    @ModelAttribute("something")//<-added so advice will trigger when endpoints are hit
    //@Before("@annotation(org.springframework.web.bind.annotation.*) && args(..,@RequestBody requestBody)")
    public String checkForLogin(HttpServletRequest everyRequest)
    {
        try {
            logger.info("");
            logger.info("------"+everyRequest.getRequestURI()+"------");

            //Reads the stream, but then it's not available for the controllers to use
//            ContentCachingRequestWrapper copy=new ContentCachingRequestWrapper(everyRequest);
//            logger.info("body="+copy.getContentAsString());

            //Reads the stream, but then it's not available for the controllers to use
//            ContentCachingRequestWrapper copy=new ContentCachingRequestWrapper(everyRequest);
//            byte[] bytes=copy.getInputStream().readAllBytes();
//            String str=new String(bytes, StandardCharsets.UTF_8);
//            logger.info("body="+str);
//            copy.getInputStream().reset();

            //Reads the stream, but then it's not available for the controllers to use
//            byte[] bytes=everyRequest.getInputStream().readAllBytes();
//            String str=new String(bytes, StandardCharsets.UTF_8);
//            logger.info("body="+str);

        }
        catch (Exception e)
        {
            //keep working anyway
        }

        return null;
    }
}
