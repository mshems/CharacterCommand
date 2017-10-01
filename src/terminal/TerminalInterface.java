package terminal;

interface TerminalInterface{
    void start();
    void advance();
    void printBlock(TerminalPrinter terminalPrinter);
    void printf(String format, Object... args);
    void print(String s);
    void print(Integer n);
    void print(Double d);
    void print(Boolean b);
    void println(String s);
    void println(Integer n);
    void println(Double d);
    void println(Boolean b);
    String queryString(String query, boolean allowEmptyString);
    boolean queryYN(String query);
    Integer queryInteger(String query, boolean allowNull);
    Boolean queryBoolean(String query, boolean allowNull);
    Double queryDouble(String query, boolean allowNull);
}
