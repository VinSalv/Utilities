// Generated by view binder compiler. Do not edit!
package com.example.utilities.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.utilities.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class FragmentBussolaBinding implements ViewBinding {
  @NonNull
  private final FrameLayout rootView;

  @NonNull
  public final ImageView mainImageDial;

  @NonNull
  public final ImageView mainImageHands;

  @NonNull
  public final TextView sotwLabel;

  private FragmentBussolaBinding(@NonNull FrameLayout rootView, @NonNull ImageView mainImageDial,
      @NonNull ImageView mainImageHands, @NonNull TextView sotwLabel) {
    this.rootView = rootView;
    this.mainImageDial = mainImageDial;
    this.mainImageHands = mainImageHands;
    this.sotwLabel = sotwLabel;
  }

  @Override
  @NonNull
  public FrameLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static FragmentBussolaBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static FragmentBussolaBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.fragment__bussola, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static FragmentBussolaBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.main_image_dial;
      ImageView mainImageDial = ViewBindings.findChildViewById(rootView, id);
      if (mainImageDial == null) {
        break missingId;
      }

      id = R.id.main_image_hands;
      ImageView mainImageHands = ViewBindings.findChildViewById(rootView, id);
      if (mainImageHands == null) {
        break missingId;
      }

      id = R.id.sotw_label;
      TextView sotwLabel = ViewBindings.findChildViewById(rootView, id);
      if (sotwLabel == null) {
        break missingId;
      }

      return new FragmentBussolaBinding((FrameLayout) rootView, mainImageDial, mainImageHands,
          sotwLabel);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
