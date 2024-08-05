package com.revature.controllers;

import com.revature.util.Help;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
    Attempted to display all the controllers endpoints using reflection. it can't be done.
    Swagger 2 wasn't dropping in very easily either.
 */
@RestController
public class ShowEndpointsController {

    private static final Logger logger= LoggerFactory.getLogger(ShowEndpointsController.class);

    @GetMapping("ends")
    public String ends() throws Exception
    {
        StringBuilder html=new StringBuilder();

        List<Class> classes=new ArrayList<>();
        classes.add(CourseController.class);
        classes.add(EnrollmentController.class);
        classes.add(QuizAttemptsController.class);
        classes.add(QuizController.class);
        classes.add(QuizQuestionController.class);
        classes.add(UserController.class);
        classes.add(UserController2.class);

        html.append("<html><body><h2>Endpoint URLs and Methods</h2>");
        html.append("<table border=\"1\">");
        html.append("<tr><th>Method</th><th>URL</th></tr>");

        for(Class c:classes) {
            Method[] ms=c.getMethods();
            for(Method m:ms) {
                Annotation[] ans=m.getAnnotations();
                Class rt=m.getReturnType();
                if(ans.length>0) {

                    html.append("<tr>");
                    html.append("<td>" + ans[0] + "</td>");

                    //Show response object
                    Constructor[] cons = rt.getConstructors();
                    if (cons.length > 0)
                    {
                        logger.info("cons="+cons.length);
                        logger.info("cons[0]="+cons[0]);
                        logger.info("cons[0].getParameterCount()="+cons[0].getParameterCount());
                        Object ri=null;

                        switch(cons[0].getParameterCount())
                        {
                            case 0: ri = cons[0].newInstance(); break;
                            case 1: ri = cons[0].newInstance(null); break;
                            case 2: ri = cons[0].newInstance(null,null); break;
                            case 3: ri = cons[0].newInstance(null,null,null); break;
                        }
                        logger.info("cons="+ri);

                        html.append("<td>" + Help.json(ri, true, true) + "</td>");
                    }

                    html.append("</tr>");
                }
            }
        }


        return html.toString();
    }

}
