package com.imovie.mogic.utills;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Base64;

import com.imovie.mogic.dbbase.model.BaseObject;
import com.imovie.mogic.dbbase.util.LogUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.util.HashMap;

public class ImageUtil extends BaseObject {

    public static enum ScalingLogic {

        CROP,
        FIT
    }

    public static Bitmap getBitmap(String path) {

        return getBitmap(new File(path));
    }

    public static Bitmap getBitmap(File file) {

        try {

            if (!file.exists()) {
                return null;
            } else {

                Bitmap bitmap = BitmapFactory.decodeFile(file.getPath());
                return bitmap;

            }


        } catch (Exception ex) {
            LogUtil.LogErr(ImageUtil.class, ex);
        }
        return null;
    }   

    public static int getOptSampleSize(String path, int width, int height) {
        File file = new File(path);
        if (!file.exists()) {
            return 0;
        }

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        int imgW = options.outWidth;
        int imgH = options.outHeight;

        int inSampleSize = 1;
        if (imgH > height || imgW > width) {
            // 计算出实际宽高和目标宽高的比率
            final int heightRatio = Math.round((float) imgH / (float) height);
            final int widthRatio = Math.round((float) imgW / (float) width);
            // 选择宽和高中最小的比率作为inSampleSize的值，这样可以保证最终图片的宽和高
            // 一定都会大于等于目标的宽和高。
            inSampleSize = heightRatio > widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    public static Bitmap getOptBitmap(String path, int width, int height) {

        int inSampleSize = getOptSampleSize(path, width, height);

        return getOptBitmap(path, inSampleSize);
    }

    public static Bitmap getOptBitmap(String path, int sampleSize) {

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        options.inSampleSize = sampleSize;

        InputStream is = null;
        try {

            is = new FileInputStream(path);

            return BitmapFactory.decodeStream(is, null, options);


        } catch (Exception ex) {
            LogUtil.LogErr(ImageUtil.class, ex);
        } finally {
            try {
                is.close();
                is = null;
            } catch (Exception ex) {

            }
        }
        return null;

//        return BitmapFactory.decodeFile(path, options);
    }

    public static String compressAndBase64Bitmap(Bitmap bitmap) {

        try {
            if (bitmap == null)
                return null;

            ByteArrayOutputStream imageBaos = null;
//            ByteArrayOutputStream baos = null;
//            GZIPOutputStream gzip = null;

            try {

                imageBaos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, imageBaos);
                byte[] imageBytes = imageBaos.toByteArray();
//
//                baos = new ByteArrayOutputStream();
//                gzip = new GZIPOutputStream(baos);
//                gzip.write(imageBytes);

//                byte[] imageCompressBytes = baos.toByteArray();

                String imageBase64String = new String(
                        Base64.encode(imageBytes, 0, imageBytes.length, Base64.DEFAULT), "UTF-8");

                return imageBase64String;

            } catch (Exception ex) {
                LogUtil.LogErr(ImageUtil.class, ex);

            } finally {
                try {
                    imageBaos.close();

                } catch (Exception e) {

                }
//                try {
//                    baos.close();
//
//                } catch (Exception e) {
//
//                }
//                try {
//                    gzip.close();
//
//                } catch (Exception e) {
//
//                }

            }

        } catch (Exception ex) {
            LogUtil.LogErr(ImageUtil.class, ex);
        }
        return null;

    }

//    public static  String passwordBase64(String password){
//
//        byte[] passwordByte =password.getBytes();
//        String passwordString = new String(
//                Base64.encode(passwordByte, Base64.DEFAULT));
//        return passwordString;
//    }


    public static String compressAndBase64ImageFile(String imagePath, int width, int height) {

        try {
            Bitmap bitmap = getOptBitmap(imagePath, width, height);
            return compressAndBase64Bitmap(bitmap);

        } catch (Exception e) {
            new ImageUtil().logErr(e);
            return null;
        } catch (OutOfMemoryError e) {
            new ImageUtil().logErr(new Exception(e.getMessage()+""));
            return null;
        }
    }

    public static String compressAndBase64ImageFile(String imagePath) {

        return compressAndBase64ImageFile(new File(imagePath));

    }

    public static String compressAndBase64ImageFile(File imageFile) {

        try {

            if (imageFile == null)
                return null;
            if (!imageFile.exists())
                return null;

            ByteArrayOutputStream fileByteArrayOutputStream = null;
            FileInputStream fileInputStream = null;

//            ByteArrayOutputStream baos = null;
//            GZIPOutputStream gzip = null;

            try {

                fileByteArrayOutputStream = new ByteArrayOutputStream();
                fileInputStream = new FileInputStream(imageFile);

                int read;
                byte[] buff = new byte[4096];
                while ((read=fileInputStream.read(buff)) != -1) {

                    fileByteArrayOutputStream.write(buff, 0, read);

                }

                byte[] imageFileBytes = fileByteArrayOutputStream.toByteArray();

//                baos = new ByteArrayOutputStream();
//                gzip = new GZIPOutputStream(baos);
//                gzip.write(imageFileBytes);

//                byte[] imageCompressBytes = baos.toByteArray();

                String imageBase64String = new String(
                        Base64.encode(imageFileBytes, 0, imageFileBytes.length, Base64.DEFAULT), "UTF-8");

                return imageBase64String;


            } catch (Exception ex) {
                LogUtil.LogErr(ImageUtil.class, ex);
            } finally {
                try {
                    fileByteArrayOutputStream.close();
                } catch (Exception e) {
                }
                try {
                    fileInputStream.close();
                } catch (Exception e) {
                }
//                try {
//                    gzip.close();
//                } catch (Exception e) {
//                }
//                try {
//                    baos.close();
//                } catch (Exception e) {
//                }
            }



        } catch (Exception ex) {
            LogUtil.LogErr(ImageUtil.class, ex);
        }

        return null;

    }

    public static void saveImageToPath(Bitmap bitmap, String path) {

        try {

            if (bitmap == null || path == null)
                return;

            ByteArrayOutputStream baos = null;
            FileOutputStream fs = null;

            try {

                baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);

                byte[] imageBytes = baos.toByteArray();

                File file = new File(path);
                if (file.isDirectory())
                    return;
                if (!file.exists()) {
                    File parentFile = file.getParentFile();
                    if (!parentFile.exists())
                        if (!parentFile.mkdirs())
                            return;
                    file.createNewFile();
                }
                fs = new FileOutputStream(path);

                fs.write(imageBytes);

            } catch (Exception ex) {
                LogUtil.LogErr(ImageUtil.class, ex);
            } finally {
                try {
                    baos.close();
                } catch (Exception e) {

                }
                try {
                    fs.close();
                } catch (Exception e) {

                }
            }

        } catch (Exception ex) {
            LogUtil.LogErr(ImageUtil.class, ex);
        }
    }


    public static Bitmap createScaledBitmap(Bitmap unscaledBitmap, int dstWidth, int dstHeight, ScalingLogic scalingLogic) {
        Rect srcRect = calculateSrcRect(unscaledBitmap.getWidth(), unscaledBitmap.getHeight(), dstWidth, dstHeight, scalingLogic);
        Rect dstRect = calculateDstRect(unscaledBitmap.getWidth(), unscaledBitmap.getHeight(), dstWidth, dstHeight, scalingLogic);
        Bitmap scaledBitmap = Bitmap.createBitmap(dstRect.width(), dstRect.height(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(scaledBitmap);
        canvas.drawBitmap(unscaledBitmap, srcRect, dstRect, new Paint(Paint.FILTER_BITMAP_FLAG));return scaledBitmap;
    }

    public static Rect calculateSrcRect(int srcWidth, int srcHeight, int dstWidth, int dstHeight, ScalingLogic scalingLogic) {
        if (scalingLogic == ScalingLogic.CROP) {
            final float srcAspect = (float)srcWidth / (float)srcHeight;
            final float dstAspect = (float)dstWidth / (float)dstHeight;
            if (srcAspect > dstAspect) {
                final int srcRectWidth = (int)(srcHeight * dstAspect);
                final int srcRectLeft = (srcWidth - srcRectWidth) / 2;
                return new Rect(srcRectLeft, 0, srcRectLeft + srcRectWidth, srcHeight);
            } else {
                final int srcRectHeight = (int)(srcWidth / dstAspect);
                final int scrRectTop = (int)(srcHeight - srcRectHeight) / 2;
                return new Rect(0, scrRectTop, srcWidth, scrRectTop + srcRectHeight);
            }
        } else {
            return new Rect(0, 0, srcWidth, srcHeight);
        }
    }
    public static Rect calculateDstRect(int srcWidth, int srcHeight, int dstWidth, int dstHeight, ScalingLogic scalingLogic) {
        if (scalingLogic == ScalingLogic.FIT) {
            final float srcAspect = (float)srcWidth / (float)srcHeight;
            final float dstAspect = (float)dstWidth / (float)dstHeight;
            if (srcAspect > dstAspect) {
                return new Rect(0, 0, dstWidth, (int)(dstWidth / srcAspect));
            } else {
                return new Rect(0, 0, (int)(dstHeight * srcAspect), dstHeight);
            }
        } else {
            return new Rect(0, 0, dstWidth, dstHeight);
        }
    }

    public static Bitmap decodeFile(String pathName, int dstWidth, int dstHeight, ScalingLogic scalingLogic) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(pathName, options);
        options.inJustDecodeBounds = false;
        options.inSampleSize = calculateSampleSize(options.outWidth, options.outHeight, dstWidth, dstHeight, scalingLogic);
        Bitmap unscaledBitmap = BitmapFactory.decodeFile(pathName, options);
        return unscaledBitmap;
    }
    public static int calculateSampleSize(int srcWidth, int srcHeight, int dstWidth, int dstHeight, ScalingLogic scalingLogic) {
        if (scalingLogic == ScalingLogic.FIT) {
            final float srcAspect = (float)srcWidth / (float)srcHeight;
            final float dstAspect = (float)dstWidth / (float)dstHeight;
            if (srcAspect > dstAspect) {
                return srcWidth / dstWidth;
            } else {
                return srcHeight / dstHeight;
            }
        } else {
            final float srcAspect = (float)srcWidth / (float)srcHeight;
            final float dstAspect = (float)dstWidth / (float)dstHeight;
            if (srcAspect > dstAspect) {
                return srcHeight / dstHeight;
            } else {
                return srcWidth / dstWidth;
            }
        }
    }

    public static Bitmap setImage(Context context, HashMap<String, SoftReference<Bitmap>> bitMap, int resouceId) {
        try {
            String id = String.valueOf(resouceId);
            SoftReference<Bitmap> softReference = bitMap.get(id);
            if ((softReference == null) || (softReference.get() == null)) {
                Bitmap bt = readBitMap(context, resouceId);
                bitMap.put(id, new SoftReference<Bitmap>(bt));
                softReference = bitMap.get(id);
            }
            return softReference.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    public static Bitmap readBitMap(Context context, int resouceId) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inPurgeable = true;
        options.inInputShareable = true;
        return BitmapFactory.decodeStream(context.getResources().openRawResource(resouceId), null, options);
    }

    public static Bitmap getBitmapFromLocal(final String filePath) throws Exception{

        try {
            FileInputStream fis = new FileInputStream(filePath);
            return BitmapFactory.decodeStream(fis);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
