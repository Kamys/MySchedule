package com.kamys.github.myschedule.logic.factory;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.kamys.github.myschedule.R;


/**
 * Фабрика для создания AlertDialog.
 */
public class AlertDialogFactory {
    private static final String TAG = AlertDialogFactory.class.getName();

    /**
     * Показать сообщение об ошибке.
     *
     * @param context Context который должен показать сообщение.
     * @param message       Ошибка которую нужно показать.
     */
    public static void showAlertDialogError(Context context, String message) {
        Log.w(TAG, "showAlertDialogError()" + message);
        createAlertDialogError(context, message).show();
    }

    /**
     * Создание сообщение об ошибке.
     *
     * @param context Context который должен показать сообщение.
     * @param message       Ошибка которую нужно показать.
     */
    public static AlertDialog createAlertDialogError(Context context, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(R.string.error)
                .setMessage(message)
                .setIcon(R.drawable.error)
                .setCancelable(false)
                .setNegativeButton(R.string.ok,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
        return builder.create();
    }

}
