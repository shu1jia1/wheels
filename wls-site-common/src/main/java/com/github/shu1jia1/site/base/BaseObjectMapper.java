package com.github.shu1jia1.site.base;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import com.fasterxml.jackson.databind.ObjectMapper;

public class BaseObjectMapper extends ObjectMapper {

    private static final long serialVersionUID = -4628513537678974273L;

    public BaseObjectMapper(String pattern) {
        super();
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern, Locale.getDefault());
        this.setDateFormat(dateFormat);
        this.setTimeZone(TimeZone.getDefault());
    }

}
