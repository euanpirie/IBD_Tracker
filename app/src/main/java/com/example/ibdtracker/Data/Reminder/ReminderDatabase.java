package com.example.ibdtracker.Data.Reminder;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.ibdtracker.Data.Crohns.CrohnsSurveyDatabase;

@Database(entities = {IBDReminder.class}, version = 1)
public abstract class ReminderDatabase extends RoomDatabase {

    //class fields
    public abstract ReminderDAO reminderDAO(); //reminder dao to deal with responses
    private static ReminderDatabase INSTANCE = null; //instance of the database

    /**
     * Returns a database of crohns survey responses
     * @param context the context the app is working in
     * @return the database of reminders
     */
    public static ReminderDatabase getDatabase(final Context context) {
        if(INSTANCE == null) {
            synchronized (ReminderDatabase.class) {
                if(INSTANCE == null) {  //if there is no instance of the database, create one
                    INSTANCE = Room.databaseBuilder(context, ReminderDatabase.class, "reminder_database")
                            .fallbackToDestructiveMigration()
                            .allowMainThreadQueries() //only for testing - remove for actual build
                            .build();
                }
            }
        }
        return INSTANCE; //return the database instance
    }
}
