package com.laioffer.tripplanner.config;

import org.postgresql.util.PGobject;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

@WritingConverter
public class StringToPgObjectConverter implements Converter<String, PGobject> {
    @Override
    public PGobject convert(String source) {
        if (source == null) return null;

        PGobject pg = new PGobject();
        try {
            pg.setType("jsonb");
            pg.setValue(source);
            return pg;
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid jsonb value", e);
        }
    }
}
