package com.github.glasspane.mesh.util.logging;

import org.apache.logging.log4j.message.AbstractMessageFactory;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.message.ParameterizedMessage;

public class PrefixMessageFactory extends AbstractMessageFactory {

    private final String prefix;

    public PrefixMessageFactory(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public Message newMessage(String message, Object... params) {
        return new ParameterizedMessage("[" + prefix + "]: " + message, params);
    }

}
