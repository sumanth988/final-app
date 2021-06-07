package com.example.mycontacts.vaccinationmanagement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



public class DummyDataBaseLogics {


    public static String selectedVaccine;
    public static String selectedPetName;


        /*
    public static List<String> getPetNames(){


        List<String> list=new ArrayList<>();
        list = dbh.getAllUserName();

        //list.add("puppu");
        //list.add("kittu");


        return list;
    }

*/

    public static List<String> getVaccineData() {
        List<String> arrayList = new ArrayList<String>(
                Arrays.asList("Bordetella",
                        "Canine Influenza",
                        "Lyme vaccine",
                        "Canine Parvovirus",
                        "Canine Distemper",
                        "Hepatitis",
                        "Rabies",
                        "Leptospirosis"));
        return arrayList;
    }

}

