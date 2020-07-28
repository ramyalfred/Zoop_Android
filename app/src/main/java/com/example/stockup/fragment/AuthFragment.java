package com.example.stockup.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.stockup.R;
import com.example.stockup.activity.MainActivity;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class AuthFragment  extends Fragment {
    public static final int RC_SIGN_IN = 1;
    private String TAG = getClass().toString();
    private Button mGoogleButton, mEmailButton;
    private LoginButton mFacebookButton;
    private EditText mPasswordEdit, mEmailEdit;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private GoogleSignInClient mGoogleSignInClient;
    private CallbackManager mCallbackManager;
    private FacebookCallback mFacebookCallback;

    //For non-UI Initialization
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Initializing the FirebaseAuth instance
        mAuth = FirebaseAuth.getInstance();

        //Initializing the GSO that specifies the user-data required from Google for the app
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();

        //Getting a Google Sign-In client
        mGoogleSignInClient = GoogleSignIn.getClient(this.getActivity(),gso);

        //Initializing Facebook login (CallBack Manager + CallBack)
        mCallbackManager = CallbackManager.Factory.create();
        mFacebookCallback = new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                //Login was successful
                Log.d(TAG, "facebook:onSucess:" + loginResult);
                //Handle the authentication through Firebase
                firebaseAuthWithFacebook(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                //Authentication cancelled
                Log.d(TAG, "facebook:onCancel");
            }

            @Override
            public void onError(FacebookException error) {
                //Authentication error
                Log.w(TAG,"facebook:onError",error);
            }
        };

    }

    //For UI initialization
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_auth,container,false);

        //Initializing the buttons
        mFacebookButton = (LoginButton) v.findViewById(R.id.button_fb);
        mGoogleButton = (Button) v.findViewById(R.id.button_google);
        mEmailButton = (Button) v.findViewById(R.id.button_email);

        //Initializing EditText objects
        mEmailEdit = (EditText) v.findViewById(R.id.edit_email);
        mPasswordEdit = (EditText) v.findViewById(R.id.edit_password);

        //Handling the Google Sign-in click
        mGoogleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });

        //Handling the create new account click
        mEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Call to Firebase to create new user while attaching listener
                mAuth.createUserWithEmailAndPassword(mEmailEdit.getText().toString(),mPasswordEdit.getText().toString()).addOnCompleteListener(getActivity(), mFirebaseAuthListener);
            }
        });

        //Handling Facebook login click
        mFacebookButton.setReadPermissions("email","public_profile");
        mFacebookButton.registerCallback(mCallbackManager,mFacebookCallback);

        return v;
    }

    //Listener to perform post-authentication actions, whether that be success or failure
    private OnCompleteListener<AuthResult> mFirebaseAuthListener = new OnCompleteListener<AuthResult> (){
        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
            if (task.isSuccessful()) {
                //If auth succeeds, go to Home
                Log.d(TAG,"signInWithEmail:success");
                mUser = mAuth.getCurrentUser();
                updateUI(mUser);
            }else{
                //If login fails display a message to the user
                Log.w(TAG,"signInWithEmail:failure",task.getException());
                Toast.makeText(getActivity(),"Authentication Failed.",Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    public void onStart() {
        super.onStart();

        //Check if authenticated and moves to Home fragment
        mUser = mAuth.getCurrentUser();
        if (mUser != null){
            updateUI(mUser);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode,resultCode,data);

        //If returning from the Google Sign-in intent
        if(requestCode == RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try{
                //Google sign-in was a success, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG,"firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getId());
            }catch (ApiException e){
                //Sign in failed
                Log.w(TAG,"Google sign in failed",e);
                Toast toast = Toast.makeText(this.getActivity(),"Google sign-in failed!",Toast.LENGTH_LONG);
            }
        }

        //Always pass back the result to the Facebook callback manager
        mCallbackManager.onActivityResult(requestCode, resultCode, data);

    }

    //Handling the Facebook-Firebase link
    private void firebaseAuthWithFacebook(AccessToken accessToken) {
        Log.d(TAG,"firebaseAuthWithFacebook:" + accessToken);

        //Get a Firebase credential from the the Facebook token
        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        mAuth.signInWithCredential(credential).addOnCompleteListener(this.getActivity(),mFirebaseAuthListener);
    }

    //Handling the Google-Firebase link
    private void firebaseAuthWithGoogle(String accountId){
        //After Google sign-in is successful, get an ID token and exchange it for a Firebase credential
        AuthCredential credential = GoogleAuthProvider.getCredential(accountId,null);
        mAuth.signInWithCredential(credential).addOnCompleteListener(this.getActivity(),mFirebaseAuthListener);
    }

    //Navigating from this fragment
    private void updateUI(FirebaseUser user){
        startActivity(new Intent(this.getActivity(), MainActivity.class));
    }

}