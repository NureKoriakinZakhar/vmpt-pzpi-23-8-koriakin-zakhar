const express = require('express');
const http = require('http');
const { Server } = require('socket.io');
const cors = require('cors');

const app = express();
app.use(cors());

const server = http.createServer(app);

const io = new Server(server, {
    cors: {
        origin: "http://localhost:3000",
        methods: ["GET", "POST"]
    }
});

let players = {}; 

io.on('connection', (socket) => {
    console.log('Кліент підʼєднався:', socket.id);

    players[socket.id] = { id: socket.id, score: 0 };

    io.emit('updateLeaderboard', Object.values(players));

    socket.on('click', () => {
        if(players[socket.id]) {
            players[socket.id].score += 1;
            io.emit('updateLeaderboard', Object.values(players));
        }
    });

    socket.on('disconnect', () => {
        console.log('Кліент відʼєднався:', socket.id);
        delete players[socket.id];
        io.emit('updateLeaderboard', Object.values(players));
    });
});

server.listen(3001, () => {
    console.log('Сервер працює на порту 3001');
});