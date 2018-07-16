package com.zoe.custom;

import android.app.ActivityManager;
import android.content.Context;

public class PdaHelper {
    private static PdaHelper sInstance;
    private Context mCon;

    public static PdaHelper getInstance(Context mAct) {
        if (sInstance == null) {
            sInstance = new PdaHelper(mAct);
        }
        return sInstance;
    }

    public PdaHelper(Context mCon) {
        this.mCon = mCon;
    }

    public void closeApp() {
        ActivityManager am = (ActivityManager) mCon
                .getSystemService(Context.ACTIVITY_SERVICE);
        am.restartPackage(mCon.getPackageName());
    }

    /** Close the update version task and exit the application. */
    public static void closeApplication() {
		/*UpdateAppVersionTask mUpdateTask = ObjectPool.mApplication
				.getWelcomeActivity().getUpdateVerTask();
		if (mUpdateTask != null) {
			mUpdateTask.stop();
		}
		//NetThread.closeTask(mUpdateTask);
		ObjectPool.mApplication.setUserDto(null);
		XmlDB.getInstance(ObjectPool.mApplication).setUpdateVerFlag(
				StringPool.UPDATE_VER_NORMAL);
		for (BaseActivity wa : MCommApplication.allActivity) {
			wa.finish();
		}*/
        System.exit(0);
        android.os.Process.killProcess(android.os.Process.myPid());
    }

}

