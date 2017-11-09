package pl.droidsonroids.gif;

import org.xbill.DNS.Message;
import pl.droidsonroids.gif.annotations.Beta;

@Beta
public class GifOptions {
    boolean inIsOpaque;
    char inSampleSize;

    public GifOptions() {
        reset();
    }

    private void reset() {
        this.inSampleSize = '\u0001';
        this.inIsOpaque = false;
    }

    public void setInSampleSize(int inSampleSize) {
        if (inSampleSize <= 0 || inSampleSize > Message.MAXLENGTH) {
            this.inSampleSize = '\u0001';
        } else {
            this.inSampleSize = (char) inSampleSize;
        }
    }

    public void setInIsOpaque(boolean inIsOpaque) {
        this.inIsOpaque = inIsOpaque;
    }

    void setFrom(GifOptions source) {
        if (source == null) {
            reset();
            return;
        }
        this.inIsOpaque = source.inIsOpaque;
        this.inSampleSize = source.inSampleSize;
    }
}
