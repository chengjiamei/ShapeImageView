# ShapeImageView
A custom ImageView that can display as circle ，rectange and round rectange  simply that used as normal ImageView.

# Usage
-----
```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/roc.cjm.shapeimageview"
    xmlns:app1="http://schemas.android.com/apk/res-auto"
    android:orientation="vertical"
    android:layout_width="match_parent"
    android:layout_height="match_parent">
    <roc.cjm.shapeimageview.ShapeImageView
        android:id="@+id/image_1"
        android:layout_weight="1"
        android:src="@mipmap/test"
        android:layout_width="match_parent"
        android:layout_height="match_parent"/>
    <roc.cjm.shapeimageview.ShapeImageView
        android:id="@+id/image_2"
        android:layout_weight="1"
        android:src="@mipmap/test1"
        android:scaleType="fitStart"
        app1:siv_shape_type="2"
        app1:siv_round_radius_y="30dp"
        app1:siv_round_radius_x="30dp"
        android:layout_width="match_parent"
        android:layout_height="match_parent"/>

    <roc.cjm.shapeimageview.ShapeImageView
        android:id="@+id/image_3"
        android:layout_weight="1"
        app1:siv_shape_type="2"
        android:scaleType="fitStart"
        android:src="@mipmap/test2"
        android:layout_width="match_parent"
        android:layout_height="match_parent"/>
</LinearLayout>
```
