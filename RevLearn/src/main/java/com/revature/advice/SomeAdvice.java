package com.revature.advice;

import com.revature.models.User;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class SomeAdvice {

    private static final Logger logger= LoggerFactory.getLogger(SomeAdvice.class);

    @ModelAttribute("something")//<-added so advice will trigger when endpoints are hit
    public String checkForLogin(HttpServletRequest everyRequest) throws InterruptedException
    {
        logger.info("");
        logger.info("------"+everyRequest.getRequestURI()+"------");
        return null;
    }
}
