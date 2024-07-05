package com.formisk.areflectiveorganism;

import android.app.Activity;
import android.content.SharedPreferences;
import net.robotmedia.billing.BillingController;
import net.robotmedia.billing.BillingController.BillingStatus;
import net.robotmedia.billing.BillingRequest.ResponseCode;
import net.robotmedia.billing.helper.AbstractBillingObserver;
import net.robotmedia.billing.model.Transaction.PurchaseState;

public class CustomBilling implements BillingController.IConfiguration {
	public Activity activity;
	public SharedPreferences preferences = null;
	private WallpaperActionbarSettings settingsScreen = null;
	protected AbstractBillingObserver mBillingObserver;
	public boolean selectedTab = true;
	
	public CustomBilling(Activity activity, SharedPreferences preferences, WallpaperActionbarSettings settingsScreen) {
		this.activity = activity;
		this.preferences = preferences;
		this.settingsScreen = settingsScreen;
	}
	
	// BILLING STUFF BEGIN ----------------------------------------------------------------

	protected void onCreate(android.os.Bundle savedInstanceState) {
		mBillingObserver = new AbstractBillingObserver(this.activity) {

			public void onBillingChecked(boolean supported) {
				System.out.println("onBillingChecked: " + supported);
			}
			
			public void onSubscriptionChecked(boolean supported) {
				System.out.println("onSubscriptionChecked: " + supported);
			}
			
//09-11 08:06:40.363: I/System.out(15072): onPurchaseStateChanged: com.formisk.test3 REFUNDED
//09-11 08:06:40.403: I/System.out(15072): onPurchaseStateChanged: com.formisk.areflectiveorganism.unlockall CANCELLED


			public void onPurchaseStateChanged(String itemId, PurchaseState state) {
				System.out.println("onPurchaseStateChanged: " + itemId + " " + state);
				if(state == PurchaseState.PURCHASED) {
					System.out.println("PURCHASED");
					preferences.edit().putInt("PURCHASED", 1).commit();
					settingsScreen.addPreferences(selectedTab);
				}
				else if(state == PurchaseState.CANCELLED) {
					preferences.edit().putInt("PURCHASED", 0).commit();
					settingsScreen.addPreferences(selectedTab);
					BillingController.confirmNotifications(this.activity, itemId);
				}
				else if(state == PurchaseState.REFUNDED) {
					BillingController.confirmNotifications(this.activity, itemId);
				}
				else {
					BillingController.confirmNotifications(this.activity, itemId);
				}
			}

			public void onRequestPurchaseResponse(String itemId, ResponseCode response) {
				System.out.println("onRequestPurchaseResponse: " + itemId + " " + response);
			}
		};
		BillingController.registerObserver(mBillingObserver);
		BillingController.setConfiguration(this); // This activity will provide
		// the public key and salt
		this.checkBillingSupported();
		if (!mBillingObserver.isTransactionsRestored()) {
			BillingController.restoreTransactions(this.activity);
		}
	}

	protected void onDestroy() {
		BillingController.unregisterObserver(mBillingObserver); // Avoid
																// receiving
		// notifications after
		// destroy
		BillingController.setConfiguration(null);
	}
	
	public BillingStatus checkBillingSupported() {
		return BillingController.checkBillingSupported(this.activity);
	}
	public BillingStatus checkSubscriptionSupported() {
		return BillingController.checkSubscriptionSupported(this.activity);
	}

	public void onBillingChecked(boolean supported) {
	}
	
	public void onSubscriptionChecked(boolean supported) {
		
	}
	public void onPurchaseStateChanged(String itemId, PurchaseState state) {
		System.out.println("onPurchaseStateChanged: " + itemId + " " + state);
	}

	public String getPublicKey() {
		return "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAuE/UXATb5zB3rcQ+Zct9ytDayrvah5g6iXlbFRpFAw7+5dw+sPyKiRFE+2zOioqS+wPuB4DfviCekL/yoN4/Yhn3sA5iFyb5K+YSQZYe/caj2HOYwBwJiC+8xVHGjG0JPdq2TB8AamfuGvWmxQhsf3RwcW7O31SuhWGbH61IR8q3CObjb1IUlA0JkaiS2k46C6QKiddrVXG0t0ove8Y9Lpeks/7xdtqK7s8dimIc6UwPXtvaRCjglAQyiM2pwJ3y3APhM6/j0ancZPvQ3UCg3xJHUhXsjyys0yZaUDXpXfDiUpDAWAhe0MQj287TJhnGXNCmbx9y8DYo5fuSVRFnqwIDAQAB";
	}
	
	public byte[] getObfuscationSalt() {
		return new byte[] {41, -21, -16, -43, 6, -55, 12, -31, -123, -93, -68, 67, 42, 15, 51, 75, 56, 118, 43, -15};
	}
	
	public void onRequestPurchaseResponse(String itemId, ResponseCode response) {
		System.out.println("onRequestPurchaseResponse: " + itemId + " " + response);
	}

	public void requestPurchase(String itemId) {
		BillingController.requestPurchase(this.activity, itemId);
	}

	public void requestSubscription(String itemId) {
		BillingController.requestSubscription(this.activity, itemId);
	}

	public void restoreTransactions() {
		BillingController.restoreTransactions(this.activity);
	}
	
	// BILLING STUFF END   ----------------------------------------------------------------	
}
