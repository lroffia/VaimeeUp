package it.unibo.arces.wot.sepa.apps.chat;

import java.util.ArrayList;

import it.unibo.arces.wot.sepa.commons.exceptions.SEPAProtocolException;
import it.unibo.arces.wot.sepa.commons.response.ErrorResponse;
import it.unibo.arces.wot.sepa.commons.response.Response;
import it.unibo.arces.wot.sepa.commons.response.SubscribeResponse;
import it.unibo.arces.wot.sepa.commons.sparql.ARBindingsResults;
import it.unibo.arces.wot.sepa.commons.sparql.Bindings;
import it.unibo.arces.wot.sepa.commons.sparql.BindingsResults;
import it.unibo.arces.wot.sepa.pattern.ApplicationProfile;
import it.unibo.arces.wot.sepa.pattern.Consumer;

/**
 * Created by luca on 19/11/17.
 */
public class UsersListener extends Consumer {
    private ChatListener listener;
    private boolean joined;

    public UsersListener(ApplicationProfile appProfile,ChatListener listener) throws SEPAProtocolException {
        super(appProfile, "USERS");
        this.listener = listener;
    }

    public boolean joinChat() {
        if (joined)
            return true;

        Response ret = subscribe(null);
        joined = !ret.isError();

        if (joined) {
            ArrayList<String> users = new ArrayList<>();
            for (Bindings bindings : ((SubscribeResponse) ret).getBindingsResults().getBindings()) {
                users.add(bindings.getBindingValue("userName"));
            }
            listener.onAddedUsers(users);
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
    public void onResults(ARBindingsResults results) {

    }

    @Override
    public void onAddedResults(BindingsResults results) {
        ArrayList<String> users = new ArrayList<>();
        for(Bindings bindings:results.getBindings()) {
            String userName = bindings.getBindingValue("userName");
            users.add(userName);
        }
        listener.onAddedUsers(users);
    }

    @Override
    public void onRemovedResults(BindingsResults results) {
        ArrayList<String> users = new ArrayList<>();
        for(Bindings bindings:results.getBindings()) {
            String userName = bindings.getBindingValue("userName");
            users.add(userName);
        }
        listener.onRemovedUsers(users);
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
