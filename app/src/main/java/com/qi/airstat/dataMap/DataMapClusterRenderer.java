package com.qi.airstat.dataMap;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;
import com.qi.airstat.Constants;
import com.qi.airstat.R;

import java.util.Collection;

/**
 * Created by JUMPSNACK on 8/5/2016.
 */
public class DataMapClusterRenderer extends DefaultClusterRenderer<DataMapMarker> {


    View view;
    ImageView imgMarker;
    TextView tvMarker;
    Context context;

    public DataMapClusterRenderer(Context context, GoogleMap map, ClusterManager<DataMapMarker> clusterManager) {
        super(context, map, clusterManager);
        this.context = context;
        view = LayoutInflater.from(context).inflate(R.layout.marker_custom, null);
        imgMarker = (ImageView) view.findViewById(R.id.img_marker);
        tvMarker = (TextView) view.findViewById(R.id.tv_marker);
    }


    @Override
    protected void onBeforeClusterRendered(Cluster<DataMapMarker> cluster, MarkerOptions markerOptions) {


        Collection<DataMapMarker> clusteredMarkers = cluster.getItems();
        IconGenerator clusterIconGenerator = new IconGenerator(context);

        float avgAqiValue = 0;
        for (DataMapMarker marker : clusteredMarkers) {
            avgAqiValue += marker.getAqiValue();
        }

        avgAqiValue = avgAqiValue / clusteredMarkers.size();

        int clusterColor = 0;
        if (0 <= avgAqiValue && avgAqiValue <= 50) {
            clusterColor = Color.parseColor(Constants.AQI_LEVEL_GOOD);
        } else if (51 <= avgAqiValue && avgAqiValue <= 100) {
            clusterColor = Color.parseColor(Constants.AQI_LEVEL_MODERATE);
        } else if (101 <= avgAqiValue && avgAqiValue <= 150) {
            clusterColor = Color.parseColor(Constants.AQI_LEVEL_SENSITIVE);
        } else if (151 <= avgAqiValue && avgAqiValue <= 200) {
            clusterColor = Color.parseColor(Constants.AQI_LEVEL_UNHEALTHY);
        } else if (201 <= avgAqiValue && avgAqiValue <= 300) {
            clusterColor = Color.parseColor(Constants.AQI_LEVEL_VERY_UNHEALTHY);
        } else if (301 <= avgAqiValue && avgAqiValue <= 500) {
            clusterColor = Color.parseColor(Constants.AQI_LEVEL_HAZARDOUS);
        } else {
            clusterColor = Color.parseColor(Constants.AQI_LEVEL_DEFAULT);
        }
//
//        if (cluster.getSize() < 10) {
//            clusterIconGenerator.setContentPadding(100, 20, 0, 0);
//        }
//        else {
//            clusterIconGenerator.setContentPadding(30, 20, 0, 0);
//        }

        Drawable clusterIcon = ResourcesCompat.getDrawable(context.getResources(), R.drawable.cluster_background, null);
        clusterIcon.setColorFilter(clusterColor, PorterDuff.Mode.SRC_ATOP);

        clusterIconGenerator.setBackground(clusterIcon);

        Bitmap icon = clusterIconGenerator.makeIcon(String.format("%.1f", avgAqiValue));
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon));
    }

//    private LayerDrawable makeClusterBackground() {
//        // Outline color
//        int clusterOutlineColor = Color.WHITE);
//
//        this.mColoredCircleBackground = new ShapeDrawable(new OvalShape());
//        ShapeDrawable outline = new ShapeDrawable(new OvalShape());
//        outline.getPaint().setColor(clusterOutlineColor);
//        LayerDrawable background = new LayerDrawable(
//                new Drawable[]{outline, this.mColoredCircleBackground});
//        int strokeWidth = (int) (this.mDensity * 3.0F);
//        background.setLayerInset(1, strokeWidth, strokeWidth, strokeWidth, strokeWidth);
//        return background;
//    }


    @Override
    protected void onBeforeClusterItemRendered(DataMapMarker item, MarkerOptions markerOptions) {
        super.onBeforeClusterItemRendered(item, markerOptions);

//        if(item.getTitle().equals("test3")){
//            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
//        } 1

        double aqiValue = item.getAqiValue();
        String snippetMsg = "";

        if (0 <= aqiValue && aqiValue <= 50) {
            imgMarker.setBackgroundResource(R.drawable.marker_good);
            snippetMsg += "GOOD";
        } else if (50 < aqiValue && aqiValue <= 100) {
            imgMarker.setBackgroundResource(R.drawable.marker_moderate);
            snippetMsg += "MODERATE";
        } else if (100 < aqiValue && aqiValue <= 150) {
            imgMarker.setBackgroundResource(R.drawable.marker_sensitive);
            snippetMsg += "SENSITIVE";
        } else if (150 < aqiValue && aqiValue <= 200) {
            imgMarker.setBackgroundResource(R.drawable.marker_unhealthy);
            snippetMsg += "UNHEALTHY";
        } else if (200 < aqiValue && aqiValue <= 300) {
            imgMarker.setBackgroundResource(R.drawable.marker_very_unhealthy);
            snippetMsg += "VERY UNHEALTHY";
        } else if (300 < aqiValue && aqiValue <= 500) {
            imgMarker.setBackgroundResource(R.drawable.marker_hazardous);
            snippetMsg += "HAZARDOUS";
        } else {
            imgMarker.setBackgroundResource(R.drawable.marker_default);
            snippetMsg += "NO DATA";
        }

        if (item.getTitle().equals("ME")) {
            tvMarker.setText("ME");
        } else {
            tvMarker.setText("");
        }
//        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.map_maroon));
//        setMarkerColor(markerOptions, generateMarkerIcon(colorCode));


//        tvMarker.setText(item.getTitle());
//        tvMarker.setTextColor(Color.WHITE);
//        tvMarker.setBackgroundResource(R.drawable.ic_marker_phone);

        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(createDrawableFromView(DataMapActivity.context, view)));
        markerOptions.title(item.getTitle());
        markerOptions.snippet(snippetMsg + " (" + String.format("%.1f", item.getAqiValue()) + ")");
    }

    // View를 Bitmap으로 변환
    private Bitmap createDrawableFromView(Context context, View view) {

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        view.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        view.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);

        return bitmap;
    }

//    private void setMarkerColor(MarkerOptions markerOptions, BitmapDescriptor bitmapDescriptor) {
////        markerOptions.icon(bitmapDescriptor);
//        markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.map_maroon));
//    }
//
//
//    private BitmapDescriptor generateMarkerIcon(String color) {
//        float[] hsv = new float[3];
//        Color.colorToHSV(Color.parseColor(color), hsv);
//        return BitmapDescriptorFactory.defaultMarker(hsv[0]);
//    }
//


/*Second version
    private Context context;
    private IconGenerator mClusterIconGenerator;

    public DataMapClusterRenderer(Context context, GoogleMap map, ClusterManager<DataMapMarker> clusterManager) {
        super(context, map, clusterManager);
        this.context = context;
        mClusterIconGenerator = new IconGenerator(this.context);
    }

    @Override
    protected void onBeforeClusterItemRendered(DataMapMarker item, MarkerOptions markerOptions) {
        BitmapDescriptor markerDescriptor = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA);
        markerOptions.icon(markerDescriptor);
    }

    @Override
    protected void onBeforeClusterRendered(Cluster<DataMapMarker> cluster, MarkerOptions markerOptions) {
//        final Drawable clusterIcon = getResources().getDrawable(R.drawable.ic_lens_black_24dp);
//        clusterIcon.setColorFilter(getResources().getColor(android.R.color.holo_orange_light), PorterDuff.Mode.SRC_ATOP);
//
//        mClusterIconGenerator.setBackground(clusterIcon);

        if(cluster.getSize() < 10){
            mClusterIconGenerator.setContentPadding(40,20,0,0);
        } else {
            mClusterIconGenerator.setContentPadding(30,20,0,0);
        }

        Bitmap icon = mClusterIconGenerator.makeIcon(String.valueOf(cluster.getSize()));
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon));
    }*/



/* First version
    private Context context;
    private GoogleMap map;
    private ClusterManager<DataMapMarker> clusterManager;

    public DataMapClusterRenderer(Context context, GoogleMap map, ClusterManager<DataMapMarker> clusterManager) {
        super(context, map, clusterManager);

        this.context = context;
        this.map = map;
        this.clusterManager = clusterManager;

    }

    @Override
    protected void onBeforeClusterRendered(Cluster<DataMapMarker> cluster, MarkerOptions markerOptions) {
        // Main color
        int clusterColor = context.getResources().getColor(R.color.colorIOSThemeBlueFont);

        int bucket = this.getBucket(cluster);
        BitmapDescriptor descriptor =  this.mIcons.get(bucket);
        if(descriptor == null){
            this.mColoredCircleBackground.getPaint().setColor(clusterColor);
            descriptor = BitmapDescriptorFactory.fromBitmap(
                    this.mIconGenerator.makeIcon(this.getClusterText(bucket)));
            this.mIcons.put(bucket, descriptor);
        }
        markerOptions.icon(descriptor);
    }*/
}