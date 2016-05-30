package com.wzq.xl.cropperimg.toasttool;


import android.content.Context;
import android.widget.Toast;

/*
* @author wzq
* @createtime 20160203
*/


public class ShowToastTool {
	/**
	 *
	 * @param
	 * @return void
	 * @throws
	 */
	public static void Short(Context context, CharSequence sequence) {

		Toast.makeText(context, sequence, Toast.LENGTH_SHORT).show();
	}

	public static void Long(Context context, CharSequence sequence) {
		Toast.makeText(context, sequence, Toast.LENGTH_LONG).show();
	}

	public static void Short(Context context, String sequence) {
		Toast.makeText(context, sequence, Toast.LENGTH_SHORT).show();
		
	}
}
