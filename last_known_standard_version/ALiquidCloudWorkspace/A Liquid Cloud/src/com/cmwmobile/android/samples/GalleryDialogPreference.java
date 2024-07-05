/**
 * Copyright CMW Mobile.com, 2011.
 */
package com.cmwmobile.android.samples;

import com.formisk.aliquidcloud.ALiquidCloudWallpaper;
import com.formisk.aliquidcloud.R;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.preference.DialogPreference;

import android.util.AttributeSet;

import android.view.LayoutInflater;
import android.view.View;

import android.widget.AdapterView;
import android.widget.Gallery;
import android.widget.ImageView;

import android.widget.AdapterView.OnItemClickListener;

/**
 * The GalleryDialogPreference class is a DialogPreference based and provides a
 * gallery preference.
 * @author Casper Wakkers
 */
public class GalleryDialogPreference extends DialogPreference {
	// Layout widgets.
	private Gallery gallery = null;
	//private ImageView imageView = null;

	// Array with icons.
	private int[] iconIds = {
		R.drawable.libertyscreen,
		R.drawable.noirscreen,
		R.drawable.phosphorscreen,
		R.drawable.metallicwavescreen,
		R.drawable.moltenlightscreen,
		R.drawable.purplescreen,
		R.drawable.skyscreen,
		R.drawable.rainbowscreen
	};

	private int[] iconIdsFree = {
			R.drawable.libertyscreen,
			R.drawable.noirscreen,
			R.drawable.phosphorfreescreen,
			R.drawable.metallicwavescreen,
			R.drawable.moltenlightfreescreen,
			R.drawable.purplefreescreen,
			R.drawable.skyfreescreen,
			R.drawable.rainbowfreescreen
		};

	/**
	 * The GalleryDialogPreference constructor.
	 * @param context of this preference.
	 * @param attrs custom xml attributes.
	 */
	public GalleryDialogPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	/**
	 * Method geBitmap retrieves the requested bitmap.
	 * @param index of the bitmap to be retrieved.
	 * @return Bitmap representing the bitmap.
	 */
	private Bitmap getBitmap(int index) {
		return BitmapFactory.decodeResource(getContext().getResources(),iconIds[index]);
	}
	/**
	 * {@inheritDoc}
	 */
	protected View onCreateDialogView() {
		LayoutInflater layoutInflater = LayoutInflater.from(getContext());

		View view = layoutInflater.inflate(
			R.layout.gallerypreference_layout, null);

		int defaultIndex = getPersistedInt(0);

		gallery = (Gallery)view.findViewById(R.id.gallery);

		gallery.setAdapter(new ImageAdapter(getContext(), iconIds));
		gallery.setSelection(defaultIndex);
		gallery.setOnItemClickListener(new OnItemClickListener() {
			/**
			 * {@inheritDoc}
			 */
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				//imageView.setImageBitmap(getBitmap(position));
			}
		});

		/*imageView = (ImageView)view.findViewById(R.id.galleryImage);

		imageView.setImageBitmap(getBitmap(defaultIndex));*/

		return view;
	}
	/**
	 * {@inheritDoc}
	 */
	public void onClick(DialogInterface dialog, int which) {
		// if the positive button is clicked, we persist the value.
		if (which == DialogInterface.BUTTON_POSITIVE) {
			if (shouldPersist()) {
				persistInt(gallery.getSelectedItemPosition());
			}
		}

		super.onClick(dialog, which);
	}
}
