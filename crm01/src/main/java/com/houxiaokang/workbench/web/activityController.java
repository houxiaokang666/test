package com.houxiaokang.workbench.web;

import com.houxiaokang.common.Util.DatesimplDateFormat;
import com.houxiaokang.common.Util.HSSFUtils;
import com.houxiaokang.common.Util.UUIDUtil;
import com.houxiaokang.common.constant.constants;
import com.houxiaokang.common.domain.Result;
import com.houxiaokang.settings.domain.User;
import com.houxiaokang.settings.server.UserServer;
import com.houxiaokang.workbench.domain.Activity;
import com.houxiaokang.workbench.domain.Remark;
import com.houxiaokang.workbench.server.ActivityServer;
import com.houxiaokang.workbench.server.RemarkServer;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.*;

@Controller
public class activityController {
    @Autowired
    private UserServer userServer;
    @Autowired
    private RemarkServer remarkServer;
    @Autowired
    private ActivityServer activityServer;

    @RequestMapping("/workbench/activity/index.do")
    public String index(HttpServletRequest request) {
        List<User> users = userServer.queryUserAll();
        request.setAttribute("userall", users);
        return "workbench/activity/index";
    }

    @RequestMapping("/workbench/activity/save.do")
    @ResponseBody
    public Object save(HttpSession session, Activity activity) {
        activity.setCreateTime(DatesimplDateFormat.DatesimplDateFormatoString(new Date()));
        User user = (User) session.getAttribute(constants.SESSION_USER);
        activity.setCreateBy(user.getId());
        activity.setId(UUIDUtil.getUUID());
        try {
            int col = activityServer.saveActivity(activity);
            if (col > 0) {
                return new Result(constants.RESULT_CODE_SUCCESS);
            } else {
                return new Result(constants.RESULT_CODE_FAIL, "系统繁忙,请稍后重试");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(constants.RESULT_CODE_FAIL, "系统繁忙,请稍后重试");
        }
    }

    @RequestMapping("/workbench/activity/queryActivity.do")
    @ResponseBody
    public Object queryActivity(String name, String owner, String start_date, String end_date, int pagenum, int pagesize) {
        Map hashMap = new HashMap();
        hashMap.put("name", name);
        hashMap.put("owner", owner);
        hashMap.put("start_date", start_date);
        hashMap.put("end_date", end_date);
        hashMap.put("pagenum", (pagenum - 1) * pagesize);
        hashMap.put("pagesize", pagesize);
        List<Activity> list = activityServer.queryActivityByConditionForPage(hashMap);
        int num = activityServer.queryActivityCountByConditionForPage(hashMap);
        Map result = new HashMap();
        result.put("list", list);
        result.put("num", num);
        return result;
    }

    @RequestMapping("/workbench/activity/deleteActivity.do")
    @ResponseBody
    public Object deleteActivity(String[] id) {
        try {
            int i = activityServer.deleteActivityByids(id);
            if (i > 0) {
                return new Result(constants.RESULT_CODE_SUCCESS);
            } else {
                return new Result(constants.RESULT_CODE_FAIL, "系统繁忙,请稍后再试");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(constants.RESULT_CODE_FAIL, "系统繁忙,请稍后再试");
        }
    }

    @RequestMapping("/workbench/activity/queryActivityById.do")
    @ResponseBody
    public Object queryActivityById(String id) {
        Activity i = activityServer.queryActivityById(id);
        if (i != null) {
            Result result = new Result(constants.RESULT_CODE_SUCCESS);
            result.setdata(i);
            return result;
        } else {
            return new Result(constants.RESULT_CODE_FAIL, "系统繁忙,请稍后再试");
        }
    }

    @RequestMapping("/workbench/activity/UpdateActivityById.do")
    @ResponseBody
    public Object UpdateActivityById(Activity activity, HttpSession session) {
        try {
            User user = (User) session.getAttribute(constants.SESSION_USER);
            Activity activity1 = activityServer.queryActivityById(activity.getId());
            activity.setEditTime(DatesimplDateFormat.DatesimplDateFormatoString(new Date()));
            activity.setEditBy(user.getId());
            activity.setCreateTime(activity1.getCreateTime());
            activity.setCreateBy(activity1.getCreateBy());
            int i = activityServer.saveActivityById(activity);
            if (i > 0) {
                return new Result(constants.RESULT_CODE_SUCCESS);
            }
            return new Result(constants.RESULT_CODE_FAIL, "系统繁忙,请稍后再试");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(constants.RESULT_CODE_FAIL, "系统繁忙,请稍后再试");
        }
    }

    @RequestMapping("/workbench/activity/exportActivitAll.do")
    public void exportActivitAll(HttpServletResponse response) throws IOException, IllegalAccessException {
        List<Activity> activities = activityServer.queryActivitiesForExport();
        response.setContentType("application/octet-stream;charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=Activities.xls");
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("市场活动");
        HSSFRow row = null;
        row = sheet.createRow(0);
        HSSFCell cell = null;
        String[] o = {"id", "owner", "name", "start_date", "end_date", "cost", "description", "create_time", "create_by", "edit_time", "edit_by"};
        for (int i = 0; i < 11; i++) {
            cell = row.createCell(i);
            cell.setCellValue(o[i]);
        }
        for (int i = 0; i < activities.size(); i++) {
            row = sheet.createRow(i + 1);
            Activity activity = activities.get(i);
            Field[] declaredFields = activity.getClass().getDeclaredFields();
            for (int j = 0; j < o.length; j++) {
                declaredFields[j].setAccessible(true);
                Object o1 = declaredFields[j].get(activity);
                cell = row.createCell(j);
                cell.setCellValue((String) o1);
            }
        }
        ServletOutputStream outputStream = response.getOutputStream();
        wb.write(outputStream);
        outputStream.flush();
        wb.close();
    }

    @RequestMapping("/workbench/activity/exportActivitById.do")
    public void exportActivitById(HttpServletResponse response, String[] id) throws IOException, IllegalAccessException {
        List<Activity> activities = activityServer.queryActivitiesByIdForExport(id);
        response.setContentType("application/octet-stream;charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename=Activities.xls");
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet = wb.createSheet("市场活动");
        HSSFRow row = null;
        row = sheet.createRow(0);
        HSSFCell cell = null;
        String[] o = {"id", "owner", "name", "start_date", "end_date", "cost", "description", "create_time", "create_by", "edit_time", "edit_by"};
        for (int i = 0; i < 11; i++) {
            cell = row.createCell(i);
            cell.setCellValue(o[i]);
        }
        for (int i = 0; i < activities.size(); i++) {
            row = sheet.createRow(i + 1);
            Activity activity = activities.get(i);
            Field[] declaredFields = activity.getClass().getDeclaredFields();
            for (int j = 0; j < o.length; j++) {
                declaredFields[j].setAccessible(true);
                Object o1 = declaredFields[j].get(activity);
                cell = row.createCell(j);
                cell.setCellValue((String) o1);
            }
        }
        ServletOutputStream outputStream = response.getOutputStream();
        wb.write(outputStream);
        outputStream.flush();
        wb.close();
    }

    @RequestMapping("/workbench/activity/importActivit.do")
    @ResponseBody
    public Object importActivit(MultipartFile multipartFile, HttpSession session) {
        try {
            User user = (User) session.getAttribute(constants.SESSION_USER);
            InputStream inputStream = multipartFile.getInputStream();
            HSSFWorkbook wb = new HSSFWorkbook(inputStream);
            HSSFSheet sheet = wb.getSheetAt(0);
            Activity activity;
            HSSFCell cell;
            HSSFRow row;
            List<Activity> arrayList = new ArrayList();
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                row = sheet.getRow(i);
                activity = new Activity();
                activity.setId(UUIDUtil.getUUID());
                activity.setOwner(user.getId());
                activity.setCreateTime(DatesimplDateFormat.DatesimplDateFormatoString(new Date()));
                activity.setCreateBy(user.getId());
                for (int j = 0; j < row.getLastCellNum(); j++) {
                    cell = row.getCell(j);
                    String cellValue = HSSFUtils.getCellValueForStr(cell);
                    if (j == 0) {
                        activity.setName(cellValue);
                    } else if (j == 1) {
                        activity.setStartDate(cellValue);
                    } else if (j == 2) {
                        activity.setEndDate(cellValue);
                    } else if (j == 3) {
                        activity.setCost(cellValue);
                    } else if (j == 4) {
                        activity.setDescription(cellValue);
                    }
                }
                arrayList.add(activity);
            }
            int i = activityServer.saveActivitiesByList(arrayList);
            Result result = new Result(constants.RESULT_CODE_SUCCESS);
            result.setdata(i);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            Result result = new Result(constants.RESULT_CODE_FAIL, "系统繁忙,请稍后再试");
            return result;
        }
    }

    @RequestMapping("/workbench/activity/queryActivityForDetail.do")
    public String queryActivityForDetail(String id, HttpServletRequest request) {
        Activity activity = activityServer.querysaveActivitiesByIdForDetail(id);
        List<Remark> remarks = remarkServer.queryRemarkByActivityId(activity.getId());
        request.setAttribute("activity", activity);
        request.setAttribute("remarks", remarks);
        return "workbench/activity/detail";
    }

    @RequestMapping("/workbench/activity/saveRemark.do")
    @ResponseBody
    public Object saveRemark(Remark remark, HttpSession session) {
        User user = (User) session.getAttribute(constants.SESSION_USER);
        remark.setId(UUIDUtil.getUUID());
        remark.setCreateTime(DatesimplDateFormat.DatesimplDateFormatoString(new Date()));
        remark.setCreateBy(user.getId());
        remark.setEditFlag(constants.REMARK_EDIT_FALG_NO);
        try {
            int i = remarkServer.saveRemarkByUser(remark);
            if (i > 0) {
                Result result = new Result(constants.RESULT_CODE_SUCCESS);
                result.setdata(remark);
                return result;
            }
            return new Result(constants.RESULT_CODE_FAIL, "系统忙,请稍后再试");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(constants.RESULT_CODE_FAIL, "系统忙,请稍后再试");
        }
    }

    @RequestMapping("/workbench/activity/delRemark.do")
    @ResponseBody
    public Object delRemark(String remarksId) {
        try {
            int i = remarkServer.deleteRemarkById(remarksId);
            if (i > 0) {
                return new Result(constants.RESULT_CODE_SUCCESS);
            }
            return new Result(constants.RESULT_CODE_FAIL, "系统忙,请稍后再试");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(constants.RESULT_CODE_FAIL, "系统忙,请稍后再试");
        }
    }

    @RequestMapping("/workbench/activity/saveEditRemark.do")
    @ResponseBody
    public Object saveEditRemark(Remark remark, HttpSession session) {
        User user = (User) session.getAttribute(constants.SESSION_USER);
        String id = user.getId();
        try {
            remark.setEditFlag(constants.REMARK_EDIT_FALG_YES);
            remark.setEditBy(id);
            remark.setEditTime(DatesimplDateFormat.DatesimplDateFormatoString(new Date()));
            int i = remarkServer.saveEditeRemarkById(remark);
            if (i > 0) {
                return new Result(constants.RESULT_CODE_SUCCESS, null, remark);
            }
            return new Result(constants.RESULT_CODE_FAIL, "系统忙,请稍后再试");
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(constants.RESULT_CODE_FAIL, "系统忙,请稍后再试");
        }
    }
}
