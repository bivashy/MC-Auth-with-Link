package me.mastercapexd.auth.messenger.commands.parser;

import java.util.Collections;
import java.util.List;

import revxrsal.commands.command.ArgumentStack;
import revxrsal.commands.exception.ArgumentParseException;

/**
 * Alternative of {@link revxrsal.commands.util.tokenize.QuotedStringTokenizer}, but without quote or any mark support/
 */
public class SimpleStringTokenizer {
    private static final List<String> EMPTY_TEXT = Collections.singletonList("");

    public static ArgumentStack parse(String arguments) throws ArgumentParseException {
        if (arguments == null || arguments.length() == 0) {
            return ArgumentStack.empty();
        }
        TokenizerState state = new TokenizerState(arguments);
        ArgumentStack returnedArgs = ArgumentStack.empty();
        while(state.hasMore()) {
            skipWhiteSpace(state);
            String arg = nextArg(state);
            returnedArgs.add(arg);
        }
        return returnedArgs;
    }

    public static ArgumentStack parseForAutoCompletion(String args) {
        if (args.isEmpty())
            return ArgumentStack.copyExact(EMPTY_TEXT);
        return parse(args);
    }

    private static void skipWhiteSpace(TokenizerState state) throws ArgumentParseException {
        if (!state.hasMore()) {
            return;
        }
        /* To make it skip ALL additional whitespace, replace if with while */
        //        while (state.hasMore() && Character.isWhitespace(state.peek())) {
        if (Character.isWhitespace(state.peek())) {
            state.next();
        }
    }

    private static String nextArg(TokenizerState state) throws ArgumentParseException {
        StringBuilder argBuilder = new StringBuilder();
        if (state.hasMore()) {
            int codePoint = state.peek();
            parseString(state, argBuilder);
        }
        return argBuilder.toString();
    }

    private static void parseString(TokenizerState state, StringBuilder builder) throws ArgumentParseException {
        while(state.hasMore()) {
            int nextCodePoint = state.peek();
            if (Character.isWhitespace(nextCodePoint)) {
                return;
            } else {
                builder.appendCodePoint(state.next());
            }
        }
    }

    private static class TokenizerState {
        private final String buffer;
        private int index = -1;

        TokenizerState(String buffer) {
            this.buffer = buffer;
        }

        // Utility methods
        public boolean hasMore() {
            return index + 1 < buffer.length();
        }

        public int peek() throws ArgumentParseException {
            if (!hasMore()) {
                throw createException("Buffer overrun while parsing args");
            }
            return buffer.codePointAt(index + 1);
        }

        public int next() throws ArgumentParseException {
            if (!hasMore()) {
                throw createException("Buffer overrun while parsing args");
            }
            return buffer.codePointAt(++index);
        }

        public ArgumentParseException createException(String message) {
            return new ArgumentParseException(message, buffer, index);
        }
    }
}
