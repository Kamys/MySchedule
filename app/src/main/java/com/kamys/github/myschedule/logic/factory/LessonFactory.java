package com.kamys.github.myschedule.logic.factory;

import android.content.Context;

import com.kamys.github.myschedule.logic.LessonHelper;
import com.kamys.github.myschedule.logic.SchedulesHelper;
import com.parsingHTML.logic.element.DayName;
import com.parsingHTML.logic.element.NumeratorName;
import com.parsingHTML.logic.extractor.xml.Lesson;

import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * This factory for {@link Lesson}
 */

public class LessonFactory {
    private SchedulesHelper helper;

    public LessonFactory(Context context) {
        this.helper = new SchedulesHelper(context);
    }


    public List<List<Lesson>> createArrayLesson(NumeratorName numeratorToday) {
        Document doc = helper.initializationDocument();
        int length = DayName.values().length;
        List<List<Lesson>> arrayLists = new ArrayList<>(length);
        for (int i = 0; i < length; i++) {
            ArrayList<Lesson> lessons = LessonHelper.getLesson(DayName.values()[i], numeratorToday, doc);
            arrayLists.add(lessons);
        }
        helper.saveDOC(doc);
        return arrayLists;
    }
}
