package com.skappsstore.quizgame.utilities;

import com.skappsstore.quizgame.R;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ImageResourceHelper {

    // Define a static map to store image name to resource ID mapping
    private static final Map<String, Integer> imageResourceMap = new HashMap<>();

    static {
        // Populate the map with image names and corresponding resource IDs
        imageResourceMap.put("category1.jpg", R.drawable.category1);
        imageResourceMap.put("category2.jpg", R.drawable.category2);
        imageResourceMap.put("category3.jpg", R.drawable.category3);
        imageResourceMap.put("category4.jpg", R.drawable.category4);
        imageResourceMap.put("category5.jpg", R.drawable.category5);
        imageResourceMap.put("category6.png", R.drawable.category6);
        imageResourceMap.put("category7.jpg", R.drawable.category7);
        imageResourceMap.put("category8.jpg", R.drawable.category8);
    }

    public static int getImageResourceByCategory(String categoryImageName) {
        // Lookup category name in the map
        Integer resId = imageResourceMap.get(categoryImageName);

        // If the image is found, return the resource ID
        // If image not found, return the default image resource ID
        // Default image ic_baseline_category_24
        return Objects.requireNonNullElse(resId, R.drawable.defaultimage);
    }
}
