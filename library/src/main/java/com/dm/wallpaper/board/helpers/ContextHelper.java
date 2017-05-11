package com.dm.wallpaper.board.helpers;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.view.ContextThemeWrapper;
import android.view.View;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/*
 * Wallpaper Board
 *
 * Copyright (c) 2017 Dani Mahardhika
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

public class ContextHelper {

    @NonNull
    public static Context getBaseContext(@NonNull View view) {
        Context context = view.getContext();
        if (context instanceof ContextThemeWrapper) {
            context = ((ContextThemeWrapper) view.getContext()).getBaseContext();
        } else if (context instanceof CalligraphyContextWrapper) {
            context = ((CalligraphyContextWrapper) view.getContext()).getBaseContext();
        }
        return context;
    }
}
