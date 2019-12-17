package com.google.ar.sceneform.samples.hellosceneform;

import android.view.MotionEvent;

import com.google.ar.sceneform.FrameTime;
import com.google.ar.sceneform.HitTestResult;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.ArFragment;



public class Object extends Node implements Node.OnTapListener {

    public final String objectName;
    private final float objectScale;
    private final float rotationalTilt;
    private final ModelRenderable objectRenderable;
    public final int stepID;
    public int objectID;
    public Node objectVisual;
   // public Vector3 localPosition;
    private ArFragment arFragment;

    public float positionX;
    public float positionY;
    public float positionZ;
    public float rotationalXAxis;
    public float rotationalYAxis;
    public float rotaionX;
    public float rotaionY;
    public float rotaionZ;


    public Object(

            String objectName,
            float objectScale,
            float rotationalTilt,
            ModelRenderable objectRenderable, int stepID,
            int objectID,
            //Vector3 position
            float positionX,
            float positionY,
            float positionZ

    ) {

        this.objectName = objectName;
        this.objectScale = objectScale;
        this.rotationalTilt = rotationalTilt;
        this.objectRenderable = objectRenderable;
        this.stepID = stepID;
        this.positionX = positionX;
        this.positionY = positionY;
        this.positionZ = positionZ;
        this.objectID = objectID;

    }


    public float getPositionX() {
        return positionX;
    }
    public float getPositionY() {
        return positionY;
    }
    public float getPositionZ() {
        return positionZ;
    }

    @Override
    @SuppressWarnings({"AndroidApiChecker", "FutureReturnValueIgnored"})
        public void onActivate() {

        if (getScene() == null) {
            throw new IllegalStateException("Scene is null!");
        }

        if (objectVisual == null) {
            // Put a rotator to counter the effects of orbit, and allow the planet orientation to remain
            // of planets like Uranus (which has high tilt) to keep tilted towards the same direction
            // wherever it is in its orbit.
            objectVisual = new Node();
            objectVisual.setParent(this);
            objectVisual.setRenderable(objectRenderable);
            objectVisual.setLocalScale(new Vector3(1, 1, 1));
          //  objectVisual.setLocalRotation(Quaternion.axisAngle((new Vector3(1, 1, 1)), 0.7f));
        }

       /* if (infoCard == null) {
            infoCard = new Node();
            infoCard.setParent(this);
            infoCard.setEnabled(false);
            infoCard.setLocalPosition(new Vector3(0.0f, objectScale * INFO_CARD_Y_POS_COEFF, 0.0f));

            ViewRenderable.builder()
                    .setView(context, R.layout.planet_card_view)
                    .build()
                    .thenAccept(
                            (renderable) -> {
                                infoCard.setRenderable(renderable);
                                TextView textView = (TextView) renderable.getView();
                                textView.setText(objectName);
                            })
                    .exceptionally(
                            (throwable) -> {
                                throw new AssertionError("Could not load plane card view.", throwable);
                            });
        }*/


    }
    @Override
    public void onTap(HitTestResult hitTestResult, MotionEvent motionEvent) {


       // infoCard.setEnabled(!infoCard.isEnabled());

    }

    @Override
    public void onUpdate(FrameTime frameTime) {

        // Typically, getScene() will never return null because onUpdate() is only called when the node
        // is in the scene.
        // However, if onUpdate is called explicitly or if the node is removed from the scene on a
        // different thread during onUpdate, then getScene may be null.
        if (getScene() == null) {
            return;
        }
       /* Vector3 cameraPosition = getScene().getCamera().getWorldPosition();
        Vector3 cardPosition = infoCard.getWorldPosition();
        Vector3 direction = Vector3.subtract(cameraPosition, cardPosition);
        Quaternion lookRotation = Quaternion.lookRotation(direction, Vector3.up());
        infoCard.setWorldRotation(lookRotation); */



    }
}
    /**
     * Node that represents a planet.
     *
     * <p>The planet creates two child nodes when it is activated:
     *
     * <ul>
     *   <li>The visual of the planet, rotates along it's own axis and renders the planet.
     *   <li>An info card, renders an Android View that displays the name of the planerendt. This can be
     *       toggled on and off.
     * </ul>
     *
     * The planet is rendered by a child instead of this node so that the spinning of the planet doesn't
     * make the info card spin as well.
     */



