/**
 * Copyright (C) 2016 Hyphenate Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.imovie.mogic.chat;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.widget.EaseAlertDialog;
import com.imovie.mogic.MyApplication;
import com.imovie.mogic.R;
import com.imovie.mogic.chat.adapter.AddContactAdapter;
import com.imovie.mogic.chat.db.DemoHelper;
import com.imovie.mogic.chat.model.ContactModel;
import com.imovie.mogic.chat.net.ChatWebHelper;
import com.imovie.mogic.home.BaseActivity;
import com.imovie.mogic.web.IModelResultListener;
import com.imovie.mogic.web.model.HttpResultModel;
import com.imovie.mogic.widget.ClearButtonEditText;
import com.imovie.mogic.widget.YSBPageListView;
import com.imovie.mogic.widget.interfaces.IPageList;

import java.util.ArrayList;
import java.util.List;

public class AddContactActivity extends BaseActivity {
	private ClearButtonEditText etNoteSearch;
	private YSBPageListView lvAddContact;
	public List<ContactModel.Contact> contactsList = new ArrayList<>();
	public AddContactAdapter addContactAdapter;
	String nickName="";

	private RelativeLayout searchedUserLayout;
	private TextView nameText;
	private Button searchBtn;
	private String toAddUsername;
	private ProgressDialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.em_activity_add_contact);

		initView();
		setView();

//		String strUserName = getResources().getString(R.string.user_name);
//		editText.setHint(strUserName);
		searchedUserLayout = (RelativeLayout) findViewById(R.id.ll_user);
		nameText = (TextView) findViewById(R.id.name);
		searchBtn = (Button) findViewById(R.id.search);
	}

	private void initView(){
		TextView mTextView = (TextView) findViewById(R.id.add_list_friends);
		etNoteSearch = (ClearButtonEditText) findViewById(R.id.etNoteSearch);
		String strAdd = getResources().getString(R.string.add_friend);
		mTextView.setText(strAdd);
		lvAddContact = (YSBPageListView) findViewById(R.id.lvAddContact);
		addContactAdapter = new AddContactAdapter(this,contactsList);
		lvAddContact.setAdapter(addContactAdapter);
//		lvAddContact.setHaveMoreData(true);
//		lvAddContact.startLoad();
	}

	private void  setView(){
		lvAddContact.setOnPageListener(new IPageList.OnPageListener() {
			@Override
			public void onLoadMoreItems() {
				getContactList(nickName,"","");
			}
		});

		lvAddContact.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//				Intent intent = new Intent(AddContactActivity.this, UserCenterActivity.class);
//				intent.putExtra(UserCenterActivity.USER_ID,addContactAdapter.getItem(position).userId);
//				startActivity(intent);

			}
		});
	}


	public void refresh(){
		addContactAdapter.list.clear();
		addContactAdapter.notifyDataSetChanged();
		lvAddContact.setHaveMoreData(true);
		lvAddContact.startLoad();
	}
	private void getContactList(String nickName,String gender,String ageArea){
		double longitude = MyApplication.getInstance().getCoordinate().longitude;
		double latitude = MyApplication.getInstance().getCoordinate().latitude;
		int page = addContactAdapter.list.size()/lvAddContact.getPageSize() + 1;
		ChatWebHelper.getNearbyUserList(longitude,latitude,nickName,gender,ageArea,page,lvAddContact.getPageSize(),new IModelResultListener<ContactModel>() {
			@Override
			public boolean onGetResultModel(HttpResultModel resultModel) {
				lvAddContact.finishLoading(true);
				return false;
			}

			@Override
			public void onSuccess(String resultCode, ContactModel resultModel, List<ContactModel> resultModelList, String resultMsg, String hint) {

				Log.e("----sch",""+resultCode);
				try {
					if(resultModel.list.size()>0){
						addContactAdapter.list.addAll(resultModel.list);
						addContactAdapter.notifyDataSetChanged();
					}
					lvAddContact.finishLoading(lvAddContact.getPageSize() == resultModel.list.size());
				} catch (Exception e) {
					lvAddContact.finishLoading(false);
					e.printStackTrace();
				}

			}

			@Override
			public void onFail(String resultCode, String resultMsg, String hint) {
				lvAddContact.finishLoading(true);

			}

			@Override
			public void onError(String errorMsg) {
				lvAddContact.finishLoading(true);

			}
		});
	}
	
	
	/**
	 * search contact
	 * @param v
	 */
	public void searchContact(View v) {
		nickName = etNoteSearch.getText().toString();
		String saveText = searchBtn.getText().toString();
		
		if (getString(R.string.button_search).equals(saveText)) {
//			toAddUsername = nickName;
			if(TextUtils.isEmpty(nickName)) {
				new EaseAlertDialog(this, R.string.Please_enter_a_username).show();
				return;
			}

		lvAddContact.setHaveMoreData(true);
		lvAddContact.startLoad();



//			searchedUserLayout.setVisibility(View.VISIBLE);
//			nameText.setText(toAddUsername);
			
		} 
	}	
	
	/**
	 *  add contact
	 * @param view
	 */
	public void addContact(View view){
		if(EMClient.getInstance().getCurrentUser().equals(nameText.getText().toString())){
			new EaseAlertDialog(this, R.string.not_add_myself).show();
			return;
		}
		
		if(DemoHelper.getInstance().getContactList().containsKey(nameText.getText().toString())){
		    //let the user know the contact already in your contact list
		    if(EMClient.getInstance().contactManager().getBlackListUsernames().contains(nameText.getText().toString())){
		        new EaseAlertDialog(this, R.string.user_already_in_contactlist).show();
		        return;
		    }
			new EaseAlertDialog(this, R.string.This_user_is_already_your_friend).show();
			return;
		}
		
		progressDialog = new ProgressDialog(this);
		String stri = getResources().getString(R.string.Is_sending_a_request);
		progressDialog.setMessage(stri);
		progressDialog.setCanceledOnTouchOutside(false);
		progressDialog.show();
		
		new Thread(new Runnable() {
			public void run() {
				
				try {
					//demo use a hardcode reason here, you need let user to input if you like
					String s = getResources().getString(R.string.Add_a_friend);
					EMClient.getInstance().contactManager().addContact(toAddUsername, s);
					runOnUiThread(new Runnable() {
						public void run() {
							progressDialog.dismiss();
							String s1 = getResources().getString(R.string.send_successful);
							Toast.makeText(getApplicationContext(), s1, Toast.LENGTH_LONG).show();
						}
					});
				} catch (final Exception e) {
					runOnUiThread(new Runnable() {
						public void run() {
							progressDialog.dismiss();
							String s2 = getResources().getString(R.string.Request_add_buddy_failure);
							Toast.makeText(getApplicationContext(), s2 + e.getMessage(), Toast.LENGTH_LONG).show();
						}
					});
				}
			}
		}).start();
	}
	
	public void back(View v) {
		finish();
	}
}
