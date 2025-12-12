package com.z1812.aidetect.service;

import com.mybatisflex.core.query.QueryWrapper;
import com.z1812.aidetect.entity.AiModelApi;
import com.z1812.aidetect.mapper.AiModelApiMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.z1812.aidetect.entity.table.AiModelApiTableDef.AI_MODEL_API;

@Service
@RequiredArgsConstructor
public class AiModelApiService {

    private final AiModelApiMapper mapper;

    public List<AiModelApi> list() {
        return mapper.selectAll();
    }

    public AiModelApi getById(Long id) {
        return mapper.selectOneById(id);
    }

    public boolean save(AiModelApi entity) {
        return mapper.insert(entity) > 0;
    }

    public boolean update(AiModelApi entity) {
        return mapper.update(entity) > 0;
    }

    public boolean delete(Long id) {
        return mapper.deleteById(id) > 0;
    }

    public List<AiModelApi> listEnabled() {
        QueryWrapper query = QueryWrapper.create()
                .where(AI_MODEL_API.ENABLED.eq(1));
        return mapper.selectListByQuery(query);
    }
}
