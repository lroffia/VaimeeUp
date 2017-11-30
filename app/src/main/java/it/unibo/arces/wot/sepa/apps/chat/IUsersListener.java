package it.unibo.arces.wot.sepa.apps.chat;

import java.util.ArrayList;

/**
 * Created by luca on 30/11/17.
 */

public interface IUsersListener {
    void onAddedUsers(ArrayList<String> users);
    void onRemovedUsers(ArrayList<String> users);
}
