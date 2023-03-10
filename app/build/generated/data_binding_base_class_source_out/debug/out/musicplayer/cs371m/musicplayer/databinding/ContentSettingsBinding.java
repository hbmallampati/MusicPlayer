// Generated by view binder compiler. Do not edit!
package musicplayer.cs371m.musicplayer.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;
import musicplayer.cs371m.musicplayer.R;

public final class ContentSettingsBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final Button cancelBt;

  @NonNull
  public final SwitchCompat loopSwitch;

  @NonNull
  public final TextView noOfSongsTv;

  @NonNull
  public final Button okBt;

  private ContentSettingsBinding(@NonNull LinearLayout rootView, @NonNull Button cancelBt,
      @NonNull SwitchCompat loopSwitch, @NonNull TextView noOfSongsTv, @NonNull Button okBt) {
    this.rootView = rootView;
    this.cancelBt = cancelBt;
    this.loopSwitch = loopSwitch;
    this.noOfSongsTv = noOfSongsTv;
    this.okBt = okBt;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ContentSettingsBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ContentSettingsBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.content_settings, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ContentSettingsBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.cancelBt;
      Button cancelBt = ViewBindings.findChildViewById(rootView, id);
      if (cancelBt == null) {
        break missingId;
      }

      id = R.id.loopSwitch;
      SwitchCompat loopSwitch = ViewBindings.findChildViewById(rootView, id);
      if (loopSwitch == null) {
        break missingId;
      }

      id = R.id.noOfSongsTv;
      TextView noOfSongsTv = ViewBindings.findChildViewById(rootView, id);
      if (noOfSongsTv == null) {
        break missingId;
      }

      id = R.id.okBt;
      Button okBt = ViewBindings.findChildViewById(rootView, id);
      if (okBt == null) {
        break missingId;
      }

      return new ContentSettingsBinding((LinearLayout) rootView, cancelBt, loopSwitch, noOfSongsTv,
          okBt);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
