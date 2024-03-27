package algonquin.cst2335.pund0006;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import algonquin.cst2335.pund0006.databinding.ActivityChatRoomBinding;
import algonquin.cst2335.pund0006.databinding.ReceiveMessageBinding;
import algonquin.cst2335.pund0006.databinding.SentMessageBinding;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ChatRoom extends AppCompatActivity {
    private ActivityChatRoomBinding binding;
    private algonquin.cst2335.jais0017.ChatRoomViewModel chatModel;
    private MyAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatRoomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        chatModel = new ViewModelProvider(this).get(algonquin.cst2335.jais0017.ChatRoomViewModel.class);

        ArrayList<ChatMessage> messages = chatModel.getMessages().getValue();
        if (messages == null) {
            messages = new ArrayList<>();
            chatModel.getMessages().postValue(messages);
        }

        myAdapter = new MyAdapter();

        binding.recycleView.setLayoutManager(new LinearLayoutManager(this));
        binding.recycleView.setAdapter(myAdapter);

        chatModel.getMessages().observe(this, newMessages -> {
            myAdapter.setMessages(newMessages);
            binding.recycleView.scrollToPosition(newMessages.size() - 1); // Scroll to the new message
        });

        binding.sendButton.setOnClickListener(click -> {
            String messageText = binding.textInput.getText().toString();
            if (!messageText.isEmpty()) {
                ChatMessage chatMessage = new ChatMessage(messageText, getCurrentTime(), true);
                chatModel.addMessage(chatMessage);
                binding.textInput.setText("");
            }
        });

        binding.receiveButton.setOnClickListener(click -> {
            String messageText = binding.textInput.getText().toString();
            if (!messageText.isEmpty()) {
                ChatMessage chatMessage = new ChatMessage(messageText, getCurrentTime(), false);
                chatModel.addMessage(chatMessage);
                binding.textInput.setText("");
            }
        });
    }

    private String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        return sdf.format(new Date());
    }

    class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        /*private ArrayList<ChatMessage> messages = new ArrayList<>();*/
        private final ArrayList<ChatMessage> messages = new ArrayList<>();

        public void setMessages(ArrayList<ChatMessage> newMessages) {
            // This prevents direct replacement of the messages reference
            messages.clear();
            messages.addAll(newMessages);
            notifyDataSetChanged(); // This line notifies the adapter to update the RecyclerView
        }

        @Override
        public int getItemViewType(int position) {
            return messages.get(position).isSentButton() ? 0 : 1;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            if (viewType == 0) {
                SentMessageBinding binding = SentMessageBinding.inflate(inflater, parent, false);
                return new SentViewHolder(binding);
            } else {
                ReceiveMessageBinding binding = ReceiveMessageBinding.inflate(inflater, parent, false);
                return new ReceivedViewHolder(binding);
            }
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            ChatMessage message = messages.get(position);
            if (getItemViewType(position) == 0) {
                ((SentViewHolder) holder).bind(message);
            } else {
                ((ReceivedViewHolder) holder).bind(message);
            }
        }

        @Override
        public int getItemCount() {
            return messages != null ? messages.size() : 0;
        }

        class SentViewHolder extends RecyclerView.ViewHolder {
            private final SentMessageBinding binding;

            SentViewHolder(SentMessageBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
            }

            void bind(ChatMessage message) {
                binding.message.setText(message.getMessage());
                binding.time.setText(message.getTimeSent());
            }
        }

        class ReceivedViewHolder extends RecyclerView.ViewHolder {
            private final ReceiveMessageBinding binding;

            ReceivedViewHolder(ReceiveMessageBinding binding) {
                super(binding.getRoot());
                this.binding = binding;
            }

            void bind(ChatMessage message) {
                binding.message.setText(message.getMessage());
                binding.time.setText(message.getTimeSent());
            }
        }
    }
}
