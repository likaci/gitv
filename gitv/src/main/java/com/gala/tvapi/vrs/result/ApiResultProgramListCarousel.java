package com.gala.tvapi.vrs.result;

import com.gala.tvapi.vrs.model.ProgramCarousel;
import com.gala.video.api.ApiResult;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ApiResultProgramListCarousel extends ApiResult {
    public int hasNext = 0;
    public Map<String, List<ProgramCarousel>> programMap;
    public List<ProgramCarousel> programs;

    public List<ProgramCarousel> getProgramsByChannelId(long id) {
        if (this.programs != null && this.programs.size() > 0) {
            List<ProgramCarousel> arrayList = new ArrayList();
            for (ProgramCarousel programCarousel : this.programs) {
                if (programCarousel.channelIds != null && programCarousel.channelIds.size() > 0) {
                    for (Long longValue : programCarousel.channelIds) {
                        if (longValue.longValue() == id) {
                            arrayList.add(programCarousel);
                        }
                    }
                }
            }
            return arrayList;
        } else if (this.programMap == null || this.programMap.size() <= 0) {
            return null;
        } else {
            return (List) this.programMap.get(String.valueOf(id));
        }
    }

    public boolean isHasNextPage() {
        return this.hasNext == 1;
    }
}
