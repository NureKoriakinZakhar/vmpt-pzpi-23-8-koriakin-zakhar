const http = require('http');
const { WebSocketServer } = require('ws');

const server = http.createServer();
const wss = new WebSocketServer({ server });

const players = new Map();

function getLeaderboard() {
    return Array.from(players.values()).sort((a, b) => b.score - a.score);
}

function broadcast(data) {
    const msg = JSON.stringify(data);
    wss.clients.forEach(client => {
        if (client.readyState === 1) client.send(msg);
    });
}

wss.on('connection', (ws) => {
    const id = Math.random().toString(36).substring(2, 10);

    ws.on('message', (raw) => {
        try {
            const msg = JSON.parse(raw.toString());
            if (msg.type === 'join') {
                const name = (msg.name || 'Гравець').substring(0, 20);
                players.set(id, { id, name, score: 0 });
                ws.send(JSON.stringify({ type: 'joined', id }));
                broadcast({ type: 'leaderboard', players: getLeaderboard() });
            } else if (msg.type === 'tap') {
                if (players.has(id)) {
                    players.get(id).score += 1;
                    broadcast({ type: 'leaderboard', players: getLeaderboard() });
                }
            }
        } catch (e) {
            console.error('Помилка обробки повідомлення:', e.message);
        }
    });

    ws.on('close', () => {
        players.delete(id);
        broadcast({ type: 'leaderboard', players: getLeaderboard() });
    });

    ws.on('error', (err) => {
        console.error('Помилка WebSocket:', err.message);
        players.delete(id);
    });
});

const PORT = process.env.PORT || 3001;
server.listen(PORT, '0.0.0.0', () => {
    console.log(`Сервер запущено на порту ${PORT}`);
});
