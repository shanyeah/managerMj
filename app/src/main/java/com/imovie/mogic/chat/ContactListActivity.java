package com.imovie.mogic.chat;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.imovie.mogic.R;
import com.imovie.mogic.chat.fragment.ContactListFragment;
import com.imovie.mogic.chat.runtimepermissions.PermissionsManager;
import com.imovie.mogic.home.BaseActivity;

public class ContactListActivity extends BaseActivity {
    public static ContactListActivity activityInstance;
    private ContactListFragment contactListFragment;
//    String toChatUsername;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.em_activity_chat);
        activityInstance = this;
        //get user id or group id
//        toChatUsername = getIntent().getExtras().getString("userId");
        //use EaseChatFratFragment
        contactListFragment = new ContactListFragment();
        //pass parameters to chat fragment
//        chatFragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction().add(R.id.container, contactListFragment).commit();
        
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        activityInstance = null;
    }
    
    @Override
    protected void onNewIntent(Intent intent) {
    	// make sure only one chat activity is opened
//        String username = intent.getStringExtra("userId");
//        if (toChatUsername.equals(username))
//            super.onNewIntent(intent);
//        else {
//            finish();
//            startActivity(intent);
//        }

    }
    
    @Override
    public void onBackPressed() {
//        chatFragment.onBackPressed();
//        if (EasyUtils.isSingleActivity(this)) {
////            Intent intent = new Intent(this, MainActivity.class);
////            startActivity(intent);
//        }
        finish();
    }
    
//    public String getToChatUsername(){
//        return toChatUsername;
//    }

    @Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
        @NonNull int[] grantResults) {
        PermissionsManager.getInstance().notifyPermissionsChange(permissions, grantResults);
    }
}
