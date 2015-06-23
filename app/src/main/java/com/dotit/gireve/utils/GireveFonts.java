package com.dotit.gireve.utils;

import android.content.Context;
import android.graphics.Typeface;

/**
 * @author Hichem LAROUSSI
 * Copyright (c) 2015. All rights reserved.
 */

public class GireveFonts {
	
	private static Typeface ClanProBold;
    private static Typeface ClanProBook;
	
	public static void Init(Context context){
        ClanProBold     = Typeface.createFromAsset(context.getAssets(), "fonts/ClanPro-Bold.otf");
        ClanProBook     = Typeface.createFromAsset(context.getAssets(), "fonts/ClanPro-Book.otf");
	}
	
	public static Typeface getClanProBold() {
		return ClanProBold;
	}
    public static Typeface getClanProBook() {
        return ClanProBook;
    }
}
