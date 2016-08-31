package cl.json;

import java.io.File;
import android.net.Uri;

import android.content.Intent;
import android.content.ActivityNotFoundException;


import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.Callback;

import android.content.pm.ResolveInfo;
import java.util.List;

public class RNShareModule extends ReactContextBaseJavaModule {

  ReactApplicationContext reactContext;

	private static final String EXTRA_PROTOCOL_VERSION = "com.facebook.orca.extra.PROTOCOL_VERSION";
	private static final String EXTRA_APP_ID = "com.facebook.orca.extra.APPLICATION_ID";
	private static final int PROTOCOL_VERSION = 20150314;
	private static final String YOUR_APP_ID = "XXXXXXXXXXXX";
	private static final int SHARE_TO_MESSENGER_REQUEST_CODE = 1;

  public RNShareModule(ReactApplicationContext reactContext) {
    super(reactContext);
    this.reactContext = reactContext;
  }

  @Override
  public String getName() {
    return "RNShare";
  }

  @ReactMethod
  public void open(ReadableMap options, Callback callback) {
    Intent share = new Intent(android.content.Intent.ACTION_SEND);
    share.setType("text/plain");
    //share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
    if (options.hasKey("share_text") && !options.isNull("share_text")) {
      share.putExtra(Intent.EXTRA_SUBJECT, options.getString("share_text"));
    }
    if (options.hasKey("share_URL") && !options.isNull("share_URL")) {
      share.putExtra(Intent.EXTRA_TEXT, options.getString("share_URL"));
    }
    String title = "Share";
    if (options.hasKey("title") && !options.isNull("title")) {
      title = options.getString("title");
    }
    try {
      Intent chooser = Intent.createChooser(share, title);
      chooser.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
      this.reactContext.startActivity(chooser);
      callback.invoke("OK");
    } catch (ActivityNotFoundException ex) {
      callback.invoke("not_available");
    }
  }

  @ReactMethod
  public void openWhatsapp(ReadableMap options, Callback callback) {

    Intent share = new Intent(android.content.Intent.ACTION_SEND);
    share.setType("text/plain");
		share.setPackage("com.whatsapp");
		share.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

    if (options.hasKey("share_text") && !options.isNull("share_text")) {
      share.putExtra(Intent.EXTRA_TEXT, options.getString("share_text"));
    }

    try {
      this.reactContext.startActivity(share);
      callback.invoke("OK");
    } catch (ActivityNotFoundException ex) {
      callback.invoke("not_available");
    }
  }

  @ReactMethod
  public void openWhatsappPicture(ReadableMap options, Callback callback) {

    Intent share = new Intent(android.content.Intent.ACTION_SEND);
    share.setType("image/png");
		share.setPackage("com.whatsapp");
		share.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

    //if (options.hasKey("share_text") && !options.isNull("share_text")) {
      //share.putExtra(Intent.EXTRA_TEXT, options.getString("share_text"));
    //}

		share.putExtra(Intent.EXTRA_STREAM, options.getString("image_url"));

    try {
      this.reactContext.startActivity(share);
      callback.invoke("OK");
    } catch (ActivityNotFoundException ex) {
      callback.invoke("not_available");
    }
  }

  @ReactMethod
  public void openMessenger(ReadableMap options, Callback callback) {

    Intent share = new Intent(android.content.Intent.ACTION_SEND);
		share.setPackage("com.facebook.ocra");
		//share.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

    share.setType("image/*");
		share.putExtra(Intent.EXTRA_STREAM, options.getString("image_url"));
		share.putExtra(EXTRA_PROTOCOL_VERSION, PROTOCOL_VERSION);
		share.putExtra(EXTRA_APP_ID, YOUR_APP_ID);

    try {
      this.reactContext.startActivity(share);
      callback.invoke("OK");
    } catch (ActivityNotFoundException ex) {
      callback.invoke("not_available");
    }
  }

  @ReactMethod
  public void generalShare(ReadableMap options, Callback callback) {

    String type = "image/*";
    String imagePath = options.getString("image_path");

    File media = new File(imagePath);
    Uri uri = Uri.fromFile(media);


    Intent share = new Intent(android.content.Intent.ACTION_SEND);
    share.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    share.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

    if (options.hasKey("share_text") && !options.isNull("share_text")) {
      share.putExtra(Intent.EXTRA_SUBJECT, options.getString("share_text"));
    }

    share.setType(type);
    share.putExtra(Intent.EXTRA_STREAM, uri);
		//share.putExtra(Intent.EXTRA_STREAM, imagePath);

    try {
      Intent chooser = Intent.createChooser(share, "Share to");
      chooser.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
      this.reactContext.startActivity(chooser);
      callback.invoke("OK");
    } catch (ActivityNotFoundException ex) {
      callback.invoke("not_available");
    }
  }

  @ReactMethod
  public void shareTwitter(ReadableMap options, Callback callback) {

    String type = "image/*";
    String imagePath = options.getString("image_path");

    File media = new File(imagePath);
    Uri uri = Uri.fromFile(media);


    Intent share = new Intent(android.content.Intent.ACTION_SEND);
    share.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

    if (options.hasKey("share_text") && !options.isNull("share_text")) {
      share.putExtra(Intent.EXTRA_SUBJECT, options.getString("share_text"));
    }

    String title = "Share";
    if (options.hasKey("title") && !options.isNull("title")) {
      title = options.getString("title");
    }

    share.setType(type);
    share.putExtra(Intent.EXTRA_STREAM, uri);


    // Narrow down to official Twitter app, if available:
    List<ResolveInfo> matches = this.reactContext.getPackageManager().queryIntentActivities(share, 0);
    for (ResolveInfo info : matches) {
      if (info.activityInfo.packageName.toLowerCase().startsWith("com.twitter")) {
        share.setPackage(info.activityInfo.packageName);
      }
    }

    try {
      Intent chooser = Intent.createChooser(share, "Share to");
      chooser.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
      this.reactContext.startActivity(chooser);
      callback.invoke("OK");
    } catch (ActivityNotFoundException ex) {
      callback.invoke("not_available");
    }
  }

  @ReactMethod
  public void shareInstagram(ReadableMap options, Callback callback) {

    String type = "image/*";
    String imagePath = options.getString("image_path");

    File media = new File(imagePath);
    Uri uri = Uri.fromFile(media);


    Intent share = new Intent(android.content.Intent.ACTION_SEND);
    share.setPackage("com.instagram.android");
    share.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);


    share.setType(type);
    share.putExtra(Intent.EXTRA_STREAM, uri);
		//share.putExtra(Intent.EXTRA_STREAM, imagePath);

    try {
      //Intent chooser = Intent.createChooser(share, "Share to");
      //chooser.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
      this.reactContext.startActivity(share);
      callback.invoke("OK");
    } catch (ActivityNotFoundException ex) {
      callback.invoke("not_available");
    }
  }
}
