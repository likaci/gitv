package com.gala.video.lib.share.ifimpl.openplay.broadcast;

import android.content.Context;
import android.util.Log;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.share.ifmanager.CreateInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.openBroadcast.ActionHolder;
import java.util.List;

public class BroadcastConfigEPG {
    private static BroadcastConfigEPG mInstance = null;
    private final String TAG = getClass().getSimpleName();
    private Context mContext;
    private List<String> mFeatures = null;

    private BroadcastConfigEPG(Context context, List<String> features) {
        this.mContext = context;
        this.mFeatures = features;
    }

    public static synchronized BroadcastConfigEPG initialize(Context context, List<String> features) {
        BroadcastConfigEPG broadcastConfigEPG;
        synchronized (BroadcastConfigEPG.class) {
            if (mInstance == null) {
                mInstance = new BroadcastConfigEPG(context, features);
            }
            broadcastConfigEPG = mInstance;
        }
        return broadcastConfigEPG;
    }

    public void initBroadcastFeatures() {
        Log.d(this.TAG, "iniBroadcastFeatures");
        try {
            addAction(CreateInterfaceTools.createEpgBroadcastHolder().getActionHolder());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addAction(ActionHolder[] All_ACTION_HOLDERS) {
        if (!ListUtils.isEmpty(this.mFeatures)) {
            Log.d(this.TAG, "All_ACTION_HOLDERS.length = " + All_ACTION_HOLDERS.length);
            for (ActionHolder holder : All_ACTION_HOLDERS) {
                if (this.mFeatures.contains(holder.getKey()) && holder.getAction() != null) {
                    Log.d(this.TAG, "iniBroadcastFeatures> holder.getKey() = " + holder.getKey() + " ;holder.getAction() = " + holder.getAction().getClass().getSimpleName());
                    BroadcastManager.instance().addBroadcastActionHolder(holder);
                }
            }
        }
    }
}
