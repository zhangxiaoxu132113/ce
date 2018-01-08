package com.water.ce.web.model;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * 对应解析的csdn知识库的json数据
 * Created by zmj on 2017/11/20.
 */
public class BaseSubject {
    private int id;
    private int index;
    private String course;
    private String href;
    private String name;
    private String subject;
    private BaseSubject prop;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public BaseSubject getProp() {
        return prop;
    }

    public void setProp(BaseSubject prop) {
        this.prop = prop;
    }

    public static class BaseSubejctFactory {
        private static final Gson gson = new Gson();

        /**
         * 传入json格式，获取BaseSubejct集合
         * @param jsonStr
         * @return
         */
        public static List<BaseSubject> getBaseSubjectList(String jsonStr) {
            List<BaseSubject> baseSubjects = null;
            Type listType = new TypeToken<ArrayList<BaseSubject>>() {}.getType();
            baseSubjects = gson.fromJson(jsonStr, listType);
            return baseSubjects;
        }
    }

//    public static void main(String[] args) {
//        String json = "[\n" +
//                "{\n" +
//                "\"course\": \"0\",\n" +
//                "\"fixed\": true,\n" +
//                "\"href\": \"http://lib.csdn.net/base/openstack\",\n" +
//                "\"id\": 33,\n" +
//                "\"index\": 1,\n" +
//                "\"prop\": {\n" +
//                "\"course\": \"0\",\n" +
//                "\"name\": \"OpenStack\",\n" +
//                "\"subject\": \"OpenStack\"\n" +
//                "},\n" +
//                "\"x\": 560,\n" +
//                "\"y\": 480\n" +
//                "},\n" +
//                "{\n" +
//                "\"course\": \"484\",\n" +
//                "\"href\": \"http://lib.csdn.net/openstack/node/484\",\n" +
//                "\"id\": \"484\",\n" +
//                "\"index\": 2,\n" +
//                "\"prop\": {\n" +
//                "\"course\": \"484\",\n" +
//                "\"name\": \"OpenStack入门\",\n" +
//                "\"subject\": \"\"\n" +
//                "}\n" +
//                "}]\n";
//
//        String rootId = "sdf";
//        String levelArr[] = new String[10];
//        TbUbLib lib;
//        List<TbUbLib> libList = new ArrayList<>();
//        List<BaseSubject> baseSubjects = BaseSubject.BaseSubejctFactory.getBaseSubjectList(json);
//        for (BaseSubject baseSubject : baseSubjects) {
//            if (baseSubject.getIndex() == 0) {
//                continue;
//            }
//            lib = new TbUbLib();
//            String libId = StringUtil.uuid();
//            lib.setId(libId);
//            lib.setName(baseSubject.getProp().getName());
//            lib.setUrl(baseSubject.getHref());
//            //lib.setParentId(levelArr[baseSubject.getIndex()-1])
//            levelArr[baseSubject.getIndex()] = libId;
//            libList.add(lib);
//        }
//
//        System.out.println(libList.size());
//    }
}
