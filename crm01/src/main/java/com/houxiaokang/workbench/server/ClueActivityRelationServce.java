package com.houxiaokang.workbench.server;

import com.houxiaokang.workbench.domain.ClueActivityRelation;

import java.util.List;

public interface ClueActivityRelationServce {
    int savaClueActivityRelati(List<ClueActivityRelation> clueActivityRelations);

    int deleteClueActivityRelati(ClueActivityRelation clueActivityRelation);
}
