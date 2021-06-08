package com.twc.guanlang.common.annotation;

import lombok.Data;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.List;


@Data
public class AnnoTest {

    private WebApplicationType webApplicationType;

    @Target(ElementType.METHOD)
    @ConditionalOnClass(name = "测试ConditionalOnClass", value = {AnnoTest.class})
    public @interface MyAnono {

        String name() default "";
    }

    @MyAnono(name = "ssss")
    private static void test() {
    }

    interface TT {

        int opt(int a, int b);
    }

    public static void main(String s[]) {


        Annotation[] annotations = AnnoTest.class.getAnnotations();


        @MyEnglishDoc(subject = "lambada expression",
                value = "the right side of this equation is lambada" +
                        "it provider an anonymous  class which  implement the interface")
        TT add = (a, b) -> a + b;
        //invocation example
        System.out.println(add.opt(2, 5));


        List<String> list = new ArrayList();
        list.add("aaaaa");

        //lambada for comparator
        list.add("fffff");
        list.add("cccccc");
        list.add("gggggg");
        list.sort((a, b) -> {
            return a.toLowerCase().toCharArray()[0] > a.toLowerCase().toCharArray()[0] ? 1 : -1;
        });

        //lambada for collection iterator
        list.forEach((e) -> System.out.println(e));
    }

}
