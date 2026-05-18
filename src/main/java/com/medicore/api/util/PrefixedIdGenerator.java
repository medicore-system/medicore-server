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

    @Override
    public void configure(Type type, Properties params, ServiceRegistry serviceRegistry) {
        this.prefix = params.getProperty("prefix");
        this.sequenceName = params.getProperty("sequence");
    }

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object) {
        // Solución estricta para Hibernate 6: 
        // 1. Ejecutamos la consulta nativa sin especificar una clase
        Object result = session
                .createNativeQuery("SELECT nextval('" + sequenceName + "')")
                .getSingleResult();
        
        // 2. Casteamos el resultado crudo a Number y extraemos el Long
        Long nextValue = ((Number) result).longValue();
        
        // 3. Retornamos la unión del texto con el número (Ej: TAEPS-1)
        return prefix + nextValue; 
    }
}