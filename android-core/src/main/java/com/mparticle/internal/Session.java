package com.mparticle.internal;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class Session {
    public int mEventCount = 0;
    public String mSessionID = Constants.NO_SESSION_ID;
    public long mSessionStartTime = 0;
    public long mLastEventTime = 0;
    private long mTimeInBackground = 0;
    public JSONObject mSessionAttributes = new JSONObject();
    private Set<Long> mpids = new TreeSet<Long>();

    public Session() {
        super();
    }


    public Session(Session session) {
        super();
        mEventCount = session.mEventCount;
        mSessionID = session.mSessionID;
        mSessionStartTime = session.mSessionStartTime;
        mLastEventTime = session.mLastEventTime;
        mTimeInBackground = session.mTimeInBackground;
        try {
            mSessionAttributes = new JSONObject(session.mSessionAttributes.toString());
        }catch (JSONException jse){

        }
    }

    public boolean isActive() {
        return mSessionStartTime > 0 && !Constants.NO_SESSION_ID.equals(mSessionID);
    }

    public Session start(Context context) {
        mLastEventTime = mSessionStartTime = System.currentTimeMillis();
        mSessionID = UUID.randomUUID().toString();
        mSessionAttributes = new JSONObject();
        mEventCount = 0;
        mTimeInBackground = 0;
        addMpid(ConfigManager.getMpid(context));
        return this;
    }

    public Boolean checkEventLimit() {
        if (mEventCount < Constants.EVENT_LIMIT) {
            mEventCount++;
            return true;
        } else {
            Logger.warning("The event limit has been exceeded for this session.");
            return false;
        }
    }

    public boolean isTimedOut(int sessionTimeout) {
        return sessionTimeout < (System.currentTimeMillis() - mLastEventTime);
    }

    public long getLength() {
        long time = mLastEventTime - mSessionStartTime;
        if (time >= 0){
            return time;
        }
        return time;
    }

    public long getBackgroundTime() {
        return mTimeInBackground;
    }

    public long getForegroundTime() {
        return getLength() - getBackgroundTime();
    }

    public void updateBackgroundTime(AtomicLong lastStoppedTime, long currentTime) {
        long time = lastStoppedTime.get();
        mTimeInBackground += (currentTime - time);
    }

    public void addMpid(long newMpid) {
        if (newMpid != Constants.TEMPORARY_MPID) {
            mpids.add(newMpid);
        }
    }

    public Set<Long> getMpids() {
        return mpids;
    }
}
