package it.unibo.arces.wot.sepa.apps.chat;

import it.unibo.arces.wot.sepa.commons.exceptions.SEPAPropertiesException;
import it.unibo.arces.wot.sepa.commons.exceptions.SEPAProtocolException;
import it.unibo.arces.wot.sepa.commons.exceptions.SEPASecurityException;
import it.unibo.arces.wot.sepa.commons.response.ErrorResponse;
import it.unibo.arces.wot.sepa.commons.response.Response;
import it.unibo.arces.wot.sepa.commons.response.SubscribeResponse;
import it.unibo.arces.wot.sepa.commons.sparql.ARBindingsResults;
import it.unibo.arces.wot.sepa.commons.sparql.Bindings;
import it.unibo.arces.wot.sepa.commons.sparql.BindingsResults;
import it.unibo.arces.wot.sepa.commons.sparql.RDFTermLiteral;
import it.unibo.arces.wot.sepa.pattern.Aggregator;
import it.unibo.arces.wot.sepa.pattern.ApplicationProfile;

public class MessageSender extends Aggregator {

	private Bindings message = new Bindings();

	private ChatListener listener;
	private boolean joined = false;
	
	public MessageSender(String nickname,ApplicationProfile jsap,ChatListener listener)
			throws SEPAProtocolException, SEPASecurityException, SEPAPropertiesException {
		super(jsap, "SEND_MESSAGE","SEND_MESSAGE");
		
		message.addBinding("sender", new RDFTermLiteral(nickname));

        this.listener = listener;
	}
	
	public boolean sendMessage(String receiver,String text) {
		message.addBinding("receiver", new RDFTermLiteral(receiver));
		message.addBinding("text", new RDFTermLiteral(text));
		
		Response ret = update(message);
		
		return ret.isUpdateResponse();
	}

	public boolean joinChat() {
		if (joined)
			return true;

		Response ret = subscribe(message);
		joined = !ret.isError();

		if (joined) {
			for (Bindings bindings : ((SubscribeResponse) ret).getBindingsResults().getBindings()) {
				listener.onMessageSent(
						new Message(
								message.getBindingValue("sender"),
								bindings.getBindingValue("receiver"),
								bindings.getBindingValue("text"),
								bindings.getBindingValue("time")));
			}
		}

		return joined;
	}

	public boolean leaveChat() {
		if(!joined) return true;
		
		Response ret = unsubscribe();
		joined = !ret.isUnsubscribeResponse();
		
		return !joined;
	}

	@Override
	public void onAddedResults(BindingsResults results){
		for (Bindings bindings : results.getBindings()) {
			listener.onMessageSent(
					new Message(
							message.getBindingValue("sender"),
							bindings.getBindingValue("receiver"),
							bindings.getBindingValue("text"),bindings.
							getBindingValue("time")));
		}
	}

	@Override
	public void onResults(ARBindingsResults results) {
		
	}

	@Override
	public void onRemovedResults(BindingsResults results) {
		for (Bindings bindings : results.getBindings()) {
			listener.onMessageRead(
					new Message(
							message.getBindingValue("sender"),
							bindings.getBindingValue("receiver"),
							bindings.getBindingValue("text"),
							bindings.getBindingValue("time")));
		}
	}

	@Override
	public void onPing() {

	}

	@Override
	public void onBrokenSocket() {
		joined = false;
		listener.onBrokenConnection();
	}

	@Override
	public void onError(ErrorResponse errorResponse) {

	}

}
