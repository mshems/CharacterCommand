package jterminal.core;

public class JTerminalPrinter {
    private final JTerminal terminal;

    public static final int LEFT_ALIGNED = 0;
    public static final int CENTERED = 1;
    public static final int RIGHT_ALIGNED = 2;

    JTerminalPrinter(JTerminal terminal){
        this.terminal = terminal;
    }

    public void printf(String format, Object... args) {
        terminal.getOutputComponent().print(String.format(format, args));
    }

    public void print(String str) {
        terminal.getOutputComponent().print(str);
    }

    public void print(Integer n) {
        terminal.getOutputComponent().print(n.toString());
    }

    public void print(Double d) {
        terminal.getOutputComponent().print(d.toString());
    }

    public void print(Boolean b) {
        terminal.getOutputComponent().print(b.toString());
    }

    public void print(Object o) {
        terminal.getOutputComponent().print(o.toString());
    }

    public void println(String str) {
        terminal.getOutputComponent().println(str);
    }

    public void println(Integer n) {
        terminal.getOutputComponent().println(n.toString());
    }

    public void println(Double d) {
        terminal.getOutputComponent().println(d.toString());
    }

    public void println(Boolean b) {
        terminal.getOutputComponent().println(b.toString());
    }

    public void println(Object o) {
        terminal.getOutputComponent().println(o.toString());
    }

    public void println(String str, int PRINT_FORMAT) {
        switch (PRINT_FORMAT) {
            case LEFT_ALIGNED:
                terminal.getOutputComponent().print(str);
                break;
            case CENTERED:
                terminal.getOutputComponent().printCentered(str);
                break;
            case RIGHT_ALIGNED:
                terminal.getOutputComponent().printRightAligned(str);
                break;
            default:
                terminal.getOutputComponent().print(str);
                break;
        }
    }
}
