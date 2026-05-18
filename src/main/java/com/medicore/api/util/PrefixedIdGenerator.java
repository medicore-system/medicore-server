package com.medicore.api.util;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.Type;

import java.io.Serializable;
import java.util.Properties;

public class PrefixedIdGenerator implements IdentifierGenerator {

    private String prefix;
    private String sequenceName;

    // Este método lee los parámetros que le pasamos desde la entidad
    @Override
    public void configure(Type type, Properties params, ServiceRegistry serviceRegistry) {
        this.prefix = params.getProperty("prefix");
        this.sequenceName = params.getProperty("sequence");
    }

    // Este método va a PostgreSQL, pide el siguiente número, y lo une al prefijo
    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) {
        Number nextValue = session
                .createNativeQuery("SELECT nextval('" + sequenceName + "')", Number.class)
                .getSingleResult();
        
        return prefix + nextValue.longValue();
    }
}