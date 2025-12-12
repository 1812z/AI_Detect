package com.z1812.aidetect.service;

import com.mybatisflex.core.query.QueryWrapper;
import com.z1812.aidetect.entity.VideoStreamRule;
import com.z1812.aidetect.mapper.VideoStreamRuleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.z1812.aidetect.entity.table.VideoStreamRuleTableDef.VIDEO_STREAM_RULE;

@Service
@RequiredArgsConstructor
public class VideoStreamRuleService {

    private final VideoStreamRuleMapper mapper;

    public List<VideoStreamRule> list() {
        return mapper.selectAll();
    }

    public VideoStreamRule getById(Long id) {
        return mapper.selectOneById(id);
    }

    public boolean save(VideoStreamRule entity) {
        return mapper.insert(entity) > 0;
    }

    public boolean update(VideoStreamRule entity) {
        return mapper.update(entity) > 0;
    }

    public boolean delete(Long id) {
        return mapper.deleteById(id) > 0;
    }

    public List<VideoStreamRule> listByVideoStreamId(Long videoStreamId) {
        QueryWrapper query = QueryWrapper.create()
                .where(VIDEO_STREAM_RULE.VIDEO_STREAM_ID.eq(videoStreamId))
                .and(VIDEO_STREAM_RULE.ENABLED.eq(1));
        return mapper.selectListByQuery(query);
    }
}
