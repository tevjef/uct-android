package com.tevinjeffrey.rutgersct.ui.chooser;

import android.app.FragmentTransaction;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.Toolbar;
import android.transition.ChangeBounds;
import android.transition.Fade;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.tevinjeffrey.rutgersct.R;
import com.tevinjeffrey.rutgersct.RutgersCTApp;
import com.tevinjeffrey.rutgersct.data.uctapi.model.Semester;
import com.tevinjeffrey.rutgersct.data.uctapi.model.University;
import com.tevinjeffrey.rutgersct.data.uctapi.search.SearchManager;
import com.tevinjeffrey.rutgersct.ui.base.MVPFragment;
import com.tevinjeffrey.rutgersct.ui.subject.SubjectFragment;
import com.tevinjeffrey.rutgersct.utils.Utils;

import org.apache.commons.lang3.text.WordUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import icepick.State;
import timber.log.Timber;

public class ChooserFragment extends MVPFragment implements ChooserView {

    @Bind(R.id.semester_radiogroup)
    RadioGroup mSemesterRadiogroup;

    @Bind(R.id.search_btn)
    TextView mSearchButton;

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @State
    ChooserViewState mViewState = new ChooserViewState();

    @Bind(R.id.university_spinner)
    Spinner universitySpinner;

    @Inject
    SearchManager searchManager;

    List<University> universities;
    List<Semester> semesters;

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
            RutgersCTApp.getObjectGraph(getParentActivity()).inject(mBasePresenter);
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
            ft.setCustomAnimations(R.animator.enter, R.animator.exit, R.animator.pop_enter, R.animator.pop_exit);
        }
        startFragment(this, sf, ft);
    }

    private Bundle createArgs(Parcelable p) {
        Bundle args = new Bundle();
        return args;
    }

    private ChooserPresenter getPresenter() {
        return (ChooserPresenter) mBasePresenter;
    }

    public static ChooserFragment newInstance() {
        ChooserFragment newInstance = new ChooserFragment();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            newInstance.setExitTransition(new Fade(Fade.OUT).setDuration(50).excludeTarget(ImageView.class, true));

            newInstance.setEnterTransition(new Fade(Fade.IN));
            newInstance.setReturnTransition(new Fade(Fade.OUT).excludeTarget(ImageView.class, true).setDuration(50));

            newInstance.setSharedElementEnterTransition(new ChangeBounds().setInterpolator(new DecelerateInterpolator()));
            newInstance.setSharedElementReturnTransition(new ChangeBounds().setInterpolator(new DecelerateInterpolator()));

        }

        return newInstance;
    }

    @Override
    public void initSpinner() {
        mSearchButton.setOnClickListener(new OnSearchButtonClick());

        universitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getPresenter().updateDefaultUniversity(universities.get(position));
                getPresenter().loadAvailableSemesters(universities.get(position).topic_name);
                mSemesterRadiogroup.clearCheck();
            }

            @Override public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        getPresenter().loadUniversities();
    }

    @Override
    public void setUniversities(List<University> universities) {
        this.universities = universities;
        List<String> universityString = new ArrayList<>();
        for (University uni: universities) {
            universityString.add(uni.name);
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getParentActivity(), R.layout.spinner_item, universityString);
        arrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown);
        universitySpinner.setAdapter(arrayAdapter);
    }

    @Override
    public void setAvailableSemesters(List<Semester> semesters) {
        this.semesters = semesters;
        mSearchButton.setEnabled(true);

        mSemesterRadiogroup.removeAllViewsInLayout();
        for (Semester semester : semesters) {
            RadioButton radioButton = (RadioButton) LayoutInflater.from(this.getParentActivity()).inflate(R.layout.radio_button, null);
            radioButton.setText(WordUtils.capitalize(semester.season) + " " + semester.year);
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            mSemesterRadiogroup.addView(radioButton, layoutParams);
        }
    }


    @Override
    public void injectTargets() {
        RutgersCTApp.getObjectGraph(getParentActivity()).inject(this);
    }


    private class OnSearchButtonClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            int rawIndex = mSemesterRadiogroup.getCheckedRadioButtonId() - 1;
            int selectedIndex = rawIndex % semesters.size();

            if (selectedIndex < 0) {
                Toast.makeText(getParentActivity(), "Please select a term", Toast.LENGTH_SHORT).show();
                return;
            }

            searchManager.newSearch();
            Semester selectedSemester = semesters.get(selectedIndex);
            University selectedUniversity = getPresenter().getDefaultUniversity();

            searchManager.getSearchFlow().university = selectedUniversity;
            searchManager.getSearchFlow().semester = selectedSemester;

            Timber.d(searchManager.getSearchFlow().toString());
            Timber.i("rawindex=%s selectedIndex=%s", rawIndex, selectedIndex);
        }
    }
}