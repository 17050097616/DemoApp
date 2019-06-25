package com.echase.cashier.utils;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.Expose;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by chm on 2018/4/17.
 */

public class GsonHelper {

    public static void demoUserTypeAdapter() {
        User user = new User("怪盗kidou", 24);
        user.email = "ikidou@example.com";
        Gson gson = new GsonBuilder()
                //为User注册TypeAdapter
                .registerTypeAdapter(User.class, new UserTypeAdapter())
                .create();
        System.out.println(gson.toJson(user));
    }

    public static void demoGsonBuilder() {
        //        Gson的创建方式二：使用GsonBuilder,
        Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation() //不对没有用@Expose注解的属性进行操作
                .enableComplexMapKeySerialization() //当Map的key为复杂对象时,需要开启该方法
                .serializeNulls() //当字段值为空或null时，依然对该字段进行转换
                .setDateFormat("yyyy-MM-dd HH:mm:ss:SSS") //时间转化为特定格式
                .setPrettyPrinting() //对结果进行格式化，增加换行
                .disableHtmlEscaping() //防止特殊字符出现乱码
                .registerTypeAdapter(User.class, new UserTypeAdapter())
                //为某特定对象设置固定的序列或反序列方式，自定义Adapter需实现JsonSerializer或者JsonDeserializer接口
                .create();
    }

    public static void demoTypeAdapter() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Integer.class, new TypeAdapter<Integer>() {
                    @Override
                    public void write(JsonWriter out, Integer value) throws IOException {
                        out.value(String.valueOf(value));
                    }

                    @Override
                    public Integer read(JsonReader in) throws IOException {
                        try {
                            return Integer.parseInt(in.nextString());
                        } catch (NumberFormatException e) {
                            return -1;
                        }
                    }
                })
                .create();
        System.out.println(gson.toJson(100)); // 结果："100"
        System.out.println(gson.fromJson("\"\"", Integer.class)); // 结果：-1
    }

    public static void demoJsonDeserializer() {
        Gson gson = new GsonBuilder().registerTypeAdapter(Integer.class, new JsonDeserializer<Integer>() {
            @Override
            public Integer deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                try {
                    return json.getAsInt();
                } catch (NumberFormatException e) {
                    return -1;
                }
            }
        })
                .create();
        System.out.println(gson.toJson(100)); //结果：100
        System.out.println(gson.fromJson("\"\"", Integer.class)); //结果-1
    }

    public static void demoJsonSerializer() {
        JsonSerializer<Number> numberJsonSerializer = new JsonSerializer<Number>() {
            @Override
            public JsonElement serialize(Number src, Type typeOfSrc, JsonSerializationContext context) {
                return new JsonPrimitive(String.valueOf(src));
            }
        };
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Integer.class, numberJsonSerializer)
                .registerTypeAdapter(Long.class, numberJsonSerializer)
                .registerTypeAdapter(Float.class, numberJsonSerializer)
                .registerTypeAdapter(Double.class, numberJsonSerializer)
                .create();
        System.out.println(gson.toJson(100.0f));//结果："100.0"
    }

    public static void demoTypeToken() {
        Type type = new TypeToken<List<User>>() {
        }.getType();
        TypeAdapter typeAdapter = new TypeAdapter<List<User>>() {
            @Override
            public void write(JsonWriter out, List<User> value) throws IOException {

            }

            @Override
            public List<User> read(JsonReader in) throws IOException {
                return null;
            }
            //略
        };
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(type, typeAdapter)
                .create();
        List<User> list = new ArrayList<>();
        list.add(new User("a", 11));
        list.add(new User("b", 22));
        //注意，多了个type参数
        String result = gson.toJson(list, type);
    }

    public static void demoTypeAdapterFactory() {
        Gson gson = new GsonBuilder()
                .registerTypeAdapterFactory(new TypeAdapterFactory() {
                    @Override
                    public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
                        return null;
                    }
                })
                .create();
    }

    public static void demoDifferentData() {
        Gson gson = new GsonBuilder().registerTypeHierarchyAdapter(List.class, new JsonDeserializer<List<?>>() {
            @Override
            public List<?> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                if (json.isJsonArray()) {
                    JsonArray array = json.getAsJsonArray();
                    Type itemType = ((ParameterizedType) typeOfT).getActualTypeArguments()[0];
                    List list = new ArrayList<>();
                    for (int i = 0; i < array.size(); i++) {
                        JsonElement element = array.get(i);
                        Object item = context.deserialize(element, itemType);
                        list.add(item);
                    }
                    return list;
                } else {
                    //和接口类型不符，返回空List
                    return Collections.EMPTY_LIST;
                }
            }
        }).create();
    }

    public void demo() {
        Gson gson = new GsonBuilder()
                .addSerializationExclusionStrategy(new ExclusionStrategy() {
                    @Override
                    public boolean shouldSkipField(FieldAttributes f) {
                        // 这里作判断，决定要不要排除该字段,return true为排除
                        if ("finalField".equals(f.getName())) return true; //按字段名排除
                        Expose expose = f.getAnnotation(Expose.class);
                        if (expose != null && expose.deserialize()) return true; //按注解排除
                        return false;
                    }

                    @Override
                    public boolean shouldSkipClass(Class<?> clazz) {
                        // 直接排除某个类 ，return true为排除
                        return (clazz == int.class || clazz == Integer.class);
                    }
                })
                .create();
    }
}
