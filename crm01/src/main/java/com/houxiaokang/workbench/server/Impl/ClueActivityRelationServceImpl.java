package com.houxiaokang.workbench.server.Impl;

import com.houxiaokang.workbench.domain.ClueActivityRelation;
import com.houxiaokang.workbench.mapper.ClueActivityRelationMapper;
import com.houxiaokang.workbench.server.ClueActivityRelationServce;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClueActivityRelationServceImpl implements ClueActivityRelationServce {
    @Autowired
    private ClueActivityRelationMapper clueActivityRelationMapper;

    @Override
    public int savaClueActivityRelati(List<ClueActivityRelation> clueActivityRelations) {
        return clueActivityRelationMapper.insertClueActivityRelation(clueActivityRelations);
    }

    @Override
    public int deleteClueActivityRelati(ClueActivityRelation clueActivityRelation) {
        return clueActivityRelationMapper.deleteClueActivityRelationByClueIdActivityId(clueActivityRelation);
    }
}
