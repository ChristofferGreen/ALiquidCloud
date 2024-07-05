/**
 * Copyright CMW Mobile.com, 2011. 
 */
package com.cmwmobile.android.samples;

import com.formisk.aliquidcloud.R;

import android.content.SharedPreferences;

import android.os.Bundle;

import android.preference.PreferenceActivity;

/**
 * The SampleGalleryDialogPreferenceSettings is responsible for the handling of
 * this sample settings.
 * @author Casper Wakkers
 */
public class SampleGalleryDialogPreferenceSettings extends
		PreferenceActivity implements
		SharedPreferences.OnSharedPreferenceChangeListener {
	/**
	 * {@inheritDoc}
	 */
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getPreferenceManager().setSharedPreferencesName(
			"com.cmwmobile.android.samples.gallery");
		addPreferencesFromResource(R.xml.settings);
		getPreferenceManager().getSharedPreferences().
			registerOnSharedPreferenceChangeListener(this);
	}
	/**
	 * {@inheritDoc}
	 */
	protected void onDestroy() {
		getPreferenceManager().getSharedPreferences().
			unregisterOnSharedPreferenceChangeListener(this);

		super.onDestroy();
	}
	/**
	 * {@inheritDoc}
	 */
	protected void onResume() {
		super.onResume();
	}
	/**
	 * {@inheritDoc}
	 */
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
	}
}
