package com.gala.video.app.player.openapi;

import android.content.Context;
import com.gala.video.app.player.openapi.feature.appsetting.GetScreenScaleCommand;
import com.gala.video.app.player.openapi.feature.appsetting.GetSkipHeaderTailerCommand;
import com.gala.video.app.player.openapi.feature.appsetting.GetStreamTypeCommand;
import com.gala.video.app.player.openapi.feature.appsetting.SetScreenScaleCommand;
import com.gala.video.app.player.openapi.feature.appsetting.SetSkipHeaderTailerCommand;
import com.gala.video.app.player.openapi.feature.appsetting.SetStreamTypeCommand;
import com.gala.video.app.player.openapi.feature.open.OpenCarouselCommand;
import com.gala.video.app.player.openapi.feature.open.OpenDailyNewsCommand;
import com.gala.video.app.player.openapi.feature.open.OpenMediaCommand;
import com.gala.video.app.player.openapi.feature.open.PlayMediaCommand;
import com.gala.video.app.player.openapi.feature.open.PlayVrsMediaCommand;
import com.gala.video.lib.share.ifimpl.openplay.service.AddInstanceHolder;
import com.gala.video.lib.share.ifmanager.bussnessIF.openplay.IOpenApiCommandHolder;
import com.gala.video.lib.share.ifmanager.bussnessIF.openplay.IOpenApiCommandHolder.Wrapper;

public class OpenApiPlayerCommandHolder extends Wrapper {
    public AddInstanceHolder[] getCommandHolder(Context context) {
        AddInstanceHolder[] ADD_INSTANCE_COMMANDS = new AddInstanceHolder[11];
        ADD_INSTANCE_COMMANDS[0] = new AddInstanceHolder("GetScreenScaleCommand", 3600000, IOpenApiCommandHolder.OAA_NO_LIMIT, 0, 0, new GetScreenScaleCommand(context));
        ADD_INSTANCE_COMMANDS[1] = new AddInstanceHolder("GetSkipHeaderTailerCommand", 3600000, IOpenApiCommandHolder.OAA_NO_LIMIT, 0, 0, new GetSkipHeaderTailerCommand(context));
        ADD_INSTANCE_COMMANDS[2] = new AddInstanceHolder("GetStreamTypeCommand", 3600000, IOpenApiCommandHolder.OAA_NO_LIMIT, 0, 0, new GetStreamTypeCommand(context));
        ADD_INSTANCE_COMMANDS[3] = new AddInstanceHolder("SetScreenScaleCommand", 3600000, IOpenApiCommandHolder.OAA_NO_LIMIT, 0, 0, new SetScreenScaleCommand(context));
        ADD_INSTANCE_COMMANDS[4] = new AddInstanceHolder("SetSkipHeaderTailerCommand", 3600000, IOpenApiCommandHolder.OAA_NO_LIMIT, 0, 0, new SetSkipHeaderTailerCommand(context));
        ADD_INSTANCE_COMMANDS[5] = new AddInstanceHolder("SetStreamTypeCommand", 3600000, IOpenApiCommandHolder.OAA_NO_LIMIT, 0, 0, new SetStreamTypeCommand(context));
        ADD_INSTANCE_COMMANDS[6] = new AddInstanceHolder("OpenDailyNewsCommand", 3600000, 3600000, 0, 0, new OpenDailyNewsCommand(context));
        ADD_INSTANCE_COMMANDS[7] = new AddInstanceHolder("OpenCarouselCommand", 3600000, 3600000, 0, 0, new OpenCarouselCommand(context));
        ADD_INSTANCE_COMMANDS[8] = new AddInstanceHolder("OpenMediaCommand", 3600000, 3600000, 0, 0, new OpenMediaCommand(context));
        ADD_INSTANCE_COMMANDS[9] = new AddInstanceHolder("PlayMediaCommand", 3600000, 3600000, 0, 0, new PlayMediaCommand(context));
        ADD_INSTANCE_COMMANDS[10] = new AddInstanceHolder("PlayVrsMediaCommand", 3600000, 3600000, 0, 0, new PlayVrsMediaCommand(context));
        return ADD_INSTANCE_COMMANDS;
    }
}
