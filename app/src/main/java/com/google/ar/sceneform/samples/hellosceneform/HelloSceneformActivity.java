/*
 * Copyright 2018 Google LLC. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.ar.sceneform.samples.hellosceneform;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.ar.core.Anchor;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.math.Quaternion;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * This is an example activity that uses the Sceneform UX package to make common AR tasks easier.
 */
public class HelloSceneformActivity extends AppCompatActivity {

    //region UI variables
    private Button btnSave;
    private Button btnAddStepInformation;
    private Button btnStartInstructionSet;
    private Button btnNextStep;
    private Button btnPreviousStep;
    private Button btnEditSet;
    private Button btnUp;
    private Button btnDown;
    private Button btnLeft;
    private Button btnRight;
    private Button btnForward;
    private Button btnBackward;
    private Button btnAddObject;
    private Button btnRotateUp;
    private Button btnRotateDown;
    private Button btnRotateLeft;
    private Button btnRotateRight;
    private EditText editText;
    private RecyclerView rvObjects;
    private RecyclerView.LayoutManager layoutManager;
    private MyAdapter mAdapter;

  //endregion

    //region variables

    public ArrayList<ObjectBlueprint> blueprintObjects = new ArrayList<ObjectBlueprint>();
    public ArrayList<Object> objects  = new ArrayList<Object>();
    public List<String> instructionSets = new ArrayList<>();
    public List<String> stepInformationList  = new ArrayList<>();

    public ObjectBlueprint.objectType objectType;

    public String currentInstructionSet;

    private int objectID = 1;
    public int currentStep = 0;

    private int currentSelectedObjectID = 0;

    public int hasMainAnchorBeenPlaced = 0;
    public Node mainNodeVisual;
    public TransformableNode mainTransformableNode;
    public Vector3 v = new Vector3(0f,5f,0f);


    //endregion

    private static final String TAG = HelloSceneformActivity.class.getSimpleName();
    private boolean checkUser = false;

    private ArFragment arFragment;

    //region Renderable variables
    private ModelRenderable lineRenderable;
    private ModelRenderable arrowRenderable;

    //endregion

    //---------------------------------------------------------------------------------------------------------------------------------

    //region methods


    private Node createObject(
            Object object,
            Node parent) {

        object.setParent(parent);

        Vector3 localPositionn = new Vector3(object.getPositionX(),object.getPositionY(),object.getPositionZ());

        object.setLocalPosition(new Vector3(localPositionn));


        return object;
    }

    public void disableButtons(){
        Button btnRight = findViewById(R.id.btnRight);
        Button btnLeft = findViewById(R.id.btnLeft);
        Button btnUp = findViewById(R.id.btnUp);
        Button btnDown = findViewById(R.id.btnDown);
        RecyclerView rvObjects = findViewById(R.id.rvObjects);
        Button btnForward = findViewById(R.id.btnForward);
        Button btnBackward = findViewById(R.id.btnBackward);
        Button btnAddObject = findViewById(R.id.btnAddObject);
        Button btnEditSet = findViewById(R.id.btnEditSet);
        Button btnRotateDown = findViewById(R.id.btnRotateDown);
        Button btnRotateUp = findViewById(R.id.btnRotateUp);
        Button btnRotateRight = findViewById(R.id.btnRotateRight);
        Button btnRotateLeft = findViewById(R.id.btnRotateLeft);

        if (this.checkUser){
            btnRight.setVisibility(View.INVISIBLE);
            btnLeft.setVisibility(View.INVISIBLE);
            btnUp.setVisibility(View.INVISIBLE);
            btnDown.setVisibility(View.INVISIBLE);
            rvObjects.setVisibility(View.INVISIBLE);
            btnForward.setVisibility(View.INVISIBLE);
            btnBackward.setVisibility(View.INVISIBLE);
            btnAddObject.setVisibility(View.INVISIBLE);
            btnEditSet.setVisibility(View.INVISIBLE);
            btnRotateDown.setVisibility(View.INVISIBLE);
            btnRotateLeft.setVisibility(View.INVISIBLE);
            btnRotateRight.setVisibility(View.INVISIBLE);
            btnRotateUp.setVisibility(View.INVISIBLE);
        }else{
            rvObjects.setVisibility(View.INVISIBLE);
            btnRight.setVisibility(View.INVISIBLE);
            btnLeft.setVisibility(View.INVISIBLE);
            btnUp.setVisibility(View.INVISIBLE);
            btnDown.setVisibility(View.INVISIBLE);
            btnForward.setVisibility(View.INVISIBLE);
            btnBackward.setVisibility(View.INVISIBLE);
            btnRotateDown.setVisibility(View.INVISIBLE);
            btnRotateLeft.setVisibility(View.INVISIBLE);
            btnRotateRight.setVisibility(View.INVISIBLE);
            btnRotateUp.setVisibility(View.INVISIBLE);
        }
    }

    //endregion

    //---------------------------------------------------------------------------------------------------------------------------------

    //---------------------------------------------------------------------------------------------------------------------------------

    //region Saving & loading from memory


    public static final String SHARED_PREFS = "sharedPrefs";

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

    public void saveStepsInformation(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        //PRIVATE MEANS NO ONE CAN CHANGE THE SHARED PREFERENCES

        Gson gson = new Gson();
        ArrayList<String> toSaveStepInformationList = new ArrayList<String>();

        try {

            for (String o : stepInformationList) {
                toSaveStepInformationList.add(o);
            }

            String json = gson.toJson(toSaveStepInformationList);

            editor.putString(currentInstructionSet + "StepInformation", json);

            editor.apply();

            Toast.makeText(this, "Sets Info Saved ", Toast.LENGTH_SHORT).show();

        }
        catch (Exception e){

            Toast.makeText(this, "Error occurred " + e.toString(), Toast.LENGTH_SHORT).show();
        }

    }
    public void loadStepsInformation(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        //PRIVATE MEANS NO ONE CAN CHANGE THE SHARED PREFERENCES
        Gson gson = new Gson();
        String json = sharedPreferences.getString(currentInstructionSet+"StepInformation" ,null );
        Type type = new TypeToken<ArrayList<String>>(){}.getType();
        stepInformationList = gson.fromJson(json,type);

        if(stepInformationList == null){
            stepInformationList = new ArrayList<>();
            for (int i = 0; i < 100; i++) {
                stepInformationList.add("");
            }
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




    public void  saveObjectBluePrints() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        //PRIVATE MEANS NO ONE CAN CHANGE THE SHARED PREFERENCES

        Gson gson = new Gson();
        blueprintObjects = new ArrayList<ObjectBlueprint>();

        try {

            for (Object o : objects) {
                blueprintObjects.add(o.getObjectBlueprint());
            }

            String json = gson.toJson(blueprintObjects);

        Log.d(TAG, "saveObjectBluePrints: " + json);

        editor.putString(currentInstructionSet, json);

        editor.apply();

        Toast.makeText(this, "Objects saved", Toast.LENGTH_SHORT).show();

        }
        catch (Exception e){

            Toast.makeText(this, "Error occurred " + e.toString(), Toast.LENGTH_SHORT).show();
        }

    }

    //endregion

    //---------------------------------------------------------------------------------------------------------------------------------



    //---------------------------------------------------------------------------------------------------------------------------------
    //region Position and Rotation Control visibility

    public void showPositionControls(){
        btnUp.setVisibility(View.VISIBLE);
        btnDown.setVisibility(View.VISIBLE);
        btnRight.setVisibility(View.VISIBLE);
        btnLeft.setVisibility(View.VISIBLE);
        btnForward.setVisibility(View.VISIBLE);
        btnBackward.setVisibility(View.VISIBLE);
        btnNextStep.setVisibility(View.INVISIBLE);
        btnPreviousStep.setVisibility(View.INVISIBLE);
        btnSave.setVisibility(View.INVISIBLE);
    }
    public void hidePositionControls(){
        btnUp.setVisibility(View.INVISIBLE);
        btnDown.setVisibility(View.INVISIBLE);
        btnRight.setVisibility(View.INVISIBLE);
        btnLeft.setVisibility(View.INVISIBLE);
        btnForward.setVisibility(View.INVISIBLE);
        btnBackward.setVisibility(View.INVISIBLE);
    }

    public void showRotationControls(){
        btnRotateRight.setVisibility(View.VISIBLE);
        btnRotateLeft.setVisibility(View.VISIBLE);
        btnRotateUp.setVisibility(View.VISIBLE);
        btnRotateDown.setVisibility(View.VISIBLE);
    }
    public void hideRotationControls(){
        btnRotateRight.setVisibility(View.INVISIBLE);
        btnRotateLeft.setVisibility(View.INVISIBLE);
        btnRotateUp.setVisibility(View.INVISIBLE);
        btnRotateDown.setVisibility(View.INVISIBLE);
    }
    //endregion
    //---------------------------------------------------------------------------------------------------------------------------------


    @SuppressLint("WrongViewCast")
    @Override

  protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);

      setContentView(R.layout.activity_ux);


      Intent intent = getIntent();
      //check if user or creator

        String textBlueprintObjects = getIntent().getStringExtra("blueprintObjects");
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<ObjectBlueprint>>(){}.getType();
        blueprintObjects = gson.fromJson(textBlueprintObjects, type);
        currentInstructionSet = getIntent().getExtras().getString("currentInstructionSet");

      this.checkUser = intent.getBooleanExtra("checkUser", false);
      this.disableButtons();
      loadStepsInformation();
      arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.ux_fragment);

      if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
          requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1000);
      }
      // When you build a Renderable, Sceneform loads its resources in the background while returning
      // a CompletableFuture. Call thenAccept(), handle(), or check isDone() before calling get().

      ModelRenderable.builder()
              .setSource(this, R.raw.line)
              //.setSource(this, R.)
              .build()
              .thenAccept(renderable -> lineRenderable = renderable)
              .exceptionally(
                      throwable -> {
                          Toast toast =
                                  Toast.makeText(this, "Unable to load line renderable", Toast.LENGTH_LONG);
                          toast.setGravity(Gravity.NO_GRAVITY, 0, 0);
                          toast.show();
                          return null;
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


      arFragment.setOnTapArPlaneListener(
              (HitResult hitResult, Plane plane, MotionEvent motionEvent) -> {
                  if (lineRenderable == null || arrowRenderable == null) {
                      return;
                  }else if (hasMainAnchorBeenPlaced == 0) {

                      Anchor anchor = hitResult.createAnchor();
                      AnchorNode anchorNode = new AnchorNode(anchor);
                      anchorNode.setParent(arFragment.getArSceneView().getScene());

                      TransformableNode transformableNode = new TransformableNode(arFragment.getTransformationSystem());

                      TransformableNode base = new TransformableNode(arFragment.getTransformationSystem());

                      mainTransformableNode = transformableNode;
                      mainTransformableNode.setParent(base);
                      mainTransformableNode.setLocalPosition(new Vector3(0.0f, 0.1f, 0.0f));


                      mainNodeVisual = new Node();
                      mainNodeVisual.setParent(mainTransformableNode);
                      mainNodeVisual.setRenderable(lineRenderable);
                      mainNodeVisual.setLocalScale(new Vector3(2f, 1f, 1f));

                      anchorNode.addChild(base);

                      hasMainAnchorBeenPlaced++;


                  }
              });

      //---------------------------------------------------------------------------------------------------------------------------------

      //region Button Methods

      // use this setting to improve performance if you know that changes
      // in content do not change the layout size of the RecyclerView

      this.rvObjects = findViewById(R.id.rvObjects);
      layoutManager = new LinearLayoutManager(this);


      loadInstructionSets();

        this.editText = findViewById(R.id.editText);
        this.editText.setVisibility(View.INVISIBLE);


        TextView textInformationView = findViewById(R.id.textInformationView);

        this.btnStartInstructionSet = findViewById(R.id.btnStartInstructionSet);
        this.btnStartInstructionSet.setVisibility(View.VISIBLE);
        this.btnStartInstructionSet.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Object o;
                for(ObjectBlueprint obj:blueprintObjects) {
                    if (obj.objectType == objectType.Arrow) {
                         o = new Object(arrowRenderable,obj);
                    }else{
                         o = new Object(lineRenderable,obj);
                    }
                    objects.add(o);
                    createObject(o,mainTransformableNode);
                    objectID = o.objectID;
                }

                ArrayList<Object> currentObj = new ArrayList<Object>();
                for(Object object : objects) {

                    object.setEnabled(false);
                    if (object.stepID == currentStep) {

                        currentObj.add(object);
                        object.setEnabled(true);
                    }
                }

                mAdapter = new MyAdapter(currentObj,this);
                mAdapter.setOnItemClickListener(new MyAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int Position) {
                        Log.d("asd","itemClicked");
                    }

                    @Override
                    public void onRotationClick(int Position) {
                        currentSelectedObjectID = currentObj.get(Position).objectID;
                        showRotationControls();
                    }

                    @Override
                    public void onPositionClick(int Position) {
                        currentSelectedObjectID = currentObj.get(Position).objectID;
                        showPositionControls();
                    }
                });
                rvObjects.setAdapter(mAdapter);

                if (hasMainAnchorBeenPlaced > 0) {
                    if(checkUser == true){
                        btnNextStep.setVisibility(View.VISIBLE);
                        btnPreviousStep.setVisibility(View.VISIBLE);
                        btnStartInstructionSet.setVisibility(View.INVISIBLE);
                        textInformationView.setText(stepInformationList.get(currentStep));

                    }else {
                        btnNextStep.setVisibility(View.VISIBLE);
                        btnPreviousStep.setVisibility(View.VISIBLE);
                        btnSave.setVisibility(View.VISIBLE);
                        btnEditSet.setVisibility(View.VISIBLE);
                        btnStartInstructionSet.setVisibility(View.INVISIBLE);
                        textInformationView.setText(stepInformationList.get(currentStep));
                    }
                }
            }
        });

        this.btnAddStepInformation = findViewById(R.id.btnAddStepInformation);
        this.btnAddStepInformation.setVisibility(View.INVISIBLE);
        this.btnAddStepInformation.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(btnAddStepInformation.getText() == "Save step information"){
                    String content = editText.getText().toString(); //gets you the contents of edit text
                    //textInformationView.setTextContent(content); //displays it in a textview..

                    stepInformationList.add(currentStep,content);
                    btnAddStepInformation.setText("Add step Information");
                    editText.setText("Add information for step " + currentStep);
                    editText.setVisibility(View.INVISIBLE);
                    textInformationView.setText(content);
                }else{
                    editText.setVisibility(View.VISIBLE);
                    btnAddStepInformation.setText("Save step information");
                }
            }
        });





        this.btnSave = findViewById(R.id.btnSave);
        this.btnSave.setVisibility(View.INVISIBLE);
        this.btnSave.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                saveObjectBluePrints();
                saveStepsInformation();
            }
        });


        this.btnNextStep = findViewById(R.id.btnNextStep);
        this.btnNextStep.setVisibility(View.INVISIBLE);
        this.btnNextStep.setOnClickListener(new View.OnClickListener(){
          @Override
            public void onClick(View view) {

              currentStep++;

              // looks through the list of objects and returns all objects affiliated with the current step

              ArrayList<Object> currentObj = new ArrayList<Object>();
              for(Object object : objects) {
                  object.setEnabled(false);
                  if (object.stepID == currentStep) {
                      currentObj.add(object);
                      object.setEnabled(true);
                  }
              }

              mAdapter = new MyAdapter(currentObj, this);
              mAdapter.setOnItemClickListener(new MyAdapter.OnItemClickListener() {
                  @Override
                  public void onItemClick(int Position) {
                      Log.d("Item clicked","Does Nothing");
                  }

                  @Override
                  public void onRotationClick(int Position) {
                      currentSelectedObjectID = currentObj.get(Position).objectID;
                      showRotationControls();
                  }

                  @Override
                  public void onPositionClick(int Position) {
                      currentSelectedObjectID = currentObj.get(Position).objectID;
                      showPositionControls();
                  }
              });
              rvObjects.setAdapter(mAdapter);
              textInformationView.setText(stepInformationList.get(currentStep));

          }
      });


        this.btnPreviousStep = findViewById(R.id.btnPreviousStep);
        this.btnPreviousStep.setVisibility(View.INVISIBLE);
        this.btnPreviousStep.setOnClickListener(new View.OnClickListener(){
          @Override
          public void onClick(View view) {

              currentStep--;

              ArrayList<Object> currentObj = new ArrayList<Object>();
              for(Object object : objects) {

                  object.setEnabled(false);
                  if (object.stepID == currentStep) {

                      currentObj.add(object);
                      object.setEnabled(true);
                  }
              }

              mAdapter = new MyAdapter(currentObj,this);
              mAdapter.setOnItemClickListener(new MyAdapter.OnItemClickListener() {
                  @Override
                  public void onItemClick(int Position) {
                      Log.d("asd","itemClicked");
                  }

                  @Override
                  public void onRotationClick(int Position) {
                      currentSelectedObjectID = currentObj.get(Position).objectID;
                      showRotationControls();
                  }

                  @Override
                  public void onPositionClick(int Position) {
                      currentSelectedObjectID = currentObj.get(Position).objectID;
                      showPositionControls();
                  }
              });
              rvObjects.setAdapter(mAdapter);
              textInformationView.setText(stepInformationList.get(currentStep));

          }

        });


        this.btnAddObject = findViewById(R.id.btnAddObject);
        this.btnAddObject.setVisibility(View.INVISIBLE);
        this.btnAddObject.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {

              ObjectBlueprint objectBluePrint = new ObjectBlueprint(objectType.Arrow,currentStep,objectID,0,0,0
                      ,0,0,true);
              Object o;
              if(objectBluePrint.objectType == objectType.Arrow) {
                   o = new Object(arrowRenderable, objectBluePrint);
              }else {
                   o = new Object(lineRenderable, objectBluePrint);
              }
              objects.add(o);

              createObject(o,mainTransformableNode);

              ArrayList<Object> currentObj = new ArrayList<Object>();

              for(Object object : objects) {
                  object.setEnabled(false);
                  if (object.stepID == currentStep) {
                      currentObj.add(object);
                      object.setEnabled(true);
                  }
              }
              mAdapter = new MyAdapter(currentObj,this);
              mAdapter.setOnItemClickListener(new MyAdapter.OnItemClickListener() {
                  @Override
                  public void onItemClick(int Position) {
                      Log.d("asd","itemClicked");
                  }

                  @Override
                  public void onRotationClick(int Position) {
                      currentSelectedObjectID = currentObj.get(Position).objectID;
                      showRotationControls();
                  }

                  @Override
                  public void onPositionClick(int Position) {
                      currentSelectedObjectID = currentObj.get(Position).objectID;
                      showPositionControls();
                  }
              });
              rvObjects.setAdapter(mAdapter);
             objectID++;
          }
      });




        this.btnEditSet = findViewById(R.id.btnEditSet);
        this.btnEditSet.setVisibility(View.INVISIBLE);
        this.btnEditSet.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {

              if (rvObjects.getVisibility() == View.INVISIBLE){

                  rvObjects.setLayoutManager(layoutManager);
                  rvObjects.setHasFixedSize(false);
                  rvObjects.setVisibility(View.VISIBLE);
                  btnAddObject.setVisibility(View.VISIBLE);
                  btnAddStepInformation.setVisibility(View.VISIBLE);

                  btnNextStep.setVisibility(View.INVISIBLE);
                  btnPreviousStep.setVisibility(View.INVISIBLE);
                  btnSave.setVisibility(View.INVISIBLE);

                  ArrayList<Object> currentObj = new ArrayList<Object>();
                  for(Object object : objects) {
                      object.setEnabled(false);
                      if (object.stepID == currentStep) {
                          currentObj.add(object);
                          object.setEnabled(true);
                      }
                  }
                  mAdapter = new MyAdapter(currentObj, this);
                  mAdapter.setOnItemClickListener(new MyAdapter.OnItemClickListener() {
                      @Override
                      public void onItemClick(int Position) {
                          Log.d("asd","itemClicked");
                      }

                      @Override
                      public void onRotationClick(int Position) {
                          currentSelectedObjectID = currentObj.get(Position).objectID;
                          showRotationControls();
                      }

                      @Override
                      public void onPositionClick(int Position) {
                          currentSelectedObjectID = currentObj.get(Position).objectID;
                          showPositionControls();
                      }
                  });
                  btnEditSet.setText("Stop Editing Steps");
              }else{
                  rvObjects.setVisibility(View.INVISIBLE);
                  btnAddObject.setVisibility(View.INVISIBLE);
                  btnAddStepInformation.setVisibility(View.INVISIBLE);

                  hidePositionControls();
                  hideRotationControls();
                  btnEditSet.setText("Edit Steps");
                  btnNextStep.setVisibility(View.VISIBLE);
                  btnPreviousStep.setVisibility(View.VISIBLE);
                  btnSave.setVisibility(View.VISIBLE);
              }
              rvObjects.setAdapter(mAdapter);
          }
      });

        this.btnUp = findViewById(R.id.btnUp);
        this.btnUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(Object object : objects) {
                    if (object.objectID == currentSelectedObjectID){
                        object.positionY += 0.05f;
                        object.setLocalPosition(new Vector3(object.positionX,object.positionY, object.positionZ));
                    }
                }
            }
        });
        this.btnDown = findViewById(R.id.btnDown);
        this.btnDown.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              for(Object object : objects) {
                  if (object.objectID == currentSelectedObjectID){
                      object.positionY -= 0.05f;
                      object.setLocalPosition(new Vector3(object.positionX,object.positionY, object.positionZ));
                  }
              }
          }
      });
        this.btnLeft = findViewById(R.id.btnLeft);
        this.btnLeft.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              for(Object object : objects) {
                  if (object.objectID == currentSelectedObjectID){
                      object.positionX += -0.05f;
                      object.setLocalPosition(new Vector3(object.positionX,object.positionY, object.positionZ));

                  }
              }
          }
      });
        this.btnRight = findViewById(R.id.btnRight);
        this.btnRight.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {

              for(Object object : objects) {
                  if (object.objectID == currentSelectedObjectID){
                      object.positionX -= -0.05f;
                      object.setLocalPosition(new Vector3(object.positionX ,object.positionY, object.positionZ));

                  }
              }
          }
      });
        this.btnForward = findViewById(R.id.btnForward);
        this.btnForward.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {

              for(Object object : objects) {
                  if (object.objectID == currentSelectedObjectID){
                      object.positionZ += 0.05f;
                      object.setLocalPosition(new Vector3(object.positionX ,object.positionY, object.positionZ));

                  }
              }
          }
      });
        this.btnBackward = findViewById(R.id.btnBackward);
        this.btnBackward.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {

              for(Object object : objects) {
                  if (object.objectID == currentSelectedObjectID){
                      object.positionZ -= 0.05f;
                      object.setLocalPosition(new Vector3(object.positionX ,object.positionY, object.positionZ));
                  }
              }
          }
      });


        this.btnRotateDown = findViewById(R.id.btnRotateDown);
        this.btnRotateDown.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              for(Object object : objects) {
                  if (object.objectID == currentSelectedObjectID){
                      object.lastRotationX = true;
                      object.rotationalXAxis+=5;
                      object.objectVisual.setLocalRotation(new Quaternion(new Vector3(1 ,0, 0),object.rotationalXAxis));
                  }
              }
          }
      });
        this.btnRotateUp = findViewById(R.id.btnRotateUp);
        this.btnRotateUp.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              for(Object object : objects) {
                  if (object.objectID == currentSelectedObjectID){
                      object.lastRotationX = true;
                      object.rotationalXAxis-=5;
                          object.objectVisual.setLocalRotation(new Quaternion(new Vector3(1 ,0, 0),object.rotationalXAxis));
                  }
              }
          }
      });
        this.btnRotateLeft = findViewById(R.id.btnRotateLeft);
        this.btnRotateLeft.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              for(Object object : objects) {
                  if (object.objectID == currentSelectedObjectID){
                      object.lastRotationX = false;
                      object.rotationalYAxis+=5;
                          object.objectVisual.setLocalRotation(new Quaternion(new Vector3(0 ,1, 0),object.rotationalYAxis));
                  }
              }
          }
      });
        this.btnRotateRight = findViewById(R.id.btnRotateRight);
        this.btnRotateRight.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              for(Object object : objects) {
                  if (object.objectID == currentSelectedObjectID){
                      object.lastRotationX = false;
                      object.rotationalYAxis-=5;
                      object.objectVisual.setLocalRotation(new Quaternion(new Vector3(0 ,1, 0),object.rotationalYAxis));
                  }
              }
          }
      });

      //endregion

      //---------------------------------------------------------------------------------------------------------------------------------
  }


}
