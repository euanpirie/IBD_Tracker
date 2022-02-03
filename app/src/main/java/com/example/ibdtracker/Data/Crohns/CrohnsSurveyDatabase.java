package com.example.ibdtracker.Data.Crohns;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {CrohnsSurveyResponse.class},  version = 2)
public abstract class CrohnsSurveyDatabase extends RoomDatabase {

    //class fields
    public abstract  CrohnsSurveyDao crohnsSurveyDao(); //crohns survey dao to deal with responses
    private static CrohnsSurveyDatabase INSTANCE = null; //instance of the database

    /**
     * Returns a database of crohns survey responses
     * @param context the context the app is working in
     * @return the database of responses
     */
    public static  CrohnsSurveyDatabase getDatabase(final Context context) {
        if(INSTANCE == null) {
            synchronized (CrohnsSurveyDatabase.class) {
                if(INSTANCE == null) {  //if there is no instance of the database, create one
                    INSTANCE = Room.databaseBuilder(context, CrohnsSurveyDatabase.class, "crohns_response_database")
                            .fallbackToDestructiveMigration()
                            .allowMainThreadQueries() //only for testing - remove for actual build
                            .build();
                }
            }
        }
        return INSTANCE; //return the database instance
    }
}
