package com.example.ibdtracker.Data.Reminder;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Stores the details of a reminder
 * Also stores IDs of radio groups to update view
 */
@Entity(tableName = "IBD Reminders")
public class IBDReminder implements Parcelable {
    //primary key methods
    @NonNull
    @PrimaryKey(autoGenerate = true)
    private int id;

    //class fields
    private String reminderText; //the text displayed in the notification

    private int reminderHour; //the hour the notification will be displayed
    private int reminderMinute; //the minute the notification will be displayed

    private int reminderInterval; //the interval of the reminder

    private String reminderIntervalType; //the type of interval (day, week etc.)
    private int reminderIntervalTypeID; //the id of the selected radio button

    public boolean reminderStatus; //whether the reminder is active

    /**
     * A constructor for a reminder
     * @param reminderText the text displayed in the notification
     * @param reminderHour the hour the notification will be displayed
     * @param reminderMinute the minute the notification will be displayed
     * @param reminderInterval the interval of the reminder
     * @param reminderIntervalType the type of interval (day, week etc.)
     * @param reminderIntervalTypeID the id of the selected radio button
     * @param reminderStatus whether the reminder is active
     */
    public IBDReminder(String reminderText,
                       int reminderHour,
                       int reminderMinute,
                       int reminderInterval,
                       String reminderIntervalType,
                       int reminderIntervalTypeID,
                       boolean reminderStatus) {
        this.reminderText = reminderText;
        this.reminderMinute = reminderMinute;
        this.reminderHour = reminderHour;
        this.reminderInterval = reminderInterval;
        this.reminderIntervalType = reminderIntervalType;
        this.reminderIntervalTypeID = reminderIntervalTypeID;
        this.reminderStatus = reminderStatus;
    }

    /**
     * empty constructor
     */
    public IBDReminder() {}

    //---------- GETTERS ----------

    public int getId() {
        return id;
    }

    public int getReminderHour() {
        return reminderHour;
    }

    public int getReminderInterval() {
        return reminderInterval;
    }

    public int getReminderIntervalTypeID() {
        return reminderIntervalTypeID;
    }

    public int getReminderMinute() {
        return reminderMinute;
    }

    public String getReminderIntervalType() {
        return reminderIntervalType;
    }

    public String getReminderText() {
        return reminderText;
    }

    public boolean isReminderStatus() {
        return reminderStatus;
    }

    //---------- SETTERS ----------


    public void setReminderStatus(boolean reminderStatus) {
        this.reminderStatus = reminderStatus;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setReminderHour(int reminderHour) {
        this.reminderHour = reminderHour;
    }

    public void setReminderInterval(int reminderInterval) {
        this.reminderInterval = reminderInterval;
    }

    public void setReminderIntervalType(String reminderIntervalType) {
        this.reminderIntervalType = reminderIntervalType;
    }

    public void setReminderIntervalTypeID(int reminderIntervalTypeID) {
        this.reminderIntervalTypeID = reminderIntervalTypeID;
    }

    public void setReminderMinute(int reminderMinute) {
        this.reminderMinute = reminderMinute;
    }

    public void setReminderText(String reminderText) {
        this.reminderText = reminderText;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * write the contents to a parcel
     * @param parcel the parcel to write to
     * @param i flags
     */
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(getId());
        parcel.writeString(getReminderText());
        parcel.writeInt(getReminderHour());
        parcel.writeInt(getReminderMinute());
        parcel.writeInt(getReminderInterval());
        parcel.writeString(getReminderIntervalType());
        parcel.writeInt(getReminderIntervalTypeID());
        parcel.writeInt(isReminderStatus() ? 1 : 0);
    }

    //A creator for reading reminders back from a Parcel
    public static final Parcelable.Creator<IBDReminder> CREATOR = new Parcelable.Creator<IBDReminder>() {
        @Override
        public IBDReminder createFromParcel(Parcel source) {
            //create and return a new reminder based on the contents in source
            IBDReminder reminder = new IBDReminder();

            //read the reminder fields in the same order they were written
            reminder.setId(source.readInt());
            reminder.setReminderText(source.readString());
            reminder.setReminderHour(source.readInt());
            reminder.setReminderMinute(source.readInt());
            reminder.setReminderInterval(source.readInt());
            reminder.setReminderIntervalType(source.readString());
            reminder.setReminderIntervalTypeID(source.readInt());
            int boolInt = source.readInt();
            //if bool int is 1, true, else false
            if (boolInt == 1) {
                reminder.setReminderStatus(true);
            } else {
                reminder.setReminderStatus(false);
            }

            //return the reminder parsed from the parcel
            return reminder;
        }

        @Override
        public IBDReminder[] newArray(int size) {
            return new IBDReminder[size];
        }
    };
}
