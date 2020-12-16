package cn.jinterest.seckill;

import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.regex.Pattern;

//@SpringBootTest
class SeckillApplicationTests {

    @Test
    void contextLoads() {

    }

    public static void main(String[] args) {
        String regx = "\\d+_" +20;
        boolean matches = Pattern.matches(regx, "2_20");
        System.out.println(matches);
    }
}
