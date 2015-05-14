package com.artisan.android.utility;

import android.content.Context;
import android.content.res.Resources;

public final class ResourcesUtility {
	
	public static final String TYPE_LAYOUT = "layout";
	public static final String TYPE_STRING = "string";
	public static final String TYPE_DRAWABLE = "drawable";
	public static final String TYPE_STYLE = "style";
	public static final String TYPE_ID = "id";
	public static final String TYPE_COLOR = "color";
	
	private ResourcesUtility() {
	}

	public static int getLayoutId(Context context,String name) {
		return getId(context, TYPE_LAYOUT, name);
	}

	public static int getStringId(Context context,String name) {
		return getId(context, TYPE_STRING, name);
	}

	public static int getDrawableId(Context context,String name) {
		return getId(context, TYPE_DRAWABLE, name);
	}

	public static int getStyleId(Context context,String name) {
		return getId(context, TYPE_STYLE, name);
	}

	public static int getIdId(Context context,String name) {
		return getId(context, TYPE_ID, name);
	}

	public static int getColorId(Context context,String name) {
		return getId(context, TYPE_COLOR, name);
	}

	public static int getId(Context context,String type,String name) {
		Resources resources = context.getResources();
		return resources.getIdentifier(name, type, context.getPackageName());
	}
}