package com.tevinjeffrey.rutgersct.receivers;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.tevinjeffrey.rutgersct.RutgersCTApp;
import com.tevinjeffrey.rutgersct.database.DatabaseHandlerImpl;
import com.tevinjeffrey.rutgersct.rutgersapi.model.Course;
import com.tevinjeffrey.rutgersct.rutgersapi.model.Request;

public class DatabaseReceiver extends BroadcastReceiver {
    public DatabaseReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Request request = intent.getParcelableExtra(RutgersCTApp.REQUEST);
        Course.Section section = intent.getParcelableExtra(RutgersCTApp.SELECTED_SECTION);
        if (request != null && section != null) {
            //Removes section from databse
            RutgersCTApp.getInstance().getDatabaseHandler().removeSectionFromDb(request);
            //Notify user with a toast.
            notifyUser(context, section);
            //Remove notification from notification panel
            removeNotification(context, request);
        }
    }

    private void removeNotification(Context context, Request request) {
        NotificationManager mNotifyMgr =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotifyMgr.cancel(Integer.valueOf(request.getIndex()));
    }

    private void notifyUser(Context context, Course.Section section) {
        Toast.makeText(context, String.format("Stopped tracking %s",
                section.getCourse().getTrueTitle()), Toast.LENGTH_SHORT).show();
    }
}
