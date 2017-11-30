package it.unibo.arces.wot.sepa.apps.chat;

public class Message {
	private String from;
	private String to;
	private String time;
	private String text;

	private boolean sent = false;
	private boolean read = false;
    private boolean received = false;

	public Message(String from, String to, String text, String time) {
		this.setFrom(from);
		this.setText(text);
		this.setTime(time);
		this.setTo(to);
	}

	public String getTo() {
		return to;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

    public String toString() {
        return time+" ["+from+"]-->["+to+"] "+text;
    }

	public String getFrom() {
		return from;
	}

	public void setTo(String to) {
		this.to = to;
	}

    public boolean isSent() {
        return sent;
    }

    public void setSent() {
        this.sent = true;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead() {
        this.read = true;
    }

    public boolean isReceived() {
        return received;
    }

    public void setReceived() {
        this.received = true;
    }
}
