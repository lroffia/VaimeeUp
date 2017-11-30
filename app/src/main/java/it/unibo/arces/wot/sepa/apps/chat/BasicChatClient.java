package it.unibo.arces.wot.sepa.apps.chat;

import it.unibo.arces.wot.sepa.commons.exceptions.SEPAPropertiesException;
import it.unibo.arces.wot.sepa.commons.exceptions.SEPAProtocolException;
import it.unibo.arces.wot.sepa.commons.exceptions.SEPASecurityException;
import it.unibo.arces.wot.sepa.pattern.ApplicationProfile;

public abstract class BasicChatClient implements ChatListener {

	private MessageSender sender;
	private MessageReceiver receiver;

	private String nickname;
	private ApplicationProfile app;

	public BasicChatClient(String nickname,ApplicationProfile app)  {
		this.nickname = nickname;
        this.app = app;
	}

	public boolean joinChat() throws SEPAPropertiesException,SEPAProtocolException,SEPASecurityException{
        sender = new MessageSender(nickname,app,this);
        receiver = new MessageReceiver(nickname,app,this);

        return (sender.joinChat() && receiver.joinChat());
	}

	public boolean leaveChat() {
		return (sender.leaveChat() && receiver.leaveChat());
	}

	public boolean sendMessage(String receiver,String message) {
		return sender.sendMessage(receiver,message);
	}
}
