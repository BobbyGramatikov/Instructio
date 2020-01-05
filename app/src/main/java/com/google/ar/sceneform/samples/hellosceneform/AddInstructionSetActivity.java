package com.google.ar.sceneform.samples.hellosceneform;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.tools.r8.code.I;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static com.google.ar.sceneform.samples.hellosceneform.HelloSceneformActivity.SHARED_PREFS;

public class AddInstructionSetActivity extends AppCompatActivity {

    List<String> instructionSets = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_instruction_set);
        this.loadInstructionSets();
       //instructionSets = getIntent().getExtras().getStringArrayList("instructionSets");
    }

    public void loadInstructionSets() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        //PRIVATE MEANS NO ONE CAN CHANGE THE SHARED PREFERENCES
        Gson gson = new Gson();
        String json = sharedPreferences.getString("InstructionSets",null );
        Type type = new TypeToken<ArrayList<String>>(){}.getType();
        instructionSets = gson.fromJson(json,type);

        if(instructionSets == null){
            instructionSets = new ArrayList<>();
            instructionSets.add("Create New InstructionSet");
        }
    }

    public void saveInstructionSet(View view) {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        //PRIVATE MEANS NO ONE CAN CHANGE THE SHARED PREFERENCES
        EditText editTextAddInstructionSet = findViewById(R.id.editTextInstructionName);
        this.instructionSets.add(editTextAddInstructionSet.getText().toString());
        editTextAddInstructionSet.setText("");
        Gson gson = new Gson();
        String json = gson.toJson(instructionSets);

        editor.putString("InstructionSets",json);
        editor.apply();

        Toast.makeText(this, "Instruction sets saved", Toast.LENGTH_SHORT).show();

    }
}
