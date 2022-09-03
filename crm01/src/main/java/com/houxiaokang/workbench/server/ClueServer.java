package com.houxiaokang.workbench.server;

import com.houxiaokang.settings.domain.User;
import com.houxiaokang.workbench.domain.Clue;
import com.houxiaokang.workbench.domain.Transaction;

public interface ClueServer {
    int saveCreateClue(Clue clue);

    Clue queryClueForDetailByClueId(String clueid);

    void clueForConvertByclueId(String clueId, User user, Transaction isCreatedTean);
}
