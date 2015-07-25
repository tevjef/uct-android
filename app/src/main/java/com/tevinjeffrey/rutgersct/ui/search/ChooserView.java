package com.tevinjeffrey.rutgersct.ui.search;

import com.tevinjeffrey.rutgersct.rutgersapi.model.SystemMessage;
import com.tevinjeffrey.rutgersct.rutgersapi.utils.SemesterUtils.Semester;
import com.tevinjeffrey.rutgersct.ui.base.BaseToolbarView;
import com.tevinjeffrey.rutgersct.ui.base.View;

public interface ChooserView extends BaseToolbarView, View {
    void showMessage(SystemMessage systemMessage);

    void initPicker();

    void restoreOtherSemester(String otherSemesterText, Semester otherSemesterTag);
}