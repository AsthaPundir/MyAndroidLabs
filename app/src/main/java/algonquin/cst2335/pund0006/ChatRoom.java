package algonquin.cst2335.pund0006;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import com.google.android.material.snackbar.Snackbar;

import algonquin.cst2335.pund0006.ChatMessage;
import algonquin.cst2335.pund0006.databinding.ActivityChatRoomBinding;
import algonquin.cst2335.pund0006.databinding.ReceiveMessageBinding;
import algonquin.cst2335.pund0006.databinding.SentMessageBinding;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ChatRoom extends AppCompatActivity {
    private ActivityChatRoomBinding binding;
    private  ChatRoomViewModel chatModel;
    private MyAdapter myAdapter;

    algonquin.cst2335.pund0006.MessageDatabase db;
    algonquin.cst2335.pund0006.ChatMessageDAO mDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatRoomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // create database connection
        db = Room.databaseBuilder(getApplicationContext(), algonquin.cst2335.pund0006.MessageDatabase.class, "database-lab8").build();
        mDAO = db.cmDAO();



        chatModel = new ViewModelProvider(this).get(ChatRoomViewModel.class);

        ArrayList<algonquin.cst2335.pund0006.ChatMessage> messages = chatModel.getMessages().getValue();
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
                // Insert message into database
                Executor dbThread = Executors.newSingleThreadExecutor();
                dbThread.execute(() -> {
                    mDAO.insertMessage(chatMessage);
                });
                chatModel.addMessage(chatMessage);
                binding.textInput.setText("");
            }
        });

        binding.receiveButton.setOnClickListener(click -> {
            String messageText = binding.textInput.getText().toString();
            if (!messageText.isEmpty()) {
                ChatMessage chatMessage = new ChatMessage(messageText, getCurrentTime(), false);
                // Insert message into database
                Executor dbThread = Executors.newSingleThreadExecutor();
                dbThread.execute(() -> {
                    mDAO.insertMessage(chatMessage);
                });
                chatModel.addMessage(chatMessage);
                binding.textInput.setText("");
            }
        });

        // Load messages from the database
        Executor thread = Executors.newSingleThreadExecutor();
        thread.execute(() -> {
            ArrayList<ChatMessage> dbMessages = new ArrayList<>(mDAO.getAllMessages());
            runOnUiThread(() -> {
                chatModel.getMessages().setValue(dbMessages);
            });
        });
    }

    private String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date());
    }

    class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
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
                itemView.setOnClickListener(v -> showDeleteConfirmation(getAbsoluteAdapterPosition()));
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
                // New: Set an OnClickListener to show the delete confirmation
                itemView.setOnClickListener(v -> showDeleteConfirmation(getAbsoluteAdapterPosition()));
            }

            void bind(ChatMessage message) {
                binding.message.setText(message.getMessage());
                binding.time.setText(message.getTimeSent());
            }
        }
        // Method to show the delete confirmation dialog
        private void showDeleteConfirmation(int position) {
            new AlertDialog.Builder(ChatRoom.this)
                    .setMessage("Do you want to delete this message?")
                    .setTitle("Delete Message")
                    .setPositiveButton("Yes", (dialog, id) -> deleteMessage(position))
                    .setNegativeButton("No", null)
                    .create()
                    .show();
        }

        // Method to handle message deletion
        private void deleteMessage(int position) {
            ChatMessage messageToDelete = messages.get(position);

            // Temporarily store the deleted message in case we need to undo
            ChatMessage tempDeletedMessage = messageToDelete;

            Executor dbThread = Executors.newSingleThreadExecutor();
            dbThread.execute(() -> {
                mDAO.deleteMessage(messageToDelete); // Make sure you have a delete method in your DAO
            });
            messages.remove(position);
            notifyItemRemoved(position);

            Snackbar.make(binding.getRoot(), "Message deleted", Snackbar.LENGTH_LONG)
                    .setAction("UNDO", view -> undoDelete(tempDeletedMessage, position))
                    .show();
        }

        // Method to undo the deletion of a message
        private void undoDelete(ChatMessage message, int position) {
            // Re-insert the message into the dataset
            messages.add(position, message);
            notifyItemInserted(position);

            // Optionally, re-insert the message into the database if necessary
            Executor dbThread = Executors.newSingleThreadExecutor();
            dbThread.execute(() -> {
                mDAO.insertMessage(message); // Assuming insertMessage is implemented in your DAO
            });
        }

        // Add this method to the MyAdapter class
        public void clearMessages() {
            messages.clear();
            notifyDataSetChanged();
        }


    } // Adapter class ends
    // create toolbar



} // chatroom class ends
