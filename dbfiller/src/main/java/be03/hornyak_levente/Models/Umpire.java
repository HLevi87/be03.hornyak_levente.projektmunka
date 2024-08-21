package be03.hornyak_levente.Models;

public class Umpire {
    private String UmpireName;
    private String emailAddress;
    private String phoneNumber;


    public Umpire(String unpireName) {
        this.UmpireName = unpireName;
    }


    public String getUmpireName() {
        return UmpireName;
    }


    public void setUmpireName(String unpireName) {
        UmpireName = unpireName;
    }


    public String getEmailAddress() {
        return emailAddress;
    }


    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }


    public String getPhoneNumber() {
        return phoneNumber;
    }


    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    
}
