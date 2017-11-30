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
    private IUsersListener listener;
    private boolean joined;
    ArrayList<String> users = new ArrayList<>();

    public UsersListener(ApplicationProfile appProfile,IUsersListener listener) throws SEPAProtocolException {
        super(appProfile, "USERS");
        this.listener = listener;
    }

    public boolean joinChat() {
        if (joined)
            return true;

        Response ret = subscribe(null);
        joined = !ret.isError();

        if (joined) {
            ArrayList<String> temp = new ArrayList<>();
            for (Bindings bindings : ((SubscribeResponse) ret).getBindingsResults().getBindings()) {
                String userName = bindings.getBindingValue("userName");
                if (users.contains(userName)) continue;
                temp.add(userName);
                users.add(userName);
            }
            if (!temp.isEmpty()) listener.onAddedUsers(temp);
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
        ArrayList<String> temp = new ArrayList<>();
        for (Bindings bindings : results.getBindings()) {
            String userName = bindings.getBindingValue("userName");
            if (users.contains(userName)) continue;
            temp.add(userName);
            users.add(userName);
        }
        if (!temp.isEmpty()) listener.onAddedUsers(temp);
    }

    @Override
    public void onRemovedResults(BindingsResults results) {
        ArrayList<String> temp = new ArrayList<>();
        for (Bindings bindings : results.getBindings()) {
            String userName = bindings.getBindingValue("userName");
            if (!users.contains(userName)) continue;
            temp.add(userName);
            users.remove(userName);
        }
        if (!temp.isEmpty()) listener.onRemovedUsers(temp);
    }

    @Override
    public void onPing() {

    }

    @Override
    public void onBrokenSocket() {
        joined = false;
        new Thread() {
            public void run() {
                while(!joinChat()) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        return;
                    }
                }
            }
        }.start();
    }

    @Override
    public void onError(ErrorResponse errorResponse) {

    }
}
