package com.houxiaokang.workbench.web;

import com.houxiaokang.common.Util.DatesimplDateFormat;
import com.houxiaokang.common.Util.UUIDUtil;
import com.houxiaokang.common.constant.constants;
import com.houxiaokang.common.domain.Result;
import com.houxiaokang.settings.domain.DicValue;
import com.houxiaokang.settings.domain.User;
import com.houxiaokang.settings.server.DicValueServer;
import com.houxiaokang.settings.server.UserServer;
import com.houxiaokang.workbench.domain.*;
import com.houxiaokang.workbench.server.ActivityServer;
import com.houxiaokang.workbench.server.ClueActivityRelationServce;
import com.houxiaokang.workbench.server.ClueRemarkServere;
import com.houxiaokang.workbench.server.ClueServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
public class ClueController {
    @Autowired
    private ClueActivityRelationServce activityRelationServce;
    @Autowired
    private ActivityServer activityServer;
    @Autowired
    private ClueRemarkServere clueRemarkServere;
    @Autowired
    private ClueServer clueServer;
    @Autowired
    private UserServer userServer;
    @Autowired
    private DicValueServer dicValueServer;

    @RequestMapping("/workbench/clue/toIndex.do")
    public String toIndex(HttpServletRequest request) {
        List<User> users = userServer.queryUserAll();
        List<DicValue> appellation = dicValueServer.queryDicValueBytypeCode("appellation");
        List<DicValue> source = dicValueServer.queryDicValueBytypeCode("source");
        List<DicValue> clueState = dicValueServer.queryDicValueBytypeCode("clueState");
        request.setAttribute("appellation", appellation);
        request.setAttribute("source", source);
        request.setAttribute("clueState", clueState);
        request.setAttribute("users", users);
        return "workbench/clue/index";
    }

    @RequestMapping("/workbench/clue/saveClue.do")
    @ResponseBody
    public Object saveClue(Clue clue, HttpSession session) {
        User user = (User) session.getAttribute(constants.SESSION_USER);
        clue.setId(UUIDUtil.getUUID());
        clue.setCreateTime(DatesimplDateFormat.DatesimplDateFormatoString(new Date()));
        clue.setCreateBy(user.getId());
        try {
            int i = clueServer.saveCreateClue(clue);
            if (i > 0) {
                return new Result(constants.RESULT_CODE_SUCCESS);
            }
            return new Result(constants.RESULT_CODE_FAIL, "系统忙,请稍后再试");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(constants.RESULT_CODE_FAIL, "系统忙,请稍后再试");
        }
    }

    @RequestMapping("/workbench/clue/toClueDetail.do")
    public String toClueDetail(String ClueId, HttpServletRequest request) {
        Clue clue = clueServer.queryClueForDetailByClueId(ClueId);
        List<ClueRemark> clueRemarks = clueRemarkServere.queryClueRemarkForDetailByClueId(ClueId);
        List<Activity> activities = activityServer.queryActivityByClueId(ClueId);
        request.setAttribute("clue", clue);
        request.setAttribute("clueRemarks", clueRemarks);
        request.setAttribute("activities", activities);
        return "workbench/clue/detail";
    }

    @RequestMapping("/workbench/clue/relatedMarketingActivities.do")
    @ResponseBody
    public Object relatedMarketingActivities(String clueId, String activitieName) {
        Map<String, Object> Map = new HashMap();
        Map.put("clueId", clueId);
        Map.put("activitieName", activitieName);
        List<Activity> activities = activityServer.queryActivityForDetailByClueNameAndId(Map);
        return activities;
    }

    @RequestMapping("/workbench/clue/createSaveClueActivityRelation.do")
    @ResponseBody
    public Object createSaveClueActivityRelation(String[] ActivityId, String clueId) {
        List<ClueActivityRelation> objects = new ArrayList();
        for (int i = 0; i < ActivityId.length; i++) {
            ClueActivityRelation clueActivityRelation = new ClueActivityRelation();
            clueActivityRelation.setId(UUIDUtil.getUUID());
            clueActivityRelation.setActivityId(ActivityId[i]);
            clueActivityRelation.setClueId(clueId);
            objects.add(clueActivityRelation);
        }
        try {
            int i = activityRelationServce.savaClueActivityRelati(objects);
            if (i > 0) {
                List<Activity> activities = activityServer.queryActivityForDetailByids(ActivityId);
                return new Result(constants.RESULT_CODE_SUCCESS, null, activities);
            }
            return new Result(constants.RESULT_CODE_FAIL, "系统忙,请稍后再试");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(constants.RESULT_CODE_FAIL, "系统忙,请稍后再试");
        }
    }

    @RequestMapping("/workbench/clue/RemoveContactForClueAndActivity.do")
    @ResponseBody
    public Object RemoveContactForClueAndActivity(ClueActivityRelation clueActivityRelation) {
        try {
            int i = activityRelationServce.deleteClueActivityRelati(clueActivityRelation);
            if (i > 0) {
                return new Result(constants.RESULT_CODE_SUCCESS);
            }
            return new Result(constants.RESULT_CODE_FAIL, "系统忙,请稍后再试");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(constants.RESULT_CODE_FAIL, "系统忙,请稍后再试");
        }
    }

    @RequestMapping("/workbench/clue/toConvert.do")
    public String toConvert(String id, HttpServletRequest request) {
        Clue clue = clueServer.queryClueForDetailByClueId(id);
        List<DicValue> stage = dicValueServer.queryDicValueBytypeCode("stage");
        request.setAttribute("clue", clue);
        request.setAttribute("stage", stage);
        return "workbench/clue/convert";
    }

    @RequestMapping("/workbench/clue/SelectSourcesOfMarketingActivity.do")
    @ResponseBody
    public Object SelectSourcesOfMarketingActivity(String clueId, String activityName) {
        Map<String, Object> obj = new HashMap();
        obj.put("clueId", clueId);
        obj.put("activitieName", activityName);
        List<Activity> activities = activityServer.queryActivityForConvertByClueyNameAndId(obj);
        return activities;
    }

    @RequestMapping("/workbench/clue/clueConvert.do")
    @ResponseBody
    public Object clueConvert(String clueId, String isCreatedTean, Transaction transaction, HttpSession session) {
        try {
            User user = (User) session.getAttribute(constants.SESSION_USER);
            if ("false".equals(isCreatedTean)) {
                transaction = null;
            }
            clueServer.clueForConvertByclueId(clueId, user, transaction);
            return new Result(constants.RESULT_CODE_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(constants.RESULT_CODE_FAIL, "系统忙,请稍后重试");
        }

    }
}
