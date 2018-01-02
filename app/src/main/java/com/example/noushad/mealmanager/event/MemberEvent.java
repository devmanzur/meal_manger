package com.example.noushad.mealmanager.event;

import com.example.noushad.mealmanager.model.Member;

/**
 * Created by noushad on 1/2/18.
 */

public class MemberEvent {
    private Member mMember;

    public MemberEvent(Member pMember) {
        mMember = pMember;
    }

    public Member getMember() {
        return mMember;
    }
}
