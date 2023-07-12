package top.latke.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;


/**
 * Java8 Predicate 使用方法与思想
 */
@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class PredicateTest {

    public static List<String> MICRO_SERVICE = Arrays.asList("nacos","authority","gateway","ribbon","feign","hystrix","fox");

    @Test
    public void predicateTest(){
        Predicate<String> letterLengthLimit = s -> s.length() > 5;
        MICRO_SERVICE.stream().filter(letterLengthLimit).forEach(System.out::println);
    }

    @Test
    public void predicateAndTest(){
        Predicate<String> letterLengthLimit = s -> s.length() > 5;
        Predicate<String> letterStartWith = s -> s.startsWith("gate");
        MICRO_SERVICE.stream().filter(letterLengthLimit.and(letterStartWith)).forEach(System.out::println);
    }

    @Test
    public void predicateOrTest(){
        Predicate<String> letterLengthLimit = s -> s.length() > 5;
        Predicate<String> letterStartWith = s -> s.startsWith("gate");
        MICRO_SERVICE.stream().filter(letterLengthLimit.or(letterStartWith)).forEach(System.out::println);
    }

    @Test
    public void predicateNeTest(){
        Predicate<String> letterLengthLimit = s -> s.length() > 5;
        Predicate<String> letterStartWith = s -> s.startsWith("gate");
        MICRO_SERVICE.stream().filter(letterStartWith.negate()).forEach(System.out::println);
        log.info("-----------------------------");
        MICRO_SERVICE.stream().filter(letterLengthLimit.negate().and(letterStartWith.negate())).forEach(System.out::println);
    }

    @Test
    public void predicateIsEqualTest(){
        Predicate<String> letterStartWith = s -> Predicate.isEqual("gateway").test(s);
        MICRO_SERVICE.stream().filter(letterStartWith).forEach(System.out::println);
    }

}
