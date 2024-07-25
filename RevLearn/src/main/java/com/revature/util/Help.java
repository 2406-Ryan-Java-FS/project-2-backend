package com.revature.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

/*
    Static helper functions to do basic things.
    May need a Util class that is a @Service in case it has to tie into spring beans in any way.
 */
public class Help {
    /**
     * @param anything          to be converted to json for viewing
     * @param indentIt          will toggle pretty indented json
     * @param limitStringLength will toggle limiting the length to 300 characters
     * @returns a json string to view for debugging
     */
    public static String json(Object anything, boolean indentIt, boolean limitStringLength) {
        ObjectMapper m = new ObjectMapper();
        if (indentIt) m.enable(SerializationFeature.INDENT_OUTPUT);

        try {
            String json = m.writeValueAsString(anything);
            return limitStringLength ? json.substring(0, Math.min(json.length(), 300)) + "........" : json;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
