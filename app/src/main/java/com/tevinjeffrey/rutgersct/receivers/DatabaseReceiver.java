package com.tevinjeffrey.rutgersct.receivers;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.tevinjeffrey.rutgersct.RutgersCTApp;
import com.tevinjeffrey.rutgersct.database.DatabaseHandler;
import com.tevinjeffrey.rutgersct.model.Course;
import com.tevinjeffrey.rutgersct.model.Request;

public class DatabaseReceiver extends BroadcastReceiver {
    public DatabaseReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Request request = intent.getParcelableExtra(RutgersCTApp.REQUEST);
        Course course = intent.getParcelableExtra(RutgersCTApp.SELECTED_COURSE);
        if (request != null && course != null) {
            //Removes section from databse
            if (DatabaseHandler.removeSectionFromDb(request) == 1) {
                //Notify user with a toast.
                notifyUser(context, course);
                //Remove notification from notification panel
                removeNotification(context, request);
            }
        }
    }

    private void removeNotification(Context context, Request request) {
        NotificationManager mNotifyMgr =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotifyMgr.cancel(Integer.valueOf(request.getIndex()));
    }

    private void notifyUser(Context context, Course course) {
        Toast.makeText(context, String.format("Stopped tracking %s", course.getTrueTitle()), Toast.LENGTH_SHORT).show();
    }
}
