/*
 * Copyright 2018 Google LLC
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

package com.example.android.bearguestmobile;

import android.app.AlertDialog;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.google.ar.core.AugmentedImage;
import com.google.ar.core.Point;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.HitTestResult;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.Scene;
import com.google.ar.sceneform.math.Quaternion;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.Color;
import com.google.ar.sceneform.rendering.Material;
import com.google.ar.sceneform.rendering.MaterialFactory;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.ShapeFactory;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Node for rendering an augmented image. The image is framed by placing the virtual picture frame
 * at the corners of the augmented image trackable.
 */
@SuppressWarnings({"AndroidApiChecker"})
public class AugmentedImageNode extends AnchorNode implements Scene.OnTouchListener {

    private static final String TAG = "AugmentedImageNode";

    // The augmented image represented by this node.
    private AugmentedImage image;
    private String imageName;

    // Arrow and plane.  We use completable futures here to simplify
    // the error handling and asynchronous loading.  The loading is started with the
    // first construction of an instance, and then used when the image is set.
    private LifecycleOwner owner;
    private static CompletableFuture<ModelRenderable> arrow;
    private static PopupWindow popup;
    private static ModelRenderable plane;
    private static Material transparentMaterial;
    String[] restaurantNameNum;


    private static Context _context;
    private static View myView;

    public AugmentedImageNode(Context context, LifecycleOwner owner, View view) {
        _context = context;
        this.owner = owner;
        myView = view;

        // Upon construction, start loading the models for the corners of the frame.
        if (arrow == null) {
            arrow = ModelRenderable.builder().setSource(context, Uri.parse("Pin.sfb")).build();

            // this is a little bit dirty :/
            // Make material for plane
            MaterialFactory.makeTransparentWithColor(context,
                    new Color(.1f, .1f, .1f, 0.2f)).thenAccept(material -> transparentMaterial = material
            );
        }
    }

    /**
     * Called when the AugmentedImage is detected and should be rendered. A Sceneform node tree is
     * created based on an Anchor created from the image. The corners are then positioned based on the
     * extents of the image. There is no need to worry about world coordinates since everything is
     * relative to the center of the image, which is the parent node of the corners.
     */
    @SuppressWarnings({"AndroidApiChecker", "FutureReturnValueIgnored"})
    public void setImage(AugmentedImage image) {

        this.image = image;
        this.imageName = image.getName();

        // If any of the models are not loaded, then recurse when all are loaded.
        if (!arrow.isDone()) {
            CompletableFuture.allOf(arrow)
                    .thenAccept((Void aVoid) -> setImage(image))
                    .exceptionally(
                            throwable -> {
                                Log.e(TAG, "Exception loading", throwable);
                                return null;
                            });
        }

        // split imageName from underscore
        // index[0] of restaurantNameNum has the restaurant name, index[1] as page number
        restaurantNameNum = imageName.split("[_\\.]");

        ///////////////////////

        // Use repo and view model
        // Create Restaurant java object with all fields null except restaurantName
        MutableLiveData<Restaurant> thisRestaurantName = new MutableLiveData<>();
        thisRestaurantName.setValue(new Restaurant(restaurantNameNum[0]));
        Log.v("AR", "restaurant name = " + restaurantNameNum[0]);

        DashboardViewModel dashboardViewModel = ViewModelProviders.of((MainActivity) _context).get(DashboardViewModel.class);

        dashboardViewModel.getMenuItemListForARByRestaurantName(thisRestaurantName).observe(owner, new Observer<List<MenuItem>>() {
            @Override
            public void onChanged(@Nullable List<MenuItem> menuItemList) {
                List<MenuItem> menuItemObjs = (dashboardViewModel.getMenuItemListForARByRestaurantName(thisRestaurantName)).getValue();

                if (menuItemList == null) {
                    // Show error message or "pins loading" message?
                    Log.v("AR", "menu item objs = null");
                } else {
                    Log.v("AR", "menu item objs size=" + menuItemList.size());
                    Log.v("AR", "item 1 = " + menuItemList.get(0));

                    // Test what has been returned
                    String allItems = "Items returned: ";
                    for (MenuItem m : menuItemList) {
                        allItems = allItems + ", " + m.getX();
                    }
                    Log.v("AR", "items returned: " + allItems);

                    // DISPLAY PINS HERE
                    addPins(menuItemList);

                }
            }
        });


        //////////////////////

        // Set the anchor based on the center of the image.
        setAnchor(image.createAnchor(image.getCenterPose()));

        // Create plane to get touches to annotate menu
        Node planeNode = new Node();
        planeNode.setParent(this);
        planeNode.setLocalPosition(new Vector3(0, 0, 0));
        planeNode.setName("menu"); // set name so we can filter on selection

        // create plane geometry if material is created
        if (transparentMaterial != null) {
            // create plane with size of image extents
            plane = ShapeFactory.makeCube(new Vector3(image.getExtentX(), 0.01f, image.getExtentZ()),
                    new Vector3(0, 0, 0),
                    transparentMaterial);
            planeNode.setRenderable(plane);
        }
    }

    private void addPins(List<MenuItem> menuItemObjs) {


        Vector3 localPosition = new Vector3();
        Node pinNode;

        for (MenuItem item : menuItemObjs) {

            Log.v("AR", "cycling thru item=" + item.getItemName() + "parsedInt=" + Integer.parseInt(restaurantNameNum[1]));

            if (item.pageNum == Integer.parseInt(restaurantNameNum[1])) {
                Log.v("AR", "entered if statement for item=" + item.getItemName() + "parsedInt=" + Integer.parseInt(restaurantNameNum[1]));

                // localPosition.set(item.x  * image.getExtentX(), -0.01f, item.z  * image.getExtentZ());

                localPosition.set(item.x, -0.01f, item.z);

                Node node = new Node();
                node.setParent(this);
                Log.v("ARAnnotationNode", "isTopLevel = " + node.isTopLevel());
                node.setLocalPosition(localPosition);
                node.setRenderable(arrow.getNow(null));
                // scale arrow down to reasonable size
                // node.setWorldScale(new Vector3(0.01f, 0.01f, 0.01f));

                // correct for arrow's initial rotation
                // Quaternion z = Quaternion.axisAngle(new Vector3(0f, 0f, 1f), 0f); // rotate on z axis by 45 degrees
                // Quaternion y = Quaternion.axisAngle(new Vector3(1f, 0f, 0), 0f); // rotate on x axis by 90 degrees
                // node.setLocalRotation(Quaternion.multiply(z, y));

                node.setOnTapListener(new OnTapListener() {
                    @Override
                    public void onTap(HitTestResult hitTestResult, MotionEvent motionEvent) {
                        if (item.substitution == null)
                            item.substitution = "No substitutions necessary.";
                        new AlertDialog.Builder(_context)
                                //.setTitle("Delete entry")
                                .setMessage(item.substitution)

                                // Specifying a listener allows you to take an action before dismissing the dialog.
                                // The dialog is automatically dismissed when a dialog button is clicked.
                                .setPositiveButton("okay", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // close window
                                    }
                                })

                                // A null listener allows the button to dismiss the dialog and take no further action.
                                //.setNegativeButton(android.R.string.no, null)
                                //.setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                    }
                });

            }


        }
    }

    public AugmentedImage getImage() {
        return image;
    }

    @Override
    public boolean onSceneTouch(HitTestResult hitTestResult, MotionEvent motionEvent) {
        Log.i(TAG, "onSceneTouch: " + hitTestResult.toString());
        return false;
    }
}










