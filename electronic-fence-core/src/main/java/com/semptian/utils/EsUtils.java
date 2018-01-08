package com.semptian.utils;

import com.google.common.collect.Lists;
import org.elasticsearch.common.geo.GeoPoint;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Es 工具类
 */
public class EsUtils {

    /**
     * 解析es查询结果
     *
     * @param hits
     * @param clazz
     * @return
     */
    public static <T> List<T> conversion(SearchHits hits, Class<T> clazz) {

        if (hits.getTotalHits() == 0) {
            return Lists.newArrayList();
        }

        List listData = Lists.newArrayList();

        for (SearchHit hit : hits) {
            Map<String, Object> source = hit.getSource();
            if (source == null || source.isEmpty()) {
                continue;
            }

            T entity = mapToBen(clazz, source);
            if (entity == null) {
                continue;
            }
            listData.add(entity);
        }

        return listData;

    }


    /**
     * 将Object类型的值，转换成bean对象属性里对应的类型值
     *
     * @param value          Object对象值
     * @param fieldTypeClass 属性的类型
     * @return 转换后的值
     */
    private static Object convertValType(Object value, Class<?> fieldTypeClass) {
        Object retVal = null;
        if (Long.class.getName().equals(fieldTypeClass.getName())
                || long.class.getName().equals(fieldTypeClass.getName())) {
            retVal = Long.parseLong(value.toString());
        } else if (Integer.class.getName().equals(fieldTypeClass.getName())
                || int.class.getName().equals(fieldTypeClass.getName())) {
            retVal = Integer.parseInt(value.toString());
        } else if (Float.class.getName().equals(fieldTypeClass.getName())
                || float.class.getName().equals(fieldTypeClass.getName())) {
            retVal = Float.parseFloat(value.toString());
        } else if (Double.class.getName().equals(fieldTypeClass.getName())
                || double.class.getName().equals(fieldTypeClass.getName())) {
            retVal = Double.parseDouble(value.toString());
        } else if (String.class.getName().equals(fieldTypeClass.getName())) {
            retVal = String.valueOf(value);
        } else if(GeoPoint.class.getName().equals(fieldTypeClass.getName())) {
            List<Object> list = (List<Object>) value;
            Double lat = (Double) list.get(1);
            Double lon = (Double) list.get(0);
            retVal =new GeoPoint(lat,lon);
        }else {
            retVal = value;
        }
        return retVal;
    }


    /**
     * BeanUtils通用工具,可以把一个Map集合中的值复制到bean中,
     *
     * @param clazz bean的class
     * @param map   bean对应的map集合值
     * @return bean对象  返回null表示失败
     */
    public static <T> T mapToBen(Class<T> clazz, Map<String, Object> map) {
        T obj = null;
        //通过空参构造反射一个对象出来
        try {
            obj = clazz.newInstance();
            //获取bean的所有的属性字段
            Field[] fields = clazz.getDeclaredFields();
            //遍历字段
            for (Field field : fields) {
                field.setAccessible(true);
                //获取字段的名字
                String name = field.getName();
                //通过属性名从map集合中得到对应的值
                Object value = map.get(name);
                //判断值是否为null,如果是null可以说明map集合中没有进行赋值
                if (value != null) {
                    //通过拼接的方式得到对应的set属性名
                    String methodName = "set" + name.substring(0, 1).toUpperCase() + name.substring(1);
                    //有了方法名就可以通过反射来得到真正的set属性方法,参数类型可以传字段的class
                    Class<?> type = field.getType();
                    Method method = clazz.getMethod(methodName, type);

                    value = convertValType(value, type);
                    //执行方法给bean对象属性赋值
                    method.invoke(obj, value);

                }
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return obj;
    }


}
