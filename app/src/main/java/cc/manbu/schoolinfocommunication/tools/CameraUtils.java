package cc.manbu.schoolinfocommunication.tools;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by manbuAndroid5 on 2016/9/22.
 */

public class CameraUtils {
    private String path = "";
    private final int ONE_K = 1024;
    private final int ONE_M = ONE_K * ONE_K;
    private final int MAX_AVATAR_SIZE = 2 * ONE_M; // 2M

    private Activity context;
    private final static String ACTION_CROP_IMAGE = "com.android.camera.action.CROP";
    private String defaultPicName = "head.jpg";
    public final String tempPicName = "temp.jpg";

    public CameraUtils(Activity context, String path){
        this.context = context;
        this.path = path;
    }

    public void setFileName(String name){
        this.defaultPicName = name;
    }

    public String getFileName(){
        return defaultPicName;
    }

    private Uri getDuplicateUri(Uri uri){
        Uri duplicateUri = null;

        String uriString = getUriString(uri);

        duplicateUri = getDuplicateUri(uri,uriString);

        return duplicateUri;
    }

    private Uri getDuplicateUri(Uri uri, String uriString){

        Uri duplicateUri = null;
        String duplicatePath = null;
        duplicatePath = uriString.replace(".", "_duplicate.");

        //cropImagePath = uriString;
        //判断原图是否旋转，旋转了进行修复
        rotateImage(uriString);

        duplicateUri = Uri.fromFile(new File(duplicatePath));

        return duplicateUri;
    }

    /**
     * 旋转图象
     * @Title: rotateImage
     * @return void
     * @date 2012-12-4 上午10:18:53
     */
    private void rotateImage(String uriString){

        try {
            ExifInterface exifInterface = new ExifInterface(uriString);

            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

            if(orientation == ExifInterface.ORIENTATION_ROTATE_90 ||
                    orientation == ExifInterface.ORIENTATION_ROTATE_180 ||
                    orientation == ExifInterface.ORIENTATION_ROTATE_270){

                String value = String.valueOf(orientation);
                exifInterface.setAttribute(ExifInterface.TAG_ORIENTATION, value);
                //exifInterface.setAttribute(ExifInterface.TAG_ORIENTATION, "no");
                exifInterface.saveAttributes();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readCropImage(Intent data){
        if (data == null) {
            return;
        }
        Uri uri = data.getData();
        Bitmap photo = null;
        if(uri != null){
            photo = getBitmap(uri);
        }else{
            Bundle extras = data.getExtras();
            if (extras != null) {
                photo = extras.getParcelable("data");
            }
        }

        if (photo != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] datas = null;
            try {
                datas = baos.toByteArray();
                baos.flush();
                baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if(datasException(datas)){
                return;
            }
            String state = Environment.getExternalStorageState();
            if (state.equals(Environment.MEDIA_MOUNTED)) {
                // 保存头像
                // 字符参数部分
                saveAvatar(datas,uri);
            }else{
                Toast.makeText(context,"No Sdcard here",Toast.LENGTH_SHORT).show();
            }
        }

    }

    private Bitmap getBitmap(Uri uri){
        Bitmap bitmap = null;
        InputStream is = null;
        try {

            is = getInputStream(uri);

            bitmap = BitmapFactory.decodeStream(is);

        }catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException ignored) {
                }
            }
        }
        return bitmap;
    }

    private boolean datasException(byte[] datas){
        // 头像处理异常
        if (datas == null || datas.length <= 0) {

            return true;
        }

        // 头像尺寸不符
        if (datas.length > MAX_AVATAR_SIZE) {

            return true;
        }

        return false;
    }

    private void saveAvatar(byte[] datas,Uri uri){
        String filepath = path + defaultPicName;
        File dir = new File(path);
        File file = new File(filepath);
        if(!dir.exists()){
            dir.mkdirs();
        }
        try {
            if(!file.exists()){
                file.createNewFile();
            }
            FileOutputStream fos=new FileOutputStream(file);
            fos.write(datas,0,datas.length);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private InputStream getInputStream(Uri mUri) throws IOException {
        try {
            if (mUri.getScheme().equals("file")) {
                return new java.io.FileInputStream(mUri.getPath());
            } else {
                return context.getContentResolver().openInputStream(mUri);
            }
        } catch (FileNotFoundException ex) {
            return null;
        }
    }

    /**
     * 根据Uri获取文件的路径
     * @Title: getUriString
     * @param uri
     * @return
     * @return String
     * @date 2012-11-28 下午1:19:31
     */
    private String getUriString(Uri uri){
        String imgPath = null;
        if (uri != null) {
            String uriString = uri.toString();
            //小米手机的适配问题，小米手机的uri以file开头，其他的手机都以content开头
            //以content开头的uri表明图片插入数据库中了，而以file开头表示没有插入数据库
            //所以就不能通过query来查询，否则获取的cursor会为null。
            if(uriString.startsWith("file")){
                //uri的格式为file:///mnt....,将前七个过滤掉获取路径
                imgPath = uriString.substring(7, uriString.length());

                return imgPath;
            }
            Cursor cursor = context.getContentResolver().query(uri, null, null,null, null);
            cursor.moveToFirst();
            imgPath = cursor.getString(1); // 图片文件路径

        }
        return imgPath;
    }

    public static Bitmap copressImage(String imgPath){
        File picture = new File(imgPath);
        Bitmap bmap = null;
        BitmapFactory.Options bitmapFactoryOptions = new BitmapFactory.Options();
        //下面这个设置是将图片边界不可调节变为可调节
        bitmapFactoryOptions.inJustDecodeBounds = true;
        bitmapFactoryOptions.inSampleSize = 2;
        int outWidth  = bitmapFactoryOptions.outWidth;
        int outHeight = bitmapFactoryOptions.outHeight;
        bmap = BitmapFactory.decodeFile(picture.getAbsolutePath(),
                bitmapFactoryOptions);
        float imagew = 150;
        float imageh = 150;
        int yRatio = (int) Math.ceil(bitmapFactoryOptions.outHeight
                / imageh);
        int xRatio = (int) Math
                .ceil(bitmapFactoryOptions.outWidth / imagew);
        if (yRatio > 1 || xRatio >1) {
            if (yRatio > xRatio) {
                bitmapFactoryOptions.inSampleSize = yRatio;
            } else {
                bitmapFactoryOptions.inSampleSize = xRatio;
            }

        }
        bitmapFactoryOptions.inJustDecodeBounds = false;
        bmap = BitmapFactory.decodeFile(picture.getAbsolutePath(),
                bitmapFactoryOptions);
        if(bmap != null){
            return bmap;
        }
        return null;
    }

    public void getLocalImage(int reqCode){
        // 抓下异常是防止有的机器不支持ACTION_PICK或ACTION_GET_CONTENT的动作
        try {
            Intent intent = new Intent(Intent.ACTION_PICK, null);
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    "image/*");
            context.startActivityForResult(intent, reqCode);
        } catch (Exception e1) {
            try {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                context.startActivityForResult(intent, reqCode);
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    public void readLocalImage(Intent data, int reqCode){
        if (data == null) {
            return;
        }
        Uri uri = null;
        uri = data.getData();
        if (uri != null) {
            startPhotoCrop(uri,null,reqCode); // 图片裁剪
        }
    }

    public void startPhotoCrop(Uri uri, String duplicatePath, int reqCode) {
//			Uri duplicateUri = preCrop(uri,duplicatePath);
        Intent intent = new Intent(ACTION_CROP_IMAGE);
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 320);
        intent.putExtra("outputY", 320);
        intent.putExtra("toolbar_back-data", true);
        context.startActivityForResult(intent, reqCode);
    }

    public void takePhotoFromCarmera(int reqCode){
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            File dir = new File(path);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File file = new File(path, tempPicName);
            file.delete();
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            // 下面这个是调用照相机的
            Intent cameraIntent = new Intent(
                    MediaStore.ACTION_IMAGE_CAPTURE);
            cameraIntent.putExtra(
                    MediaStore.EXTRA_OUTPUT,
                    Uri.fromFile(file));
            context.startActivityForResult(cameraIntent, reqCode);
        }else {
            Toast.makeText(context,"No Sdcard here",Toast.LENGTH_SHORT).show();
        }
    }
}
