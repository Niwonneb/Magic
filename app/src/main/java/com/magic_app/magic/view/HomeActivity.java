package com.magic_app.magic.view;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import com.magic_app.magic.R;
import com.magic_app.magic.view.camera.CameraActivity;
import com.magic_app.magic.view.camera.ImageUtility;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends Activity {
    private static final int REQUEST_CAMERA = 0;
    static boolean tookImageToday = false;

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        if (!tookImageToday) {
            tookImageToday = true;
            Intent startCustomCameraIntent = new Intent(this, CameraActivity.class);
            startActivityForResult(startCustomCameraIntent, REQUEST_CAMERA);
        }

        recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL));

//        List<ImageItem> staggeredList = getListItemData();
//
//        SolventRecyclerViewAdapter rcAdapter = new SolventRecyclerViewAdapter(HomeActivity.this, staggeredList);
//        recyclerView.setAdapter(rcAdapter);
    }

//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (resultCode != RESULT_OK) return;
//
//        if (requestCode == REQUEST_CAMERA) {
//            Uri photoUri = data.getData();
//            // Get the bitmap in according to the width of the device
//            Bitmap bitmap = ImageUtility.decodeSampledBitmapFromPath(photoUri.getPath(), 40, 40);
//            listViewItems.add(0, new ImageItem(new ImageItem.Image(bitmap, this), new ImageItem.Image(R.drawable.sample_5x20, this)));
//            recyclerView.getAdapter().notifyDataSetChanged();
//        }
//        super.onActivityResult(requestCode, resultCode, data);
//    }
//
//    List<ImageItem> listViewItems = new ArrayList<>();
//
//    private List<ImageItem> getListItemData(){
//        for (int i = 0; i < 100; ++i) {
//            listViewItems.add(new ImageItem(R.drawable.sample_0x20, R.drawable.sample_1x20, this));
//            listViewItems.add(new ImageItem(R.drawable.sample_2x20, R.drawable.sample_3x20, this));
//            listViewItems.add(new ImageItem(R.drawable.sample_4x20, R.drawable.sample_5x20, this));
//            listViewItems.add(new ImageItem(R.drawable.sample_6x20, R.drawable.sample_7x20, this));
//        }
//
//        return listViewItems;
//    }
}
