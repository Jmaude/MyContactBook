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
    public int getContactId(){
        return contactID;
    }
    public void setContactId(int i){
        contactID = i;
    }
    public String getContactName(){
        return contactName;
    }
    public void setContactName(String s){
        contactName = s;
    }
    public Calendar getBirthday(){
        return birthday;
    }
    public void setBirthday(Calendar c){
        birthday = c;
    }
    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        streetAddress = streetAddress;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        state = state;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        zipCode = zipCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        phoneNumber = phoneNumber;
    }

    public String getCellNumber() {
        return cellNumber;
    }

    public void setCellNumber(String cellNumber) {
        cellNumber = cellNumber;
    }

    public String getEMail() {
        return EMail;
    }

    public void setEMail(String eMail) {
       EMail = eMail;
    }

}
