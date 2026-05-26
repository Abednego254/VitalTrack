package app.websocket;

import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint("/chat")
public class ChatWs {

    private static final Set<Session> chatSessions =
        new CopyOnWriteArraySet<>();

    @OnOpen
    public void onOpen(Session session){
        chatSessions.add(session);
        System.out.println("Ws chat: session opened: " + session.getId());
        broadcastOnlineCount();
    }

    @OnClose
    public void onClose(Session session) {
        chatSessions.remove(session);
        broadcastOnlineCount();
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        // Broadcast incoming chat message payload to all connected users
        for (Session chatSession : chatSessions) {
            if (chatSession.isOpen()) {
                try {
                    chatSession.getBasicRemote().sendText(message);
                } catch (IOException e) {
                    System.err.println("Ws chat: failed to forward message to " + chatSession.getId());
                }
            }
        }
    }

    private static void broadcastOnlineCount() {
        String payload = "{\"type\":\"online_count\",\"count\":" + chatSessions.size() + "}";
        for (Session chatSession : chatSessions) {
            if (chatSession.isOpen()) {
                try {
                    chatSession.getBasicRemote().sendText(payload);
                } catch (IOException e) {
                    // Ignore
                }
            }
        }
    }
}
