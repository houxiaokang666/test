package com.houxiaokang.workbench.server.Impl;

import com.houxiaokang.workbench.domain.Activity;
import com.houxiaokang.workbench.mapper.activityMapper;
import com.houxiaokang.workbench.server.ActivityServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("activityServer")
public class activityServerImpl implements ActivityServer {
    @Autowired
    private activityMapper activiay;

    public int saveActivity(Activity activity) {
        return activiay.insertActivity(activity);
    }

    @Override
    public List<Activity> queryActivityByConditionForPage(Map<String, Object> map) {
        return activiay.selectActivityByConditionForPage(map);
    }

    @Override
    public int queryActivityCountByConditionForPage(Map<String, Object> map) {
        return activiay.selectActivityCountByConditionForPage(map);
    }

    @Override
    public int deleteActivityByids(String[] ids) {
        return activiay.deleteActivityByids(ids);
    }

    @Override
    public Activity queryActivityById(String id) {
        return activiay.selectActivitById(id);
    }

    @Override
    public int saveActivityById(Activity activity) {
        return activiay.updateById(activity);
    }

    @Override
    public List<Activity> queryActivitiesForExport() {
        return activiay.selectActivitAllForexport();
    }

    @Override
    public List<Activity> queryActivitiesByIdForExport(String[] id) {
        return activiay.selectActivitByIdForexport(id);
    }

    @Override
    public int saveActivitiesByList(List<Activity> activities) {
        return activiay.insertActivitByList(activities);
    }

    @Override
    public Activity querysaveActivitiesByIdForDetail(String id) {
        return activiay.selectActivityByIdForDetail(id);
    }

    @Override
    public List<Activity> queryActivityByClueId(String clueId) {
        return activiay.selectActivityByClueId(clueId);
    }

    @Override
    public List<Activity> queryActivityForDetailByClueNameAndId(Map<String, Object> obj) {
        return activiay.selectActivityForDetailByClueNameAndId(obj);
    }

    @Override
    public List<Activity> queryActivityForDetailByids(String[] ids) {
        return activiay.selectActivityForDetailByids(ids);
    }

    @Override
    public List<Activity> queryActivityForConvertByClueyNameAndId(Map<String, Object> obj) {
        return activiay.selectActivityForConvertByClueyNameAndId(obj);
    }

}
