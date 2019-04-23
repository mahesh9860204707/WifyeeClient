package com.mukeshsolanki.sociallogin.google;

import android.net.Uri;

public interface GoogleListener {
  void onGoogleAuthSignIn(String token, String id, String displayName, Uri photoUrl, String authToken, String userId);

  void onGoogleAuthSignInFailed(String errorMessage);

  void onGoogleAuthSignOut();
}
