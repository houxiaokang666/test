package com.houxiaokang.workbench.web;

import com.houxiaokang.common.constant.constants;
import com.houxiaokang.common.domain.Result;
import com.houxiaokang.settings.domain.DicValue;
import com.houxiaokang.settings.domain.User;
import com.houxiaokang.settings.server.DicValueServer;
import com.houxiaokang.settings.server.UserServer;
import com.houxiaokang.workbench.domain.Customer;
import com.houxiaokang.workbench.domain.TranHistory;
import com.houxiaokang.workbench.domain.Transaction;
import com.houxiaokang.workbench.domain.TransactionRemark;
import com.houxiaokang.workbench.server.CustomerService;
import com.houxiaokang.workbench.server.TranHistoryService;
import com.houxiaokang.workbench.server.TransactionRemarkService;
import com.houxiaokang.workbench.server.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

@Controller
public class TransactionController {
    @Autowired
    private UserServer userServer;
    @Autowired
    private DicValueServer dicValueServer;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private TransactionRemarkService transactionRemarkService;
    @Autowired
    private TranHistoryService tranHistoryService;

    //*市场活动源是可搜索的
//*联系人也是可搜索的
//*可能性是可配置的
//*客户名称支持自动补全
//*表单验证
//*保存成功之后，跳转到交易主页面
//*保存失败，提示信息，页面不跳转
    @RequestMapping("/workbench/transaction/index.do")
    private String index() {
        return "workbench/transaction/index";
    }

    @RequestMapping("/workbench/transaction/save.do")
    private String save(HttpServletRequest request) {
        //*所有者、阶段、类型、来源 都是动态的
        List<User> users = userServer.queryUserAll();
        List<DicValue> stage = dicValueServer.queryDicValueBytypeCode("stage");
        List<DicValue> transactionType = dicValueServer.queryDicValueBytypeCode("transactionType");
        List<DicValue> source = dicValueServer.queryDicValueBytypeCode("source");
        request.setAttribute("users", users);
        request.setAttribute("stage", stage);
        request.setAttribute("transactionType", transactionType);
        request.setAttribute("source", source);
        return "workbench/transaction/save";
    }

    @RequestMapping("/workbench/transaction/getPossibility.do")
    @ResponseBody
    private Object getPossibility(String possibility) {
        ResourceBundle bundle = ResourceBundle.getBundle("a");
        String string = bundle.getString(possibility);
        return string;
    }

    @RequestMapping("/workbench/transaction/getaccountName.do")
    @ResponseBody
    private Object getaccountName(String name) {
        List<Customer> customers = customerService.quertCustomerForAutoCompletionByname(name);
        return customers;
    }

    @RequestMapping("/workbench/transaction/saveTransaction.do")
    @ResponseBody
    private Object saveTransaction(HttpSession session, @RequestParam Map<String, Object> maps) {
        User User = (User) session.getAttribute(constants.SESSION_USER);
        try {
            transactionService.saveCreatedTransaction(maps, User);
            return new Result(constants.RESULT_CODE_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(constants.RESULT_CODE_FAIL, "系统忙，请稍后再试");
        }
    }

    @RequestMapping("workbench/transaction/toDetail.do")
    public String toDetail(String id, HttpServletRequest request) {
        Transaction transaction = transactionService.queryTransactionForDetailById(id);
        String possibility = (String) this.getPossibility(transaction.getStage());
        List<DicValue> stage = dicValueServer.queryDicValueBytypeCode("stage");
        transaction.setPossibility(possibility);
        List<TransactionRemark> transactionRemarks = transactionRemarkService.queeryTransactionRemarkForDetailByTransactionId(id);
        request.setAttribute("transaction", transaction);
        request.setAttribute("transactionRemarks", transactionRemarks);
        List<TranHistory> tranHistories = tranHistoryService.queryTranHistoryForDetailByTransactionId(id);
        request.setAttribute("tranHistories", tranHistories);
        request.setAttribute("stage", stage);
        return "workbench/transaction/detail";
    }
}
