package com.google.ar.sceneform.samples.hellosceneform;

import android.support.v7.app.AppCompatActivity;

public class ObjectActivities extends AppCompatActivity {

    /*
    private static final int RC_PERMISSIONS = 0x123;
    private boolean installRequested;

    private GestureDetector gestureDetector;
    private Snackbar loadingMessageSnackbar = null;

    private ArSceneView arSceneView;

    private ModelRenderable arrowRenderable;
    private ModelRenderable lineRenderable;

    private final ObjectSettings objectSettings = new ObjectSettings();

    // True once scene is loaded
    private boolean hasFinishedLoading = false;
    private int stepID = 0;
    private int objectID = 0;
    // True once the scene has been placed.
    private boolean hasPlacedMainObject = false;

    // Astronomical units to meters ratio. Used for positioning the planets of the solar system.
    private static final float AU_TO_METERS = 0.5f;

    @Override
    @SuppressWarnings({"AndroidApiChecker", "FutureReturnValueIgnored"})
    // CompletableFuture requires api level 24
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_objects);
        arSceneView = findViewById(R.id.ar_scene_view);

        // Build all the planet models.
        CompletableFuture<ModelRenderable> arrowStage =
                ModelRenderable.builder().setSource(this, Uri.parse("arrow.sfb")).build();
        CompletableFuture<ModelRenderable> lineStage =
                ModelRenderable.builder().setSource(this, Uri.parse("line.sfb")).build();


        CompletableFuture.allOf(
                arrowStage,
                lineStage
                ).handle(
                (notUsed, throwable) -> {
                    // When you build a Renderable, Sceneform loads its resources in the background while
                    // returning a CompletableFuture. Call handle(), thenAccept(), or check isDone()
                    // before calling get().

                    if (throwable != null) {

                        return null;
                    }

                    try {
                        arrowRenderable = arrowStage.get();
                        lineRenderable = lineStage.get();


                        // Everything finished loading successfully.
                        hasFinishedLoading = true;

                    } catch (InterruptedException | ExecutionException ex) {

                    }

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

        arSceneView
                .getScene()
                .setOnTouchListener(
                        (HitTestResult hitTestResult, MotionEvent event) -> {
                            // If the solar system hasn't been placed yet, detect a tap and then check to see if
                            // the tap occurred on an ARCore plane to place the solar system.
                            if (!hasPlacedMainObject) {
                                return gestureDetector.onTouchEvent(event);
                            }

                            // Otherwise return false so that the touch event can propagate to the scene.
                            return false;
                        });

        // Set a touch listener on the Scene to listen for taps.
        // Set an update listener on the Scene that will hide the loading message once a Plane is
        // detected.
        arSceneView
                .getScene()
                .addOnUpdateListener(
                        frameTime -> {
                            if (loadingMessageSnackbar == null) {
                                return;
                            }

                            Frame frame = arSceneView.getArFrame();
                            if (frame == null) {
                                return;
                            }

                            if (frame.getCamera().getTrackingState() != TrackingState.TRACKING) {
                                return;
                            }

                            for (Plane plane : frame.getUpdatedTrackables(Plane.class)) {
                                if (plane.getTrackingState() == TrackingState.TRACKING) {
                                    hideLoadingMessage();
                                }
                            }
                        });

        // Lastly request CAMERA permission which is required by ARCore.
       // DemoUtils.requestCameraPermission(this, RC_PERMISSIONS);
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
        lineVisual.setRenderable(lineRenderable);
        lineVisual.setLocalScale(new Vector3(0.5f, 0.5f, 0.5f));


        // Toggle the solar controls on and off by tapping the sun.

        //objectArray of items to be created

       // createObject("Arrow", line, 0.4f, arrowRenderable,  0.019f, 0.03f,stepID);

        // createObject("Line", sun, 0.7f, 35f, venusRenderable, 0.0475f, 2.64f);

       // Node earth = createObject("Earth", sun, 1.0f, 29f,  0.05f, 23.4f);



        return base;
    }

    private Node (
            String name,
            Node parent,
            float auFromParent,
            ModelRenderable renderable,
            float objectScale,
            float rotationalTilt,
            int stepID) {
        // Orbit is a rotating node with no renderable positioned at the sun.
        // The planet is positioned relative to the orbit so that it appears to rotate around the sun.
        // This is done instead of making the sun rotate so each planet can orbit at its own speed.
        /*RotatingNode orbit = new RotatingNode(solarSettings, true, false, 0);
        orbit.setDegreesPerSecond(orbitDegreesPerSecond);
        orbit.setParent(parent);*/

        // Create the planet and position it relative to the sun.

}
