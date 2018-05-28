package com.zsj.recyclerviewsimple;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.zsj.recyclerviewsimple.comm.MultiTypeSupport;
import com.zsj.recyclerviewsimple.comm.RecyclerViewComAdapter;
import com.zsj.recyclerviewsimple.comm.ViewHolder;

import java.util.ArrayList;

public class MulitTypeActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private ArrayList<ChatMsg> mChatMsgs = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mulit_type);
        initData();
        initView();
    }

    private void initData() {

        for (int i = 0; i < 100; i++) {
            if (i % 2 == 0) {
                mChatMsgs.add(new ChatMsg("朋友的内容 " + i, false));
            } else {
                mChatMsgs.add(new ChatMsg("自己的内容 " + i, true));
            }
        }
    }

    class ChatMsg {
        private String chatContext;
        private boolean isMe;

        public ChatMsg(String chatContext, boolean isMe) {
            this.chatContext = chatContext;
            this.isMe = isMe;
        }

        public String getChatContext() {
            return chatContext;
        }

        public void setChatContext(String chatContext) {
            this.chatContext = chatContext;
        }

        public boolean isMe() {
            return isMe;
        }

        public void setMe(boolean me) {
            isMe = me;
        }
    }

    private void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new LinearItemDecoration());

        MulitTypeAdapter mulitTypeAdapter = new MulitTypeAdapter(this, mChatMsgs, new MultiTypeSupport<ChatMsg>() {
            @Override
            public int getLayout(ChatMsg item) {
                if (item.isMe) {
                    return R.layout.item_layout_me;
                }
                return R.layout.item_layout_friend;
            }
        });

        mRecyclerView.setAdapter(mulitTypeAdapter);
    }

    class MulitTypeAdapter extends RecyclerViewComAdapter<ChatMsg> {

        public MulitTypeAdapter(Context context, ArrayList<ChatMsg> chatMsgs, MultiTypeSupport<ChatMsg> multiTypeSupport) {
            super(context, chatMsgs, multiTypeSupport);
        }

        @Override
        protected void onBind(ViewHolder holder, ChatMsg item, int position) {
            ChatMsg chatMsg = mData.get(position);
            holder.setText(R.id.tv_context, chatMsg.getChatContext());
        }
    }
}
