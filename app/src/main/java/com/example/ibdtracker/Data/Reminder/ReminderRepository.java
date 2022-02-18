package com.example.ibdtracker.Data.Reminder;

import android.content.Context;

import com.example.ibdtracker.Data.Crohns.CrohnsResponseRepository;
import com.example.ibdtracker.Data.Crohns.CrohnsSurveyDatabase;
import com.example.ibdtracker.Data.Crohns.CrohnsSurveyResponse;

import java.util.List;

public class ReminderRepository {

    //class fields
    private static ReminderRepository INSTANCE;
    private Context context;
    private ReminderDAO reminderDAO;

    /**
     * Gets the singleton {@link ReminderRepository} for use when managing {@link IBDReminder}s
     * @param context the context the app is working in
     * @return the singleton instance used for managing responses.
     */
    public static ReminderRepository getRepository(Context context) {
        if(INSTANCE == null) { //if an instance exists doesn't exist, create a new instance
            synchronized (ReminderRepository.class) {
                if(INSTANCE == null) {
                    //set up the instance of the repository, using the current context
                    INSTANCE = new ReminderRepository();
                    INSTANCE.context = context;

                    //setup reminder dao
                    ReminderDatabase db = ReminderDatabase.getDatabase(context);
                    INSTANCE.reminderDAO = db.reminderDAO();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * insert a reminder into the db
     * @param reminder the reminder to insert
     */
    public void storeReminder(IBDReminder reminder) {
        reminderDAO.insert(reminder);
    }

    /**
     * update an existing entry in the db
     * @param reminder the reminder to update
     */
    public void updateReminder(IBDReminder reminder) {
        reminderDAO.update(reminder);
    }

    /**
     * get a list of all reminders in the db
     * @return a list of all reminders
     */
    public List<IBDReminder> getAllReminders() {
        return reminderDAO.getAllReminders();
    }

    /**
     * get a reminder associated with an id
     * @param id the id to find
     * @return the record with the corresponding date
     */
    public IBDReminder getReminderFromID(int id) {
        return reminderDAO.getReminderFromID(id);
    }

    /**
     * delete a reminder associated with an id
     * @param id the id of the reminder to delete
     */
    public void deleteReminder(int id) {
        reminderDAO.delete(id);
    }

    /**
     * delete all reminders from the db
     */
    public void deleteAll() {
        reminderDAO.deleteAll();
    }

    /**
     * check if a response exists in the database
     * @param reminder reminder to check
     * @return a boolean value - true if in db
     */
    public boolean exists(IBDReminder reminder) {
        int entries = reminderDAO.checkIfExists(reminder.getReminderText(),
                reminder.getReminderHour(),
                reminder.getReminderMinute(),
                reminder.getReminderIntervalType(),
                reminder.getReminderInterval());
        return entries > 0;
    }
}
