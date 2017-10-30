package org.aospextended.aexpapers.activities;

import android.support.annotation.NonNull;

import com.dm.wallpaper.board.activities.WallpaperBoardMuzeiActivity;
import org.aospextended.aexpapers.services.MuzeiService;

public class MuzeiActivity extends WallpaperBoardMuzeiActivity {

    @NonNull
    @Override
    public Class<?> onInit() {
        return MuzeiService.class;
    }
}
