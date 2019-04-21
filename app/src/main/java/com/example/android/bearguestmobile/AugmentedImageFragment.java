package com.example.android.bearguestmobile;

import android.app.ActivityManager;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.ar.core.AugmentedImageDatabase;
import com.google.ar.core.Config;
import com.google.ar.core.Session;
//import com.google.ar.sceneform.samples.common.helpers.SnackbarHelper;
import com.google.ar.sceneform.ux.ArFragment;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

//Extend the ArFragment to customize the ARCore session configuration to include Augmented Images.
public class AugmentedImageFragment extends ArFragment {
    private static final String TAG = "AugmentedImageFragment";

    // menuImages.imgdb currently contains skipper canteen pages 1 and 2 and tony's town square
    private static final String IMAGE_DATABASE = "menuImages.imgdb";

    boolean returnVal = true;
    AugmentedImageDatabase augmentedImageDatabase;


    // Do a runtime check for the OpenGL level available at runtime to avoid Sceneform crashing the
    // application.
    private static final double MIN_OPENGL_VERSION = 3.0;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        // Check for Sceneform being supported on this device.  This check will be integrated into
        // Sceneform eventually.
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            Log.e(TAG, "Sceneform requires Android N or later");
            /*SnackbarHelper.getInstance()
                    .showError(getActivity(), "Sceneform requires Android N or later");*/
        }

        String openGlVersionString =
                ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE))
                        .getDeviceConfigurationInfo()
                        .getGlEsVersion();
        if (Double.parseDouble(openGlVersionString) < MIN_OPENGL_VERSION) {
            Log.e(TAG, "Sceneform requires OpenGL ES 3.0 or later");
            /*SnackbarHelper.getInstance()
                    .showError(getActivity(), "Sceneform requires OpenGL ES 3.0 or later");*/
        }
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        // Turn off the plane discovery since we're only looking for images
        getPlaneDiscoveryController().hide();
        getPlaneDiscoveryController().setInstructionView(null);
        getArSceneView().getPlaneRenderer().setEnabled(false);
        return view;
    }

    @Override
    protected Config getSessionConfiguration(Session session) {
        Config config = super.getSessionConfiguration(session);
        if (!setupAugmentedImageDatabase(config, session)) {
            /*SnackbarHelper.getInstance()
                    .showError(getActivity(), "Could not setup augmented image database");*/
            Log.v("TAG", "setupAugImageDb is false");
        }
        return config;
    }

    private boolean setupAugmentedImageDatabase(Config config, Session session) {

        AssetManager assetManager = getContext() != null ? getContext().getAssets() : null;
        if (assetManager == null) {
            Log.e(TAG, "Context is null, cannot intitialize image database.");
            return false;
        }



       /*

        DashboardViewModel dashboardViewModel = ViewModelProviders.of((MainActivity)getActivity()).get(DashboardViewModel.class);

        dashboardViewModel.getImgdb().observe(getViewLifecycleOwner(), new Observer<BlobClass>() {
            @Override
            public void onChanged(@Nullable BlobClass blobClass) {
                if(blobClass!=null) {
                    // Capture imgdb object from blobclass and do a thing with it
                    Blob imgdb = blobClass.getBlobby();
                    try (InputStream is = imgdb.getBinaryStream()) {
                        augmentedImageDatabase = AugmentedImageDatabase.deserialize(session, is);
                        config.setAugmentedImageDatabase(augmentedImageDatabase);
                    } catch (IOException e) {
                        Log.e(TAG, "IO exception loading augmented image database.", e);
                        returnVal = false;
                    } catch (Exception e) {
                        Log.e(TAG, "General exception loading augmented image database.", e);
                        returnVal = false;
                    }
                }
                else {
                    Log.e(TAG, "blobclass is null");
                    returnVal = false;
                }
            }
        });
        */



        // load the image database
        try (InputStream is = getContext().getAssets().open(IMAGE_DATABASE)){
            Log.e(TAG, "try passes inputstream");
            augmentedImageDatabase = AugmentedImageDatabase.deserialize(session, is);
            config.setAugmentedImageDatabase(augmentedImageDatabase);
        } catch (IOException e) {
            Log.e(TAG, "IO exception loading augmented image database.", e);
            return false;
        }

        return returnVal;
    }
}
