package com.example.ibdtracker.Data;

import android.content.Context;

import java.util.List;

public class CrohnsResponseRepository {

    //class fields
    private static CrohnsResponseRepository INSTANCE;
    private Context context;
    private CrohnsSurveyDao crohnsSurveyDao;

    /**
     * Gets the singleton {@link CrohnsResponseRepository} for use when managing {@link CrohnsSurveyResponse}s
     * @param context the context the app is workling in
     * @return the singleton instance used for managing responses.
     */
    public static CrohnsResponseRepository getRepository(Context context) {
        if(INSTANCE == null) { //if an instance exists doesn't exist, create a new instance
            synchronized (CrohnsResponseRepository.class) {
                if(INSTANCE == null) {
                    //set up the instance of the repository, using the current context
                    INSTANCE = new CrohnsResponseRepository();
                    INSTANCE.context = context;

                    //setup crohns survey dao
                    CrohnsSurveyDatabase db = CrohnsSurveyDatabase.getDatabase(context);
                    INSTANCE.crohnsSurveyDao = db.crohnsSurveyDao();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * insert a response into the db
     * @param response to be inserted
     */
    public void storeCrohnsResponse(CrohnsSurveyResponse response) {
        this.crohnsSurveyDao.insert(response);
    }

    /**
     * get every entry in the db
     * @return a List of all entries
     */
    public List<CrohnsSurveyResponse> getAllResponses() {
        return this.crohnsSurveyDao.getAllResponses();
    }

    /**
     * check if a response exists in the database
     * @param response to check
     * @return a boolean value - true if in db
     */
    public boolean exists(CrohnsSurveyResponse response) {
        int entries = this.crohnsSurveyDao.checkIfExists(response.getDate());
        return entries > 0;
    }

    /**
     * delete a response from the db
     * @param response to be deleted
     */
    public void removeResponse(CrohnsSurveyResponse response) {
        this.crohnsSurveyDao.delete(response.getDate());
    }

    /**
     * delete all entries in the db
     */
    public void deleteAll() {
        this.crohnsSurveyDao.deleteAll();
    }

    /**
     * update an existing response in the db
     * @param response to be updated
     */
    public void updateResponse(CrohnsSurveyResponse response) {
        this.crohnsSurveyDao.update(response);
    }

    /**
     * Return the record with the associated date
     * @param date the date to get the record for
     * @return the record with the corresponding date
     */
    public CrohnsSurveyResponse getFromDate(String date) {
        return this.crohnsSurveyDao.getFromDate(date);
    }
}
