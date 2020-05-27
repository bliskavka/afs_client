package com.example.afs_client;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {

        Measurement measurement = new Measurement("-0.1/-15.5/754/26".getBytes());
        assertEquals(-0.1f, measurement.getSpeed(), 0.1);
        assertEquals(-15.5f, measurement.getDynamicPressure(), 0.1);
        assertEquals(754f, measurement.getStaticPressure(), 0.1);
        assertEquals(26f, measurement.getTemperature(), 0.1);
    }
}