package algonquin.cst2335.pund0006;


import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import java.util.ArrayList;

import algonquin.cst2335.pund0006.ChatMessage;


/** ViewModel to hold the list of chat messages */
public class ChatRoomViewModel extends ViewModel {
    private final MutableLiveData<ArrayList<ChatMessage>> messages = new MutableLiveData<>(new ArrayList<>());

    public ChatRoomViewModel() {
        messages.setValue(new ArrayList<>());
    }
    public MutableLiveData<ArrayList<ChatMessage>> getMessages() {
        return messages;
    }

    public void addMessage(ChatMessage message) {
        ArrayList<ChatMessage> currentMessages = messages.getValue();
        if (currentMessages == null) {
            currentMessages = new ArrayList<>();
        }
        currentMessages.add(message);
        messages.setValue(currentMessages);
    }

}