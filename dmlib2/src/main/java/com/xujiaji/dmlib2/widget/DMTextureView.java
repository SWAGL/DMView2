package com.xujiaji.dmlib2.widget;
/*
 * Copyright 2018 xujiaji
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.SurfaceTexture;
import android.util.AttributeSet;
import android.view.Surface;
import android.view.TextureView;

import com.xujiaji.dmlib2.DM;
import com.xujiaji.dmlib2.Direction;
import com.xujiaji.dmlib2.R;
import com.xujiaji.dmlib2.SurfaceProxy;
import com.xujiaji.dmlib2.Util;

public class DMTextureView extends TextureView implements TextureView.SurfaceTextureListener, DM {
    private Surface mSurface;
    private Controller mController;

    public DMTextureView(Context context) {
        this(context, null);
    }

    public DMTextureView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DMTextureView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.setSurfaceTextureListener(this);
        mController = new Controller();
        setOpaque(false);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DMTextureView, defStyleAttr, 0);

        final Direction direction = Direction.getType(a.getInt(R.styleable.DMTextureView_dm_direction, Direction.RIGHT_LEFT.value));
        final int span = a.getDimensionPixelOffset(R.styleable.DMTextureView_dm_span, Util.dp2px(context, 2));
        final int sleep = a.getInteger(R.styleable.DMTextureView_dm_sleep, 0);
        final int spanTime = a.getInteger(R.styleable.DMTextureView_dm_span_time, 0);
        final int vSpace = a.getDimensionPixelOffset(R.styleable.DMTextureView_dm_v_space, Util.dp2px(context, 10));
        final int hSpace = a.getDimensionPixelOffset(R.styleable.DMTextureView_dm_h_space, Util.dp2px(context, 10));

        a.recycle();

        mController.setDirection(direction);
        mController.sethSpace(hSpace);
        mController.setvSpace(vSpace);
        mController.setSpan(span);
        mController.setSpanTime(spanTime == 0 ? sleep : spanTime);
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        mSurface = new Surface(surface);
        mController.init(width, height, new SurfaceProxy(mSurface));
        mController.draw(0, true);
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        if (mSurface != null) mSurface.release();
        mSurface = null;
        mController.destroy();
        return true;
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        if (mSurface == null) return;
        if (hasWindowFocus) {
            mController.prepare();
        } else {
            mController.pause();
        }
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }

    @Override
    public Controller getController() {
        return mController;
    }
}
