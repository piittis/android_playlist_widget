package com.wavy.spotifyplaylistwidget;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.wavy.spotifyplaylistwidget.network.SpotifyApi;

/**
 * Activity without an UI whose only purpose is to fetch a Spotify access token and give it to
 * SpotifyApi.
 */
public class AuthActivity extends AppCompatActivity {

    private static final String TAG = "AuthActivity";
    private static final String CLIENT_ID = "a2277433f3ba4c9a9b0ae0859c30f808";
    private static final String REDIRECT_URI = "spotifyquicklaunch://logincallback";
    private static final int REQUEST_CODE = 1337;
    //todo use resources
    private static final String FailMessage = "Spotify authentication error";
    private static final String cancelMessage = "Spotify authentication is required";
    private Boolean mReturnToActivity = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // This activity doesnt need any UI?
        //setContentView(R.layout.activity_auth);

        if (!spotifyInstalled()) {
            quitWithMessage("Please install the spotify app first");
            //AuthenticationClient.openDownloadSpotifyActivity(this);
        }

        mReturnToActivity = getIntent().getBooleanExtra("returnToActivity", false);
        doAuthentication();
    }

    private void doAuthentication() {

        AuthenticationRequest.Builder builder =
                new AuthenticationRequest.Builder(CLIENT_ID, AuthenticationResponse.Type.TOKEN, REDIRECT_URI);

        builder.setScopes(new String[]{"user-read-private",
                "playlist-read-private",
                "playlist-read-collaborative"});

        AuthenticationRequest request = builder.build();
        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);

    }

    private void goToSelectActivity() {
        Intent intent = new Intent(getApplicationContext(), SelectActivity.class);
        overridePendingTransition(R.anim.fade_in_hard_nodelay, R.anim.hide_delayed);
        startActivity(intent);
    }

    private void authenticationFailed() {
        quitWithMessage(FailMessage);
    }

    private void authenticationCancelled() {
        quitWithMessage(cancelMessage);
    }

    private boolean spotifyInstalled() {
        //todo implement
        return true;
    }

    private void quitWithMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
        this.finishAffinity();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE) {

            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);
            switch (response.getType()) {
                // Response was successful and contains auth token
                case TOKEN:
                    // Get token and go to select view
                    String token = response.getAccessToken();
                    SpotifyApi.setAccessToken(token);

                    setResult(RESULT_OK);
                    finish();
                    overridePendingTransition(R.anim.fade_in_hard_nodelay, R.anim.hide_delayed);
                    if (!mReturnToActivity) {
                        // not returning to any previous activity, go to select activity
                        goToSelectActivity();
                    }

                    break;

                // Auth flow returned an error
                case ERROR:
                    Log.d("onActivityResult", "error");
                    authenticationFailed();

                // Most likely auth flow was cancelled
                default:
                    Log.d("onActivityResult", "default");
                    authenticationCancelled();
                    // Handle other cases
            }
        }
    }
}