package app.ui;

import jterminal.core.JTerminal;

public class CCExtensions {
    public static String buildNameFromTokens(JTerminal terminal) {
        StringBuilder builder = new StringBuilder();
        if(terminal.peekToken().startsWith("\"")){
            while(terminal.hasTokens()){
                String token = terminal.nextToken();
                if(token.equals("")) return null;
                if(builder.length()>0) builder.append(" ").append(token);
                else builder.append(token);
                if(token.endsWith("\"")){
                    return builder.toString().replace("\"", "");
                }
            }
        }
        return null;
    }
}
