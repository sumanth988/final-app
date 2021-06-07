package com.example.mycontacts;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class KnowYourPet extends Fragment {
    RecyclerView knowPet;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.know_your_pet,container,false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Spinner spinner = (Spinner) view.findViewById(R.id.spinner);

        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(getContext(),
                R.array.animals_array, android.R.layout.simple_spinner_item);

        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
        knowPet=(RecyclerView) view.findViewById(R.id.youtube_recycler_view);
        KnowListAdapter adapter = new KnowListAdapter(getYouTubeLinks(0));
        knowPet.setHasFixedSize(true);
        knowPet.setLayoutManager(new LinearLayoutManager(getContext()));
        knowPet.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                adapter.setList(getYouTubeLinks(position));
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private KnowListData[] getYouTubeLinks(int pos) {
        if(pos==0){
        return new KnowListData[]{
                new KnowListData("How to Understand Your Dog Better", "https://www.youtube.com/watch?v=SIgwo49yTk8"),
                new KnowListData("12 Harmful Things You Do to Your Dog Without Realizing It", "https://www.youtube.com/watch?v=-G16EX6b8Xg"),
                new KnowListData("15 Things You Should Know About Dogs", "https://www.youtube.com/watch?v=ltg2J4geVbQ"),
                new KnowListData("Different Types of Dog Breeds ","https://www.youtube.com/watch?v=EKG89O05K5A"),
                new KnowListData("Cat language","https://www.youtube.com/watch?v=rb2xTAHVqjs"),
                new KnowListData("Dogs Body Language Every Dog Owner Should Know","https://www.youtube.com/watch?v=v4kEHQL7S8I"),
                new KnowListData("How to understand your cat better","https://www.youtube.com/watch?v=Xz6yBbBRr8Y"),
                new KnowListData("10 things you should know Before getting a cat/kitten","https://www.youtube.com/watch?v=Z-D-0VXUdlE")
        };}
        else if(pos==1){
            return new KnowListData[]{
                    new KnowListData("How to Understand Your Dog Better", "https://www.youtube.com/watch?v=SIgwo49yTk8"),
                    new KnowListData("12 Harmful Things You Do to Your Dog Without Realizing It", "https://www.youtube.com/watch?v=-G16EX6b8Xg"),
                    new KnowListData("15 Things You Should Know About Dogs", "https://www.youtube.com/watch?v=ltg2J4geVbQ"),
                    new KnowListData("Different Types of Dog Breeds ","https://www.youtube.com/watch?v=EKG89O05K5A"),
                    new KnowListData("Dogs Body Language Every Dog Owner Should Know","https://www.youtube.com/watch?v=v4kEHQL7S8I")
            };
            }
        else
            return new KnowListData[]{

                    new KnowListData("How To Understand Your Cat Better","https://www.youtube.com/watch?v=Xz6yBbBRr8Y"),
                    new KnowListData("10 things you should know Before getting a cat/kitten","https://www.youtube.com/watch?v=Z-D-0VXUdlE"),
                    new KnowListData("Cat language","https://www.youtube.com/watch?v=rb2xTAHVqjs")
            };
    }
}