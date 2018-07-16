package com.zoe.custom;

import android.app.*;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.provider.Settings;
import android.view.KeyEvent;

import com.zoe.oa.aoge_android.R;

public class DialogManager {

    /**
     * Holds the single instance of a DialogManager that is shared by the process.
     */
    private static DialogManager sInstance;
    private static Dialog loadingDialog ;

    /**
     * Return the single instance of a DialogManager that is shared by the process.
     */
    public static DialogManager getInstance() {
        if (sInstance == null) {
            sInstance = new DialogManager();
        }
        return sInstance;
    }


    /** Show the network settings page and close the application process id. */
    public void showNetMsgDialog(final Activity mAct) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(mAct);
        builder.setTitle(R.string.warning_nowireless_msg)

                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                        intent.addCategory("android.intent.category.DEFAULT");
                        mAct.startActivity(intent);

                        PdaHelper.closeApplication();
                    }
                });
        AlertDialog ad = builder.create();
        ad.show();
        /** 禁用back键响应事件. */
        ad.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                switch (keyCode) {
                    case KeyEvent.KEYCODE_BACK:
                        break;
                }
                return true;
            }
        });
    }

    /** 提示上传错误日志
     public void showErrorReportDialog(final Welcome mWelAct) {

     final AlertDialog.Builder builder = new AlertDialog.Builder(mWelAct);
     builder.setMessage(R.string.msErrorReportTips)
     .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
     public void onClick(DialogInterface dialog, int which) {
     dialog.dismiss();
     new CrashHandler(mWelAct).uploadError(new UploadErrorCallBack(mWelAct));
     }
     }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
     public void onClick(DialogInterface dialog, int which) {
     XmlDB.getInstance(mWelAct).saveKeyStringValue(
     XmlDB.XmlDBKey.ERROR_REPORT_MSG, "");
     dialog.dismiss();
     mWelAct.init();
     }
     });
     builder.show();
     }       */

    /**private class UploadErrorCallBack implements UICallBack<ResponseResult> {
     WelcomeActivity mWelAct;
     public UploadErrorCallBack(WelcomeActivity mWelAct) {
     this.mWelAct=mWelAct;
     }

     @Override
     public void callInMain(ResponseResult e) {
     XmlDB.getInstance(mWelAct).saveKeyStringValue(XmlDB.XmlDBKey.ERROR_REPORT_MSG, "");
     DialogManager.dismissLoadingDialog();
     mWelAct.init();
     }
     }          */


    /** Make a toast that the phone is not sd card. */
    /*public void showSdCardTipDialog(final Welcome mWelAct) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(mWelAct);
        builder.setIcon(android.R.drawable.ic_dialog_info);
        builder.setTitle(R.string.InfoAbout);
        builder.setMessage(R.string.noSdCardExit);
        builder.setPositiveButton(R.string.confirm, new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                PdaHelper.closeApplication();
            }
        });
        AlertDialog ad = builder.create();
        ad.show();
        // 禁用back键响应事件.
        ad.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                switch (keyCode) {
                    case KeyEvent.KEYCODE_BACK:
                        break;
                }
                return true;
            }
        });
    }*/

    /*public static void showLoadingDialog(final Context context){
        loadingDialog = new Dialog(context,R.style.ms_dialog);
        if (null != loadingDialog) {
            synchronized (loadingDialog) {
                loadingDialog.setContentView(R.layout.ms_common_loading_dialog);
                loadingDialog.setCancelable(true);
            //    DialogManager dialogManager = new DialogManager(context);
                DialogManager dialogManager = new DialogManager();
                loadingDialog.setOnKeyListener(dialogManager.new BackOnKeyListener(context));
                loadingDialog.show();
            }
        }



    }*/

    class BackOnKeyListener implements OnKeyListener {
        private Context context;
        public BackOnKeyListener(Context context){
            this.context = context;
        }
        @Override
        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                DialogManager.dismissLoadingDialog();
                ((Activity)context).finish();
            }
            return false;
        }

    }

    public static void dismissLoadingDialog( ){
        if (null != loadingDialog) {
            synchronized (loadingDialog) {
                if (null != loadingDialog && loadingDialog.isShowing()) {
                    loadingDialog.dismiss();
                    loadingDialog = null;
                }
            }
        }
    }

    /** Give the tip when change cusCard login.
     public void showClearCardDialog(final AssistantGroup mAssAct) {
     final AlertDialog.Builder builder = new AlertDialog.Builder(mAssAct);
     builder.setTitle(R.string.notice).setIcon(R.drawable.img6)
     .setMessage(R.string.changeCusCardLogin)
     .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
     public void onClick(DialogInterface dialog, int which) {
     XmlDB.getInstance(mAssAct).saveKeyBooleanValue("mNotices", false);
     DatabaseConstant.mDBAdapter.deleteDatabase();
     XmlDB.getInstance(mAssAct).setNoticeUpdateTime(StringPool.mNoticeUpdateTime);
     // ObjectPool.mApplication.getWelcomeActivity().init();


     // Add by reven.wu 清除二期的数据

     new DBUtil().clearDatabases(mAssAct);
     XmlDB.getInstance(mAssAct).clear();
     // 清除数据表
     // 清除缓存
     // 清除xml



     // ((MyAssistant) mAssAct.mActivityManager
     // .getActivity(StringPool.CONST_MYASSIST)).mWebView.clearCache(true);
     // ((MyAssistant) mAssAct.mActivityManager
     // .getActivity(StringPool.CONST_MYASSIST)).mWebView.clearHistory();
     mAssAct.finish();
     restartApp();
     }
     }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
     public void onClick(DialogInterface dialog, int which) {
     dialog.dismiss();
     }
     });
     AlertDialog ad = builder.create();
     ad.show();
     }     */

    //by fred restartApp
    /*private void restartApp() {
        Intent intent = new Intent();
        intent.setClassName("com.amway.mcommerce", "com.amway.mcommerce.main.WelcomeActivity"); // by xukejun 修复一期清除账号数据crash
        MyApplication app = ObjectPool.mApplication;
        PendingIntent restartIntent = PendingIntent.getActivity(app, 0,
                intent, Intent.FLAG_ACTIVITY_NEW_TASK);

        AlarmManager mgr = (AlarmManager) app.getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 1500,
                restartIntent); // 1.5秒钟后重启应用
        // 自定义方法，关闭当前打开的所有avtivity

//      System.exit(0);
        PdaHelper.closeApplication(); // by xukejun 修复一期清除账号数据crash
    }*/

    /** Show the dialog when update the tip pictures.
     public void showImgUpdateBubble(final Notice mNotice) {
     Dialog dialog =
     new AlertDialog.Builder(ObjectPool.mApplication.getPageAct())
     .setIcon(android.R.drawable.btn_star).setTitle(R.string.uodateTip)
     .setMessage(R.string.isUpdatePic)
     .setPositiveButton(R.string.updateNow, new DialogInterface.OnClickListener() {
    @Override
    public void onClick(DialogInterface arg0, int arg1) {
    mNotice.updatePicTask();
    }
    }).setNegativeButton(R.string.updateLater, new DialogInterface.OnClickListener() {
    @Override
    public void onClick(DialogInterface dialog, int which) {
    dialog.dismiss();
    }
    }).create();
     dialog.show();
     }
     */

    /**   public void showDeleteNoticeAlert(final ShowTipMsgActivity mSTAct, final int mPos) {
     final AlertDialog.Builder builder =
     new AlertDialog.Builder(ObjectPool.mApplication.getPageAct());
     builder.setMessage(mSTAct.getString(R.string.mDelNoticeInfo))
     .setPositiveButton(mSTAct.getString(R.string.yes), new DialogInterface.OnClickListener() {
     public void onClick(DialogInterface dialog, int which) {
     mSTAct.delOneNoticeItem(mPos);
     }
     }).setNegativeButton(mSTAct.getString(R.string.no), new DialogInterface.OnClickListener() {
     public void onClick(DialogInterface dialog, int which) {
     dialog.dismiss();
     }
     });
     builder.show();
     }
     */

    /*public void showNoDataDialog(String str) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ObjectPool.mApplication.getWelcomeActivity());
        builder.setTitle(R.string.notice).setMessage(str)
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.show();
    }

    public void showNetLoseDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ObjectPool.mApplication.getWelcomeActivity());
        builder.setTitle(R.string.notice).setIcon(R.drawable.img6).setMessage(R.string.netlose)
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.show();
    }*/

}