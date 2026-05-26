// VitalTrack WebSocket Client Library
(function() {
    if (!wsUsername || !wsRole || wsUsername === 'null' || wsRole === 'null') {
        console.log("WebSocket client inactive: user not authenticated.");
        return;
    }

    let chatSocket = null;
    let auditSocket = null;
    let stockSocket = null;

    function createWebSocket(pathSuffix, onMessageCallback, onOpenStateChange) {
        let ws = null;
        let timer = null;

        function connect() {
            const protocol = window.location.protocol === 'https:' ? 'wss:' : 'ws:';
            const host = window.location.host;
            const endpoint = `${protocol}//${host}${contextPath}${pathSuffix}`;

            console.log(`Connecting to WebSocket: ${endpoint}`);
            ws = new WebSocket(endpoint);

            ws.onopen = function() {
                console.log(`WebSocket connection opened: ${pathSuffix}`);
                if (onOpenStateChange) onOpenStateChange(true);
            };

            ws.onmessage = function(event) {
                onMessageCallback(event.data);
            };

            ws.onclose = function() {
                console.warn(`WebSocket connection closed: ${pathSuffix}. Reconnecting...`);
                if (onOpenStateChange) onOpenStateChange(false);
                clearTimeout(timer);
                timer = setTimeout(connect, 3000);
            };

            ws.onerror = function(error) {
                console.error(`WebSocket error on ${pathSuffix}:`, error);
            };
        }

        connect();

        return {
            send: function(msg) {
                if (ws && ws.readyState === WebSocket.OPEN) {
                    ws.send(msg);
                    return true;
                }
                return false;
            },
            isOpen: function() {
                return ws && ws.readyState === WebSocket.OPEN;
            }
        };
    }

    // --- UI Interactions ---

    function updateChatConnectionStatus(connected) {
        const dot = document.getElementById('chat-status-dot');
        if (dot) {
            dot.style.background = connected ? '#10b981' : '#ef4444';
        }
    }

    function updateOnlineCount(count) {
        const countBadge = document.getElementById('chat-online-count');
        if (countBadge) {
            countBadge.innerText = `${count} Online`;
        }
    }

    function appendChatMessage(sender, role, message) {
        const body = document.getElementById('chat-widget-body');
        if (!body) return;

        const isMe = sender === wsUsername;
        const messageDiv = document.createElement('div');
        messageDiv.className = `chat-message ${isMe ? 'chat-me' : 'chat-other'}`;

        const senderSpan = document.createElement('span');
        senderSpan.className = 'chat-sender';
        senderSpan.innerText = isMe ? 'You' : `${sender} (${role})`;

        const textDiv = document.createElement('div');
        textDiv.className = 'chat-text';
        textDiv.innerText = message;

        messageDiv.appendChild(senderSpan);
        messageDiv.appendChild(textDiv);
        body.appendChild(messageDiv);

        // Auto Scroll to bottom
        body.scrollTop = body.scrollHeight;

        // If chat widget is minimized and it's not my message, pulse badge
        const wrapper = document.getElementById('chat-widget-wrapper');
        if (wrapper && wrapper.classList.contains('minimized') && !isMe) {
            const toggle = document.getElementById('chat-widget-toggle');
            if (toggle) toggle.classList.add('chat-pulse');
        }
    }

    function showToastNotification(message, type) {
        let container = document.getElementById('toast-container');
        if (!container) {
            container = document.createElement('div');
            container.id = 'toast-container';
            document.body.appendChild(container);
        }

        const toast = document.createElement('div');
        toast.className = `toast toast-${type}`;
        
        const icon = document.createElement('i');
        icon.className = 'fa-solid fa-triangle-exclamation';
        toast.appendChild(icon);

        const textSpan = document.createElement('span');
        textSpan.className = 'toast-text';
        textSpan.innerText = message;
        toast.appendChild(textSpan);

        const closeBtn = document.createElement('button');
        closeBtn.innerHTML = '&times;';
        closeBtn.onclick = () => toast.remove();
        toast.appendChild(closeBtn);

        container.appendChild(toast);

        setTimeout(() => toast.classList.add('visible'), 10);

        setTimeout(() => {
            toast.classList.remove('visible');
            setTimeout(() => toast.remove(), 400);
        }, 7000);
    }

    function appendActivityFeed(activityMsg) {
        const feedList = document.getElementById('activity-feed-list');
        if (!feedList) return;

        const li = document.createElement('li');
        li.className = 'activity-item new-activity';
        
        const isUrgent = activityMsg.includes("CRITICAL") || activityMsg.includes("STOCK ALERT") || activityMsg.includes("WARNING");
        if (isUrgent) {
            li.style.color = '#ef4444';
            li.style.fontWeight = 'bold';
        }
        
        const time = new Date().toLocaleTimeString([], { hour: '2-digit', minute: '2-digit', second: '2-digit' });
        li.innerHTML = `<span class='activity-time'>[${time}]</span> <span class='activity-text'>${activityMsg}</span>`;

        feedList.insertBefore(li, feedList.firstChild);

        while (feedList.children.length > 8) {
            feedList.removeChild(feedList.lastChild);
        }
    }

    // --- Setup Listeners when DOM is Ready ---
    document.addEventListener('DOMContentLoaded', function() {
        // Initialize WebSocket connections
        auditSocket = createWebSocket('/audit_feeds', function(data) {
            appendActivityFeed(data);
        });

        stockSocket = createWebSocket('/stock_alerts', function(data) {
            showToastNotification(data, 'error'); // Show red toast
            
            const warningsCard = document.getElementById('stock-warnings-card');
            const warningsList = document.getElementById('stock-warnings-list');
            if (warningsCard && warningsList) {
                const li = document.createElement('li');
                li.style.display = 'flex';
                li.style.alignItems = 'center';
                li.style.gap = '0.5rem';
                li.innerHTML = `<i class='fa-solid fa-triangle-exclamation' style='color: #dc2626;'></i> ${data}`;
                warningsList.insertBefore(li, warningsList.firstChild);
                
                warningsCard.style.display = 'block';
            }
        });

        chatSocket = createWebSocket('/chat', function(data) {
            try {
                const payload = JSON.parse(data);
                if (payload.type === 'online_count') {
                    updateOnlineCount(payload.count);
                } else {
                    appendChatMessage(payload.sender, payload.role, payload.message);
                }
            } catch (err) {
                console.error("Error parsing chat message:", err);
            }
        }, updateChatConnectionStatus);

        // Chat Toggle Handling
        const toggle = document.getElementById('chat-widget-toggle');
        const wrapper = document.getElementById('chat-widget-wrapper');
        const closeBtn = document.getElementById('chat-close-btn');

        if (toggle && wrapper) {
            const savedState = localStorage.getItem('vt_chat_minimized');
            if (savedState === 'false') {
                wrapper.classList.remove('minimized');
            } else {
                wrapper.classList.add('minimized');
            }

            toggle.addEventListener('click', function() {
                wrapper.classList.toggle('minimized');
                toggle.classList.remove('chat-pulse');
                localStorage.setItem('vt_chat_minimized', wrapper.classList.contains('minimized'));
            });

            if (closeBtn) {
                closeBtn.addEventListener('click', function(e) {
                    e.stopPropagation();
                    wrapper.classList.add('minimized');
                    localStorage.setItem('vt_chat_minimized', 'true');
                });
            }
        }

        // Chat Input Submission
        const chatInput = document.getElementById('chat-input-field');
        const sendBtn = document.getElementById('chat-send-btn');

        function sendChatMessage() {
            if (!chatInput) return;
            const message = chatInput.value.trim();
            if (message.length === 0) return;

            const sent = chatSocket.send(JSON.stringify({
                sender: wsUsername,
                role: wsRole,
                message: message
            }));

            if (sent) {
                chatInput.value = '';
            } else {
                console.error("Unable to send chat: WebSocket is offline.");
                showToastNotification("Chat offline. Attempting to reconnect...", "error");
            }
        }

        if (chatInput) {
            chatInput.addEventListener('keydown', function(event) {
                if (event.key === 'Enter') {
                    event.preventDefault();
                    sendChatMessage();
                }
            });
        }

        if (sendBtn) {
            sendBtn.addEventListener('click', sendChatMessage);
        }
    });
})();
