package com.example.noushad.mealmanager.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.noushad.mealmanager.R;
import com.example.noushad.mealmanager.activity.MainActivity;
import com.example.noushad.mealmanager.model.Member;
import com.example.noushad.mealmanager.utility.SharedPrefManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by noushad on 12/30/17.
 */

public class MembersAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private List<Member> mItems;

    public MembersAdapter(Context mContext) {
        this.mContext = mContext;
        this.mItems = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View viewItem = inflater.inflate(R.layout.list_item, parent, false);
        viewHolder = new MembersVH(viewItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Member member = mItems.get(position);
        ((MembersVH) holder).updateUI(member);
    }

    @Override
    public int getItemCount() {
        return mItems == null ? 0 : mItems.size();
    }

    private class MembersVH extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        private TextView firstLetter;
        private TextView nameText;
        private TextView mealCountText;
        private TextView totalSpentText;
        private TextView statusText;
        private ImageView indicator;
        private Member mMember;
        private ImageButton editButton;

        public MembersVH(View v) {
            super(v);
            firstLetter = v.findViewById(R.id.member_first_word);
            nameText = v.findViewById(R.id.member_name_text);
            mealCountText = v.findViewById(R.id.member_meal_text);
            totalSpentText = v.findViewById(R.id.member_money_spent);
            statusText = v.findViewById(R.id.member_finance_status);
            indicator = v.findViewById(R.id.status_indicator);
            editButton = v.findViewById(R.id.edit_member_button);
            v.setOnClickListener(this);
            v.setOnLongClickListener(this);
        }

        public void updateUI(final Member member) {
            mMember = member;
            firstLetter.setText(String.valueOf(member.getName().toUpperCase().charAt(0)));
            nameText.setText(member.getName());
            mealCountText.setText(String.valueOf(member.getTotalMeal()));
            totalSpentText.setText(String.valueOf(member.getTotalMoneySpent()));

            int spent = member.getTotalMoneySpent();
            double mealPrice = SharedPrefManager.getInstance(mContext).getCurrentMealPrice();
            double cost = member.getTotalMeal() * mealPrice;

            if (spent > cost) {
                indicator.setImageResource(R.drawable.ic_positive_indicator);
                statusText.setTextColor(mContext.getResources().getColor(R.color.colorPos));
                statusText.setText(String.valueOf(Math.round(spent - cost)));
            } else if (spent < cost) {
                indicator.setImageResource(R.drawable.ic_negative_indicator);
                statusText.setTextColor(mContext.getResources().getColor(R.color.colorNeg));
                statusText.setText(String.valueOf(Math.round(cost - spent)));
            } else {
                indicator.setImageResource(R.drawable.ic_equal_indicator);
                statusText.setTextColor(mContext.getResources().getColor(R.color.colorAccent));
                statusText.setText("0");
            }
            editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showLongClickDialog(member);
                }
            });

        }

        @Override
        public void onClick(View v) {
            showClickDialog(mMember);
        }

        private void showClickDialog(final Member pMember) {
            final AlertDialog.Builder builder;
            builder = new AlertDialog.Builder(mContext);
            final AlertDialog dialog = builder.create();
            dialog.setTitle(mMember.getName().toUpperCase());

            View viewInflated = LayoutInflater.from(mContext).inflate(R.layout.onclick_options, null, false);
            FloatingActionButton fabMeal = viewInflated.findViewById(R.id.fab_meal);
            FloatingActionButton fabExpense = viewInflated.findViewById(R.id.fab_expense);
            FloatingActionButton fabInfo = viewInflated.findViewById(R.id.fab_info);
            final ConstraintLayout mlExLayout = viewInflated.findViewById(R.id.input_container);
            final TextView hint = viewInflated.findViewById(R.id.hint_text);
            final ConstraintLayout optionLayout = viewInflated.findViewById(R.id.options_container);
            final EditText mlExInput = viewInflated.findViewById(R.id.ml_ex_input);
            final Button mlExButton = viewInflated.findViewById(R.id.ml_ex_button);

            fabMeal.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    optionLayout.setVisibility(View.GONE);
                    mlExLayout.setVisibility(View.VISIBLE);
                    hint.setText(R.string.today_meals);
                    mlExButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                float value = Float.parseFloat(mlExInput.getText().toString());
                                pMember.addMeal(value);
                                ((MainActivity) mContext).dbUpdateMember(pMember);
                                SharedPrefManager.getInstance(mContext).setTotalMeals(value, 0);
                                notifyDataSetChanged();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            dialog.dismiss();
                        }
                    });

                }
            });
            fabExpense.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    optionLayout.setVisibility(View.GONE);
                    mlExLayout.setVisibility(View.VISIBLE);
                    hint.setText(R.string.today_expense);
                    mlExButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                if (!mlExInput.getText().toString().matches("")) {
                                    int value = Integer.parseInt(mlExInput.getText().toString());
                                    pMember.addTotalMoney(value);
                                    ((MainActivity) mContext).dbUpdateMember(pMember);

//this line below needs to move somewhere else and used via a different button.

                                    // SharedPrefManager.getInstance(mContext).setTotalExpense(value, 0);
                                    notifyDataSetChanged();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            dialog.dismiss();
                        }
                    });

                }
            });
            fabInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainActivity) mContext).startDetailFragment(pMember.getId());
                    dialog.dismiss();
                }
            });


            dialog.setView(viewInflated);


            dialog.show();
        }

        @Override
        public boolean onLongClick(View v) {
            showLongClickDialog(mMember);
            return true;
        }

        private void showLongClickDialog(final Member pMember) {
            final AlertDialog.Builder builder;
            builder = new AlertDialog.Builder(mContext);
            final AlertDialog dialog = builder.create();
            dialog.setTitle(mMember.getName().toUpperCase());

            View viewInflated = LayoutInflater.from(mContext).inflate(R.layout.onlongclick_options, null, false);
            FloatingActionButton fabEdit = viewInflated.findViewById(R.id.fab_edit_user);
            FloatingActionButton fabDelete = viewInflated.findViewById(R.id.fab_user_delete);
            final ConstraintLayout optionView = viewInflated.findViewById(R.id.long_options_container);
            final ConstraintLayout inputView = viewInflated.findViewById(R.id.long_input_container);
            final EditText infoUpdateInput = viewInflated.findViewById(R.id.info_update_input);
            final Button infoUpdateButton = viewInflated.findViewById(R.id.info_update_button);
            final TextView hint = viewInflated.findViewById(R.id.info_hint);
            fabEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //update user
                    optionView.setVisibility(View.GONE);
                    inputView.setVisibility(View.VISIBLE);
                    hint.setText(R.string.name);
                    infoUpdateButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {

                                String name = infoUpdateInput.getText().toString();
                                if (!name.matches("")) {
                                    pMember.setName(name);
                                    ((MainActivity) mContext).dbUpdateMember(pMember);
                                    notifyDataSetChanged();
                                } else {
                                    Toast.makeText(mContext, "No Name Entered", Toast.LENGTH_SHORT).show();
                                }

                            } catch (Exception e) {

                            }
                            dialog.dismiss();
                        }
                    });
                }
            });

            fabDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //delete user
                    showDeleteDialog();
                    dialog.dismiss();
                }
            });
            dialog.setView(viewInflated);
            dialog.show();
        }

        private void showDeleteDialog() {
            final AlertDialog.Builder builder;
            builder = new AlertDialog.Builder(mContext);
            final AlertDialog dialog = builder.create();
            dialog.setTitle(mContext.getString(R.string.remove_user));
            dialog.setMessage(mContext.getString(R.string.removing_confirm) + mMember.getName());
            dialog.setButton(DialogInterface.BUTTON_POSITIVE, mContext.getString(R.string.ok), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ((MainActivity) mContext).dbDeleteMember(mMember);
                    notifyDataSetChanged();
                    dialog.dismiss();
                }
            });

            dialog.setButton(DialogInterface.BUTTON_NEGATIVE, mContext.getString(R.string.cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        }
    }

    public void add(Member member) {
        mItems.add(member);
        notifyDataSetChanged();
    }

    public void addItems(List<Member> members) {
        this.mItems = members;
        notifyDataSetChanged();
    }
}
