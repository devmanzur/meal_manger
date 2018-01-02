package com.example.noushad.mealmanager.activity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.example.noushad.mealmanager.R;
import com.example.noushad.mealmanager.adapter.MembersAdapter;
import com.example.noushad.mealmanager.event.UpdateEvent;
import com.example.noushad.mealmanager.fragment.InforamtionFragment;
import com.example.noushad.mealmanager.model.Member;
import com.example.noushad.mealmanager.utility.SharedPrefManager;
import com.example.noushad.mealmanager.utility.TagManager;
import com.example.noushad.mealmanager.viewmodel.MemberListViewModel;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private MemberListViewModel mViewModel;
    private TextView mTotalExpense;
    private TextView mTotalMeals;
    private TextView mMealPrice;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private MembersAdapter mAdapter;
    private TextView currentMealPrice;
    private FloatingActionButton fab;
    private FloatingActionButton fabMealInfo;
    private FloatingActionButton fabExpenseInfo;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    return true;
                case R.id.navigation_dashboard:
//                    clearData();
                    return true;
                case R.id.navigation_notifications:
                    return true;
            }
            return false;
        }
    };
    private int expense;
    private float meals;
    private float currentPrice;

    private void clearData() {
        SharedPrefManager.getInstance(MainActivity.this).clear();
        EventBus.getDefault().post(new UpdateEvent(0, "EMPTY"));
        mViewModel.deleteALL();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFullScreen();
        setContentView(R.layout.activity_main);
        initializeViews();
        mViewModel = ViewModelProviders.of(this).get(MemberListViewModel.class);
        showDetails();
    }

    private void setUpPieChart(List<Member> members) {

        List<Integer> colors = getColors();


        List<PieEntry> pieEntries = new ArrayList<>();
        for (Member member : members) {
            if (member.getTotalMeal() > 0)
                pieEntries.add(new PieEntry((member.getTotalMoneySpent() / member.getTotalMeal()), member.getName()));
        }

        PieDataSet pieDataSet = new PieDataSet(pieEntries, "");
        pieDataSet.setColors(colors);
        PieData data = new PieData(pieDataSet);

        PieChart chart = findViewById(R.id.contribution_chart);
        chart.setData(data);
        chart.setUsePercentValues(true);
        chart.animateY(1000);
        chart.invalidate();

    }

    @NonNull
    private List<Integer> getColors() {
        List<Integer> colors = new ArrayList<>();
        colors.add(getResources().getColor(R.color.colorAccent));
        colors.add(getResources().getColor(R.color.colorBlu));
        colors.add(getResources().getColor(R.color.colorBlu1));
        colors.add(getResources().getColor(R.color.colorBlu2));
        colors.add(getResources().getColor(R.color.colorBlu4));
        colors.add(getResources().getColor(R.color.colorBlu));
        colors.add(getResources().getColor(R.color.colorBlu1));
        colors.add(getResources().getColor(R.color.colorBlu2));
        colors.add(getResources().getColor(R.color.colorBlu4));
        colors.add(getResources().getColor(R.color.colorBlu));
        colors.add(getResources().getColor(R.color.colorBlu1));
        colors.add(getResources().getColor(R.color.colorBlu2));
        colors.add(getResources().getColor(R.color.colorBlu4));
        colors.add(getResources().getColor(R.color.colorBlu));
        colors.add(getResources().getColor(R.color.colorBlu1));
        colors.add(getResources().getColor(R.color.colorBlu2));
        colors.add(getResources().getColor(R.color.colorBlu4));
        colors.add(getResources().getColor(R.color.colorAccent));
        colors.add(getResources().getColor(R.color.colorBlu));
        colors.add(getResources().getColor(R.color.colorBlu1));
        colors.add(getResources().getColor(R.color.colorBlu2));
        colors.add(getResources().getColor(R.color.colorBlu4));
        return colors;
    }

    private void showDetails() {
        expense = SharedPrefManager.getInstance(this).getTotalExpense();
        meals = SharedPrefManager.getInstance(this).getTotalMeals();
        currentPrice = SharedPrefManager.getInstance(this).getCurrentMealPrice();
        mTotalExpense.setText(String.valueOf(Math.round(expense)));
        mTotalMeals.setText(String.valueOf(Math.round(meals)));
        mMealPrice.setText(String.valueOf(Math.round(currentPrice)));
    }

    private void setFullScreen() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    private void initializeViews() {
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        mRecyclerView = findViewById(R.id.members_list);
        mLinearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        fab = findViewById(R.id.add_new_fab);
        mTotalExpense = findViewById(R.id.total_expense);
        mTotalMeals = findViewById(R.id.total_meal);
        mMealPrice = findViewById(R.id.current_price);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddNewDialog();
            }
        });
        fabMealInfo = findViewById(R.id.floatingActionButton2);
        fabExpenseInfo = findViewById(R.id.floatingActionButton);
        currentMealPrice = findViewById(R.id.current_price);

        currentMealPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSnack("Current Price Per Meal : ", Math.round(currentPrice));
            }
        });

        currentMealPrice.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showClearDialog();
                return true;
            }
        });


        fabMealInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSnack("Total Meals : ", Math.round(meals));
            }
        });

        fabExpenseInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSnack("Total Expense : ", expense);
            }
        });
    }

    private void showClearDialog() {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to ERASE all Data?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        clearData();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void showSnack(String tag, int count) {

        Snackbar.make(this.findViewById(R.id.container), tag + String.valueOf(count), Snackbar.LENGTH_LONG)
                .setAction("CLOSE", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                })
                .setActionTextColor(getResources().getColor(android.R.color.holo_blue_light))
                .show();

    }

    private void showAddNewDialog() {
        AlertDialog.Builder builder;

        builder = new AlertDialog.Builder(this);
        builder.setTitle("New Member");

        View viewInflated = LayoutInflater.from(this).inflate(R.layout.add_new_member, null, false);
        final EditText input = (EditText) viewInflated.findViewById(R.id.name_input);
        builder.setView(viewInflated);

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = input.getText().toString();
                Member member = new Member(name);
                mAdapter.add(member);
                dbAddMember(member);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUI();
        mViewModel.getMembers().observe(MainActivity.this, new Observer<List<Member>>() {
            @Override
            public void onChanged(@Nullable List<Member> pMembers) {
                mAdapter.addItems(pMembers);
                setUpPieChart(pMembers);
            }
        });
    }

    private void updateUI() {
        if (mAdapter == null) {
            mAdapter = new MembersAdapter(this);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mRecyclerView.setAdapter(mAdapter);
        }

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
    public void onUpdateEvent(UpdateEvent event) {
        mAdapter.notifyDataSetChanged();

        switch (event.getTag()) {
            case "Expense":
                expense = Math.round(event.getTotal());
                mTotalExpense.setText(String.valueOf(Math.round(event.getTotal())));
                break;
            case "Meal":
                meals = Math.round(event.getTotal());
                mTotalMeals.setText(String.valueOf(Math.round(event.getTotal())));
                break;
            case "Price":
                currentPrice = Math.round(event.getTotal());
                mMealPrice.setText(String.valueOf(Math.round(event.getTotal())));
                break;
            case "EMPTY":
                mMealPrice.setText("0");
                mTotalMeals.setText("0");
                mTotalExpense.setText("0");
                break;

        }
    }

    public void dbAddMember(Member pMember) {
        mViewModel.addItem(pMember);
    }

    public void dbUpdateMember(Member pMember) {
        mViewModel.updateItem(pMember);
    }

    public void dbDeleteMember(Member pMember) {
        mViewModel.deleteItem(pMember);
    }

    public void startDetailFragment(int pId) {
        startFragment(InforamtionFragment.newInstance(pId), TagManager.INFO_FRAGMENT);
    }

    public void startFragment(Fragment fragment, String tag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment, tag).addToBackStack(null);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            new AlertDialog.Builder(this)
                    .setMessage("Are you sure you want to exit?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            MainActivity.super.onBackPressed();
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        }
    }
}
