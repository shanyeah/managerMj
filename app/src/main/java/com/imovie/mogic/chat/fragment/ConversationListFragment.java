package com.imovie.mogic.chat.fragment;

import android.content.Intent;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMConversation.EMConversationType;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.model.EaseAtMessageHelper;
import com.hyphenate.easeui.ui.EaseConversationListFragment;
import com.hyphenate.easeui.widget.EaseConversationList.EaseConversationListHelper;
import com.hyphenate.util.NetUtils;
import com.imovie.mogic.R;
import com.imovie.mogic.ScanPay.model.InternetBarModel;
import com.imovie.mogic.chat.AddContactActivity;
import com.imovie.mogic.chat.ContactListActivity;
import com.imovie.mogic.chat.GroupListActivity;
import com.imovie.mogic.chat.db.InviteMessgeDao;
import com.imovie.mogic.chat.widget.HomePopWindow;
import com.imovie.mogic.home.MainActivity;

import java.util.ArrayList;
import java.util.List;

public class ConversationListFragment extends EaseConversationListFragment {

    private TextView errorText;
    private HomePopWindow<InternetBarModel> popWindow;
    private List<InternetBarModel> listPopup;

    @Override
    protected void initView() {
        super.initView();
        View errorView = (LinearLayout) View.inflate(getActivity(), R.layout.em_chat_neterror_item, null);
        errorItemContainer.addView(errorView);
        errorText = (TextView) errorView.findViewById(R.id.tv_connect_errormsg);

        listPopup = new ArrayList<>();
        for(int i=0;i<3;i++){
            InternetBarModel model = new InternetBarModel();
            model.id=i;
            if(i==0) {
                model.name = "扫一扫";
            }else if(i==1){
                model.name = "添加朋友";
            }else if(i==2){
                model.name = "发起群聊";
            }
            listPopup.add(model);

        }
        popWindow = new HomePopWindow<>(getActivity(),listPopup,itemClickListener);
    }
    
    @Override
    protected void setUpView() {
        super.setUpView();
        // register context menu
        registerForContextMenu(conversationListView);
        conversationListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EMConversation conversation = conversationListView.getItem(position);
                String username = conversation.conversationId();
                if (username.equals(EMClient.getInstance().getCurrentUser()))
                    Toast.makeText(getActivity(), R.string.Cant_chat_with_yourself, Toast.LENGTH_SHORT).show();
                else {
                    // start chat acitivity
//                    Intent intent = new Intent(getActivity(), ChatActivity.class);
//                    if(conversation.isGroup()){
//                        if(conversation.getType() == EMConversationType.ChatRoom){
//                            // it's group chat
//                            intent.putExtra(Constant.EXTRA_CHAT_TYPE, Constant.CHATTYPE_CHATROOM);
//                        }else{
//                            intent.putExtra(Constant.EXTRA_CHAT_TYPE, Constant.CHATTYPE_GROUP);
//                        }
//
//                    }
//                    // it's single chat
//                    intent.putExtra(Constant.EXTRA_USER_ID, username);
//                    startActivity(intent);
                }
            }
        });
        //red packet code : 红包回执消息在会话列表最后一条消息的展示
        conversationListView.setConversationListHelper(new EaseConversationListHelper() {
            @Override
            public String onSetItemSecondaryText(EMMessage lastMessage) {
//                if (lastMessage.getBooleanAttribute(RPConstant.MESSAGE_ATTR_IS_RED_PACKET_ACK_MESSAGE, false)) {
//                    String sendNick = lastMessage.getStringAttribute(RPConstant.EXTRA_RED_PACKET_SENDER_NAME, "");
//                    String receiveNick = lastMessage.getStringAttribute(RPConstant.EXTRA_RED_PACKET_RECEIVER_NAME, "");
//                    String msg;
//                    if (lastMessage.direct() == EMMessage.Direct.RECEIVE) {
//                        msg = String.format(getResources().getString(R.string.msg_someone_take_red_packet), receiveNick);
//                    } else {
//                        if (sendNick.equals(receiveNick)) {
//                            msg = getResources().getString(R.string.msg_take_red_packet);
//                        } else {
//                            msg = String.format(getResources().getString(R.string.msg_take_someone_red_packet), sendNick);
//                        }
//                    }
//                    return msg;
//                }
                return null;
            }
        });





        super.setUpView();
        //end of red packet code
    }

    @Override
    protected void onConnectionDisconnected() {
        super.onConnectionDisconnected();
        if (NetUtils.hasNetwork(getActivity())){
//         errorText.setText(R.string.can_not_connect_chat_server_connection);
            errorText.setText("暂无消息");
        } else {
          errorText.setText(R.string.the_current_network);
        }
    }
    
    
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        getActivity().getMenuInflater().inflate(R.menu.em_delete_message, menu); 
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        boolean deleteMessage = false;
        if (item.getItemId() == R.id.delete_message) {
            deleteMessage = true;
        } else if (item.getItemId() == R.id.delete_conversation) {
            deleteMessage = false;
        }
    	EMConversation tobeDeleteCons = conversationListView.getItem(((AdapterContextMenuInfo) item.getMenuInfo()).position);
    	if (tobeDeleteCons == null) {
    	    return true;
    	}
        if(tobeDeleteCons.getType() == EMConversationType.GroupChat){
            EaseAtMessageHelper.get().removeAtMeGroup(tobeDeleteCons.conversationId());
        }
        try {
            // delete conversation
            EMClient.getInstance().chatManager().deleteConversation(tobeDeleteCons.conversationId(), deleteMessage);
            InviteMessgeDao inviteMessgeDao = new InviteMessgeDao(getActivity());
            inviteMessgeDao.deleteMessage(tobeDeleteCons.conversationId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        refresh();

        // update unread count
        ((MainActivity) getActivity()).updateUnreadLabel();
        return true;
    }

    /**
     * popupwindow显示的ListView的item点击事件
     */
    private OnItemClickListener itemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
            switch (position){
                case 0:
//                    Intent i = new Intent(getContext(), ScanActivity.class);
//                    startActivity(i);
//                    ScanPayManager.enterCaptureActivity(getContext());
                    break;
                case 1:
                    Intent intent = new Intent(getContext(), AddContactActivity.class);
                    startActivity(intent);
                    break;
                case 2:
                    Intent intent2 = new Intent(getContext(), GroupListActivity.class);
                    startActivity(intent2);
                    break;
                default:
                    break;
            }
            popWindow.dismiss();

        }
    };

}
