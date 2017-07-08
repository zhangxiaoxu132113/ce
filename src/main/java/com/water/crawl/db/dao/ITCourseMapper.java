package com.water.crawl.db.dao;

import com.water.crawl.db.dao.extend.ITCourseMapperExtend;
import com.water.crawl.db.model.ITCourse;
import com.water.crawl.db.model.ITCourseCriteria;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

public interface ITCourseMapper extends ITCourseMapperExtend {
    int countByExample(ITCourseCriteria example);

    int deleteByExample(ITCourseCriteria example);

    int insert(ITCourse record);

    int insertSelective(ITCourse record);

    List<ITCourse> selectByExampleWithRowbounds(ITCourseCriteria example, RowBounds rowBounds);

    List<ITCourse> selectByExample(ITCourseCriteria example);

    int updateByExampleSelective(@Param("record") ITCourse record, @Param("example") ITCourseCriteria example);

    int updateByExample(@Param("record") ITCourse record, @Param("example") ITCourseCriteria example);

    int insertBatch(List<ITCourse> list);
}