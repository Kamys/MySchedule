package com.kamys.github.myschedule.presenter;

import org.junit.Before;
import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class LessonTimeManagerTest {

    private final LessonTimeManager timeManager = new LessonTimeManager();

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void ifDataAlreadyEnd() throws Exception {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MINUTE, -1);

        final long result = timeManager.calculateHowToStart(calendar.getTime());
        assertThat(result, is(0L));
    }

    @Test
    public void calculateHowToStart() throws Exception {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MINUTE, 1);

        final long beforeTheStart = timeManager.calculateHowToStart(calendar.getTime());
        System.out.println(beforeTheStart);
        assertThat(beforeTheStart, not(0L));
        assertTrue("Mils not de more 60000", beforeTheStart < 60000);
        assertTrue("Mils de more 40000", beforeTheStart > 50000);
    }
}