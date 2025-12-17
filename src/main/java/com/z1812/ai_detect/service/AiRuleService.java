package com.z1812.ai_detect.service;

import com.mybatisflex.core.query.QueryWrapper;
import com.z1812.ai_detect.entity.AiRule;
import com.z1812.ai_detect.mapper.AiRuleMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.z1812.ai_detect.entity.table.AiRuleTableDef.AI_RULE;

@Service
@RequiredArgsConstructor
public class AiRuleService {

    private final AiRuleMapper mapper;

    public List<AiRule> list() {
        return mapper.selectAll();
    }

    public AiRule getById(Long id) {
        return mapper.selectOneById(id);
    }

    public boolean save(AiRule entity) {
        return mapper.insert(entity) > 0;
    }

    public boolean update(AiRule entity) {
        return mapper.update(entity) > 0;
    }

    public boolean delete(Long id) {
        return mapper.deleteById(id) > 0;
    }

    public List<AiRule> listByModelApiId(Long modelApiId) {
        QueryWrapper query = QueryWrapper.create()
                .where(AI_RULE.MODEL_API_ID.eq(modelApiId))
                .and(AI_RULE.ENABLED.eq(1));
        return mapper.selectListByQuery(query);
    }
}
