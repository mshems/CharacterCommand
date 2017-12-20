package app.ui.terminal.core;

public class IllegalTokenException extends Exception{
    private String token;

    public IllegalTokenException(String token){
        this.token = token;
    }

    public String getToken(){
        return token;
    }
}
