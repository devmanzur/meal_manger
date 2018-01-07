package com.example.noushad.mealmanager.activity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
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
import com.example.noushad.mealmanager.event.DataUploadedEvent;
import com.example.noushad.mealmanager.event.ErrorEvent;
import com.example.noushad.mealmanager.event.UpdateEvent;
import com.example.noushad.mealmanager.fragment.DashboardFragment;
import com.example.noushad.mealmanager.fragment.InforamtionFragment;
import com.example.noushad.mealmanager.model.Member;
import com.example.noushad.mealmanager.utility.FirebaseService;
import com.example.noushad.mealmanager.utility.SharedPrefManager;
import com.example.noushad.mealmanager.utility.TagManager;
import com.example.noushad.mealmanager.viewmodel.MemberListViewModel;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements DashboardFragment.OnFragmentInteractionListener {

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
    private ConstraintLayout MainContainer;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    return true;
                case R.id.navigation_dashboard:
                    startFragment(DashboardFragment.newInstance(), TagManager.DASHBOARD_FRAGMENT);
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
    private List<Member> mMembersList;
    FirebaseService mService;

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
        MainContainer = findViewById(R.id.main_container);
        mService = new FirebaseService();
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
                showSnack("Current Price Per Meal : " + String.valueOf(Math.round(currentPrice)));
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
                showSnack("Total Meals : " + String.valueOf(Math.round(meals)));
            }
        });

        fabExpenseInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSnack("Total Expense : " + String.valueOf(expense));
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

    private void showSnack(String text) {

        Snackbar.make(this.findViewById(R.id.container), text, Snackbar.LENGTH_LONG)
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
                mMembersList = pMembers;
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
                currentPrice = 0;
                expense = 0;
                meals = 0;
                mMealPrice.setText("0");
                mTotalMeals.setText("0");
                mTotalExpense.setText("0");
                break;

        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onUploadEvent(DataUploadedEvent event) {
        showSnack("Data Uploaded Successfully");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onErrorEvent(ErrorEvent event) {
        showSnack(event.getErrorMessage());
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
        MainContainer.setVisibility(View.GONE);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment, tag).addToBackStack(null);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        fragmentTransaction.commit();

    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
                MainContainer.setVisibility(View.VISIBLE);
            }
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

    @Override
    public void onDashboardInteraction(int command) {
        switch (command) {
            case TagManager.UPLOAD_TASK:
                getLastUpdate(1);

                break;
            case TagManager.DOWNLOAD_TASK:
                getLastUpdate(2);

                break;
        }
    }

    private void getLastUpdate(final int mode) {
        String id = SharedPrefManager.getInstance(this).getUserId();
        DatabaseReference database = FirebaseDatabase.getInstance().getReference();
        database.child("users").child(id).child("last_updated").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    Date date = dataSnapshot.getValue(Date.class);
                    DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.UK);
                    String dateString = String.valueOf(dateFormat.format(date));
                    if(mode==2) {
                        new AlertDialog.Builder(MainActivity.this)
                                .setMessage("You are adding data from " + dateString)
                                .setPositiveButton("CONFIRM", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        downloadInfoFromDB();
                                    }
                                })
                                .setNegativeButton("CANCEL", null)
                                .show();
                    }else{
                        new AlertDialog.Builder(MainActivity.this)
                                .setMessage("You are replacing data from " + dateString)
                                .setPositiveButton("CONFIRM", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        uploadInfoToDB();
                                    }
                                })
                                .setNegativeButton("CANCEL", null)
                                .show();

                    }
                } catch (Exception e) {

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void downloadInfoFromDB() {

        try {
            String id = SharedPrefManager.getInstance(this).getUserId();
            DatabaseReference database = FirebaseDatabase.getInstance().getReference();
            database.child("users").child(id).child("expense").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    try {
                        expense = dataSnapshot.getValue(Integer.class);
                        SharedPrefManager.getInstance(MainActivity.this).setTotalExpense(expense, 101);
                    } catch (Exception e) {

                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            database.child("users").child(id).child("meals").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    try {
                        meals = dataSnapshot.getValue(Float.class);
                        SharedPrefManager.getInstance(MainActivity.this).setTotalMeals(meals, 101);

                    } catch (Exception e) {

                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    showSnack(databaseError.getMessage());
                }
            });
            database.child("users").child(id).child("current_price").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    try {
                        currentPrice = dataSnapshot.getValue(Float.class);
                        SharedPrefManager.getInstance(MainActivity.this).setMealPrice(currentPrice);

                    } catch (Exception e) {

                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            Query query = database.child("users").child(id).child("members");
            makeQuery(query);

            database.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    showSnack("Database has been updated");
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } catch (Exception e) {

        }

    }

    private void makeQuery(Query query) {

        try {
            query.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Member member = dataSnapshot.getValue(Member.class);
                    dbAddMember(member);
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        } catch (Exception e) {

        }

    }

    private void uploadInfoToDB() {
        String id = SharedPrefManager.getInstance(this).getUserId();
        mService.uploadLocalDB(id, expense, meals, currentPrice, mMembersList);
    }
}
