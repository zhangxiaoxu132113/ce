package com.water.crawl.db.dao;

import com.water.crawl.db.dao.extend.CourseMapperExtend;
import com.water.crawl.db.model.Course;
import com.water.crawl.db.model.CourseCriteria;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

public interface CourseMapper extends CourseMapperExtend {
    int countByExample(CourseCriteria example);

    int deleteByExample(CourseCriteria example);

    int deleteByPrimaryKey(String id);

    int insert(Course record);

    int insertSelective(Course record);

    List<Course> selectByExampleWithRowbounds(CourseCriteria example, RowBounds rowBounds);

    List<Course> selectByExample(CourseCriteria example);

    Course selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") Course record, @Param("example") CourseCriteria example);

    int updateByExample(@Param("record") Course record, @Param("example") CourseCriteria example);

    int updateByPrimaryKeySelective(Course record);

    int updateByPrimaryKey(Course record);

    int insertBatch(List<Course> list);
}