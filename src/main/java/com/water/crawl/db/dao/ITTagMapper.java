package com.water.crawl.db.dao;

import com.water.crawl.db.dao.extend.ITTagMapperExtend;
import com.water.crawl.db.model.ITTag;
import com.water.crawl.db.model.ITTagCriteria;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface ITTagMapper extends ITTagMapperExtend {
    int countByExample(ITTagCriteria example);

    int deleteByExample(ITTagCriteria example);

    int deleteByPrimaryKey(String id);

    int insert(ITTag record);

    int insertSelective(ITTag record);

    List<ITTag> selectByExampleWithRowbounds(ITTagCriteria example, RowBounds rowBounds);

    List<ITTag> selectByExample(ITTagCriteria example);

    ITTag selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") ITTag record, @Param("example") ITTagCriteria example);

    int updateByExample(@Param("record") ITTag record, @Param("example") ITTagCriteria example);

    int updateByPrimaryKeySelective(ITTag record);

    int updateByPrimaryKey(ITTag record);

    int insertBatch(List<ITTag> list);
}