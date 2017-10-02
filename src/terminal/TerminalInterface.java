package terminal;

interface TerminalInterface{
    void initFrame(String title);
    void start();
    void advance();
    void clear();
    void newLine();
    void printf(String format, Object... args);
    void print(String str);
    void println(String str);
    String query(String queryPrompt);
    String queryString(String query, boolean allowEmptyString);
    boolean queryYN(String query);
    Integer queryInteger(String query, boolean allowNull);
    Boolean queryBoolean(String query, boolean allowNull);
    Double queryDouble(String query, boolean allowNull);
}
