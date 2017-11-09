package com.mcto.ads.test;

import android.content.Context;
import android.content.res.AssetManager;
import android.test.AndroidTestCase;
import com.gala.video.app.epg.ui.search.ad.Keys;
import com.mcto.ads.AdsClient;
import com.mcto.ads.internal.common.CupidUtils;
import org.json.JSONException;

public class AdsClientTest extends AndroidTestCase {
    private final String CONSTANT_UDID = "TEST_UDID";
    AdsClient adsclient;
    private AssetManager mAssets;
    private Context mContext;
    private String url = ("http://static." + CupidUtils.strReverse("iyiq") + ".com/ext/cupid/common/sdkconfig.xml");

    protected void setUp() throws Exception {
        super.setUp();
        try {
            this.mContext = (Context) AndroidTestCase.class.getMethod("getTestContext", new Class[0]).invoke(this, (Object[]) null);
            AdsClient.initialise(getContext());
            this.mAssets = this.mContext.getAssets();
        } catch (Exception x) {
            throw x;
        }
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    private void resetJsonBundle(String fileName) throws JSONException {
        this.adsclient = new AdsClient("100001", "qc_100001_100015", "100002", "3.6");
        this.adsclient.onRequestMobileServer();
        this.adsclient.onRequestMobileServerSucceededWithAdData(ReadAssetsFile.readAssetsFile(this.mAssets, fileName), "100003", "100004");
    }

    public void _testGetAd() throws JSONException {
        String res = ReadAssetsFile.readAssetsFile(this.mAssets, "interstitials_adsource.json");
        this.adsclient = new AdsClient("uaaUserId", Keys.PLAYER_ID, "albumId", "appVersion");
    }

    public void testGetPingBack() throws JSONException {
        resetJsonBundle("json_debug.json");
        String url = this.adsclient.getFinalUrl();
        this.adsclient.onAdStarted(0);
    }
}
