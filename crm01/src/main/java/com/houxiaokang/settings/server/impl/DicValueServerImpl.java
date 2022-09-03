package com.houxiaokang.settings.server.impl;

import com.houxiaokang.settings.domain.DicValue;
import com.houxiaokang.settings.mapper.DicValueMapper;
import com.houxiaokang.settings.server.DicValueServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DicValueServerImpl implements DicValueServer {
    @Autowired
    private DicValueMapper dicValueMapper;

    @Override
    public List<DicValue> queryDicValueBytypeCode(String typecode) {
        return dicValueMapper.selectDicValueBytypeCode(typecode);
    }
}
