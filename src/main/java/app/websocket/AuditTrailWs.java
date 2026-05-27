package app.websocket;

import jakarta.websocket.CloseReason;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint("/audit_feeds")
public class AuditTrailWs {

    private static final Set<Session> auditSessions =
        new CopyOnWriteArraySet<>();

    @OnOpen
    public void onOpen(Session session) throws IOException {
        if (session.getUserPrincipal() == null) {
            System.out.println(">>> WebSocket Audit Trail: Unauthorized attempt to connect: " + session.getId());
            session.close(new CloseReason(CloseReason.CloseCodes.VIOLATED_POLICY, "Unauthorized"));
            return;
        }
        auditSessions.add(session);
        System.out.println("Ws: session opened: " + session.getId());
    }

    @OnClose
    public void onClose(Session session) {
        auditSessions.remove(session);
    }

    public static void broadcast(String auditActivity) {
        for (Session auditSession : auditSessions){
            if (auditSession.isOpen()){
                try {
                    auditSession.getBasicRemote().sendText(auditActivity);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
