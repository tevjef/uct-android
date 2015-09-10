package com.tevinjeffrey.rutgersct.ui.chooser;

import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.transition.ChangeBounds;
import android.transition.Fade;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;
import com.tevinjeffrey.rutgersct.R;
import com.tevinjeffrey.rutgersct.RutgersCTApp;
import com.tevinjeffrey.rutgersct.animator.EaseOutQuint;
import com.tevinjeffrey.rutgersct.rutgersapi.model.Request;
import com.tevinjeffrey.rutgersct.rutgersapi.model.SystemMessage;
import com.tevinjeffrey.rutgersct.rutgersapi.utils.SemesterUtils;
import com.tevinjeffrey.rutgersct.rutgersapi.utils.SemesterUtils.Semester;
import com.tevinjeffrey.rutgersct.ui.base.MVPFragment;
import com.tevinjeffrey.rutgersct.ui.subject.SubjectFragment;
import com.tevinjeffrey.rutgersct.utils.Utils;
import com.tevinjeffrey.stringpicker.StringPicker;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnLongClick;
import icepick.Icicle;

public class ChooserFragment extends MVPFragment implements ChooserView {

    @Bind(R.id.systemMessage)
    TextView mSystemMessage;

    @Bind(R.id.semester_radiogroup)
    RadioGroup mSemesterRadiogroup;

    @Bind(R.id.location1)
    CheckBox mLocation1;

    @Bind(R.id.location2)
    CheckBox mLocation2;

    @Bind(R.id.location3)
    CheckBox mLocation3;

    @Bind(R.id.level1)
    CheckBox mLevel1;

    @Bind(R.id.level2)
    CheckBox mLevel2;

    @Bind(R.id.search_btn)
    TextView mSearchButton;

    @Bind(R.id.primarySemester)
    RadioButton mPrimarySemester;

    @Bind(R.id.secondarySemester)
    RadioButton mSecondarySemester;

    @Bind(R.id.otherSemester)
    RadioButton mOtherSemester;

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Icicle
    ChooserViewState mViewState = new ChooserViewState();

    public ChooserFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        LayoutInflater themedInflator = inflater.cloneInContext(Utils.wrapContextTheme(getActivity(), R.style.RutgersCT));
        final View rootView = themedInflator.inflate(R.layout.fragment_chooser, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (mBasePresenter == null) {
            mBasePresenter = new ChooserPresenterImpl();
            getObjectGraph().inject(mBasePresenter);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewState.apply(this, savedInstanceState != null);
        //Attach view to presenter
        mBasePresenter.attachView(this);

        if (mIsInitialLoad) {
            mSemesterRadiogroup.clearCheck();
        }
        //Silently refresh tracked sections
        if (!getPresenter().isLoading()) {
            getPresenter().loadSystemMessage();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void initToolbar() {
        setToolbar(mToolbar);
    }

    @Override
    public void showMessage(SystemMessage systemMessage) {
        if (systemMessage.getMessageText().length() > 0) {
            mViewState.systemMessage = systemMessage;
            mSystemMessage.setText(Html.fromHtml(systemMessage.getMessageText()));
        }
    }

    @Override
    public void initPicker() {
        final SemesterUtils utils = new SemesterUtils(Calendar.getInstance());
        mPrimarySemester.setText(utils.getPrimarySemester());
        mPrimarySemester.setTag(utils.resolvePrimarySemester());

        mSecondarySemester.setText(utils.getSecondarySemester());
        mSecondarySemester.setTag(utils.resolveSecondarySemester());

        mOtherSemester.setOnClickListener(new OnOtherSemesterClick(utils));

        mSearchButton.setOnClickListener(new OnSearchButtonClick());
    }

    @Override
    public void restoreOtherSemester(String text, Semester tag) {
        mOtherSemester.setText(text);
        mOtherSemester.setTag(tag);
    }

    @OnLongClick(R.id.systemMessage)
    public boolean onSystemMessageLongClick(View view) {
        mSystemMessage.setVisibility(View.GONE);
        return true;
    }

    private void startSubjectFragment(Bundle b) {
        SubjectFragment sf = new SubjectFragment();
        sf.setArguments(b);
        FragmentTransaction ft = getFragmentManager().beginTransaction();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            sf.setExitTransition(new Fade(Fade.OUT).excludeTarget(ImageView.class, true).setDuration(100));
            sf.setReturnTransition(new Fade(Fade.OUT).excludeTarget(ImageView.class, true).setDuration(100));
            sf.setAllowReturnTransitionOverlap(false);
            sf.setAllowEnterTransitionOverlap(false);
            sf.setSharedElementEnterTransition(new ChangeBounds().setInterpolator(new DecelerateInterpolator()));
            sf.setSharedElementReturnTransition(new ChangeBounds().setInterpolator(new DecelerateInterpolator()));
            ft.addSharedElement(mToolbar, getString(R.string.transition_name_tool_background));
        } else {
            ft.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit);
        }
        startFragment(this, sf, ft);
    }

    private Bundle createArgs(Parcelable p) {
        Bundle args = new Bundle();
        args.putParcelable(RutgersCTApp.REQUEST, p);
        return args;
    }

    private ChooserPresenter getPresenter() {
        return (ChooserPresenter) mBasePresenter;
    }

    public static ChooserFragment newInstance() {
        ChooserFragment newInstance = new ChooserFragment();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            newInstance.setAllowEnterTransitionOverlap(false);
            newInstance.setAllowReturnTransitionOverlap(false);

            newInstance.setExitTransition(new Fade(Fade.OUT).setDuration(50).excludeTarget(ImageView.class, true));

            newInstance.setEnterTransition(new Fade(Fade.IN).excludeTarget(ImageView.class, true));
            newInstance.setReturnTransition(new Fade(Fade.OUT).excludeTarget(ImageView.class, true).setDuration(50));

            newInstance.setSharedElementEnterTransition(new ChangeBounds().setInterpolator(new EaseOutQuint()));
            newInstance.setSharedElementReturnTransition(new ChangeBounds().setInterpolator(new EaseOutQuint()));

        }

        return newInstance;
    }

    private class OnOtherSemesterClick implements View.OnClickListener {

        private final SemesterUtils su;
        String currentYear;
        String currentSeason;

        View pickerRoot;

        public OnOtherSemesterClick(SemesterUtils su) {
            this.su = su;
            currentYear = su.resolveCurrentSemester().getYear();
            currentSeason = su.resolveCurrentSemester().getSeason().toString();
        }

        @Override
        public void onClick(View v) {
            pickerRoot = createPicker(su);
            new MaterialDialog.Builder(getParentActivity())
                    .title("Choose a semester")
                    .customView(pickerRoot, false)
                    .positiveText("Done")
                    .negativeText("Cancel")
                    .dismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            if (mOtherSemester.getTag() == null) mSemesterRadiogroup.clearCheck();
                        }
                    })
                    .showListener(new DialogInterface.OnShowListener() {
                        @Override
                        public void onShow(DialogInterface dialog) {
                            Semester sm =
                                    mOtherSemester.getTag() == null ? su.resolveCurrentSemester() :
                                            (Semester) mOtherSemester.getTag();

                            currentSeason = sm.getSeason().toString();
                            currentYear = sm.getYear();

                            StringPicker yearPicker = (StringPicker) pickerRoot.findViewById(R.id.yearPicker);
                            StringPicker seasonPicker = (StringPicker) pickerRoot.findViewById(R.id.seasonPicker);

                            yearPicker.setCurrent(su.getListOfYears().indexOf(currentYear));
                            seasonPicker.setCurrent(sm.getSeason().ordinal());

                        }

                    })
                    .callback(new MaterialDialog.ButtonCallback() {
                        @Override
                        public void onPositive(MaterialDialog dialog) {
                            StringPicker yearPicker = (StringPicker) pickerRoot.findViewById(R.id.yearPicker);
                            StringPicker seasonPicker = (StringPicker) pickerRoot.findViewById(R.id.seasonPicker);
                            currentYear = yearPicker.getCurrentValue();
                            currentSeason = seasonPicker.getCurrentValue();
                            Semester semester = new Semester(currentSeason, currentYear);

                            mViewState.otherSemesterText = semester.toString();
                            mViewState.otherSemesterTag = semester;

                            mOtherSemester.setText(semester.toString());
                            mOtherSemester.setTag(semester);

                            super.onPositive(dialog);
                        }
                    })
                    .show();
        }

        private View createPicker(SemesterUtils su) {
            final LinearLayout pickerRoot = (LinearLayout) getParentActivity().getLayoutInflater().inflate(R.layout.picker, null);

            final StringPicker seasonPicker = (StringPicker) pickerRoot.findViewById(R.id.seasonPicker);
            seasonPicker.setValues(su.getListOfSeasons());

            final StringPicker yearPicker = (StringPicker) pickerRoot.findViewById(R.id.yearPicker);
            yearPicker.setValues(su.getListOfYears());

            return pickerRoot;
        }

        @Override
        public String toString() {
            return "OnOtherSemesterClick{" +
                    "su=" + su +
                    ", currentYear='" + currentYear + '\'' +
                    ", currentSeason='" + currentSeason + '\'' +
                    ", pickerRoot=" + pickerRoot +
                    '}';
        }
    }

    private class OnSearchButtonClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (isValidInputs()) {
                Answers.getInstance().logCustom(new CustomEvent("Search")
                        .putCustomAttribute("Location", Request.toStringList(getLocations()))
                        .putCustomAttribute("Level", Request.toStringList(getLevels()))
                        .putCustomAttribute("Semester", getSemester().toString()));

                startSubjectFragment(createArgs(createRequest()));
            }
        }

        public Parcelable createRequest() {
            return new Request(null, getSemester(), getLocations(), getLevels());
        }



        private boolean isValidInputs() {
            int checkedButton = mSemesterRadiogroup.getCheckedRadioButtonId();
            RadioButton selectedButton = (RadioButton) getParentActivity().findViewById(checkedButton);
            if (selectedButton == null) {
                makeToast(getParentActivity().getString(R.string.select_a_semester));
                return false;
            } else if (!mLocation1.isChecked() && !mLocation2.isChecked() && !mLocation3.isChecked()) {
                makeToast(getParentActivity().getString(R.string.select_a_location));
                return false;
            } else if (!mLevel1.isChecked() && !mLevel2.isChecked()) {
                makeToast(getParentActivity().getString(R.string.select_a_level));
                return false;
            } else {
                return true;
            }
        }

        private void makeToast(CharSequence s) {
            Toast.makeText(getParentActivity().getApplicationContext(), s, Toast.LENGTH_LONG).show();
        }

        private Semester getSemester() {
            int checkedButton = mSemesterRadiogroup.getCheckedRadioButtonId();
            RadioButton selectedButton = (RadioButton) getParentActivity().findViewById(checkedButton);
            return (Semester) selectedButton.getTag();
        }

        private ArrayList<String> getLocations() {
            ArrayList<String> locations = new ArrayList<>();
            if (mLocation1.isChecked()) {
                locations.add(mLocation1.getText().toString());
            }
            if (mLocation2.isChecked()) {
                locations.add(mLocation2.getText().toString());
            }
            if (mLocation3.isChecked()) {
                locations.add(mLocation3.getText().toString());
            }

            return locations;
        }

        private ArrayList<String> getLevels() {
            ArrayList<String> levels = new ArrayList<>();
            if (mLevel1.isChecked()) {
                levels.add(mLevel1.getText().toString());
            }
            if (mLevel2.isChecked()) {
                levels.add(mLevel2.getText().toString());
            }

            return levels;
        }
    }
}