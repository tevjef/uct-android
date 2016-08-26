package com.tevinjeffrey.rutgersct.receivers;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.tevinjeffrey.rutgersct.RutgersCTApp;
import com.tevinjeffrey.rutgersct.data.uctapi.RetroUCT;
import com.tevinjeffrey.rutgersct.data.uctapi.search.UCTSubscription;

import javax.inject.Inject;

public class DatabaseReceiver extends BroadcastReceiver {

    @Inject
    RetroUCT retroUCT;

    public DatabaseReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        RutgersCTApp.getObjectGraph(context).inject(this);

        UCTSubscription subscription = intent.getParcelableExtra(UCTSubscription.SUBSCRIPTION);

        if (subscription != null) {
            //Removes section from databse
            retroUCT.unsubscribe(subscription.getSectionTopicName());
            //Notify user with a toast.
            Toast.makeText(context, String.format("Stopped tracking %s", subscription.getCourse().name), Toast.LENGTH_SHORT).show();
            //Remove notification from notification panel
            removeNotification(context, subscription.getSection().topic_id);
        }
    }

    private void removeNotification(Context context, String topicId) {
        NotificationManager mNotifyMgr =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotifyMgr.cancel(Integer.valueOf(topicId));
    }
}
