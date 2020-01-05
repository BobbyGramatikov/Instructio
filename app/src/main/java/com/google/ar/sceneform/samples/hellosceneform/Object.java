package com.google.ar.sceneform.samples.hellosceneform;

import android.view.MotionEvent;

import com.google.ar.sceneform.FrameTime;
import com.google.ar.sceneform.HitTestResult;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.ArFragment;

import proguard.classfile.attribute.preverification.ObjectType;


public class Object extends Node  {



    private ModelRenderable objectRenderable;

    public int stepID;
    public int objectID;
    public Node objectVisual;

   // public Vector3 localPosition;
    public float positionX;
    public float positionY;
    public float positionZ;

    public float rotationalXAxis;
    public float rotationalYAxis;

    public boolean lastRotationX;

    public ObjectBlueprint.objectType objectType;

    public Object(

            ModelRenderable objectRenderable,

            ObjectBlueprint objectBlueprint
    ) {

        this.objectRenderable = objectRenderable;

        this.objectType = objectBlueprint.objectType;
        this.stepID = objectBlueprint.stepID;
        this.positionX = objectBlueprint.positionX;
        this.positionY = objectBlueprint.positionY;
        this.positionZ = objectBlueprint.positionZ;
        this.objectID = objectBlueprint.objectID;
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

    public void DeleteRenderable(){
        this.objectVisual = null;
        this.objectRenderable = null;
    }

    public ObjectBlueprint getObjectBlueprint(){
       ObjectBlueprint o = new ObjectBlueprint(this.objectType,this.stepID,this.objectID,this.positionX,
               this.positionY,this.positionZ,this.rotationalXAxis,this.rotationalYAxis,this.lastRotationX);
       return  o;
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



