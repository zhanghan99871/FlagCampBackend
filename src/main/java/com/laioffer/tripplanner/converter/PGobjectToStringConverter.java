package com.laioffer.tripplanner.converter;

import org.postgresql.util.PGobject;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

@ReadingConverter
public class PGobjectToStringConverter implements Converter<PGobject, String> {
    
    @Override
    public String convert(PGobject source) {
        if (source == null) {
            return null;
        }
        return source.getValue();
    }
}

