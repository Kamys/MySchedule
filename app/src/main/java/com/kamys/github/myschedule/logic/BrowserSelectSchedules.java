package com.kamys.github.myschedule.logic;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.http.SslError;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.ClientCertRequest;
import android.webkit.ConsoleMessage;
import android.webkit.HttpAuthHandler;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.kamys.github.myschedule.R;
import com.kamys.github.myschedule.logic.factory.AlertDialogFactory;
import com.kamys.github.myschedule.view.activity.BrowserActivity;
import com.parsingHTML.logic.ParsingHTML;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * Браузер с для выбора расписания.
 */
public class BrowserSelectSchedules {
    private static final String TAG = BrowserSelectSchedules.class.getName();
    private static final String startURL = "http://rasp.bukep.ru/Default.aspx";
    private final SchedulesHelper helper;
    private final BrowserActivity browserActivity;
    private Document timeDoc = null;
    private Document lessonDoc = null;

    public BrowserSelectSchedules(SchedulesHelper helper, BrowserActivity browserActivity) {
        this.helper = helper;
        this.browserActivity = browserActivity;
        checkConnectInternet();
        settingBrowserActivity();
    }

    private void checkConnectInternet() {
        if (!isOnline()) {
            Log.w(TAG, "checkConnectInternet() Failed! ");
            String errorMessage = browserActivity.getString(R.string.failedConnect);
            AlertDialogFactory.showAlertDialogError(
                    browserActivity, errorMessage);
        } else {
            Log.i(TAG, "checkConnectInternet() Successfully! ");
        }
    }

    private void settingBrowserActivity() {
        final FloatingActionButton fab = browserActivity.getFab();
        fab.setOnClickListener(new ButtonSaveListener());
        browserActivity.fabHide();

        final WebView webView = browserActivity.getWebView();
        webView.setWebChromeClient(new MyWebChromeClient());
        webView.setWebViewClient(new MyWebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(startURL);

    }

    /**
     * Проверка есть ли подключение к интернету.
     *
     * @return true если да.
     */
    private boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) browserActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        boolean isOnline = netInfo != null && netInfo.isConnectedOrConnecting();
        Log.d(TAG, "isOnline() return " + isOnline);
        return isOnline;
    }

    private class MyWebChromeClient extends WebChromeClient {

        /**
         * Префикс используется для обозначения html документа передоваймого в onConsoleMessage()
         */
        static final String prefix = "MessageHTML";

        @Override
        public boolean onConsoleMessage(ConsoleMessage cmsg) {
            Log.d(TAG, "onConsoleMessage() =  " + cmsg);
            // check prefix
            if (cmsg.message().startsWith(prefix)) {
                String msg = cmsg.message().substring(prefix.length()); // delete prefix
                processingHtml(msg);
                return true;
            }

            return false;
        }

        /**
         * Работа с HTML на которой перешол пользователь.
         */
        private void processingHtml(String msg) {
            Log.d(TAG, "processingHtml()");
            Document parse = Jsoup.parse(msg);

            if (ParsingHTML.checkSchedules(parse)) {
                processingSchedules(parse);
            } else {
                browserActivity.fabHide();
                Log.d(TAG, "This is NOT Schedules!! ");
            }

            if (ParsingHTML.checkSchedulesTime(parse)) {
                processingTime(parse);
            } else {
                Log.d(TAG, "This is NOT Time!! ");
            }
        }

        /**
         * Обработка времени расписания.
         * Вызывается в том случии если HTML содержит время расписания.
         */
        private void processingTime(Document timeDoc) {
            Log.d(TAG, "processingTime() " + timeDoc.title());
            BrowserSelectSchedules.this.timeDoc = timeDoc;
        }

        /**
         * Обработка рассписания.
         * Вызывается в том случии если HTML содержит расписание.
         */
        private void processingSchedules(Document lessonDoc) {
            Log.d(TAG, "processingSchedules() " + timeDoc.title());
            BrowserSelectSchedules.this.lessonDoc = lessonDoc;
            browserActivity.fabShow();
        }

    }

    private class ButtonSaveListener implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            try {
                if (timeDoc == null) {
                    final String errorMessages = "onClick() timeDoc == null";
                    Log.w(TAG, errorMessages);
                    throw new Exception(errorMessages);
                }
                if (lessonDoc == null) {
                    final String errorMessages = "onClick() lessonDoc == null";
                    Log.w(TAG, errorMessages);
                    throw new Exception(errorMessages);
                }

                helper.saveDOC(LessonHelper.parsingHTML(timeDoc, lessonDoc));
                browserActivity.fabHide();
                Toast toast = Toast.makeText(
                        browserActivity, R.string.schedule_save, Toast.LENGTH_SHORT);
                toast.show();
            } catch (Exception e) {
                Log.w(TAG, "onClick() ", e);
                AlertDialogFactory.showAlertDialogError(
                        browserActivity, e.getMessage());
            }
        }
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            Log.d(TAG, "shouldOverrideUrlLoading() request = " + request);
            return super.shouldOverrideUrlLoading(view, request);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            Log.d(TAG, "onPageStarted() url = " + url + " favicon = " + favicon);
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            Log.d(TAG, "onPageFinished() url = " + url);
            view.loadUrl("javascript:console.log('" + MyWebChromeClient.prefix + "'+document.getElementsByTagName('html')[0].innerHTML);");
        }

        @Override
        public void onLoadResource(WebView view, String url) {
            Log.d(TAG, "onLoadResource() url = " + url);
            super.onLoadResource(view, url);
        }

        @Override
        public void onPageCommitVisible(WebView view, String url) {
            Log.d(TAG, "onPageCommitVisible() url = " + url);
            super.onPageCommitVisible(view, url);
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            Log.d(TAG, "onReceivedError() request = " + request + " error = " + error);
            super.onReceivedError(view, request, error);
        }

        @Override
        public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
            Log.d(TAG, "onReceivedHttpError() request = " + request + " errorResponse = " + errorResponse);
            super.onReceivedHttpError(view, request, errorResponse);
        }

        @Override
        public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {
            Log.d(TAG, "doUpdateVisitedHistory() url = " + url + " isReload = " + isReload);
            super.doUpdateVisitedHistory(view, url, isReload);
        }

        @Override
        public void onFormResubmission(WebView view, Message dontResend, Message resend) {
            Log.d(TAG, "onFormResubmission() dontResend = " + dontResend + " resend = " + resend);
            super.onFormResubmission(view, dontResend, resend);
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            Log.d(TAG, "onReceivedSslError() handler = " + handler + " error = " + error);
            super.onReceivedSslError(view, handler, error);
        }

        @Override
        public void onReceivedClientCertRequest(WebView view, ClientCertRequest request) {
            Log.d(TAG, "onReceivedClientCertRequest() request = " + request);
            super.onReceivedClientCertRequest(view, request);
        }

        @Override
        public void onReceivedHttpAuthRequest(WebView view, HttpAuthHandler handler, String host, String realm) {
            Log.d(TAG, "onReceivedHttpAuthRequest() handler = " + handler + " host = " + host + "realm = " + realm);
            super.onReceivedHttpAuthRequest(view, handler, host, realm);
        }

        @Override
        public boolean shouldOverrideKeyEvent(WebView view, KeyEvent event) {
            Log.d(TAG, "shouldOverrideKeyEvent() event = " + event);
            return super.shouldOverrideKeyEvent(view, event);
        }

        @Override
        public void onUnhandledKeyEvent(WebView view, KeyEvent event) {
            Log.d(TAG, "onUnhandledKeyEvent() event = " + event);
            super.onUnhandledKeyEvent(view, event);
        }

        @Override
        public void onReceivedLoginRequest(WebView view, String realm, String account, String args) {
            Log.d(TAG, "onReceivedLoginRequest() realm = " + realm + " account = " + account + "args = " + args);
            super.onReceivedLoginRequest(view, realm, account, args);
        }

        @Override
        public void onScaleChanged(WebView view, float oldScale, float newScale) {
            Log.d(TAG, "onScaleChanged() oldScale = " + oldScale + " newScale = " + newScale);
            super.onScaleChanged(view, oldScale, newScale);
        }
    }

}
