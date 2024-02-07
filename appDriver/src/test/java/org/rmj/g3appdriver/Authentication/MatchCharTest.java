package org.rmj.g3appdriver.Authentication;

import static org.junit.Assert.assertTrue;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.regex.Pattern;

@RunWith(JUnit4.class)
public class MatchCharTest {
    @Test
    public void TestCharRangeLower(){
        System.out.println(Pattern.matches("[a-z]+", "agutoman"));
    }
    @Test
    public void TestCharRangeUpper(){
        System.out.println(Pattern.matches("[A-Z]+", "GGUTOMAN"));
    }
    @Test
    public void TestCharRangeNumber(){
        System.out.println(Pattern.matches("[0-9]+", "1982"));
    }
    @Test
    public void TestCharCombination(){
        System.out.println(Pattern.matches("[a-zA-Z0-9]+", "0GGuto1man"));
    }
}
