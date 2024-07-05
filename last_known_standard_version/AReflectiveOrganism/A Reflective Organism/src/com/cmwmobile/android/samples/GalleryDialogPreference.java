/**
 * Copyright CMW Mobile.com, 2011.
 */
package com.cmwmobile.android.samples;

import com.formisk.areflectiveorganism.R;

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
	private Gallery gallery = null;

	private int[] iconIds = {
	};

	public GalleryDialogPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public void addBitmap(int id) {
		int[] iconIds2 = new int[iconIds.length+1];
		for(int i = 0; i < iconIds.length; i++) {
			iconIds2[i] = iconIds[i]; 
		}
		iconIds = iconIds2;
		iconIds[iconIds.length-1] = id;
	}

	private Bitmap getBitmap(int index) {
		return BitmapFactory.decodeResource(getContext().getResources(),iconIds[index]);
	}

	protected View onCreateDialogView() {
		LayoutInflater layoutInflater = LayoutInflater.from(getContext());

		View view = layoutInflater.inflate(
			R.layout.gallerypreference_layout, null);

		int defaultIndex = getPersistedInt(0);

		gallery = (Gallery)view.findViewById(R.id.gallery);

		gallery.setAdapter(new ImageAdapter(getContext(), iconIds));
		gallery.setSelection(defaultIndex);

		return view;
	}

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
