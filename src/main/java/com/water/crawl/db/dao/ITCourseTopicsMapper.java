package com.water.crawl.db.dao;

import com.water.crawl.db.dao.extend.ITCourseTopicsMapperExtend;
import com.water.crawl.db.model.ITCourseTopics;
import com.water.crawl.db.model.ITCourseTopicsCriteria;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

public interface ITCourseTopicsMapper extends ITCourseTopicsMapperExtend {
    int countByExample(ITCourseTopicsCriteria example);

    int deleteByExample(ITCourseTopicsCriteria example);

    int deleteByPrimaryKey(String id);

    int insert(ITCourseTopics record);

    int insertSelective(ITCourseTopics record);

    List<ITCourseTopics> selectByExampleWithRowbounds(ITCourseTopicsCriteria example, RowBounds rowBounds);

    List<ITCourseTopics> selectByExample(ITCourseTopicsCriteria example);

    ITCourseTopics selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") ITCourseTopics record, @Param("example") ITCourseTopicsCriteria example);

    int updateByExample(@Param("record") ITCourseTopics record, @Param("example") ITCourseTopicsCriteria example);

    int updateByPrimaryKeySelective(ITCourseTopics record);

    int updateByPrimaryKey(ITCourseTopics record);

    int insertBatch(List<ITCourseTopics> list);
}