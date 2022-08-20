package com.mz.bf.Utilities;

import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;


import com.mz.bf.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class Utilities {
  public static Uri img;
  RecyclerView.LayoutManager menulayoutmanager;
  public static boolean isNetworkAvailable(Context context) {
    ConnectivityManager connectivityManager
            = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
    if (!(activeNetworkInfo != null && activeNetworkInfo.isConnected())) {
      Toast.makeText(context, context.getString(R.string.checkNetworkConnection), Toast.LENGTH_LONG).show();
    }
    return activeNetworkInfo != null && activeNetworkInfo.isConnected();
  }

  public static void showMessage(Context context, String message) {
    Toast.makeText(context, message, Toast.LENGTH_LONG).show();
  }

  public static Typeface getTypeFace(Context context) {
    return Typeface.createFromAsset(context.getAssets(), "font/twolight.otf");
  }

  public static String getLang(Context context) {
    return "ar";
  }

  public static RequestBody getRequestBodyText(String data) {
    RequestBody requestBody = RequestBody.create(MediaType.parse("text/plain"), data);
    return requestBody;
  }

  @TargetApi(Build.VERSION_CODES.KITKAT)
  public static String getImagePath(Context context, Uri uri) {
    int currentApiVersion;
    try {
      currentApiVersion = Build.VERSION.SDK_INT;
    } catch (NumberFormatException e) {
      //API 3 will crash if SDK_INT is called
      currentApiVersion = 3;
    }
    if (currentApiVersion >= Build.VERSION_CODES.KITKAT) {


      if (DocumentsContract.isDocumentUri(context, uri)) {

        if (isExternalStorageDocument(uri)) {
          final String docId = DocumentsContract.getDocumentId(uri);
          final String[] split = docId.split(":");
          final String type = split[0];

          if ("primary".equalsIgnoreCase(type)) {
            return Environment.getExternalStorageDirectory() + "/"
                    + split[1];
          }
        } else if (isDownloadsDocument(uri)) {
          String fileName = getImagePath(context, uri);
          if (fileName != null) {
            return Environment.getExternalStorageDirectory().toString() + "/Download/" + fileName;
          }

          String id = DocumentsContract.getDocumentId(uri);
          if (id.startsWith("raw:")) {
            id = id.replaceFirst("raw:", "");
            File file = new File(id);
            if (file.exists())
              return id;
          }

          final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
          return getDataColumn(context, contentUri, null, null);
        }
        // MediaProvider
        else if (isMediaDocument(uri)) {
          final String docId = DocumentsContract.getDocumentId(uri);
          final String[] split = docId.split(":");
          final String type = split[0];

          Uri contentUri = null;
          if ("image".equals(type)) {
            contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
          } else if ("video".equals(type)) {
            contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
          } else if ("audio".equals(type)) {
            contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
          }

          final String selection = "_id=?";
          final String[] selectionArgs = new String[]{split[1]};

          return getDataColumn(context, contentUri, selection,
                  selectionArgs);
        }
      }
      // MediaStore (and general)
      else if ("content".equalsIgnoreCase(uri.getScheme())) {

        // Return the remote address
        if (isGooglePhotosUri(uri))
          return uri.getLastPathSegment();

        return getDataColumn(context, uri, null, null);
      }
      // File
      else if ("file".equalsIgnoreCase(uri.getScheme())) {
        return uri.getPath();
      }

      return null;
    } else if (currentApiVersion <= Build.VERSION_CODES.HONEYCOMB_MR2 && currentApiVersion >= Build.VERSION_CODES.HONEYCOMB) {
      String[] proj = {MediaStore.Images.Media.DATA};
      String result = null;

      CursorLoader cursorLoader = new CursorLoader(
              context,
              uri, proj, null, null, null);
      Cursor cursor = cursorLoader.loadInBackground();

      if (cursor != null) {
        int column_index =
                cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        result = cursor.getString(column_index);
      }
      return result;
    } else {

      String[] proj = {MediaStore.Images.Media.DATA};
      Cursor cursor = context.getContentResolver().query(uri, proj, null, null, null);
      int column_index
              = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
      cursor.moveToFirst();
      return cursor.getString(column_index);
    }
  }

  public static MultipartBody.Part getMultiPart(Context context, Uri uri, String partName) {
    File file = getFileFromImagePath(getImagePath(context, uri));
    RequestBody requestBody = getRequestBodyImage(file);
    MultipartBody.Part part = MultipartBody.Part.createFormData(partName, file.getName(), requestBody);
    return part;

  }

  private static RequestBody getRequestBodyImage(File file) {
    RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
    return requestBody;
  }

  private static File getFileFromImagePath(String path) {
    File file = new File(path);
    return file;
  }

  public static boolean isExternalStorageDocument(Uri uri) {
    return "com.android.externalstorage.documents".equals(uri
            .getAuthority());
  }

  public static boolean isDownloadsDocument(Uri uri) {
    return "com.android.providers.downloads.documents".equals(uri
            .getAuthority());
  }

  public static boolean isMediaDocument(Uri uri) {
    return "com.android.providers.media.documents".equals(uri
            .getAuthority());
  }

  public static boolean isGooglePhotosUri(Uri uri) {
    return "com.google.android.apps.photos.content".equals(uri
            .getAuthority());
  }

  public static String getDataColumn(Context context, Uri uri,
                                     String selection, String[] selectionArgs) {

    Cursor cursor = null;
    final String column = "_data";
    final String[] projection = {column};

    try {
      cursor = context.getContentResolver().query(uri, projection,
              selection, selectionArgs, null);
      if (cursor != null && cursor.moveToFirst()) {
        final int index = cursor.getColumnIndexOrThrow(column);
        return cursor.getString(index);
      }
    } finally {
      if (cursor != null)
        cursor.close();
    }
    return null;
  }

  @RequiresApi(api = Build.VERSION_CODES.KITKAT)
  public static Uri compressImage(Context context, String dataString) {

    Uri contentUri = Uri.parse(dataString);
    String filePath = getRealPathFromURI2(context, contentUri);
    Bitmap scaledBitmap = null;

    BitmapFactory.Options options = new BitmapFactory.Options();

//      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
//      you try the use the bitmap here, you will get null.
    options.inJustDecodeBounds = true;
    Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

    int actualHeight = options.outHeight;
    int actualWidth = options.outWidth;

//      max Height and width values of the compressed image is taken as 816x612

    float maxHeight = 816.0f;
    float maxWidth = 612.0f;
    float imgRatio = actualWidth / actualHeight;
    float maxRatio = maxWidth / maxHeight;

//      width and height values are set maintaining the aspect ratio of the image

    if (actualHeight > maxHeight || actualWidth > maxWidth) {
      if (imgRatio < maxRatio) {
        imgRatio = maxHeight / actualHeight;
        actualWidth = (int) (imgRatio * actualWidth);
        actualHeight = (int) maxHeight;
      } else if (imgRatio > maxRatio) {
        imgRatio = maxWidth / actualWidth;
        actualHeight = (int) (imgRatio * actualHeight);
        actualWidth = (int) maxWidth;
      } else {
        actualHeight = (int) maxHeight;
        actualWidth = (int) maxWidth;

      }
    }

//      setting inSampleSize value allows to load a scaled down version of the original image

    options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

//      inJustDecodeBounds set to false to load the actual bitmap
    options.inJustDecodeBounds = false;

//      this options allow android to claim the bitmap memory if it runs low on memory
    options.inPurgeable = true;
    options.inInputShareable = true;
    options.inTempStorage = new byte[16 * 1024];

    try {
//          load the bitmap from its path
      bmp = BitmapFactory.decodeFile(filePath, options);
    } catch (OutOfMemoryError exception) {
      exception.printStackTrace();

    }
    try {
      scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
    } catch (OutOfMemoryError exception) {
      exception.printStackTrace();
    }

    float ratioX = actualWidth / (float) options.outWidth;
    float ratioY = actualHeight / (float) options.outHeight;
    float middleX = actualWidth / 2.0f;
    float middleY = actualHeight / 2.0f;

    Matrix scaleMatrix = new Matrix();
    scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

    Canvas canvas = new Canvas(scaledBitmap);
    canvas.setMatrix(scaleMatrix);
    canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

//      check the rotation of the image and display it properly
    ExifInterface exif;
    try {
      exif = new ExifInterface(filePath);

      int orientation = exif.getAttributeInt(
              ExifInterface.TAG_ORIENTATION, 0);
      Log.d("EXIF", "Exif: " + orientation);
      Matrix matrix = new Matrix();
      if (orientation == 6) {
        matrix.postRotate(90);
        Log.d("EXIF", "Exif: " + orientation);
      } else if (orientation == 3) {
        matrix.postRotate(180);
        Log.d("EXIF", "Exif: " + orientation);
      } else if (orientation == 8) {
        matrix.postRotate(270);
        Log.d("EXIF", "Exif: " + orientation);
      }
      scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
              scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
              true);
    } catch (IOException e) {
      e.printStackTrace();
    }
    FileOutputStream out = null;
    String filename = getFilename();
    try {
      out = new FileOutputStream(filename);

//          write the compressed bitmap at the destination specified by filename.
      scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);

    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    try {
      //display
      Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), Uri.fromFile(new File(filename)));
      //fragmentAddMiliteryBinding.personalImg.setImageBitmap(bitmap);
      String imgPath = filename;

      String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "Title", null);
      img = Uri.parse(path);
      //Picasso.get().load(militery_img).into(fragmentUpdateMiliteryBinding.personalImg);
      //fragmentAddMiliteryBinding.personalImg.setImageBitmap(bitmap);
      //Toast.makeText(getActivity(), "added successfully", Toast.LENGTH_SHORT).show();
    } catch (IOException e) {
      e.printStackTrace();
      Log.d("ERROR", "Error " + e.getMessage());
    }
    return img;
  }

  private static String getFilename() {
    String root = Environment.getExternalStorageDirectory().toString();
    File myDir = new File(root + "/saved_images");
    myDir.mkdirs();
    if (!myDir.exists()) {
      myDir.mkdirs();
    }
    String uriSting = (myDir.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg");
    Log.d("IMAGES", "" + uriSting);
    return uriSting;
  }

  public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
    final int height = options.outHeight;
    final int width = options.outWidth;
    int inSampleSize = 1;

    if (height > reqHeight || width > reqWidth) {
      final int heightRatio = Math.round((float) height / (float) reqHeight);
      final int widthRatio = Math.round((float) width / (float) reqWidth);
      inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
    }
    final float totalPixels = width * height;
    final float totalReqPixelsCap = reqWidth * reqHeight * 2;
    while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
      inSampleSize++;
    }

    return inSampleSize;
  }

  @RequiresApi(api = Build.VERSION_CODES.KITKAT)
  private static String getRealPathFromURI2(Context context, Uri uri) {

    final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

    // DocumentProvider
    if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
      // ExternalStorageProvider
      if (isExternalStorageDocument(uri)) {
        final String docId = DocumentsContract.getDocumentId(uri);
        final String[] split = docId.split(":");
        final String type = split[0];

        if ("primary".equalsIgnoreCase(type)) {
          return Environment.getExternalStorageDirectory() + "/" + split[1];
        }

        // TODO handle non-primary volumes
      }
      // DownloadsProvider
      else if (isDownloadsDocument(uri)) {

        final String id = DocumentsContract.getDocumentId(uri);
        if (!TextUtils.isEmpty(id)) {
          if (id.startsWith("raw:")) {
            return id.replaceFirst("raw:", "");
          }
          try {
            final Uri contentUri = ContentUris.withAppendedId(
                    Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
            return getDataColumn(context, contentUri, null, null);
          } catch (NumberFormatException e) {
            return null;
          }
        }
      }
      // MediaProvider
      else if (isMediaDocument(uri)) {
        final String docId = DocumentsContract.getDocumentId(uri);
        final String[] split = docId.split(":");
        final String type = split[0];

        Uri contentUri = null;
        if ("image".equals(type)) {
          contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        } else if ("video".equals(type)) {
          contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        } else if ("audio".equals(type)) {
          contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        }

        final String selection = "_id=?";
        final String[] selectionArgs = new String[]{
                split[1]
        };

        return getDataColumn(context, contentUri, selection, selectionArgs);
      }
    }
    // MediaStore (and general)
    else if ("content".equalsIgnoreCase(uri.getScheme())) {

      // Return the remote address
      if (isGooglePhotosUri(uri))
        return uri.getLastPathSegment();

      return getDataColumn(context, uri, null, null);
    }
    // File
    else if ("file".equalsIgnoreCase(uri.getScheme())) {
      return uri.getPath();
    }

    return null;
  }
 /* private void init_navigation_menu(Context context) {
    menuItemList = new ArrayList<>();
    menuItemList.add(new com.alatheer.shebinbook.home.slider.MenuItem("الرئيسية",R.drawable.home2));
    menuItemList.add(new com.alatheer.shebinbook.home.slider.MenuItem("المفضلة",R.drawable.fav2));
    menuItemList.add(new com.alatheer.shebinbook.home.slider.MenuItem("الإعدادات",R.drawable.setting));
    menuItemList.add(new com.alatheer.shebinbook.home.slider.MenuItem("تواصل معنا",R.drawable.contactus));
    menuItemList.add(new com.alatheer.shebinbook.home.slider.MenuItem("تسجيل خروج",R.drawable.logout2));
    if (user_type == 4){
      menuItemList.add(new com.alatheer.shebinbook.home.slider.MenuItem("متجري",R.drawable.store));
    }
    menuAdapter = new MenuAdapter(menuItemList,);
    menulayoutmanager = new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false);
    View headerLayout = activityHomeBinding.navView.getHeaderView(0);
    RoundedImageView img = headerLayout.findViewById(R.id.user_img);
    if (user_img != null){
      Picasso.get().load("https://mymissing.online/shebin_book/public/uploads/images/images/"+user_img).into(img);
    }
    TextView txt_name = headerLayout.findViewById(R.id.txt_user_name);
    TextView txt_phone = headerLayout.findViewById(R.id.txt_phone);
    txt_name.setText(user_name);
    txt_phone.setText(user_phone);
    menu_recycler = headerLayout.findViewById(R.id.recycler_view);
    menu_recycler.setHasFixedSize(true);
    menu_recycler.setAdapter(menuAdapter);
    menu_recycler.setLayoutManager(menulayoutmanager);
  }*/
}