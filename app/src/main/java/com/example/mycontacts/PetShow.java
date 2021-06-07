package com.example.mycontacts;

public class PetShow {

    private String img;
    private  String pet_name;
    private String  pet_type;

    public  PetShow(String pet_name,String pet_type,String img)
    {
        this.img=img;
        this.pet_name=pet_name;
        this.pet_type=pet_type;
    }

    public void setImg(String img1)
    {
        this.img=img1;
    }

    public  String getImg()
    {
        return this.img;
    }

    public void setPet_name(String pet_name1)
    {
        this.pet_name=pet_name1;
    }

    public  String getPet_name()
    {
        return this.pet_name;
    }


    public void setPet_type(String pet_type1)
    {
        this.pet_type=pet_type1;
    }

    public  String getPet_type()
    {
        return this.pet_type;
    }
}
