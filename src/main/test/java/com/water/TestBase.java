package com.water;

import com.water.ce.utils.lang.StringUtil;
import com.water.ce.web.model.BaseSubject;
import com.water.uubook.dao.ITLibMapper;
import com.water.uubook.model.ITLib;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by admin on 2017/11/20.
 */
public class TestBase {
    @Resource
    private ITLibMapper libMapper;

    public void insertBatchAllBase() {
        List<ITLib> libList = libMapper.selectByExample(null);
        libList.stream().forEach(p -> {
            String json = p.getName();
            String rootId = p.getId();
            String levelArr[] = new String[10];
            ITLib lib;
            List<BaseSubject> baseSubjects = BaseSubject.BaseSubejctFactory.getBaseSubjectList(json);
            for (BaseSubject baseSubject : baseSubjects) {
                if (baseSubject.getIndex() == 0) {
                    continue;
                }
                lib = new ITLib();
                String libId = StringUtil.uuid();
                lib.setId(libId);
                lib.setName(baseSubject.getProp().getName());
                lib.setUrl(baseSubject.getHref());
                //lib.setParentId(levelArr[baseSubject.getIndex()-1])
                levelArr[baseSubject.getIndex()] = libId;
            }
        });
    }
}
