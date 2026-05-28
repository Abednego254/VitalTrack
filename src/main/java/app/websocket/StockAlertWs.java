package app.websocket;

import jakarta.websocket.OnClose;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint("/stock_alerts")
public class StockAlertWs {

    // Thread-safe set of all connected dashboards (Admins & Nurses)
    private static final Set<Session> alertSessions =
        new CopyOnWriteArraySet<>();

    @OnOpen
    public void onOpen(Session session) {
        // Auth is enforced at HTTP layer by HospitalAuthenticationFilter.
        // Only authenticated users can reach the dashboard page that opens this socket.
        alertSessions.add(session);
        System.out.println("Ws stock alert: session opened: " + session.getId());
    }

    @OnClose
    public void onClose(Session session) {
        alertSessions.remove(session);
    }

    public static void broadcast(String alertMessage) {
        for (Session alertSession : alertSessions){
            if (alertSession.isOpen()){
                try {
                    // Instantly push the text to the browser client
                    alertSession.getBasicRemote().sendText(alertMessage);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
