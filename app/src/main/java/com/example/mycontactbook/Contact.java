package com.example.mycontactbook;
import java.util.Calendar;

public class Contact {
    private int contactID;
    private String contactName;
    private String streetAddress;
    private String city;
    private String state;
    private String zipCode;
    private String phoneNumber;
    private String cellNumber;
    private String EMail;
    private Calendar birthday;

    public Contact() {
        contactID = -1;
        birthday = Calendar.getInstance();
    }
    public int getContactID(){
        return contactID;
    }
    public void setContactID(int i){
        this.contactID = i;
    }
    public String getContactName(){
        return contactName;
    }
    public void setContactName(String s){
        this.contactName = s;
    }
    public Calendar getBirthday(){
        return birthday;
    }
    public void setBirthday(Calendar c){
        this.birthday = c;
    }
    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCellNumber() {
        return cellNumber;
    }

    public void setCellNumber(String cellNumber) {
        this.cellNumber = cellNumber;
    }

    public String getEMail() {
        return EMail;
    }

    public void setEMail(String eMail) {
       this.EMail = eMail;
    }

}
