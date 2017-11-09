package tv.gitv.ptqy.security.fingerprint.Utils;

import android.net.wifi.ScanResult;
import java.util.Comparator;

public class WifiScanResultComparator implements Comparator<ScanResult> {
    public int compare(ScanResult o1, ScanResult o2) {
        return o2.level - o1.level;
    }
}
