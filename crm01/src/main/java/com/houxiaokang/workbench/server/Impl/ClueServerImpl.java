package com.houxiaokang.workbench.server.Impl;

import com.houxiaokang.common.Util.DatesimplDateFormat;
import com.houxiaokang.common.Util.UUIDUtil;
import com.houxiaokang.settings.domain.User;
import com.houxiaokang.workbench.domain.*;
import com.houxiaokang.workbench.mapper.*;
import com.houxiaokang.workbench.server.ClueServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ClueServerImpl implements ClueServer {
    @Autowired
    private ContactsMapper contactsMapper;
    @Autowired
    private ClueMapper clueMapper;
    @Autowired
    private CustomerMapper customerMapper;
    @Autowired
    private ClueRemarkMapper clueRemarkMapper;
    @Autowired
    private CustomerRemarkMapper customerRemarkMapper;
    @Autowired
    private ContactsRemarkMapper contactsRemarkMapper;
    @Autowired
    private ClueActivityRelationMapper activityRelationMapper;
    @Autowired
    private ContactsActivityRelationMapper contactsActivityRelationMapper;
    @Autowired
    private TransactionMapper transactionMapper;
    @Autowired
    private TransactionRemarkMapper transactionRemarkMapper;

    @Override
    public int saveCreateClue(Clue clue) {
        return clueMapper.insertClue(clue);
    }

    @Override
    public Clue queryClueForDetailByClueId(String clueid) {
        return clueMapper.selectClueForDetailById(clueid);
    }

    @Override
    public void clueForConvertByclueId(String clueId, User user, Transaction isCreatedTean) {
        //    把线索中有关公司的信息转换到客户表中
        Clue clue = clueMapper.selectClueForConvertById(clueId);
        Customer customer = new Customer();
        customer.setId(UUIDUtil.getUUID());
        customer.setOwner(user.getId());
        customer.setAddress(clue.getAddress());
        customer.setContactSummary(clue.getContactSummary());
        customer.setCreateBy(user.getId());
        customer.setCreateTime(DatesimplDateFormat.DatesimplDateFormatoString(new Date()));
        customer.setDescription(clue.getDescription());
        customer.setName(clue.getCompany());
        customer.setNextContactTime(clue.getNextContactTime());
        customer.setWebsite(clue.getWebsite());
        customer.setPhone(clue.getPhone());
        customerMapper.insertCustomerForConvertByClue(customer);
        // 把线索中有关个人的信息转换到联系人表中
        Contacts contacts = new Contacts();
        contacts.setId(UUIDUtil.getUUID());
        contacts.setOwner(user.getId());
        contacts.setSource(clue.getSource());
        contacts.setContactSummary(clue.getContactSummary());
        contacts.setAddress(clue.getAddress());
        contacts.setCreateBy(user.getCreateBy());
        contacts.setAppellation(clue.getAppellation());
        contacts.setDescription(clue.getDescription());
        contacts.setEmail(clue.getEmail());
        contacts.setCustomerId(customer.getId());
        contacts.setFullname(clue.getFullname());
        contacts.setJob(clue.getJob());
        contacts.setMphone(clue.getMphone());
        contacts.setNextContactTime(clue.getNextContactTime());
        contacts.setCreateTime(DatesimplDateFormat.DatesimplDateFormatoString(new Date()));
        contactsMapper.insertContactsForConvertByClue(contacts);
        //   把线索的备注信息转换到客户备注表中一份
        //    把线索的备注信息转换到联系人备注表中一份
        List<ClueRemark> clueRemarks = clueRemarkMapper.selectClueRemarkForConvertByClueId(clueId);
        List<CustomerRemark> cr = new ArrayList();
        List<ContactsRemark> crm = new ArrayList();
        List<TransactionRemark> tr = new ArrayList();
        if (clueRemarks != null && clueRemarks.size() > 0) {
            for (ClueRemark clueRemark : clueRemarks) {
                CustomerRemark customerRemark = new CustomerRemark();
                customerRemark.setCustomerId(contacts.getId());
                customerRemark.setCreateBy(clueRemark.getCreateBy());
                customerRemark.setId(UUIDUtil.getUUID());
                customerRemark.setEditFlag(clueRemark.getEditFlag());
                customerRemark.setCreateTime(clueRemark.getCreateTime());
                customerRemark.setEditBy(clueRemark.getEditBy());
                customerRemark.setNoteContent(clueRemark.getNoteContent());
                customerRemark.setEditTime(clueRemark.getEditTime());
                cr.add(customerRemark);
                ContactsRemark ContactsRemark = new ContactsRemark();
                ContactsRemark.setContactsId(customer.getId());
                ContactsRemark.setCreateBy(clueRemark.getCreateBy());
                ContactsRemark.setId(UUIDUtil.getUUID());
                ContactsRemark.setEditFlag(clueRemark.getEditFlag());
                ContactsRemark.setCreateTime(clueRemark.getCreateTime());
                ContactsRemark.setEditBy(clueRemark.getEditBy());
                ContactsRemark.setNoteContent(clueRemark.getNoteContent());
                ContactsRemark.setEditTime(clueRemark.getEditTime());
                crm.add(ContactsRemark);
                //    如果需要创建交易,还要把线索的备注信息转换到交易备注表中一份
                if (isCreatedTean != null) {
                    TransactionRemark transactionRemark = new TransactionRemark();
                    transactionRemark.setTranId(isCreatedTean.getId());
                    transactionRemark.setCreateBy(clueRemark.getCreateBy());
                    transactionRemark.setId(UUIDUtil.getUUID());
                    transactionRemark.setEditFlag(clueRemark.getEditFlag());
                    transactionRemark.setCreateTime(clueRemark.getCreateTime());
                    transactionRemark.setEditBy(clueRemark.getEditBy());
                    transactionRemark.setNoteContent(clueRemark.getNoteContent());
                    transactionRemark.setEditTime(clueRemark.getEditTime());
                    tr.add(transactionRemark);
                }
            }
            if (isCreatedTean != null) {
                transactionRemarkMapper.insertTransactionRemarkForConvertByClue(tr);
            }
            int insert = contactsRemarkMapper.insert(crm);
            int i = customerRemarkMapper.insertCustomerRemarkForConvertByClue(cr);
        }

        //    把线索和市场活动的关联关系转换到联系人和市场活动的关联关系表中
        List<ClueActivityRelation> clueActivityRelations = activityRelationMapper.selectClueActivityRelationForConvertByClueId(clueId);
        if (clueActivityRelations != null && clueActivityRelations.size() > 0) {
            List<ContactsActivityRelation> car = new ArrayList();
            for (ClueActivityRelation clueActivityRelation : clueActivityRelations) {
                ContactsActivityRelation contactsActivityRelation = new ContactsActivityRelation();
                contactsActivityRelation.setId(UUIDUtil.getUUID());
                contactsActivityRelation.setActivityId(clueActivityRelation.getActivityId());
                contactsActivityRelation.setContactsId(contacts.getId());
                car.add(contactsActivityRelation);
            }
            contactsActivityRelationMapper.insertContactsActivityRelationForConvertByClue(car);
        }
        //如果需要创建交易,还要往交易表中添加一条记录
        if (isCreatedTean != null) {
            isCreatedTean.setCreateBy(user.getId());
            isCreatedTean.setOwner(user.getId());
            isCreatedTean.setId(UUIDUtil.getUUID());
            isCreatedTean.setCustomerId(customer.getId());
            isCreatedTean.setCreateTime(DatesimplDateFormat.DatesimplDateFormatoString(new Date()));
            int insert1 = transactionMapper.insert(isCreatedTean);
        }
        //            删除线索的备注
        clueRemarkMapper.deleteClueRemarkByClueId(clueId);
//    删除线索和市场活动的关联关系
        activityRelationMapper.deleteClueActivityRelationByClueId(clueId);
//            删除线索
        clueMapper.deleteByPrimaryKey(clueId);
    }
}
