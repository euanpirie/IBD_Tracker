package com.example.ibdtracker.AI;

import com.example.ibdtracker.Data.Colitis.ColitisSurveyResponse;
import com.example.ibdtracker.Data.Crohns.CrohnsSurveyResponse;
import com.example.ibdtracker.MainActivity;

import java.util.List;

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

    public String predictStatus(List<?> responses) {

        //check the list is empty or is null
        if(responses.isEmpty() || responses == null) {
            return "The AI needs 7 days of data in order to work.";
        }

        //if the list doesn't contain 7 entries
        if(responses.size() < 7) {
            return "The AI requires 7 days worth of data to work.";
        }
        //otherwise, cut the list to seven elements
        else {
            responses = responses.subList(responses.size() - 7, responses.size());
        }

        if(IBDType.equals("Crohns")) {
            //score and averages initialisation
            float totalScore;

            float q1Average = 0;
            float q2Average = 0;
            float q3Average = 0;
            float q4Average = 0;
            float q5Average = 0;
            float q6Average = 0;
            float q7Average = 0;

            float q1Score = 0;
            float q2Score = 0;
            float q3Score = 0;
            float q4Score = 0;
            float q5Score = 0;
            float q6Score = 0;
            float q7Score = 0;

            //for every object in the responses list
            for(Object o: responses) {
                //if the object is a crohns survey response
                if(o instanceof CrohnsSurveyResponse) {
                    //cast o to crohns survey response object
                    CrohnsSurveyResponse r = (CrohnsSurveyResponse) o;

                    //get the answers from the response
                    q1Average += r.getCrohnsQ1();
                    q2Average += r.getCrohnsQ2();
                    q3Average += r.getCrohnsQ3();
                    q4Average += r.getCrohnsQ4();
                    q5Average += r.getCrohnsQ5();
                    q6Average += r.getCrohnsQ6();
                    q7Average += r.getCrohnsQ7();
                }

                //otherwise return an error
                else {
                    return "Error: didn't receive a crohns survey response.";
                }
            }

            //average and round
            q1Average = Math.round(q1Average/7);
            q2Average = Math.round(q2Average/7);
            q3Average = Math.round(q3Average/7);
            q4Average = Math.round(q4Average/7);
            q5Average = Math.round(q5Average/7);
            q6Average = Math.round(q6Average/7);
            q7Average = Math.round(q7Average/7);

            //calculate score for question 1
            q1Score = q1Average * 7.0f;

            //calculate score for question 2
            q2Score = q2Average * 30.f;

            //calculate score for question 3
            q3Score = q3Average * 35.0f;

            //calculate score for question 4
            q4Score = q4Average * 49.0f;

            //calculate the score for question 5
            q5Score = q5Average * 20.0f;

            //calculate the score for question 6
            float smallestDifference = 100.0f;
            float[] q6PossibleScores = new float[]{0.0f, 20.0f, 50.0f};

            //find the value closest to the average
            for(int i = 0; i < q6PossibleScores.length; i++) {
                //calculate the difference between the average and the
                float difference = Math.abs(q6Average - q6PossibleScores[i]);

                //if the difference is less than the current smallest
                if(difference < smallestDifference) {
                    //set the new smallest and set the score
                    smallestDifference = difference;
                    q6Score = q6PossibleScores[i];
                }
            }

            //calculate the score for question 7
            q7Score = q7Average * 6.0f;

            //sum the scores
            totalScore = q1Score + q2Score + q3Score + q4Score + q5Score + q6Score + q7Score;

            //return final classification
            if(totalScore < 150) {
                return "The AI has predicted you are experiencing asymptomatic remission.";
            }
            else if (totalScore >=150 && totalScore <220) {
                return "The AI has predicted you are experiencing a mild-to-moderate flare up.";
            }
            else if (totalScore >= 220 && totalScore <450) {
                return "The AI has predicted you are experiencing a moderate-to-severe flare up.";
            }
            else {
                return "The AI has predicted you are experiencing a severe flare up.";
            }
        }

        //if the IBD Type is colitis
        else if(IBDType.equals("Colitis")) {
            //score and averages initialisation
            float totalScore;

            float q1Average = 0;
            float q2Average = 0;
            float q3Average = 0;
            float q4Average = 0;
            float q5Average = 0;
            float q6Average = 0;

            float q1Score = 0;
            float q2Score = 0;
            float q4Score = 0;

            //for every element in the array
            for(Object o : responses) {
                //if the object is a colitis survey response
                if (o instanceof ColitisSurveyResponse) {

                    ColitisSurveyResponse response = (ColitisSurveyResponse) o;

                    //add answers to score totals
                    q1Average += response.getColitisQ1();
                    q2Average += response.getColitisQ2();
                    q3Average += response.getColitisQ3();
                    q4Average += response.getColitisQ4();
                    q5Average += response.getColitisQ5();
                    q6Average += response.getColitisQ6();
                }

                //if the object isnt a colitis survey response
                else {
                    return "Error: didn't receive a ColitisSurveyResponse.";
                }
            }

            //divide summed scores by 7 to get average
            q1Average = Math.round(q1Average/7);
            q2Average = Math.round(q2Average/7);
            q3Average = Math.round(q3Average/7);
            q4Average = Math.round(q4Average/7);
            q5Average = Math.round(q5Average/7);
            q6Average = Math.round(q6Average/7);

            //calculate score for q1 depending on the average
            if(q1Average >= 0 && q1Average <= 3) {
                q1Score = 0;
            }
            else if(q1Average >=4 && q1Average <=6) {
                q1Score = 1;
            }
            else if(q1Average >= 7 && q1Average <= 9) {
                q1Score = 2;
            }
            else {
                q1Score = 3;
            }

            //calculate the score for q2 depending on the average
            if(q2Average == 0) {
                q2Score = 0;
            }
            else if(q2Average >= 1 && q2Average <= 3) {
                q2Score = 1;
            }
            else {
                q2Score = 2;
            }

            //calculate the score for q4 depending on the average
            if(q4Average >= 7) {
                q4Score = 0;
            }
            else if(q4Average == 6) {
                q4Score = 1;
            }
            else if(q4Average == 5) {
                q4Score = 2;
            }
            else {
                q4Score = 3;
            }

            //sum all neccessary averages/scores
            totalScore = q1Score + q2Score + q3Average + q4Score + q5Average + q6Average;

            //return final classification depending on the results
            if(totalScore >=5) {
                return "The AI has predicted your Colitis is currently active.";
            }
            else {
                return "The AI has predicted your Colitis is in remission.";
            }
        }

        //if no other return statements are reached, return an error message
        return "There has been an error. No other returns reached.";
    }
}
