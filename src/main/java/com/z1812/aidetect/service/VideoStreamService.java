package com.z1812.aidetect.service;

import com.mybatisflex.core.query.QueryWrapper;
import com.z1812.aidetect.entity.VideoStream;
import com.z1812.aidetect.mapper.VideoStreamMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.z1812.aidetect.entity.table.VideoStreamTableDef.VIDEO_STREAM;

@Service
@RequiredArgsConstructor
public class VideoStreamService {

    private final VideoStreamMapper mapper;

    public List<VideoStream> list() {
        return mapper.selectAll();
    }

    public VideoStream getById(Long id) {
        return mapper.selectOneById(id);
    }

    public boolean save(VideoStream entity) {
        return mapper.insert(entity) > 0;
    }

    public boolean update(VideoStream entity) {
        return mapper.update(entity) > 0;
    }

    public boolean delete(Long id) {
        return mapper.deleteById(id) > 0;
    }

    public List<VideoStream> listEnabled() {
        QueryWrapper query = QueryWrapper.create()
                .where(VIDEO_STREAM.ENABLED.eq(1));
        return mapper.selectListByQuery(query);
    }
}
