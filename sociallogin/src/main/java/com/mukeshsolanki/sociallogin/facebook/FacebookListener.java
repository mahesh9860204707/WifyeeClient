package com.mukeshsolanki.sociallogin.facebook;

import com.facebook.login.LoginResult;

public interface FacebookListener {
  void onFbSignInFail(String errorMessage);

  void onFbSignInSuccess(LoginResult authToken, String userId);

  void onFBSignOut();
}
