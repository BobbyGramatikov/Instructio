package com.google.ar.sceneform.samples.hellosceneform;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static com.google.ar.sceneform.samples.hellosceneform.HelloSceneformActivity.SHARED_PREFS;

public class activity_instruction_set extends AppCompatActivity {


    private ModelRenderable arrowRenderable;
    private ArrayAdapter<String> adapter;
    private Boolean checkUser = false;
    private Spinner spinnerSelectInstructionSet;

    List<String> instructionSets = new ArrayList<>();

    public String currentInstructionSet;
    public ArrayList<ObjectBlueprint> blueprintObjects = new ArrayList<ObjectBlueprint>();

   // public ArrayList<Car> Cars = new ArrayList<Car>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instruction_set);
        Intent intent = getIntent();
        this.checkUser = intent.getBooleanExtra("checkUser", false);
        this.spinnerSelectInstructionSet = findViewById(R.id.spinnerChooseInstructionSet);

        if(this.checkUser){
           this.hideAddButton();
        }
        loadInstructionSets();
        this.setSpinner();

        this.spinnerSelectInstructionSet.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView parent, View view, int position, long id) {

                currentInstructionSet =  parent.getItemAtPosition(position).toString();
                loadObjects();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ModelRenderable.builder()
                .setSource(this, R.raw.arrow)
                //.setSource(this, R.)
                .build()
                .thenAccept(renderable -> arrowRenderable = renderable)
                .exceptionally(
                        throwable -> {
                            Toast toast =
                                    Toast.makeText(this, "Unable to load arrow renderable", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.NO_GRAVITY, 0, 0);
                            toast.show();
                            return null;
                        });

    }

    public void hideAddButton(){
        ImageButton btnAdd = findViewById(R.id.imageButtonAdd);
        btnAdd.setVisibility(View.INVISIBLE);
    }


    public ObjectBlueprint.objectType objectType;

    public void loadObjects() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        //PRIVATE MEANS NO ONE CAN CHANGE THE SHARED PREFERENCES
        Gson gson = new Gson();
        String json = sharedPreferences.getString(currentInstructionSet, null);
        Type type = new TypeToken<ArrayList<ObjectBlueprint>>(){}.getType();
        blueprintObjects = gson.fromJson(json,type);
        if(blueprintObjects == null){
            blueprintObjects = new ArrayList<ObjectBlueprint>();
        }
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

    public void  saveObjects() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        //PRIVATE MEANS NO ONE CAN CHANGE THE SHARED PREFERENCES

        GsonBuilder builder = new GsonBuilder()
                .serializeNulls();

        Gson gson = builder.create();

        String json = gson.toJson(blueprintObjects);

        editor.putString(currentInstructionSet, json);

        editor.apply();

        Toast.makeText(this, "Objects saved", Toast.LENGTH_SHORT).show();

    }

    public void saveInstructionSet() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        //PRIVATE MEANS NO ONE CAN CHANGE THE SHARED PREFERENCES

        Gson gson = new Gson();
        String json = gson.toJson(instructionSets);

        editor.putString("InstructionSets",json);
        editor.apply();

        Toast.makeText(this, "Instruction sets saved", Toast.LENGTH_SHORT).show();

    }

    public void setSpinner(){
        adapter = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item
                ,instructionSets);

        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinnerSelectInstructionSet.setAdapter(adapter);
    }
    public void ShowMainScreen(View view){
        Intent intent = new Intent(this, HelloSceneformActivity.class);

        //put transferring variables using putExtra
        Gson gson = new Gson();

        String jsonObject = gson.toJson(blueprintObjects);

        intent.putExtra("blueprintObjects", jsonObject);
        intent.putExtra("currentInstructionSet", currentInstructionSet);
        intent.putExtra("checkUser", this.checkUser);
        startActivity(intent);
    }

    public void showAddInstruction(View view){
        Intent intent = new Intent(this, AddInstructionSetActivity.class);
        Gson gson = new Gson();

        String jsonObject = gson.toJson(instructionSets);
        intent.putExtra("instructionSets", jsonObject);
        startActivity(intent);

    }

    @Override
    protected void onResume() {
        super.onResume();
        this.loadInstructionSets();
        this.setSpinner();
        Toast.makeText(this, "testt3estest", Toast.LENGTH_SHORT).show();
    }

}
