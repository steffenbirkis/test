package com.example.steffen.naevatest;

public class User {
    private String name;
    private String workplace;
    private String phoneNr;
    private String NameSpace;
    private String Instance;

    public User(){

    }
    public User  (String name, String workplace, String phone, String ns, String inst){
        this.name=name;
        this.Instance=inst;
        this.phoneNr=phone;
        this.workplace=workplace;
        this.NameSpace=ns;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWorkplace() {
        return workplace;
    }

    public void setWorkplace(String workplace) {
        this.workplace = workplace;
    }

    public String getPhoneNr() {
        return phoneNr;
    }

    public void setPhoneNr(String phoneNr) {
        this.phoneNr = phoneNr;
    }

    public String getNameSpace() {
        return NameSpace;
    }

    public void setNameSpace(String nameSpace) {
        NameSpace = nameSpace;
    }

    public String getInstance() {
        return Instance;
    }

    public void setInstance(String instance) {
        Instance = instance;
    }
}
