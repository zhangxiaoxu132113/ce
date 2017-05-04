package com.water.crawl.db.dao;

import com.water.crawl.db.dao.extend.CourseSubjectMapperExtend;
import com.water.crawl.db.model.CourseSubject;
import com.water.crawl.db.model.CourseSubjectCriteria;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

public interface CourseSubjectMapper extends CourseSubjectMapperExtend {
    int countByExample(CourseSubjectCriteria example);

    int deleteByExample(CourseSubjectCriteria example);

    int deleteByPrimaryKey(String id);

    int insert(CourseSubject record);

    int insertSelective(CourseSubject record);

    List<CourseSubject> selectByExampleWithRowbounds(CourseSubjectCriteria example, RowBounds rowBounds);

    List<CourseSubject> selectByExample(CourseSubjectCriteria example);

    CourseSubject selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") CourseSubject record, @Param("example") CourseSubjectCriteria example);

    int updateByExample(@Param("record") CourseSubject record, @Param("example") CourseSubjectCriteria example);

    int updateByPrimaryKeySelective(CourseSubject record);

    int updateByPrimaryKey(CourseSubject record);

    int insertBatch(List<CourseSubject> list);
}