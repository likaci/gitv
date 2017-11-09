package com.gala.video.lib.share.project;

import com.gala.video.lib.share.ifmanager.CreateInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.project.build.IBuildInterface;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.project.config.IConfigInterface;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.project.control.IControlInterface;
import com.gala.video.lib.share.project.plugin.IPluginEnv;
import com.gala.video.lib.share.project.plugin.PluginEnvProvider;
import com.gala.video.lib.share.project.res.IResourceInterface;
import com.gala.video.lib.share.project.res.ResourceProvider;

public class Project {
    private static final String TAG = "Project";
    private IBuildInterface mBuildProvider;
    private IConfigInterface mConfigProvider;
    private IControlInterface mControlProvider;
    private IPluginEnv mPluginEnv;
    private IResourceInterface mResProvider;

    private static class SingletonHolder {
        private static final Project INSTANCE = new Project();

        private SingletonHolder() {
        }
    }

    private Project() {
        this.mResProvider = new ResourceProvider();
        this.mBuildProvider = CreateInterfaceTools.createBuildInterface();
        this.mControlProvider = CreateInterfaceTools.createControlInterface();
        this.mConfigProvider = CreateInterfaceTools.createConfigInterface();
        this.mConfigProvider.initialize(this.mBuildProvider);
        this.mPluginEnv = new PluginEnvProvider();
    }

    public static final Project getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public IConfigInterface getConfig() {
        return this.mConfigProvider;
    }

    public IBuildInterface getBuild() {
        return this.mBuildProvider;
    }

    public IControlInterface getControl() {
        return this.mControlProvider;
    }

    public IResourceInterface getResProvider() {
        return this.mResProvider;
    }

    public IPluginEnv getPluginEnv() {
        return this.mPluginEnv;
    }
}
