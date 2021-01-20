package org.jivesoftware.openfire.plugin;

import java.io.File;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.jivesoftware.openfire.XMPPServer;
import org.jivesoftware.openfire.container.Plugin;
import org.jivesoftware.openfire.container.PluginManager;
import org.jivesoftware.openfire.group.Group;
import org.jivesoftware.openfire.group.GroupManager;
import org.jivesoftware.openfire.group.GroupNotFoundException;
import org.jivesoftware.openfire.user.User;
import org.jivesoftware.openfire.user.UserManager;
import org.jivesoftware.openfire.user.UserNotFoundException;
import org.jivesoftware.openfire.muc.*;

import org.xmpp.packet.JID;

public class RawPropertyEditorPlugin implements Plugin {
    private static final Logger Log = LoggerFactory.getLogger(RawPropertyEditorPlugin.class);
    private UserManager userManager;
    private XMPPServer server;
    private GroupManager groupManager;
    private Map<String, Map<String, String>> muc_properties;

    @Override
    public void initializePlugin(PluginManager manager, File pluginDirectory)
    {
        Log.debug("Starting Raw Property Editor Plugin");
        server = XMPPServer.getInstance();
        userManager = server.getUserManager();
        groupManager = GroupManager.getInstance();
        muc_properties = new HashMap<String, Map<String, String>>();
    }

    private User getAndCheckUser(String username) {
        JID targetJID = server.createJID(username, null);
        try {
            return userManager.getUser(targetJID.getNode());
        } catch (UserNotFoundException e) {
            // TODO Auto-generated catch block
            Log.error("getAndCheckUser", e);
        }
        return null;
    }

    private Group getAndCheckGroup(String groupname) {
        JID targetJID = server.createJID(groupname, null, true);

        try {
            return groupManager.getGroup(targetJID.getNode());
        } catch (GroupNotFoundException e) {
            // TODO Auto-generated catch block
            Log.error("getAndCheckGroup", e);
        }
        return null;
    }

    @Override
    public void destroyPlugin() {
        Log.debug("Destroy Raw Property Editor Plugin");
    }

    public void addProperties(String username, String propname, String propvalue) {

        try {
            User user = getAndCheckUser(username);
            user.getProperties().put(propname, propvalue);
        } catch (Exception e) {
            Log.error("", e);
        }

    }

    public void addGroupProperties(String groupname, String propname, String propvalue) {

        try {
            Group group = getAndCheckGroup(groupname);
            group.getProperties().put(propname, propvalue);
        } catch (Exception e) {
            Log.error("addProperties", e);
        }

    }

    public void deleteGroupProperties(String groupname, String propname) {
        try {
            Group group = getAndCheckGroup(groupname);
            group.getProperties().remove(propname);
        } catch (Exception e) {
            Log.error("deleteGroupProperties", e);
        }
    }

    public void deleteProperties(String username, String propname) {
        try {
            User user = getAndCheckUser(username);
            user.getProperties().remove(propname);
        } catch (Exception e) {
            Log.error("deleteProperties", e);
        }
    }

    public Map<String, String> getUserProperties(String username) {
        User user = getAndCheckUser(username);
        return user.getProperties();
    }

    public Map<String, String> getGroupProperties(String groupname) {
        Group group = getAndCheckGroup(groupname);
        return group.getProperties();
    }

    public Map<String, String> getAndCheckGroupChat(JID roomJID)
    {
        Map<String, String> properties = new HashMap<String, String>();

        try {
            MUCRoom room = server.getMultiUserChatManager().getMultiUserChatService(roomJID).getChatRoom(roomJID.getNode());
            properties = muc_properties.get(roomJID.toString());

            if (properties == null)
            {
                properties = new MUCRoomProperties(room.getID());
                muc_properties.put(roomJID.toString(), properties);
            }

        } catch (Exception e) {
            Log.error("getGroupChatProperties", e);
        }
        return properties;
    }

    public Map<String, String> getGroupChatProperties(JID roomJID) {
        Map<String, String> properties = getAndCheckGroupChat(roomJID);
        return properties;
    }

    public void addGroupChatProperties(JID roomJID, String propname, String propvalue)
    {
        try {
            Map<String, String> properties = getAndCheckGroupChat(roomJID);
            properties.put(propname, propvalue);
            Log.debug("addGroupChatProperties " + roomJID + " " + propname + " " + propvalue);
        } catch (Exception e) {
            Log.error("addGroupChatProperties", e);
        }
    }

    public void deleteGroupChatProperties(JID roomJID, String propname)
    {
        try {
            Map<String, String> properties = getAndCheckGroupChat(roomJID);
            properties.remove(propname);
            Log.debug("deleteGroupChatProperties " + roomJID + " " + propname);
        } catch (Exception e) {
            Log.error("deleteGroupChatProperties", e);
        }
    }
}