package android.support.v4.app;

import java.util.Collections;

public class FragmentTransactionBugFixHack {
    public static void reorderIndices(FragmentManager fragmentManager) {
        if (fragmentManager instanceof FragmentManagerImpl) {
            try {
                FragmentManagerImpl fragmentManagerImpl = (FragmentManagerImpl) fragmentManager;
                if (fragmentManagerImpl.mAvailIndices != null && fragmentManagerImpl.mAvailIndices.size() > 1) {
                    Collections.sort(fragmentManagerImpl.mAvailIndices, Collections.reverseOrder());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
