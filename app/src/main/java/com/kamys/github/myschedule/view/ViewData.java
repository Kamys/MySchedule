package com.kamys.github.myschedule.view;

import android.content.Context;

/**
 * This view need for display data and error.
 * D - Data which need display.
 */

public interface ViewData<D> {
    void showData(D data);

    void showError(String s);

    Context getContext();
}
