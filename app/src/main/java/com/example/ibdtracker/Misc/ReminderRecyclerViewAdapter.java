package com.example.ibdtracker.Misc;

import static android.content.Context.ALARM_SERVICE;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ibdtracker.Data.Reminder.IBDReminder;
import com.example.ibdtracker.Data.Reminder.ReminderRepository;
import com.example.ibdtracker.R;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.List;

public class ReminderRecyclerViewAdapter extends RecyclerView.Adapter<ReminderRecyclerViewAdapter.ReminderViewHolder> {

    //names of extras used when creating an intent
    public static final String EXTRA_REMINDER_ID = "com.example.ibdtracker.Misc.EXTRA_REMINDER_ID";
    public static final String EXTRA_REMINDER_TEXT = "com.example.ibdtracker.Misc.EXTRA_REMINDER_TEXT";
    public static final String EXTRA_REMINDER_HOUR = "com.example.ibdtracker.Misc.EXTRA_REMINDER_HOUR";
    public static final String EXTRA_REMINDER_MINUTE = "com.example.ibdtracker.Misc.EXTRA_REMINDER_MINUTE";
    public static final String EXTRA_REMINDER_INTERVAL = "com.example.ibdtracker.Misc.EXTRA_REMINDER_INTERVAL";
    public static final String EXTRA_REMINDER_INTERVAL_TYPE = "com.example.ibdtracker.Misc.EXTRA_REMINDER_INTERVAL_TYPE";
    public static final String EXTRA_REMINDER_INTERVAL_TYPE_ID = "com.example.ibdtracker.Misc.EXTRA_REMINDER_INTERVAL_TYPE_ID";
    public static final String EXTRA_REMINDER_STATUS = "com.example.ibdtracker.Misc.EXTRA_REMINDER_STATUS";

    //class fields
    private Context context; //the context the app is working in
    private List<IBDReminder> reminders; //a list of reminders to display

    //final values of number of milliseconds in a given timeframe
    private static final long milInHour = 3600000L;
    private static final long milInDay = 86400000L;
    private static final long milInWeek = 604800000L;
    private static final long milInMonth = 2592000000L;

    /**
     * A constructor for the view adapater
     * @param context the context the app is working in
     * @param reminders a list of reminders to display
     */
    public ReminderRecyclerViewAdapter(Context context, List<IBDReminder> reminders) {
        super();

        this.context = context;
        this.reminders = reminders;
    }

    @NonNull
    @Override
    public ReminderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //create a view with the layout of a recycler item
        View reminderView = LayoutInflater.from(context).inflate(R.layout.reminder_recycler_item, parent, false);

        //create and return the view holder
        ReminderViewHolder holder = new ReminderViewHolder(reminderView, this);
        return holder;
    }

    @Override
    public void onBindViewHolder(ReminderViewHolder holder, int position) {
        //get the reminder at the specified position
        IBDReminder reminder = reminders.get(position);

        //update the reminder text text view
        TextView tvReminderText = holder.reminderItemView.findViewById(R.id.tvReminderText);
        tvReminderText.setText(reminder.getReminderText());

        //if the minute is a single number, add a preceding 0
        String minuteString = "" + reminder.getReminderMinute();
        if(minuteString.length() == 1) {
            minuteString = "0" + minuteString;
        }

        //create a time string from the reminder
        String time = reminder.getReminderHour() + ":" + minuteString;

        //update the reminder time text view
        TextView tvReminderTime = holder.reminderItemView.findViewById(R.id.tvReminderTime);
        tvReminderTime.setText(time);

        //create a frequency string from the reminder
        String frequency = "Every " + reminder.getReminderInterval() + " " + reminder.getReminderIntervalType() + "(s)";

        //update the reminder frequency text view
        TextView tvReminderFrequency = holder.reminderItemView.findViewById(R.id.tvReminderFrequency);
        tvReminderFrequency.setText(frequency);

        //set the active switch to the reminder status
        Switch switchActive = holder.reminderItemView.findViewById(R.id.switchActive);
        switchActive.setChecked(reminder.isReminderStatus());

        //cancel notification
    }

    @Override
    public int getItemCount() {
        return this.reminders.size();
    }

    /**
     * A view holder for reminder items
     */
    class ReminderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        //class fields
        private View reminderItemView; //the item view
        public ReminderRecyclerViewAdapter adapter; //the recycler adapter

        /**
         * A constructor for a view holder
         * @param reminderItemView the view of the item
         * @param adapter the recycler adapter
         */
        public ReminderViewHolder(View reminderItemView, ReminderRecyclerViewAdapter adapter) {
            super(reminderItemView);

            this.reminderItemView = reminderItemView;
            this.adapter = adapter;

            //add on click listeners to the item view, button and switch
            reminderItemView.setOnClickListener(this);
            reminderItemView.findViewById(R.id.btnDelete).setOnClickListener(this);
            reminderItemView.findViewById(R.id.switchActive).setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            //if the delete button was pressed
            if(view.getId() == R.id.btnDelete) {
                //get the item position in the list
                int position = getAdapterPosition();

                //get the reminder from the position
                IBDReminder reminder = reminders.get(position);

                //delete the reminder from the database
                ReminderRepository.getRepository(context.getApplicationContext()).deleteReminder(reminder.getId());

                //remove the reminder from the list
                reminders.remove(position);

                //create a new calendar object
                Calendar calendar = Calendar.getInstance();

                //set the time of the calendar to the reminder time - reminder will start at this time
                calendar.set(Calendar.HOUR_OF_DAY, reminder.getReminderHour());
                calendar.set(Calendar.MINUTE, reminder.getReminderMinute());

                //create a pending intent
                PendingIntent pendingIntent = createPendingIntent(reminder);

                //delete the alarm
                deleteAlarm(pendingIntent);

                //notify the adapter that the data set has changed - refresh
                adapter.notifyDataSetChanged();
            }
            //if the switch was pressed
            else if(view.getId() == R.id.switchActive) {
                //get the item position in the list
                int position = getAdapterPosition();

                //get the reminder from the position
                IBDReminder reminder = reminders.get(position);

                //get the opposite active value
                boolean active = !reminder.isReminderStatus();

                //set the value to the opposite
                reminder.setReminderStatus(active);

                //update the reminder in the db
                ReminderRepository.getRepository(context).updateReminder(reminder);

                //update the switch
                Switch switchActive = reminderItemView.findViewById(R.id.switchActive);
                switchActive.setChecked(active);

                //create a new calendar object
                Calendar calendar = Calendar.getInstance();

                //set the time of the calendar to the reminder time - reminder will start at this time
                calendar.set(Calendar.HOUR_OF_DAY, reminder.getReminderHour());
                calendar.set(Calendar.MINUTE, reminder.getReminderMinute());

                //create a pending intent to be carried out by the alarm manager
                PendingIntent pendingIntent = createPendingIntent(reminder);

                //create an alarm manager to handle repeating alarms
                AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);

                //if the reminder is active
                if(active) {
                    //create a long
                    long interval = 0L;

                    //if the interval type is hours
                    if(reminder.getReminderIntervalType().equals("Hours")) {
                        interval = milInHour;
                    }
                    //if the interval type is days
                    else if(reminder.getReminderIntervalType().equals("Days")) {
                        interval = milInDay;
                    }
                    //if the interval type is weeks
                    else if(reminder.getReminderIntervalType().equals("Weeks")) {
                        interval = milInWeek;
                    }
                    //else it is month
                    else {
                        interval = milInMonth;
                    }

                    //multiply the interval by the frequency
                    interval = interval * reminder.getReminderInterval();

                    //create a repeating task
                    //inexact repeating is used as all repeating alarms should be inexeact
                    alarmManager.setInexactRepeating(AlarmManager.RTC, calendar.getTimeInMillis(), interval, pendingIntent);
                }
                //othwerwise
                else {
                    //cancel the alarm
                    alarmManager.cancel(pendingIntent);
                }
            }
            //else, the view was pressed
            else
            {
                //get the position of the item
                int position = getAdapterPosition();

                //get the reminder in position
                IBDReminder reminder = reminders.get(position);

                //Create the intent using the app context and the add reminder activity
                Intent intent = new Intent(adapter.context, AddReminderActivity.class);

                //add all of the details needed by the activity
                intent.putExtra(EXTRA_REMINDER_TEXT, reminder.getReminderText());
                intent.putExtra(EXTRA_REMINDER_HOUR, reminder.getReminderHour());
                intent.putExtra(EXTRA_REMINDER_MINUTE, reminder.getReminderMinute());
                intent.putExtra(EXTRA_REMINDER_INTERVAL, reminder.getReminderInterval());
                intent.putExtra(EXTRA_REMINDER_INTERVAL_TYPE_ID, reminder.getReminderIntervalTypeID());
                intent.putExtra(EXTRA_REMINDER_STATUS, reminder.isReminderStatus());
                intent.putExtra(EXTRA_REMINDER_ID, reminder.getId());

                //add flag as activity is not being launched from an activity
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                //start the activity
                adapter.context.startActivity(intent);
            }
        }
    }

    /**
     * A method to delete an alarm
     * @param intent the intent of the alarm to delete
     */
    public void deleteAlarm(PendingIntent intent) {
        //create an alarm manager to handle repeating alarms
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);

        //cancel alarm
        alarmManager.cancel(intent);
    }

    /**
     * A method to create a pending intent from a reminder
     * @param reminder the reminder to create the pending intent for
     * @return the pending intent
     */
    public PendingIntent createPendingIntent(IBDReminder reminder) {
        //create a new alarm reciever intent
        Intent intent = new Intent(context, AlarmReceiver.class);

        //put the id and reminder text as extras
        intent.putExtra(EXTRA_REMINDER_ID, reminder.getId());
        intent.putExtra(EXTRA_REMINDER_TEXT, reminder.getReminderText());

        //create a pending indent from the previous intent - flag immutable as per documentation
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, reminder.getId(), intent, PendingIntent.FLAG_IMMUTABLE);

        return pendingIntent;
    }
}
