package com.kamys.github.myschedule.logic;

import android.content.Context;
import android.util.Log;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 * Класс для работы с расрисанием.
 */
public class SchedulesHelper {
    private static final String TAG = SchedulesHelper.class.getName();
    private final Context context;


    public SchedulesHelper(Context context) {
        this.context = context;
        Log.i(TAG, "Create SchedulesHelper. Activity = " + context);
    }

    public Document initializationDocument() {
        Log.i(TAG, "initializationDocument() context = " + context);
        Document document = renewDocument();
        if (document == null) {
            try {
                Log.i(TAG, "initializationDocument parsingHTML!");
                document = LessonHelper.parsingHTML(context.getResources());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Log.i(TAG, "initializationDocument return " + document);
        return document;
    }


    public void saveDOC(Document document) {
        Log.i(TAG, "saveDOC() context = " + context);
        try {
            File file = new File(context.getFilesDir().getAbsolutePath(), "Schedules.xml");
            Log.i(TAG, "File path = " + file.getAbsolutePath());
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            Result output = new StreamResult(file);
            Source input = new DOMSource(document);

            transformer.transform(input, output);
        } catch (TransformerException e) {
            Log.e(TAG, "Failed saveDOC()", e);
            e.printStackTrace();
        }
    }

    public Document renewDocument() {
        Log.i(TAG, "renewDocument() context = " + context);
        try {
            File file = new File(context.getFilesDir(), "Schedules.xml");
            if (!file.exists()) {
                Log.i(TAG, "File not exists!");
                return null;
            }
            Log.i(TAG, "File path = " + file.getAbsolutePath());
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            return dBuilder.parse(file);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            Log.e(TAG, "Failed renewDocument()", e);
        }
        return null;
    }


}
