package algonquin.cst2335.pund0006;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity
public class ChatMessage {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name="message")
    public String message;

    @ColumnInfo(name="TimeSent")
    public String timeSent;

    @ColumnInfo(name="SendOrReceive")
    public boolean isSend;

    public ChatMessage(String message, String timeSent, boolean isSend) {
        this.message = message;
        this.timeSent = timeSent;
        this.isSend = isSend;
    }

    // Getter for message
    public String getMessage() {
        return message;
    }

    // Setter for message
    public void setMessage(String message) {
        this.message = message;
    }

    // Getter for timeSent
    public String getTimeSent() {
        return timeSent;
    }

    // Setter for timeSent
    public void setTimeSent(String timeSent) {
        this.timeSent = timeSent;
    }

    // Getter for isSend
    public boolean isSentButton() {
        return isSend;
    }

    // Setter for isSend
    public void setSentButton(boolean isSend) {
        this.isSend = isSend;
    }
}
