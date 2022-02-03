package com.example.ibdtracker.Data.Colitis;

import android.content.Context;

import com.example.ibdtracker.Data.Crohns.CrohnsResponseRepository;
import com.example.ibdtracker.Data.Crohns.CrohnsSurveyDao;
import com.example.ibdtracker.Data.Crohns.CrohnsSurveyDatabase;
import com.example.ibdtracker.Data.Crohns.CrohnsSurveyResponse;

import java.util.List;

public class ColitisResponseRepository {

    //class fields
    private static ColitisResponseRepository INSTANCE;
    private Context context;
    private ColitisSurveyDao colitisSurveyDao;

    /**
     * Gets the singleton {@link ColitisResponseRepository} for use when managing {@link ColitisSurveyResponse}s
     * @param context the context the app is working in
     * @return the singleton instance used for managing responses.
     */
    public static ColitisResponseRepository getRepository(Context context) {
        if(INSTANCE == null) { //if an instance exists doesn't exist, create a new instance
            synchronized (ColitisResponseRepository.class) {
                if(INSTANCE == null) {
                    //set up the instance of the repository, using the current context
                    INSTANCE = new ColitisResponseRepository();
                    INSTANCE.context = context;

                    //setup crohns survey dao
                    ColitisSurveyDatabase db = ColitisSurveyDatabase.getDatabase(context);
                    INSTANCE.colitisSurveyDao = db.colitisSurveyDao();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * Insert a survey response into the database
     * @param response the response to be entered
     */
    public void storeColitisResponse(ColitisSurveyResponse response) {
        this.colitisSurveyDao.insert(response);
    }

    /**
     * Get every response stored in the database
     * @return a List of every response
     */
    public List<ColitisSurveyResponse> getAllResponses() {
        return this.colitisSurveyDao.getAllResponses();
    }

    /**
     * Check if a response for the responses date exists in the database
     * @param response the response to check if it exists
     * @return true or false
     */
    public boolean exisits(ColitisSurveyResponse response) {
        int number = this.colitisSurveyDao.checkIfExists(response.getDate());
        return number >0;
    }

    /**
     * Remove a response from the database
     * @param response the response to remove from the database
     */
    public void removeResponse(ColitisSurveyResponse response) {
        this.colitisSurveyDao.delete(response.getDate());
    }

    /**
     * Delete all entries in the database
     */
    public void deleteAll() {
        this.colitisSurveyDao.deleteAll();
    }

    /**
     * Update an existing response in the database
     * @param response the response to update
     */
    public void updateResponse(ColitisSurveyResponse response) {
        this.colitisSurveyDao.update(response);
    }

    /**
     * Get the record with a matching date
     * @param date the date of the record
     * @return the record of the matching date
     */
    public ColitisSurveyResponse getFromDate(String date) {
        return this.colitisSurveyDao.getFromDate(date);
    }

    /**
     * Get a list of all responses in the database sorted by date (ascending)
     * @return the sorted list of all responses
     */
    public List<ColitisSurveyResponse> getAllResponsesSorted() {
        return this.colitisSurveyDao.getAllResponsesSorted();
    }
}
