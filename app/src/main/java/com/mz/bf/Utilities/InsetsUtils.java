package com.mz.bf.Utilities;

import android.view.View;
import android.view.ViewGroup;

import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class InsetsUtils {

    // Android 15+ (targetSdk 35+) draws edge-to-edge, so the toolbar would sit under the
    // status bar and its icons would not receive clicks; grow it by the status bar height
    public static void applyStatusBarInset(View toolbar) {
        if (toolbar == null) return;
        final int baseHeight = toolbar.getLayoutParams().height;
        ViewCompat.setOnApplyWindowInsetsListener(toolbar, (v, insets) -> {
            int top = insets.getInsets(WindowInsetsCompat.Type.systemBars()
                    | WindowInsetsCompat.Type.displayCutout()).top;
            v.setPadding(v.getPaddingLeft(), top, v.getPaddingRight(), v.getPaddingBottom());
            ViewGroup.LayoutParams lp = v.getLayoutParams();
            lp.height = baseHeight + top;
            v.setLayoutParams(lp);
            return insets;
        });
    }
}
