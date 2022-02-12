package com.example.ibdtracker.Data.Colitis;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.LocalDate;

/**
 * Stores the answers of the colitis survey as numbers
 * outlined in the Colitis Activity index
 * Also stores indexes of selected values
 */
@Entity(tableName = "ColitisSurveyResponse")
public class ColitisSurveyResponse {

    //primary key methods
    @NonNull
    @PrimaryKey(autoGenerate = false)
    private String date; //date the survey was taken on

    //class fields
    private int ColitisQ1; //the answer to q1
    private int ColitisQ2; //the answer to q1
    private float ColitisQ3; //the numeric value of Q3
    private int ColitisQ4; //the answer to q4
    private float ColitisQ5; //the numeric value of Q5
    public float ColitisQ6;//the numeric value of q6

    private float weight; //the weight entered in question 6

    private int ColitisQ3ID; //the selected radio button of Q3
    private String ColitisQ5IDs; // the selected chip buttons of Q5
    private int ColitisQ6ID; //the selected radio button of q6

    /**
     * The constructor for a colitis survey response
     * @param ColitisQ1 the numeric value of Q1
     * @param ColitisQ2 the numeric value of Q2
     * @param ColitisQ3 the numeric value of Q3
     * @param ColitisQ4 the numeric value of Q4
     * @param ColitisQ5 the numeric value of Q5
     * @param weight the weight entered in q 6
     * @param ColitisQ3ID the selected radio button of q3
     * @param ColitisQ5IDs the selected chip buttons of q5
     */
    public ColitisSurveyResponse(int ColitisQ1, int ColitisQ2, float ColitisQ3, int ColitisQ4, float ColitisQ5, float ColitisQ6, float weight,
                                 int ColitisQ3ID, String ColitisQ5IDs, int ColitisQ6ID) {
        this.ColitisQ1 = ColitisQ1;
        this.ColitisQ2 = ColitisQ2;
        this.ColitisQ3 = ColitisQ3;
        this.ColitisQ4 = ColitisQ4;
        this.ColitisQ5 = ColitisQ5;
        this.ColitisQ6 = ColitisQ6;

        this.weight = weight;

        this.ColitisQ3ID = ColitisQ3ID;
        this.ColitisQ5IDs = ColitisQ5IDs;
        this.ColitisQ6ID = ColitisQ6ID;

        this.date = LocalDate.now().toString();
    }

    //------------ GETTERS --------------


    public int getColitisQ1() {
        return ColitisQ1;
    }

    public int getColitisQ2() {
        return ColitisQ2;
    }

    public float getColitisQ3() {
        return ColitisQ3;
    }

    public int getColitisQ4() {
        return ColitisQ4;
    }

    public float getColitisQ5() {
        return ColitisQ5;
    }

    public float getWeight() {
        return weight;
    }

    public int getColitisQ3ID() {
        return ColitisQ3ID;
    }

    public String getColitisQ5IDs() {
        return ColitisQ5IDs;
    }

    public float getColitisQ6() {
        return ColitisQ6;
    }

    public int getColitisQ6ID() {
        return ColitisQ6ID;
    }

    @NonNull
    public String getDate() {
        return date;
    }

    //------------ SETTERS ------------


    public void setColitisQ1(int colitisQ1) {
        ColitisQ1 = colitisQ1;
    }

    public void setColitisQ2(int colitisQ2) {
        ColitisQ2 = colitisQ2;
    }

    public void setColitisQ3(float colitisQ3) {
        ColitisQ3 = colitisQ3;
    }

    public void setColitisQ3ID(int colitisQ3ID) {
        ColitisQ3ID = colitisQ3ID;
    }

    public void setColitisQ4(int colitisQ4) {
        ColitisQ4 = colitisQ4;
    }

    public void setColitisQ5(float colitisQ5) {
        ColitisQ5 = colitisQ5;
    }

    public void setColitisQ5IDs(String colitisQ5IDs) {
        ColitisQ5IDs = colitisQ5IDs;
    }

    public void setColitisQ6(float colitisQ6) {
        ColitisQ6 = colitisQ6;
    }

    public void setColitisQ6ID(int colitisQ6ID) {
        ColitisQ6ID = colitisQ6ID;
    }

    public void setDate(@NonNull String date) {
        this.date = date;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }
}
