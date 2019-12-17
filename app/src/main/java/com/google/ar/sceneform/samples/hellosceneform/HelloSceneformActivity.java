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
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.ar.core.Anchor;
import com.google.ar.core.Frame;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.core.Trackable;
import com.google.ar.core.TrackingState;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.ArSceneView;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.math.Quaternion;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * This is an example activity that uses the Sceneform UX package to make common AR tasks easier.
 */
public class HelloSceneformActivity extends AppCompatActivity {

  private static final String TAG = HelloSceneformActivity.class.getSimpleName();
  private static final double MIN_OPENGL_VERSION = 3.0;
  private boolean checkUser = false;

    private GestureDetector gestureDetector;
    private ArSceneView arSceneView;
    private ArFragment arFragment;

    //region Renderable variables
    private ModelRenderable lineRenderable;
    private ModelRenderable arrowRenderable;

    List<String> instructionSets = new ArrayList<>();
    //endregion


    //region UI variables
    private Button btnSave;
    private Button btnSaveInstructionSet;
    private EditText editText;


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
    private Spinner spinnerSelectInstructionSet;

    private RecyclerView rvObjects;
    private MyAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private Text textInformationView;
   // private RecyclerView

  //endregion

    private int objectID = 1;
    private int currentSelectedObjectID = 0;
    public int currentStep = 0;

    private boolean hasFinishedLoading = false;

    public int hasMainAnchorBeenPlaced = 0;

    private boolean hasPlacedMainObject = false;


    public ArrayList<Object> ObjectsWithNoNodes = new ArrayList<Object>();
    public Node mainNodeVisual;


    public TransformableNode mainTransformableNode;


    public Vector3 v = new Vector3(0f,5f,0f);

    public ArrayList<TransformableNode> transformableNodes = new ArrayList<TransformableNode>();

    public ArrayList<Object> objects = new ArrayList<Object>();


    public enum Position{
        UP,
        DOWN,
        RIGHT,
        LEFT,
        FORWARD,
        BACKWARD
    }

    //---------------------------------------------------------------------------------------------------------------------------------

    //region methods
    public void changeObjectPosition(Position position,int objectID){

 switch(position){
     case UP:

}
                for(Object object : objects) {
                    if (object.objectID == currentSelectedObjectID){

                        object.positionY += 0.05f;
                        object.setLocalPosition(new Vector3(object.positionX,object.positionY, object.positionZ));
                    }
                }




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





    }





    //endregion

    //---------------------------------------------------------------------------------------------------------------------------------

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
    public void saveObjects() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        //PRIVATE MEANS NO ONE CAN CHANGE THE SHARED PREFERENCES

        Gson gson = new Gson();
        List<Object> objectsForSaving = ObjectsWithNoNodes;
        String json = gson.toJson(objectsForSaving);
        Log.d(TAG, "saveObjects: " + json);
        editor.putString(currentInstructionSet, json);

        editor.apply();

        Toast.makeText(this, "Objects saved", Toast.LENGTH_SHORT).show();

    }


    public void loadObjects() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        //PRIVATE MEANS NO ONE CAN CHANGE THE SHARED PREFERENCES
        Gson gson = new Gson();
        String json = sharedPreferences.getString(currentInstructionSet, null);
        Type type = new TypeToken<ArrayList<Object>>(){}.getType();
        objects = gson.fromJson(json,type);

        if(objects == null){
            objects = new ArrayList<>();
        }
    }

    private void onSingleTap(MotionEvent tap) {
        if (!hasFinishedLoading) {
            // We can't do anything yet.
            return;
        }

        Frame frame = arSceneView.getArFrame();
        if (frame != null) {
            if (!hasPlacedMainObject && tryPlaceMainObject(tap, frame)) {
                hasPlacedMainObject = true;
            }
        }

        // Create file output stream

    }

    private boolean tryPlaceMainObject(MotionEvent tap, Frame frame) {
        if (tap != null && frame.getCamera().getTrackingState() == TrackingState.TRACKING) {
            for (HitResult hit : frame.hitTest(tap)) {
                Trackable trackable = hit.getTrackable();
                if (trackable instanceof Plane && ((Plane) trackable).isPoseInPolygon(hit.getHitPose())) {
                    // Create the Anchor.
                    Anchor anchor = hit.createAnchor();
                    AnchorNode anchorNode = new AnchorNode(anchor);
                    anchorNode.setParent(arSceneView.getScene());
                    Node objectSystem = createObjectSystem();
                    anchorNode.addChild(objectSystem);
                    return true;
                }
            }
        }

        return false;
    }

    private Node createObjectSystem() {
        Node base = new Node();
        Node line = new Node();
        line.setParent(base);
        line.setLocalPosition(new Vector3(0.0f, 0.0f, 0.0f));

        Node lineVisual = new Node();
        lineVisual.setParent(line);
        lineVisual.setRenderable(arrowRenderable);
        lineVisual.setLocalScale(new Vector3(0.5f, 0.5f, 0.5f));


        // createObject("Line", sun, 0.7f, 35f, venusRenderable, 0.0475f, 2.64f);

        // Node earth = createObject("Earth", sun, 1.0f, 29f,  0.05f, 23.4f);

        return base;
    }


    public String currentInstructionSet;
    //fileOutPutStream creates a file in the the device's "internal" storage



    private Node createObject(
            Object object,
            Node parent
           ) {
        // Orbit is a rotating node with no renderable positioned at the sun.
        // The planet is positioned relative to the orbit so that it appears to rotate around the sun.
        // This is done instead of making the sun rotate so each planet can orbit at its own speed.
        /*RotatingNode orbit = new RotatingNode(solarSettings, true, false, 0);
        orbit.setDegreesPerSecond(orbitDegreesPerSecond);
        orbit.setParent(parent);*/

        // Create the planet and position it relative to the sun.

         object.setParent(parent);

         Vector3 localPositionn = new Vector3(object.getPositionX(),object.getPositionY(),object.getPositionZ());

         object.setLocalPosition(new Vector3(localPositionn));

        //object.removeChild();
        //maybe set the local positioninside the object object.setLocalPosition(v);

        //sunVisual.setOnTapListener(
        //        (hitTestResult, motionEvent) -> solarControls.setEnabled(!solarControls.isEnabled()));
        return object;
    }

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

    public void showSavingInstructionSetControls(){
        btnSaveInstructionSet.setVisibility(View.VISIBLE);
        editText.setVisibility(View.VISIBLE);
    }

    public void hideSavingInstructionSetControls(){
        this.btnSaveInstructionSet.setVisibility(View.INVISIBLE);
        this.editText.setVisibility(View.INVISIBLE);
    }

    @Override
  @SuppressWarnings({"AndroidApiChecker", "FutureReturnValueIgnored"})
  // CompletableFuture requires api level 24
  // FutureReturnValueIgnored is not valid
  protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);

      setContentView(R.layout.activity_ux);

      Intent intent = getIntent();
      this.checkUser = intent.getBooleanExtra("HomeActivity", false);
      this.disableButtons();

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

      // Set up a tap gesture detector.
      gestureDetector =
              new GestureDetector(
                      this,
                      new GestureDetector.SimpleOnGestureListener() {
                          @Override
                          public boolean onSingleTapUp(MotionEvent e) {
                              onSingleTap(e);
                              return true;
                          }

                          @Override
                          public boolean onDown(MotionEvent e) {
                              return true;
                          }
                      });

      if (!checkIsSupportedDeviceOrFinish(this)) {
          return;
      }

      // Session mSession = null;



      arFragment.setOnTapArPlaneListener(
              (HitResult hitResult, Plane plane, MotionEvent motionEvent) -> {
                  if (lineRenderable == null || arrowRenderable == null) {
                      return;
                  }else if (hasMainAnchorBeenPlaced == 0 && spinnerSelectInstructionSet.getSelectedItem() != "Create New InstructionSet"){
                  // Pose mCameraRelativePose= Pose.makeTranslation(0.0f, 0.0f, -0.5f);

                  // mSession.createAnchor(mCameraRelativePose);

                  // Create the Anchor.

                  //in the other if you make a list that keeps all the anchors
                  //and the positions they were dropped by
                  //should get the position then later on relative to the main anchor load up the messages
                  Anchor anchor = hitResult.createAnchor();
                  AnchorNode anchorNode = new AnchorNode(anchor);
                  anchorNode.setParent(arFragment.getArSceneView().getScene());
                  //    TransformableNode transformableNode = new TransformableNode(arFragment.getTransformationSystem());


                       // createObjectSystem();

                      TransformableNode transformableNode = new TransformableNode(arFragment.getTransformationSystem());
                      // transformableNode.setParent(anchorNode);

                      //  xAxis = anchor.getPose().getXAxis();
                      //  yAxis = anchor.getPose().getYAxis();
                      //  zAxis = anchor.getPose().getZAxis();

                      // v.x = anchor.getPose().getTransformedAxis();
                      // MainAnchor = anchor;
                      // MainAnchorNode = new AnchorNode(MainAnchor);

                      //mainTransformableNode = transformableNode;
                      //mainTransformableNode.setParent(anchorNode);
                      // mainTransformableNode.setRenderable(lineRenderable);

                      TransformableNode base = new TransformableNode(arFragment.getTransformationSystem());
                     // mainNode = new Node();
                     // mainNode.setParent(base);
                     // mainNode.setLocalPosition(new Vector3(0.0f,0.1f,0.0f));

                      mainTransformableNode = transformableNode;
                      mainTransformableNode.setParent(base);
                      mainTransformableNode.setLocalPosition(new Vector3(0.0f,0.1f,0.0f));


                      mainNodeVisual = new Node();
                      mainNodeVisual.setParent(mainTransformableNode);
                      mainNodeVisual.setRenderable(lineRenderable);
                      mainNodeVisual.setLocalScale(new Vector3(2f, 1f, 1f));

                      anchorNode.addChild(base);
/*
                      Object object = new Object("arrow",1f,1f,arrowRenderable,stepID,objectID,0.1f,0.1f,0.2f);
                      objects.add(object);
                      stepID++;

                     // createObject(object,mainTransformableNode);


                      Object object1 = new Object("arrow",1f,1f,arrowRenderable,stepID,objectID,0.1f,0.1f,0.1f);
                      objects.add(object1);
                      stepID++;

                     // createObject(object1,mainTransformableNode);


                      stepID++;
                      Object object2 = new Object("arrow",0.5f,0.5f,arrowRenderable,stepID,objectID,0.1f,0.1f,0.3f);
                      objects.add(object2);

                      mainNodeVisual.setEnabled(true);

                      currentStep = 1;
                      objectID = 1;
                      for(Object objectt : objects) {
                          objectt.objectID = objectID;
                          createObject(objectt, mainTransformableNode);
                          objectt.setEnabled(false);
                          if (objectt.stepID == 1){
                              objectt.setEnabled(true);
                          }
                          objectID++;
                      }
                      objectID = 1;
*/

                      // mainTransformableNode.setLocalPosition(v);
                      hasMainAnchorBeenPlaced++;

                      btnNextStep.setVisibility(View.VISIBLE);
                      btnPreviousStep.setVisibility(View.VISIBLE);
                  }

                     // createObject("arrow",mainTransformableNode)

                  //transformableNode.setRenderable(arrowRenderable);
              });

      // Orbit is a rotating node with no renderable positioned at the sun.
      // The planet is positioned relative to the orbit so that it appears to rotate around the sun.
      // This is done instead of making the sun rotate so each planet can orbit at its own speed.
        /*RotatingNode orbit = new RotatingNode(solarSettings, true, false, 0);
        orbit.setDegreesPerSecond(orbitDegreesPerSecond);
        orbit.setParent(parent);

          // Create the planet and position it relative to the sun.

      // Set a touch listener on the Scene to listen for taps.
      // Set an update listener on the Scene that will hide the loading message once a Plane is
      // detected.

  }

    @Override
    public void onPause() {
        super.onPause();
        if (arSceneView != null) {
            arSceneView.pause();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (arSceneView != null) {
            arSceneView.destroy();
        }
    }

  /**
   * Returns false and displays an error message if Sceneform can not run, true if Sceneform can run
   * on this device.
   *
   * <p>Sceneform requires Android N on the device as well as OpenGL 3.0 capabilities.
   *
   * <p>Finishes the activity if Sceneform can not run
   */
      //---------------------------------------------------------------------------------------------------------------------------------

      //region Button Methods

      //rvObjects = findViewById(R.id.rvObjects);

      // use this setting to improve performance if you know that changes
      // in content do not change the layout size of the RecyclerView

      this.rvObjects = findViewById(R.id.rvObjects);
      layoutManager = new LinearLayoutManager(this);



      this.spinnerSelectInstructionSet = findViewById(R.id.spinnerChooseInstructionSet);
      loadInstructionSets();
      ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.support_simple_spinner_dropdown_item
              ,instructionSets);


      adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
      spinnerSelectInstructionSet.setAdapter(adapter);

        this.spinnerSelectInstructionSet.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView parent, View view, int position, long id) {

                currentInstructionSet =  parent.getItemAtPosition(position).toString();
               loadObjects();
                hideSavingInstructionSetControls();

                if (currentInstructionSet == "Create New InstructionSet");{
                    showSavingInstructionSetControls();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        this.editText = findViewById(R.id.editText);

        this.btnSaveInstructionSet = findViewById(R.id.btnSaveInstructionSet);
    //    this.btnSaveInstructionSet.setVisibility(View.INVISIBLE);
        this.btnSaveInstructionSet.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String newSet = (editText.getText().toString());
                instructionSets.add(newSet);
               saveInstructionSet();

            }
        });

        this.btnSave = findViewById(R.id.btnSave);
       // this.btnSave.setVisibility(View.INVISIBLE);
        this.btnSave.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                saveObjects();
            }
        });


        this.btnNextStep = findViewById(R.id.btnNextStep);
      btnNextStep.setVisibility(View.INVISIBLE);
      this.btnNextStep.setOnClickListener(new View.OnClickListener(){
          @Override
          public void onClick(View view) {

              currentStep++;

              ArrayList<Object> currentObj = new ArrayList<Object>();
              for(Object object : objects) {

                  object.setEnabled(false);
                  if (object.stepID == currentStep) {
                      Log.d("", "" + object.positionX);
                      Log.d("", "" + object.positionZ);
                      Log.d("", "" + object.positionY);
                      currentObj.add(object);
                      object.setEnabled(true);
                  }

              }

              mAdapter = new MyAdapter(currentObj, this);
              mAdapter.setOnItemClickListener(new MyAdapter.OnItemClickListener() {
                  @Override
                  public void onItemClick(int Position) {
                      Log.d("asd","ItemClicked");
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



          }
      });


      this.btnPreviousStep = findViewById(R.id.btnPreviousStep);
      btnPreviousStep.setVisibility(View.INVISIBLE);
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
          }
      });

      this.btnAddObject = findViewById(R.id.btnAddObject);
      this.btnAddObject.setVisibility(View.INVISIBLE);
      this.btnAddObject.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              Object o = new Object("arrow",1f,1f,arrowRenderable,currentStep,objectID,0.1f,0.1f,0.5f);
             ObjectsWithNoNodes.add(o);
              createObject(o,mainTransformableNode);
             objects.add(o);
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
      this.btnEditSet.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {

              if (rvObjects.getVisibility() == view.INVISIBLE){
                  rvObjects.setVisibility(view.VISIBLE);
                  btnAddObject.setVisibility(view.VISIBLE);
                  rvObjects.setLayoutManager(layoutManager);
                  rvObjects.setHasFixedSize(false);
                  btnNextStep.setVisibility(View.INVISIBLE);
                  btnPreviousStep.setVisibility(View.INVISIBLE);
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
                  rvObjects.setVisibility(view.INVISIBLE);
                  btnAddObject.setVisibility(view.INVISIBLE);
                  hidePositionControls();
                  hideRotationControls();
                  btnEditSet.setText("Edit Steps");
                  btnNextStep.setVisibility(View.VISIBLE);
                  btnPreviousStep.setVisibility(View.VISIBLE);
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
                      object.rotaionX =1f;
                      object.rotaionY =0f;
                      object.rotaionZ =0f;
                      object.rotationalXAxis+=5;
                      //if (object.objectVisual.getLocalRotation() == null){
                      //    object.objectVisual.setLocalRotation(new Quaternion(new Vector3(object.rotaionX ,object.rotaionY, object.rotaionZ),object.rotationalXAxis));
                      //}else{
                      object.objectVisual.setLocalRotation(new Quaternion(new Vector3(object.rotaionX ,object.rotaionY, object.rotaionZ),object.rotationalXAxis));

                      //}
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
                      object.rotaionX =1f;
                      object.rotaionY =0f;
                      object.rotaionZ =0f;
                      object.rotationalXAxis-=5;
                     // if (object.objectVisual.getLocalRotation() == null){
                          object.objectVisual.setLocalRotation(new Quaternion(new Vector3(object.rotaionX ,object.rotaionY, object.rotaionZ),object.rotationalXAxis));
                      //}else{
                       //   object.objectVisual.getLocalRotation().set(new Vector3(object.rotaionX ,object.rotaionY, object.rotaionZ),object.rotationalXAxis);

                     // }
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
                      object.rotaionX =0f;
                      object.rotaionY =1f;
                      object.rotaionZ =0f;
                      object.rotationalYAxis+=5;
                     // if (object.objectVisual.getLocalRotation() == null){
                          object.objectVisual.setLocalRotation(new Quaternion(new Vector3(object.rotaionX ,object.rotaionY, object.rotaionZ),object.rotationalYAxis));
                      //    object.getLocalRotation();
                     // }else{
                      //    object.objectVisual.getLocalRotation().set(new Vector3(object.rotaionX ,object.rotaionY, object.rotaionZ),object.rotationalYAxis);

                     // }
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
                      object.rotaionX =0f;
                      object.rotaionY =1f;
                      object.rotaionZ =0f;
                      object.rotationalYAxis-=5;
                      //if (object.objectVisual.getLocalRotation() == null){
                      //}else{
                      //    Quaternion current = object.getLocalRotation();

                          object.objectVisual.setLocalRotation(new Quaternion(new Vector3(object.rotaionX ,object.rotaionY, object.rotaionZ),object.rotationalYAxis));

                      //}
                  }
              }
          }
      });



      //endregion

      //---------------------------------------------------------------------------------------------------------------------------------
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

  public static boolean checkIsSupportedDeviceOrFinish(final Activity activity) {
    if (Build.VERSION.SDK_INT < VERSION_CODES.N) {
      Log.e(TAG, "Sceneform requires Android N or later");
      Toast.makeText(activity, "Sceneform requires Android N or later", Toast.LENGTH_LONG).show();
      activity.finish();
      return false;
    }
    String openGlVersionString =
        ((ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE))
            .getDeviceConfigurationInfo()
            .getGlEsVersion();
    if (Double.parseDouble(openGlVersionString) < MIN_OPENGL_VERSION) {
      Log.e(TAG, "Sceneform requires OpenGL ES 3.0 later");
      Toast.makeText(activity, "Sceneform requires OpenGL ES 3.0 or later", Toast.LENGTH_LONG)
          .show();
      activity.finish();
      return false;
    }
    return true;
  }

  public void showMessage(){
        Toast.makeText(this, "test test", Toast.LENGTH_LONG).show();
  }
}
