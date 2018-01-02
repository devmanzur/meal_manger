package com.example.noushad.mealmanager.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.example.noushad.mealmanager.database.MemberDB;
import com.example.noushad.mealmanager.event.MemberEvent;
import com.example.noushad.mealmanager.model.Member;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by noushad on 12/31/17.
 */

public class MemberListViewModel extends AndroidViewModel {

    private static final int ADD_TASK = 1;
    private static final int UPDATE_TASK = 2;
    private static final int DELETE_TASK = 3;
    private static final int CLEAR_TASK = 4;

    private final LiveData<List<Member>> members;

    private MemberDB appDatabase;


    public MemberListViewModel(@NonNull Application application) {
        super(application);
        appDatabase = MemberDB.getDatabase(this.getApplication());
        members = appDatabase.memberDAO().getAllMembers();

    }

    public LiveData<List<Member>> getMembers() {
        return this.members;
    }

    public void addItem(Member item) {
        new bgTask(appDatabase, ADD_TASK).execute(item);
    }

    public void updateItem(Member item) {
        new bgTask(appDatabase, UPDATE_TASK).execute(item);
    }

    public void deleteItem(Member item) {
        new bgTask(appDatabase, DELETE_TASK).execute(item);
    }

    public void deleteALL() {
        new bgTask(appDatabase, CLEAR_TASK).execute();
    }

    public void getMember(int pId) {
        new BackgrounTask(appDatabase).execute(pId);
    }

    private static class bgTask extends AsyncTask<Member, Void, Void> {

        private MemberDB db;
        private int command;

        bgTask(MemberDB appDatabase, int pCommand) {
            db = appDatabase;
            command = pCommand;
        }

        @Override
        protected Void doInBackground(Member... members) {
            switch (command) {
                case ADD_TASK:
                    db.memberDAO().addMember(members[0]);
                    break;
                case UPDATE_TASK:
                    db.memberDAO().updateMember(members[0]);
                    break;
                case DELETE_TASK:
                    db.memberDAO().deleteMember(members[0]);
                    break;
                case CLEAR_TASK:
                    db.memberDAO().clearAllMembers();
                    break;
            }
            return null;
        }
    }

    private static class BackgrounTask extends AsyncTask<Integer,Void,Member>{

        private MemberDB db;

        BackgrounTask(MemberDB appDatabase) {
            db = appDatabase;
        }


        @Override
        protected Member doInBackground(Integer... ids) {
            return db.memberDAO().getMemberById(ids[0]);
        }

        @Override
        protected void onPostExecute(Member pMember) {
            super.onPostExecute(pMember);
            EventBus.getDefault().post(new MemberEvent(pMember));
        }
    }
}
