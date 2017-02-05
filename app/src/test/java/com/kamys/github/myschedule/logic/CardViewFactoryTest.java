package com.kamys.github.myschedule.logic;


import com.kamys.github.myschedule.logic.factory.CardViewFactory;

import org.junit.Test;

public class CardViewFactoryTest {
    // TODO: 05.02.2017 Add automate check.
    @Test
    public void getImageForLessonNumber() throws Exception {
        for (int i = 0; i < 10; i++) {
            int imageForLessonNumber = CardViewFactory.getImageResourceForLessonNumber(i);
            System.out.println(i + " - " + imageForLessonNumber);
        }
    }

}