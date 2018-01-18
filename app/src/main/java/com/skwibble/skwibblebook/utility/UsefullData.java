package com.skwibble.skwibblebook.utility;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Patterns;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.URLUtil;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.desai.vatsal.mydynamictoast.MyCustomToast;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.skwibble.skwibblebook.R;
import com.skwibble.skwibblebook.loader.CallPhoneDialog;
import com.skwibble.skwibblebook.volley.InitializeActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Pattern;

public class UsefullData  {

	public Context _context;

    SaveData save;
	Dialog dialog ;

	public UsefullData(Context c) {
		_context = c;
	}

	// ================== DEVICE INFORMATION ============//

	public static String getCountryCodeFromDevice() {
		String countryCode = Locale.getDefault().getCountry();
		if (countryCode.equals("")) {
			countryCode = "IN";
		}
		return countryCode;
	}

	// ================== CREATE FILE AND RELATED ACTION ============//

	public File getRootFile() {

		File f = new File(Environment.getExternalStorageDirectory(), _context
				.getString(R.string.app_name).toString());
		if (!f.isDirectory()) {
			f.mkdirs();
		}

		return f;
	}

	public void deleteRootDir(File root) {

		if (root.isDirectory()) {
			String[] children = root.list();
			for (int i = 0; i < children.length; i++) {
				File f = new File(root, children[i]);
				Log("file name:" + f.getName());
				if (f.isDirectory()) {
					deleteRootDir(f);
				} else {
					f.delete();
				}
			}
		}
	}

	// ================ DOWNLOAD ============================//



	public String getNameFromURL(String url) {

		String fileName = "item_image.jpg";
		if (url != null) {
			fileName = url.substring(url.lastIndexOf('/') + 1, url.length());
		}
		return fileName;
	}

	// ================== LOG AND TOAST====================//

	public static void Log(final String msg) {

//		if (SHOW_LOG) {
//			android.util.Log.e(LOG_TAG, msg);
//		}

	}

	public void showMsgOnUI(final String msg) {
		((Activity) _context).runOnUiThread(new Runnable() {

			@Override
			public void run() {
				make_toast(msg);
			}
		});

	}

	public void make_toast(final String msg) {
		((Activity) _context).runOnUiThread(new Runnable() {

			@Override
			public void run() {
				MyCustomToast myCustomToast = new MyCustomToast(_context);
				myCustomToast.setCustomMessageText(msg);
				myCustomToast.setCustomMessageTextSize(12);
				myCustomToast.setCustomMessageTextColor(Color.WHITE);
				myCustomToast.setCustomMessageIcon(R.drawable.ic_info, MyCustomToast.POSITION_LEFT);
				myCustomToast.setCustomMessageIconColor(Color.WHITE);
                myCustomToast.setCustomMessageBackgroundColor(Color.BLACK);

				myCustomToast.setCustomMessageBackgroundDrawable(R.drawable.info_message_background);
				myCustomToast.setCustomMessageDuration(MyCustomToast.LENGTH_LONG);
				myCustomToast.setGravity(Gravity.BOTTOM, 0, 200);
				myCustomToast.setCustomMessageTypeface("Ubuntu-R.ttf");
				myCustomToast.show();


			}
		});

	}

	// =================== INTERNET ===================//
	public boolean isNetworkConnected() {
		ConnectivityManager cm = (ConnectivityManager) _context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		if (ni == null) {
			return false;
		} else
			return true;
	}

	// ==================== PROGRESS DIALOG ==================//

	public void showProgress() {

//		Intent i = new Intent(_context, AnimationActivity.class);
//		i.putExtra("anim","start");
//		_context.startActivity(i);

//		pDialog = new SweetAlertDialog(_context, SweetAlertDialog.PROGRESS_TYPE);
//		pDialog.setTitleText("Loading...");
//		pDialog.setCancelable(false);
//		pDialog.show();
		try {
			if ((dialog != null) && dialog.isShowing()){
                dialog.dismiss();
            dialog = null;
                }
			dialog = new CallPhoneDialog(_context);
			dialog.show();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}


	public void dismissProgress() {

		try {
			if (dialog != null) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                    dialog = null;
                }
            }
		} catch (Exception e) {
			e.printStackTrace();
		}


	}

	// ====================SET FONT SIZE==================//
	public Typeface get_ubntu_regular() {
		Typeface raleway_font = Typeface.createFromAsset(_context.getAssets(),
				"Ubuntu-R.ttf");
		return raleway_font;
	}
	
	public Typeface get_proxima_regusr() {
		Typeface raleway_font = Typeface.createFromAsset(_context.getAssets(),
				"ProximaNova-Regular.otf");
		return raleway_font;
	}
	public Typeface get_proxima_light() {
		Typeface raleway_font = Typeface.createFromAsset(_context.getAssets(),
				"ProximaNova-Light.otf");
		return raleway_font;
	}

	public Typeface get_ubntu_bold() {
		Typeface typeFace = Typeface.createFromAsset(_context.getAssets(),
				"Ubuntu-B.ttf");
		return typeFace;
	}
	public Typeface get_chalkduster() {
		Typeface typeFace = Typeface.createFromAsset(_context.getAssets(),
				"Chalkduster.ttf");
		return typeFace;
	}



	
	public File createFile(String fileName) {
		File f = null;
		try {
			f = new File(getRootFile(), fileName);
			if (f.exists()) {
				f.delete();
			}

			f.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return f;
	}

	public static String getLocation(Context context, double latitude,
			double longitude) throws IOException {

		String address;
		String city;
		String country;

		try {
			Geocoder geocoder;
			List<Address> addresses;
			geocoder = new Geocoder(context, Locale.getDefault());
			addresses = geocoder.getFromLocation(latitude, longitude, 1);

			address = addresses.get(0).getAddressLine(0);
			city = addresses.get(0).getAddressLine(1);
			country = addresses.get(0).getAddressLine(2);

		} catch (Exception e) {
			address = "";
			city = "";
			country = "";
		}
		return address + ", " + city;
	}

	public static String getDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy hh:mm aa");
		// SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		Calendar cal = Calendar.getInstance();
		// sdf.applyPattern("dd MMM yyyy");
		String strDate = sdf.format(cal.getTime());
		return strDate;
	}





	public static void hideKeyboardFrom(Context context, View view) {
		InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(view.getWindowToken(), 0);


	}

	public static int randInt(int min, int max) {
		// Usually this should be a field rather than a method variable so
		// that it is not re-seeded every call.
		Random rand = new Random();

		// nextInt is normally exclusive of the top value,
		// so add 1 to make it inclusive
		int randomNum = rand.nextInt((max - min) + 1) + min;

		return randomNum;
	}

	public String BitMapToString(Bitmap bitmap){
		ByteArrayOutputStream ByteStream=new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG,90, ByteStream);

		bitmap.recycle();
		bitmap = null;
		byte [] b=ByteStream.toByteArray();
		String temp=Base64.encodeToString(b, Base64.DEFAULT);


		return temp;


	}




	public Bitmap getBitmap(Uri uri) {


		InputStream in = null;
		try {
			final int IMAGE_MAX_SIZE = 204800; // 200kb
			in = _context.getContentResolver().openInputStream(uri);

			// Decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(in, null, o);
			in.close();


			int scale = 1;
			while ((o.outWidth * o.outHeight) * (1 / Math.pow(scale, 2)) >
					IMAGE_MAX_SIZE) {
				scale++;
			}
			Log.d("", "scale = " + scale + ", orig-width: " + o.outWidth + ", orig-height: " + o.outHeight);

			Bitmap b = null;
			in = _context.getContentResolver().openInputStream(uri);
			if (scale > 1) {
				scale--;
				// scale to max possible inSampleSize that still yields an image
				// larger than target
				o = new BitmapFactory.Options();
				o.inSampleSize = scale;
				b = BitmapFactory.decodeStream(in, null, o);

				// resize to desired dimensions
				int height = b.getHeight();
				int width = b.getWidth();
				Log.d("", "1th scale operation dimenions - width: " + width + ", height: " + height);

				double y = Math.sqrt(IMAGE_MAX_SIZE
						/ (((double) width) / height));
				double x = (y / height) * width;

				Bitmap scaledBitmap = Bitmap.createScaledBitmap(b, (int) x,
						(int) y, true);
								b.recycle();
				b = scaledBitmap;

				System.gc();
			} else {
				b = BitmapFactory.decodeStream(in);
			}
			in.close();

			Log.d("", "bitmap size - width: " + b.getWidth() + ", height: " +
					b.getHeight());
			return b;
		} catch (IOException e) {
			Log.e("", e.getMessage(), e);
			return null;
		}
	}


	public boolean has_image(@NonNull ImageView view) {
		Drawable drawable = view.getDrawable();
		boolean hasImage = (drawable != null);

		if (hasImage && (drawable instanceof BitmapDrawable)) {
			hasImage = ((BitmapDrawable)drawable).getBitmap() != null;
		}

		return hasImage;
	}

	public int screen_size(){

        int size=20;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) _context).getWindowManager()
                .getDefaultDisplay()
                .getMetrics(displayMetrics);
//        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;


		switch(width) {
			case 720:
				size = 20;;
				break;
			case 1080:
				size = 30;
				break;
			case 1440:
				size = 35;
				break;
			default:
				size = 20;;
		}
		return size;
	}

	public int post_details_size(){

		int size=470;
		DisplayMetrics displayMetrics = new DisplayMetrics();
		((Activity) _context).getWindowManager()
				.getDefaultDisplay()
				.getMetrics(displayMetrics);
//        int height = displayMetrics.heightPixels;
		int width = displayMetrics.widthPixels;


		switch(width) {
			case 720:
				size = 470;
				break;
			case 1080:
				size = 620;
				break;
			case 1440:
				size = 720;
				break;
			default:
				size = 470;
		}
		return size;
	}

    public int screen_resolution_width(){


        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) _context).getWindowManager()
                .getDefaultDisplay()
                .getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        return width;
    }




	public void share_options(String shareBody ) {


		try {

			Intent sharingIntent = new Intent(Intent.ACTION_SEND);
			sharingIntent.setType("text/plain");
			sharingIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
			sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Skwibble");
			sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
			_context.startActivity(Intent.createChooser(sharingIntent, "Skwibble share"));




		} catch (Exception e) {
			make_toast("App not Installed");
		}

	}

	public boolean emailValidator(String email)
	{
		return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
	}



	public void firebase_analytics(String event_name) {
		save = new SaveData(_context);
		Bundle bundle = new Bundle();
		bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "" + save.getInt(Definitions.id));
		bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, event_name);
		bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, event_name);
		InitializeActivity.getFirebaseAnalytics().logEvent(event_name, bundle);
		InitializeActivity.getFirebaseAnalytics().setAnalyticsCollectionEnabled(true);

		if (!save.getBoolean(Definitions.has_child)) {
			InitializeActivity.getFirebaseAnalytics().setUserProperty("Child", "withoutChild");
		} else {
			InitializeActivity.getFirebaseAnalytics().setUserProperty("Child", "withChild");
		}
		InitializeActivity.getFirebaseAnalytics().setUserProperty(event_name, save.getString(Definitions.user_name));
		InitializeActivity.getFirebaseAnalytics().setUserId("" + save.getInt(Definitions.id));
		InitializeActivity.getFirebaseAnalytics().setSessionTimeoutDuration(1000000);


	}

	public  String getMimeType(String url) {

		final String emptyExtension = "";
		if(url == null){
			return emptyExtension;
		}
		int index = url.lastIndexOf(".");
		if(index == -1){
			return emptyExtension;
		}
		String ext=url.substring(index + 1);
		if(ext.equalsIgnoreCase("mp4")){
			ext="video/mp4";
		}else {
			ext="image/jpeg";
		}

		return ext;
	}

	public static boolean checkURL(CharSequence input) {
		if (TextUtils.isEmpty(input)) {
			return false;
		}
		Pattern URL_PATTERN = Patterns.WEB_URL;
		boolean isURL = URL_PATTERN.matcher(input).matches();
		if (!isURL) {
			String urlString = input + "";
			if (URLUtil.isNetworkUrl(urlString)) {
				try {
					new URL(urlString);
					isURL = true;
				} catch (Exception e) {
				}
			}
		}
		return isURL;
	}

	public boolean is_image(File file)
	{
		final String[] okFileExtensions =  new String[] {"jpg", "png", "gif","jpeg"};

		for (String extension : okFileExtensions)
		{
			if (file.getName().toLowerCase().endsWith(extension))
			{
				return true;
			}
		}
		return false;
	}
	public String set_date(int d, int m, int y) {

		m=m+1;
		return d+"-"+m+"-"+y;
	}


	public void showpopup(final int image,final String name) {
		((Activity) _context).runOnUiThread(new Runnable() {

			@Override
			public void run() {
				try {
					LayoutInflater inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					final View layout = inflater.inflate(R.layout.bod_buy_popup,null,false);
					final PopupWindow pwindo;pwindo = new PopupWindow(layout, AbsListView.LayoutParams.MATCH_PARENT, AbsListView.LayoutParams.MATCH_PARENT, true);



					new Handler().postDelayed(new Runnable(){

						public void run() {
							try {
								pwindo.showAtLocation(layout, Gravity.CENTER, 0, 0);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}

					}, 100L);

					pwindo.setOutsideTouchable(false);

					pwindo.setFocusable(true);
					save=new SaveData(_context);
					ImageView my_showcase = (ImageView) layout.findViewById(R.id.my_showcase);
					my_showcase.setImageResource(image);
					my_showcase.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							pwindo.dismiss();


						}
					});
					save.save(name,true);

				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});

	}



}
