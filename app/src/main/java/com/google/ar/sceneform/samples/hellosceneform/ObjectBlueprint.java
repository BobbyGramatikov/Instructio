package com.google.ar.sceneform.samples.hellosceneform;

public class ObjectBlueprint {


    public enum objectType{
        Arrow,
    }
    public final objectType objectType;

    public  int stepID;
    public int objectID;
    // public Vector3 localPosition;

    public float positionX;
    public float positionY;
    public float positionZ;

    public float rotationalXAxis;
    public float rotationalYAxis;

    public boolean lastRotationX;

    public ObjectBlueprint(

            objectType objectType,
            int stepID,
            int objectID,
            float positionX,
            float positionY,
            float positionZ,

            float rotationalXAxis,
            float rotationalYAxis,

            boolean lastRotationX
            ) {

        this.objectType = objectType;

        this.stepID = stepID;

        this.positionX = positionX;
        this.positionY = positionY;
        this.positionZ = positionZ;

        this.rotationalXAxis = rotationalXAxis;
        this.rotationalYAxis = rotationalYAxis;

        this.lastRotationX = lastRotationX;

        this.objectID = objectID;

    }
}
