package com.tevinjeffrey.rutgersct.receivers;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.tevinjeffrey.rutgersct.RutgersCTApp;
import com.tevinjeffrey.rutgersct.database.DatabaseHandler;
import com.tevinjeffrey.rutgersct.data.rutgersapi.model.Course;
import com.tevinjeffrey.rutgersct.data.rutgersapi.model.Request;
import com.tevinjeffrey.rutgersct.ui.courseinfo.CourseInfoView;
import com.tevinjeffrey.rutgersct.ui.trackedsections.TrackedSectionsView;

import javax.inject.Inject;

public class DatabaseReceiver extends BroadcastReceiver {

    @Inject
    DatabaseHandler mDatabaseHandler;

    public DatabaseReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        RutgersCTApp.getObjectGraph(context).inject(this);

        Request request = intent.getParcelableExtra(TrackedSectionsView.REQUEST);
        Course.Section section = intent.getParcelableExtra(CourseInfoView.SELECTED_SECTION);
        if (request != null && section != null) {
            //Removes section from databse
            mDatabaseHandler.removeSectionFromDb(request);
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
