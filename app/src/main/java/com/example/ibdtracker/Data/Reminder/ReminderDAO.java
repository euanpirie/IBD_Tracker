package com.example.ibdtracker.Data.Reminder;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ReminderDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(IBDReminder reminder); //insert response into room db

    @Update
    void update(IBDReminder reminder); //update an existing response

    @Query("SELECT * FROM `IBD Reminders`")
    List<IBDReminder> getAllReminders(); //get all reminders from the db

    @Query("SELECT * FROM `IBD Reminders` WHERE id LIKE :id")
    IBDReminder getReminderFromID(int id); //get the reminder from ID

    @Query("DELETE FROM `IBD Reminders` WHERE id LIKE :id")
    void delete(int id); //delete an entry from the db

    @Query("DELETE FROM `IBD Reminders`")
    void deleteAll(); //delete all entries in the db

    @Query("SELECT COUNT(*) from `IBD Reminders` WHERE reminderText LIKE :reminderText AND reminderHour LIKE :reminderHour AND reminderMinute LIKE :reminderMinute AND reminderIntervalType LIKE :reminderIntervalType AND reminderInterval like :reminderInterval")
    int checkIfExists(String reminderText, int reminderHour, int reminderMinute, String reminderIntervalType, int reminderInterval); //count all occurrences of an entry
}
