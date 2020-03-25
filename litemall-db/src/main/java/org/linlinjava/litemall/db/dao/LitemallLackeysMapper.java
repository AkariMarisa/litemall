package org.linlinjava.litemall.db.dao;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.linlinjava.litemall.db.domain.LitemallLackeys;
import org.linlinjava.litemall.db.domain.LitemallLackeysExample;

public interface LitemallLackeysMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table litemall_lackeys
     *
     * @mbg.generated
     */
    long countByExample(LitemallLackeysExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table litemall_lackeys
     *
     * @mbg.generated
     */
    int deleteByExample(LitemallLackeysExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table litemall_lackeys
     *
     * @mbg.generated
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table litemall_lackeys
     *
     * @mbg.generated
     */
    int insert(LitemallLackeys record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table litemall_lackeys
     *
     * @mbg.generated
     */
    int insertSelective(LitemallLackeys record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table litemall_lackeys
     *
     * @mbg.generated
     */
    LitemallLackeys selectOneByExample(LitemallLackeysExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table litemall_lackeys
     *
     * @mbg.generated
     */
    LitemallLackeys selectOneByExampleSelective(@Param("example") LitemallLackeysExample example, @Param("selective") LitemallLackeys.Column ... selective);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table litemall_lackeys
     *
     * @mbg.generated
     */
    List<LitemallLackeys> selectByExampleSelective(@Param("example") LitemallLackeysExample example, @Param("selective") LitemallLackeys.Column ... selective);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table litemall_lackeys
     *
     * @mbg.generated
     */
    List<LitemallLackeys> selectByExample(LitemallLackeysExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table litemall_lackeys
     *
     * @mbg.generated
     */
    LitemallLackeys selectByPrimaryKeySelective(@Param("id") Integer id, @Param("selective") LitemallLackeys.Column ... selective);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table litemall_lackeys
     *
     * @mbg.generated
     */
    LitemallLackeys selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table litemall_lackeys
     *
     * @mbg.generated
     */
    int updateByExampleSelective(@Param("record") LitemallLackeys record, @Param("example") LitemallLackeysExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table litemall_lackeys
     *
     * @mbg.generated
     */
    int updateByExample(@Param("record") LitemallLackeys record, @Param("example") LitemallLackeysExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table litemall_lackeys
     *
     * @mbg.generated
     */
    int updateByPrimaryKeySelective(LitemallLackeys record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table litemall_lackeys
     *
     * @mbg.generated
     */
    int updateByPrimaryKey(LitemallLackeys record);
}