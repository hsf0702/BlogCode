package com.zsj.lib.androidlib.uitils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.util.Base64;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * 创建者     朱胜军
 * 创建时间   2017/10/9 22:00
 * 描述	      TODO
 * <p>
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   TODO
 */
public class UiUtils {


    /**
     * @param context      上下文
     * @param typefacePath 字体路径
     * @param textView
     */
    public static void setTypeface(Context context, String typefacePath, TextView textView) {
        Typeface typeface;
        if (typefacePath == null) {
            typeface = Typeface.createFromAsset(context.getAssets(), "fonts/FONT.TTF");
        } else {
            typeface = Typeface.createFromAsset(context.getAssets(), typefacePath);
        }
        textView.setTypeface(typeface);
    }

    //保存图片到sp中
    public static void putImageToSp(Context mContext, ImageView imageView) {
        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
        Bitmap bitmap = drawable.getBitmap();
        //第一步：将Bitmap压缩成字节数组输出流
        ByteArrayOutputStream byStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byStream);
        //第二步：利用Base64将我们的字节数组输出流转换成String
        byte[] byteArray = byStream.toByteArray();
        String imgString = new String(Base64.encodeToString(byteArray, Base64.DEFAULT));
        //第三步：将String保存shareUtils
        SPUtils.getInstance().put("image_title", imgString);
    }

    //读取图片
    public static void getImageToSp(Context mContext, ImageView imageView) {
        //1.拿到string
        String imgString = SPUtils.getInstance().getString("image_title", "");
        if (!imgString.equals("")) {
            //2.利用Base64将我们string转换
            byte[] byteArray = Base64.decode(imgString, Base64.DEFAULT);
            ByteArrayInputStream byStream = new ByteArrayInputStream(byteArray);
            //3.生成bitmap
            Bitmap bitmap = BitmapFactory.decodeStream(byStream);
            imageView.setImageBitmap(bitmap);
        }
    }
}
