package com.example.mycontacts.vaccinationmanagement.Database;

public class VaccinationDataItem {
    public String getPetName() {
        return petName;
    }

    public String getVaccinationDate() {
        return vaccinationDate;
    }

    public String getVaccineName() {
        return vaccineName;
    }

    public int getVaccinationDone() {
        return vaccinationDone;
    }

    public int getEventId() {
        return eventId;
    }

    private String petName;
    private String vaccinationDate;
    private String vaccineName;
    private int vaccinationDone;
    int eventId;
    public VaccinationDataItem(int eventId, String petName, String date, String vaccineName, int vaccinationDone){
        this.petName=petName;
        this.vaccinationDate=date;
        this.vaccineName=vaccineName;
        this.vaccinationDone=vaccinationDone;
        this.eventId=eventId;
    }
}
