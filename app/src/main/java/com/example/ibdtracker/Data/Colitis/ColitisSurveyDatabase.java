package com.example.ibdtracker.Data.Colitis;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {ColitisSurveyResponse.class},  version = 3)
public abstract class ColitisSurveyDatabase extends RoomDatabase {

    //class fields
    public abstract  ColitisSurveyDao colitisSurveyDao(); //crohns survey dao to deal with responses
    private static ColitisSurveyDatabase INSTANCE = null; //instance of the database

    /**
     * Returns a database of colitis survey responses
     * @param context the context the app is working in
     * @return the database of responses
     */
    public static  ColitisSurveyDatabase getDatabase(final Context context) {
        if(INSTANCE == null) {
            synchronized (ColitisSurveyDatabase.class) {
                if(INSTANCE == null) {  //if there is no instance of the database, create one
                    INSTANCE = Room.databaseBuilder(context, ColitisSurveyDatabase.class, "colitis_response_database")
                            .fallbackToDestructiveMigration()
                            .allowMainThreadQueries() //only for testing - remove for actual build
                            .build();
                }
            }
        }
        return INSTANCE; //return the database instance
    }
}
