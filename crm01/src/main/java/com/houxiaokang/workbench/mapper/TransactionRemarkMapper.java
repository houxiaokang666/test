package com.houxiaokang.workbench.mapper;

import com.houxiaokang.workbench.domain.TransactionRemark;

import java.util.List;

public interface TransactionRemarkMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_transaction_remark
     *
     * @mbggenerated Wed Aug 10 16:37:02 CST 2022
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_transaction_remark
     *
     * @mbggenerated Wed Aug 10 16:37:02 CST 2022
     */
    int insert(TransactionRemark record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_transaction_remark
     *
     * @mbggenerated Wed Aug 10 16:37:02 CST 2022
     */
    int insertSelective(TransactionRemark record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_transaction_remark
     *
     * @mbggenerated Wed Aug 10 16:37:02 CST 2022
     */
    TransactionRemark selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_transaction_remark
     *
     * @mbggenerated Wed Aug 10 16:37:02 CST 2022
     */
    int updateByPrimaryKeySelective(TransactionRemark record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_transaction_remark
     *
     * @mbggenerated Wed Aug 10 16:37:02 CST 2022
     */
    int updateByPrimaryKey(TransactionRemark record);

    int insertTransactionRemarkForConvertByClue(List<TransactionRemark> transactionRemarks);

    List<TransactionRemark> selectTransactionRemarkForDetailByTransactionId(String transactionId);
}