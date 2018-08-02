package com.skwibble.skwibblebook.user_group;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.skwibble.skwibblebook.R;
import com.skwibble.skwibblebook.utility.Definitions;
import com.skwibble.skwibblebook.utility.UsefullData;


public class WebView_link extends Activity{
	private WebView webView;
	private ProgressBar progress;
	private LinearLayout back;
	UsefullData objUsefullData;
	TextView label;
	String url;



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.web_link);
		progress = (ProgressBar) findViewById(R.id.web_view_progress);
		back=(LinearLayout) findViewById(R.id.my_button);
		label=(TextView) findViewById(R.id.textViezw7_reset);

		label.setText(getResources().getString(R.string.text_tc_small));
		objUsefullData = new UsefullData(WebView_link.this);
		url= Definitions.APIdomain+Definitions.TERMS;


		label.setTypeface(objUsefullData.get_proxima_regusr());
		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				finish();
			}
		});
		setWebView();


	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (webView == null) {
			webView.freeMemory();
			webView.destroy();
			webView = null;
		}


	}

	@Override
	protected void onPause() {
		super.onPause();
		if (webView != null) {
			webView.onPause();
			webView.pauseTimers();
		}

	}

	@Override
	protected void onResume() {
		super.onResume();
		if (webView != null) {
			webView.onResume();
			webView.resumeTimers();
		}
	}



	@SuppressLint("SetJavaScriptEnabled")
	private void setWebView(){


		webView = (WebView) findViewById(R.id.webView1);
		webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
		webView.setScrollbarFadingEnabled(false);
		webView.setInitialScale(1);

		WebSettings settings = webView.getSettings();
		settings.setJavaScriptEnabled(true);		// Enable to run Javascript

		// set width of web page is same as webview
		settings.setLoadWithOverviewMode(true);
		settings.setUseWideViewPort(true);


		//set the webview can be zoom in/out
		settings.setSupportZoom(true);
		settings.setBuiltInZoomControls(true);

		int sdkVersion = android.os.Build.VERSION.SDK_INT;
		if (sdkVersion > 10) {
			settings.setDisplayZoomControls(true);	// Display zoom in/out tool
		}

		webView.setWebViewClient(webViewClient); 		// set the default browser
		webView.setWebChromeClient(webChromeClient);	// Display the tile in the title of Activity 
		webView.loadUrl(url);

	}

	WebViewClient webViewClient = new WebViewClient() {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {

			if(!url.contains("about:blank"))
			{
//				webView.loadUrl(url);
				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
				startActivity(browserIntent);
			}
			return true;
		}
//		public void onPageFinished(WebView view , String url){
//			view.loadUrl("javascript:clickFunction()");
//		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {

			//run the javascript
			String js = "function doLoad() {"
					+ "HideLayer('cntr_us_header');"
					+ "HideLayer('cntr_footer');" + "}";
			js += "function HideLayer(layerid){"
					+ "var layer = document.getElementById(layerid);"
					+ "if(layer != null){" + "layer.style.display= 'none';"
					+ "}}";

			js = js + "if ( window.addEventListener ) { "
					+ "window.addEventListener( 'load', doLoad, false );"
					+ "} else if ( window.attachEvent ) { "
					+ "window.attachEvent( 'onload', doLoad );"
					+ " } else if ( window.onLoad ) {"
					+ "window.onload = doLoad;}";


			view.loadUrl("javascript:" + js + ";");


			super.onPageStarted(view, url, favicon);

		}


	};

	WebChromeClient webChromeClient = new WebChromeClient() {

		@Override
		public void onProgressChanged(WebView view, int newProgress) {
			super.onProgressChanged(view, newProgress);

			// show the progress bar when loading the web page

			progress.setProgress(newProgress);
			if (newProgress == 100) {
				progress.setVisibility(View.GONE);
			}else {
				progress.setVisibility(View.VISIBLE);
			}

		}

		@Override
		public void onReceivedTitle(WebView view, String title) {

		}
	};

//	public boolean onKeyDown(int keyCode, KeyEvent event)
//	{
//		if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
//			onBackPressed();
//			return true;
//		}
//		return super.onKeyDown(keyCode, event);
//	}

//	@Override
//	public void onBackPressed() {
//		webView.goBack();
//
//
//	}


}
