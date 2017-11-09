package com.gala.sdk.player;

public interface ISceneActionData {

    public static class SceneKey {
        public Object[] mParams;
        public SceneType mType;

        public SceneKey(SceneType type, Object... param) {
            this.mType = type;
            this.mParams = param;
        }
    }

    public enum SceneType {
        PRE_VIDEO,
        NEXT_VIDEO,
        LAST_VIDEO,
        ON_SKIP_TAIL,
        OFF_SKIP_TAIL,
        SELECT_EPISODE,
        RECOMMEND_LIST,
        CHANGE_BITSTREAM
    }

    Runnable getActionRunnable();

    SceneKey getKey();

    SceneType getType();
}
