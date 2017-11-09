package com.gala.video.lib.framework.coreservice.netdiagnose.job;

import com.gala.video.lib.framework.core.job.Job;
import com.gala.video.lib.framework.core.job.JobListener;
import com.gala.video.lib.framework.coreservice.netdiagnose.model.NetDiagnoseInfo;

public interface NetDiagnoseJobListener extends JobListener<Job<NetDiagnoseInfo>> {
}
