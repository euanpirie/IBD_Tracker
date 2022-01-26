package com.example.ibdtracker.AI;

import com.example.ibdtracker.Data.CrohnsSurveyResponse;
import com.example.ibdtracker.MainActivity;

/**
 * The rule based system that will predict the status of the users IBD
 * Classifications come from the relevent IBD activity indexes.
 */
public class RuleBasedSystem {

    private String IBDType;

    /**
     * The constructor for the rule based system
     * @param IBDType the type of IBD to predict the status of
     */
    public RuleBasedSystem(String IBDType) {
        this.IBDType = IBDType;
    }

    public String predictStatus(CrohnsSurveyResponse response) {

        if(IBDType.equals("Crohns")) {
            float totalScore;

            //get the answers for each question
            float Q1 = response.getCrohnsQ1();
            float Q2 = response.getCrohnsQ2();
            float Q3 = response.getCrohnsQ3();
            float Q4 = response.getCrohnsQ4();
            float Q5 = response.getCrohnsQ5();
            float Q6 = response.getCrohnsQ6();
            float Q7 = response.getCrohnsQ7();

            //sum the scores
            totalScore = Q1 + Q2 + Q3 + Q4 + Q5 + Q6 + Q7;

            if(totalScore < 150) {
                return "asymptomatic remission.";
            }
            else if (totalScore >=150 && totalScore <220) {
                return "a mild-to-moderate flare up.";
            }
            else if (totalScore >= 220 && totalScore <450) {
                return "a moderate-to-severe flare up.";
            }
            else {
                return "a severe flare up.";
            }
        }

        else {
            return "error";
        }

    }
}
