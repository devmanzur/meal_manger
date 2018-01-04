package com.example.noushad.mealmanager.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.noushad.mealmanager.R;
import com.example.noushad.mealmanager.adapter.BazarAdapter;
import com.example.noushad.mealmanager.adapter.MealAdapter;
import com.example.noushad.mealmanager.event.MemberEvent;
import com.example.noushad.mealmanager.model.Member;
import com.example.noushad.mealmanager.viewmodel.MemberListViewModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DateFormat;
import java.util.Locale;

public class InforamtionFragment extends Fragment {

    private static final String ARG_ID = "com.example.noushad.mealmanager.member_id";

    private int mId;
    private MemberListViewModel mViewModel;
    private Member mMember;
    private TextView memberNameText;
    private TextView memberHeaderText;
    private TextView memberMealText;
    private TextView memberExpenseText;
    private TextView memberCreateDateText;
    private TextView memberLastBazarDateText;
    private RecyclerView bazarList;
    private RecyclerView mealList;
    private MealAdapter mMealAdapter;
    private BazarAdapter mBazarAdapter;

    public InforamtionFragment() {
        // Required empty public constructor
    }

    public static InforamtionFragment newInstance(int id) {
        InforamtionFragment fragment = new InforamtionFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_ID, id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mId = getArguments().getInt(ARG_ID);
            mViewModel = ViewModelProviders.of(this).get(MemberListViewModel.class);
            mViewModel.getMember(mId);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inforamtion, container, false);
        initialize(view);
        return view;
    }

    private void initialize(View v) {
        memberNameText = v.findViewById(R.id.member_info_name);
        memberHeaderText = v.findViewById(R.id.member_info_first_word);
        memberMealText = v.findViewById(R.id.member_info_totalmeal);
        memberExpenseText = v.findViewById(R.id.member_info_totaspent);
        memberCreateDateText = v.findViewById(R.id.member_info_creation_date);
        memberLastBazarDateText = v.findViewById(R.id.member_info_last_bazar);

        mealList = v.findViewById(R.id.meal_list);
        mealList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mealList.setItemAnimator(new DefaultItemAnimator());

        bazarList = v.findViewById(R.id.bazar_list);
        bazarList.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        bazarList.setItemAnimator(new DefaultItemAnimator());



    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMemberEvent(MemberEvent event) {
        updateUI(event.getMember());
    }

    private void updateUI(Member pMember) {
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.UK);

        memberNameText.setText(pMember.getName());
        memberHeaderText.setText(String.valueOf(pMember.getName().charAt(0)));
        memberMealText.setText(String.valueOf(pMember.getTotalMeal()));
        memberExpenseText.setText(String.valueOf(pMember.getTotalMoneySpent()));
        memberCreateDateText.setText(dateFormat.format(pMember.getCreationDate()));
        try {
            memberLastBazarDateText.setText(dateFormat.format(pMember.getLastBazarDate()));
        }catch (Exception e){

        }
        mMealAdapter = new MealAdapter(getContext(), pMember.getMeals());
        mealList.setAdapter(mMealAdapter);

        mBazarAdapter = new BazarAdapter(getContext(), pMember.getBazars());
        bazarList.setAdapter(mBazarAdapter);



    }

}
