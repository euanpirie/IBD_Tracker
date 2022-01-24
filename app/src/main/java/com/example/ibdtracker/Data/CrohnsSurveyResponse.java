package com.example.ibdtracker.Data;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.LocalDate;
import java.util.Date;

/**
 * Stores the answers to the crohns survey as the numbers
 * outlined in the crohns disease activityu index
 *
 */
@Entity(tableName = "CrohnsSurveyResponse")
public class CrohnsSurveyResponse {

    //primary key methods
    @NonNull
    @PrimaryKey(autoGenerate = false)
    private String date; //date the survey was taken on

    //class fields
    private float CrohnsQ1; //the numeric value of q1
    private float CrohnsQ2; //the numeric value of q2
    private float CrohnsQ3; //the numeric value of q3
    private float CrohnsQ4; //the numeric value of q4
    private float CrohnsQ5; //the numeric value of q5
    private float CrohnsQ6; //the numeric value of q6
    private float CrohnsQ7; //the numeric value of q7
    private float Weight; //the weight entered in the question - used for dashboard


    /**
     * Create s CrohnsSurveyResponse object
     * @param q1 the numeric value of q1
     * @param q2 the numeric value of q2
     * @param q3 the numeric value of q3
     * @param q4 the numeric value of q4
     * @param q5 the numeric value of q5
     * @param q6 the numeric value of q6
     * @param q7 the numeric value of q7
     * @param weight the weight entered in the question - used for dashboard
     */
    public CrohnsSurveyResponse(float CrohnsQ1, float CrohnsQ2, float CrohnsQ3, float CrohnsQ4,float CrohnsQ5, float CrohnsQ6, float CrohnsQ7, float Weight) {
        this.CrohnsQ1 = CrohnsQ1;
        this.CrohnsQ2 = CrohnsQ2;
        this.CrohnsQ3 = CrohnsQ3;
        this.CrohnsQ4 = CrohnsQ4;
        this.CrohnsQ5 = CrohnsQ5;
        this.CrohnsQ6 = CrohnsQ6;
        this.CrohnsQ7 = CrohnsQ7;
        this.Weight = Weight;
        this.date = LocalDate.now().toString();
    }

    // ------------------------- GETTERS ---------------------------
    public String getDate() {
        return date;
    }

    public float getCrohnsQ1() {
        return CrohnsQ1;
    }

    public float getCrohnsQ2() {
        return CrohnsQ2;
    }

    public float getCrohnsQ3() {
        return CrohnsQ3;
    }

    public float getCrohnsQ4() {
        return CrohnsQ4;
    }

    public float getCrohnsQ5() {
        return CrohnsQ5;
    }

    public float getCrohnsQ6() {
        return CrohnsQ6;
    }

    public float getCrohnsQ7() {
        return CrohnsQ7;
    }

    public float getWeight() {
        return Weight;
    }

    //--------------------- SETTERS -----------------------

    public void setCrohnsQ1(float crohnsQ1) {
        CrohnsQ1 = crohnsQ1;
    }

    public void setCrohnsQ2(float crohnsQ2) {
        CrohnsQ2 = crohnsQ2;
    }

    public void setCrohnsQ3(float crohnsQ3) {
        CrohnsQ3 = crohnsQ3;
    }

    public void setCrohnsQ4(float crohnsQ4) {
        CrohnsQ4 = crohnsQ4;
    }

    public void setCrohnsQ5(float crohnsQ5) {
        CrohnsQ5 = crohnsQ5;
    }

    public void setCrohnsQ6(float crohnsQ6) {
        CrohnsQ6 = crohnsQ6;
    }

    public void setCrohnsQ7(float crohnsQ7) {
        CrohnsQ7 = crohnsQ7;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setWeight(float weight) {
        this.Weight = weight;
    }
}

