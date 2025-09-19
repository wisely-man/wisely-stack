package io.github.wisely.core.data.getter;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

public class PojoProxyTest {


    @AllArgsConstructor
    @Setter
    @Getter
    static class Person {
        String name;
        int age;
        Address address;
        List<String> hobbies;
    }

    @AllArgsConstructor
    @Setter
    @Getter
    static class Address {
        String city;
    }

    @Test
    public void proxyTest() {
        Person person = new Person("张三", 18, new Address("上海"), Lists.newArrayList("reading", "balls"));
        PojoProxy proxy = PojoProxy.proxy(person);
        Assertions.assertEquals("张三", proxy.get("name"));
    }


    @Test
    public void setTest() {
        Person person = new Person("张三", 18, new Address("上海"), Lists.newArrayList("reading", "balls"));
        PojoProxy proxy = PojoProxy.proxy(person);
        proxy.set("name", "李四");
        Assertions.assertEquals("李四", person.name);

        person.age = 20;
        Assertions.assertEquals(20, proxy.getInt("age"));
    }


    @Test
    public void getTest() {
        Person person = new Person("张三", 18, new Address("上海"), Lists.newArrayList("reading", "balls"));
        PojoProxy proxy = PojoProxy.proxy(person);

        // 路径获取
        Assertions.assertEquals("上海", proxy.getString("address.city"));
        Assertions.assertEquals("balls", proxy.get("hobbies[1]"));

        // 类型转换
        person.address.city = null;
        Assertions.assertEquals("", proxy.getString("address.city"));
        person.address = null;
        Assertions.assertEquals("", proxy.getString("address.city"));
        Assertions.assertEquals(new BigDecimal("18"), proxy.getBigDecimal("age"));
        Assertions.assertEquals("18", proxy.getString("age"));
    }


    @Test
    public void listenerTest() {
        Person person = new Person("张三", 18, new Address("上海"), Lists.newArrayList("reading", "balls"));
        PojoProxy proxy = PojoProxy.proxy(person);
        List<String> changes = Lists.newArrayList();
        proxy.onPropertyChange(event ->
                changes.add(event.propertyName() + ": from [" + event.oldValue() + "] to" + "["+ event.newValue() +"]"));

        proxy.set("name", "李四");
        proxy.set("age", 20);

        proxy.set("hobbies", Lists.newArrayList("balls"));

        System.out.println(changes);
    }

}
