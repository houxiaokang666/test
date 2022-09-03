package com.houxiaokang.settings.server;

import com.houxiaokang.settings.domain.DicValue;

import java.util.List;

public interface DicValueServer {
    List<DicValue> queryDicValueBytypeCode(String typecode);
}
