package com.zoe.custom;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.webkit.DownloadListener;
import android.webkit.URLUtil;
import android.widget.Toast;

/*import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;*/

import com.zoe.oa.aoge_android.MainActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;

import static android.content.Context.DOWNLOAD_SERVICE;

/**
 * Created by scorp on 2018/4/24.
 */


public class MyWebViewDownLoadListener implements DownloadListener {
    private Context context;


    /**
     * author Destiny
     * created 2018/7/10 10:51
     */
    public static MainActivity mainActivity;


    public MyWebViewDownLoadListener(Context context) {
        this.context = context;

    }


    @Override
    public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype,
                                long contentLength) {
        Log.i("tag", "url=" + url);
        Log.i("tag", "userAgent=" + userAgent);
        Log.i("tag", "contentDisposition=" + contentDisposition);
        Log.i("tag", "mimetype=" + mimetype);
        Log.i("tag", "contentLength=" + contentLength);
        Uri uri = Uri.parse(url);


        fileIsExists("/storage/emulated/0/Download/" + getFileName(url), url);


        //打开系统浏览器
//        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//        context.startActivity(intent);


        /*if(!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            Toast t=Toast.makeText(context, "需要SD卡。", Toast.LENGTH_LONG);
            t.setGravity(Gravity.CENTER, 0, 0);
            t.show();
            return;
        }
        DownloaderTask task=new DownloaderTask();
        task.execute(url);*/
    }

    private void downloadBySystem(String url, String contentDisposition, String mimeType) {
        // 指定下载地址
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        // 允许媒体扫描，根据下载的文件类型被加入相册、音乐等媒体库
        request.allowScanningByMediaScanner();
        // 设置通知的显示类型，下载进行时和完成后显示通知
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        // 设置通知栏的标题，如果不设置，默认使用文件名
//        request.setTitle("This is title");
        // 设置通知栏的描述
//        request.setDescription("This is description");
        // 允许在计费流量下下载
        request.setAllowedOverMetered(false);
        // 允许该记录在下载管理界面可见
        request.setVisibleInDownloadsUi(false);
        // 允许漫游时下载
        request.setAllowedOverRoaming(true);
        // 允许下载的网路类型
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
        // 设置下载文件保存的路径和文件名
        String fileName = URLUtil.guessFileName(url, contentDisposition, mimeType);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);
//        另外可选一下方法，自定义下载路径
//        request.setDestinationUri()
//        request.setDestinationInExternalFilesDir()
        final DownloadManager downloadManager = (DownloadManager) context.getSystemService(DOWNLOAD_SERVICE);
        // 添加一个下载任务

        long downloadId = downloadManager.enqueue(request);


    }


    /**
     * Android--从路径中提取文件名
     * author Destiny
     * created 2018/7/10 11:26
     */
    public String getFileName(String pathandname) {

//        int start = pathandname.lastIndexOf("/");
//        int end = pathandname.lastIndexOf(".");
//        if (start != -1 && end != -1) {
//            return pathandname.substring(start + 1, end);
//        } else {
//            return null;
//        }
        int start = pathandname.lastIndexOf("/");
        if (start != -1) {
            return pathandname.substring(start + 1);
        } else {
            return null;
        }

    }


    /**
     * android 判断文件是否存在
     * author Destiny
     * created 2018/7/10 11:37
     */
    public void fileIsExists(String strFile, String url) {
        try {
            File f = new File(strFile);
            if (!f.exists()) {
                Toast.makeText(mainActivity, "下载中", Toast.LENGTH_LONG).show();
                downloadBySystem(url, "", "");
            } else {
                Toast.makeText(mainActivity, "你要下载的文件已存在目录\n/storage/emulated/0/Download/\n文件名：" + getFileName(url), Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /*//内部类
    private class DownloaderTask extends AsyncTask<String, Void, String> {

        public DownloaderTask() {
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            String url = params[0];
//          Log.i("tag", "url="+url);
            String fileName = url.substring(url.lastIndexOf("/") + 1);
            fileName = URLDecoder.decode(fileName);
            Log.i("tag", "fileName=" + fileName);

            File directory = Environment.getExternalStorageDirectory();
            File file = new File(directory, fileName);
            if (file.exists()) {
                Log.i("tag", "The file has already exists.");
                return fileName;
            }
            try {
                HttpClient client = new DefaultHttpClient();
//                client.getParams().setIntParameter("http.socket.timeout",3000);//设置超时
                HttpGet get = new HttpGet(url);
                HttpResponse response = client.execute(get);
                if (HttpStatus.SC_OK == response.getStatusLine().getStatusCode()) {
                    HttpEntity entity = response.getEntity();
                    InputStream input = entity.getContent();

                    writeToSDCard(fileName, input);

                    input.close();
//                  entity.consumeContent();
                    return fileName;
                } else {
                    return null;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onCancelled() {
            // TODO Auto-generated method stub
            super.onCancelled();
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            closeProgressDialog();
            if (result == null) {
                Toast t = Toast.makeText(context, "连接错误！请稍后再试！", Toast.LENGTH_LONG);
                t.setGravity(Gravity.CENTER, 0, 0);
                t.show();
                return;
            }

            Toast t = Toast.makeText(context, "已保存到SD卡。", Toast.LENGTH_LONG);
            t.setGravity(Gravity.CENTER, 0, 0);
            t.show();
            File directory = Environment.getExternalStorageDirectory();
            File file = new File(directory, result);
            Log.i("tag", "Path=" + file.getAbsolutePath());

            Intent intent = getFileIntent(file);

            context.startActivity(intent);

        }

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            showProgressDialog();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            // TODO Auto-generated method stub
            super.onProgressUpdate(values);
        }
    }

    private ProgressDialog mDialog;
    private void showProgressDialog(){
        if(mDialog==null){
            mDialog = new ProgressDialog(context);
            mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);//设置风格为圆形进度条
            mDialog.setMessage("正在加载 ，请等待...");
            mDialog.setIndeterminate(false);//设置进度条是否为不明确
            mDialog.setCancelable(true);//设置进度条是否可以按退回键取消
            mDialog.setCanceledOnTouchOutside(false);
            mDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {

                @Override
                public void onDismiss(DialogInterface dialog) {
                    // TODO Auto-generated method stub
                    mDialog=null;
                }
            });
            mDialog.show();

        }
    }
    private void closeProgressDialog(){
        if(mDialog!=null){
            mDialog.dismiss();
            mDialog=null;
        }
    }
    public Intent getFileIntent(File file){
//       Uri uri = Uri.parse("http://m.ql18.com.cn/hpf10/1.pdf");
        Uri uri = Uri.fromFile(file);
        String type = getMIMEType(file);
        Log.i("tag", "type="+type);
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(uri, type);
        return intent;
    }

    public void writeToSDCard(String fileName,InputStream input){

        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            File directory=Environment.getExternalStorageDirectory();
            File file=new File(directory,fileName);
//          if(file.exists()){
//              Log.i("tag", "The file has already exists.");
//              return;
//          }
            try {
                FileOutputStream fos = new FileOutputStream(file);
                byte[] b = new byte[2048];
                int j = 0;
                while ((j = input.read(b)) != -1) {
                    fos.write(b, 0, j);
                }
                fos.flush();
                fos.close();
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }else{
            Log.i("tag", "NO SDCard.");
        }
    }

    private String getMIMEType(File f){
        String type="";
        String fName=f.getName();
      //取得扩展名
        String end=fName.substring(fName.lastIndexOf(".")+1,fName.length()).toLowerCase();

      //依扩展名的类型决定MimeType
        if(end.equals("pdf")){
            type = "application/pdf";//
        }
        else if(end.equals("m4a")||end.equals("mp3")||end.equals("mid")||
                end.equals("xmf")||end.equals("ogg")||end.equals("wav")){
            type = "audio/*";
        }
        else if(end.equals("3gp")||end.equals("mp4")){
            type = "video/*";
        }
        else if(end.equals("jpg")||end.equals("gif")||end.equals("png")||
                end.equals("jpeg")||end.equals("bmp")){
            type = "image/*";
        }
        else if(end.equals("apk")){
        // android.permission.INSTALL_PACKAGES
            type = "application/vnd.android.package-archive";
        }
//      else if(end.equals("pptx")||end.equals("ppt")){
//        type = "application/vnd.ms-powerpoint";
//      }else if(end.equals("docx")||end.equals("doc")){
//        type = "application/vnd.ms-word";
//      }else if(end.equals("xlsx")||end.equals("xls")){
//        type = "application/vnd.ms-excel";
//      }
        else{
//        //如果无法直接打开，就跳出软件列表给用户选择
            type="* / *";
        }
        return type;
    }
        */
}


