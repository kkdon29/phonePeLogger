package com.phonepe.logger.impl;

import org.junit.Test;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;

public class LogMessageImplTest {

    @Test
    public void testEqualsObject() {
        EqualsVerifier.forClass(LogMessageImpl.class)
                        .suppress(Warning.NONFINAL_FIELDS).usingGetClass()
                        .verify();
    }

}
