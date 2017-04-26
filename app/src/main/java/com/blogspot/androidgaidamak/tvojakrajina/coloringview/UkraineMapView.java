package com.blogspot.androidgaidamak.tvojakrajina.coloringview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.IdRes;
import android.support.annotation.StringRes;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import com.blogspot.androidgaidamak.tvojakrajina.R;
import com.blogspot.androidgaidamak.tvojakrajina.databinding.DistrictsBinding;

/**
 * Created by GaidamakUA on 4/25/17.
 */

public class UkraineMapView extends FrameLayout {

    private DistrictsBinding binding;

    public UkraineMapView(Context context) {
        this(context, null, 0);
    }

    public UkraineMapView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public UkraineMapView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        binding = DistrictsBinding.inflate(layoutInflater, this, true);
    }

    public void paintOblast(Oblast oblast, @ColorInt int color) {
        Drawable drawable = ((AppCompatImageView) findViewById(oblast.getId())).getDrawable();
        DrawableCompat.setTint(drawable, color);
    }

    public void clearOblast(Oblast oblast) {
        Drawable drawable = ((AppCompatImageView) findViewById(oblast.getId())).getDrawable();
        DrawableCompat.setTint(drawable, Color.TRANSPARENT);
    }

    public Bitmap createBitmap() {
        int width = getWidth();
        int height = getHeight();
        Bitmap b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        layout(getLeft(), getTop(), getRight(), getBottom());
        draw(c);
        return b;
    }

    public enum Oblast {
        CHERKASY_OBLAST(R.id.cherkasy_oblast, R.string.cherkasy_oblast),
        CHERNIHIV_OBLAST(R.id.chernihiv_oblast, R.string.chernihiv_oblast),
        CHERNIVTSI_OBLAST(R.id.chernivtsi_oblast, R.string.chernivtsi_oblast),
        CRIMEAN_OBLAST(R.id.crimean_oblast, R.string.crimean_oblast),
        DNIPROPETROVSK_OBLAST(R.id.dnipropetrovsk_oblast, R.string.dnipropetrovsk_oblast),
        DONETSK_OBLAST(R.id.donetsk_oblast, R.string.donetsk_oblast),
        IVANO_FRANKIVSK_OBLAST(R.id.ivano_frankivsk_oblast, R.string.ivano_frankivsk_oblast),
        KHARKIV_OBLAST(R.id.kharkiv_oblast, R.string.kharkiv_oblast),
        KHERSON_OBLAST(R.id.kherson_oblast, R.string.kherson_oblast),
        KHMELNYTSKYI_OBLAST(R.id.khmelnytskyi_oblast, R.string.khmelnytskyi_oblast),
        KIROVOHRAD_OBLAST(R.id.kirovohrad_oblast, R.string.kirovohrad_oblast),
        KYIV_OBLAST(R.id.kyiv_oblast, R.string.kyiv_oblast),
        LUHANSK_OBLAST(R.id.luhansk_oblast, R.string.luhansk_oblast),
        LVIV_OBLAST(R.id.lviv_oblast, R.string.lviv_oblast),
        MYKOLAIV_OBLAST(R.id.mykolaiv_oblast, R.string.mykolaiv_oblast),
        ODESA_OBLAST(R.id.odesa_oblast, R.string.odesa_oblast),
        POLTAVA_OBLAST(R.id.poltava_oblast, R.string.poltava_oblast),
        RIVNE_OBLAST(R.id.rivne_oblast, R.string.rivne_oblast),
        SUMY_OBLAST(R.id.sumy_oblast, R.string.sumy_oblast),
        TERNOPIL_OBLAST(R.id.ternopil_oblast, R.string.ternopil_oblast),
        VINNYTSIA_OBLAST(R.id.vinnytsia_oblast, R.string.vinnytsia_oblast),
        VOLYN_OBLAST(R.id.volyn_oblast, R.string.volyn_oblast),
        ZAKARPATTIA_OBLAST(R.id.zakarpattia_oblast, R.string.zakarpattia_oblast),
        ZAPORIZHIA_OBLAST(R.id.zaporizhia_oblast, R.string.zaporizhia_oblast),
        ZHYTOMYR_OBLAST(R.id.zhytomyr_oblast, R.string.zhytomyr_oblast);

        @IdRes
        final int id;
        @StringRes
        final int oblastName;

        Oblast(int id, int oblastName) {
            this.id = id;
            this.oblastName = oblastName;
        }

        @IdRes
        public int getId() {
            return id;
        }

        @StringRes
        public int getOblastName() {
            return oblastName;
        }
    }
}
