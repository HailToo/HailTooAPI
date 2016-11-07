package edu.hail.models;

public class LogonData {
	public String username;
    public String password;
    public String uri;

    public boolean isValid() {
        return !(username == null && password == null && uri == null
            && username.trim().isEmpty() && password.trim().isEmpty() && uri.trim().isEmpty());
    }
}
