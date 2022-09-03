package com.houxiaokang.workbench.server;

import com.houxiaokang.workbench.domain.Activity;

import java.util.List;
import java.util.Map;

public interface ActivityServer {
   int saveActivity(Activity activity);

   List<Activity> queryActivityByConditionForPage(Map<String, Object> map);

   int queryActivityCountByConditionForPage(Map<String, Object> map);

   int deleteActivityByids(String[] ids);

   Activity queryActivityById(String id);

   int saveActivityById(Activity activity);

   List<Activity> queryActivitiesForExport();

   List<Activity> queryActivitiesByIdForExport(String[] id);

   int saveActivitiesByList(List<Activity> activities);

   Activity querysaveActivitiesByIdForDetail(String id);

   List<Activity> queryActivityByClueId(String clueId);

   List<Activity> queryActivityForDetailByClueNameAndId(Map<String, Object> obj);

   List<Activity> queryActivityForDetailByids(String[] ids);

   List<Activity> queryActivityForConvertByClueyNameAndId(Map<String, Object> obj);

}
