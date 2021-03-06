package app.terminal;

public interface TerminalEventListener {
    void submitActionPerformed(SubmitEvent e);
    void queryActionPerformed(QueryEvent e);
    void menuActionPerformed(MenuEvent e);
}
