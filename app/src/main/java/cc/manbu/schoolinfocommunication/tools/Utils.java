package cc.manbu.schoolinfocommunication.tools;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.Base64;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import cc.manbu.schoolinfocommunication.bean.ContactEntity;
import cc.manbu.schoolinfocommunication.bean.Device_Geography;
import cc.manbu.schoolinfocommunication.bean.PhoneListBean;
import cc.manbu.schoolinfocommunication.bean.SHX007Scenemode;

/**
 * Created by manbuAndroid5 on 2017/2/21.
 */

public class Utils {
    public static boolean isSuccess = false;
    public static List<Device_Geography> fenceList = new ArrayList<>();
    public static Device_Geography CurGeography;
    public static List<String> stringList = new ArrayList<>();
    public static List<PhoneListBean> datas = new ArrayList<>();
    public static int position = -1;
    public static int allPosition = 0;
    public static ContactEntity contactEntity;
    public static SHX007Scenemode scenemode;
    public static int modeCode = -1;
    //tintDrawable这个方法是用来向下兼容用的。你如果不考虑向下兼容的问题 用系统自带的方法 就可以了
    //当然你也可以用http://andraskindler.com/blog/2015/tinting_drawables/ 这个文章里的方法来做向下兼容：
    public static Drawable tintDrawable(Drawable drawable, ColorStateList colors) {
        final Drawable wrappedDrawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTintList(wrappedDrawable, colors);
        return wrappedDrawable;
    }

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    /**
     * 防止连续点击
     */
    private static long lastClickTime;
    public synchronized static boolean isFastClick() {
        long time = System.currentTimeMillis();
        if ( time - lastClickTime < 500) {
            return true;
        }
        lastClickTime = time;
        return false;
    }
    public static void createDirs(String path){
        File dir = new File(path);
        if(!dir.exists()){
            dir.mkdirs();
        }
    }
    /**
     * 删除所有文件
     * @param file
     */
    public static void deleteFileAll(File file) {
        if (!file.exists()) {
            return;
        } else {
            if (file.isFile()) {
                file.delete();
                return;
            }
            if (file.isDirectory()) {
                File[] childFile = file.listFiles();
                if (childFile == null || childFile.length == 0) {
                    return;
                }
                for (File f : childFile) {
                    deleteFileAll(f);
                }
            }
        }
    }
    /**
     * 将字符串转为图片
     * @param imgStr
     * @return
     */
    public static boolean generateImage(String imgStr,String imgFile)throws Exception {// 对字节数组字符串进行Base64解码并生成图片
        if (imgStr == null || imgFile.equals("")) // 图像数据为空
            return false;
        try {
            // Base64解码
            byte[] b = Base64.decode(imgStr,Base64.DEFAULT);
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {// 调整异常数据
                    b[i] += 256;
                }
            }
            // 生成jpeg图片
            String imgFilePath = imgFile;// 新生成的图片
            OutputStream out = new FileOutputStream(imgFilePath);
            out.write(b);
            out.flush();
            out.close();
            return true;
        } catch (Exception e) {
            throw e;
        }
    }
    /**
     * 将突破转化为字符串
     * @param imgFile
     * @return
     */
    public static String getImageStr(String imgFile) {// 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
        InputStream in = null;
        byte[] data = null;
        // 读取图片字节数组
        try {
            in = new FileInputStream(imgFile);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Base64.encodeToString(data,Base64.DEFAULT);// 返回Base64编码过的字节数组字符串
    }
    public static void saveBitmapToFile(Bitmap bitmap, String path){
        int quality = 100;
        File file=new File(path);//将要保存图片的路径
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
