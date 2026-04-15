import React, { useState, useEffect } from 'react';
import io from 'socket.io-client';

const socket = io.connect("http://localhost:3001");

function App() {
  const [leaderboard, setLeaderboard] = useState([]);

  useEffect(() => {
    socket.on("updateLeaderboard", (data) => {
      const sortedData = data.sort((a, b) => b.score - a.score);
      setLeaderboard(sortedData);
    });
  }, []);

  const handleClick = () => {
    socket.emit("click");
  };

  return (
    <div style={{ textAlign: 'center', marginTop: '50px', fontFamily: 'sans-serif' }}>
      <h1>Онлайн Клікер</h1>
      
      <button 
        onClick={handleClick} 
        style={{ 
          padding: '20px 40px', 
          fontSize: '24px', 
          cursor: 'pointer', 
          backgroundColor: '#4CAF50', 
          color: 'white', 
          border: 'none', 
          borderRadius: '10px',
          boxShadow: '0 4px 6px rgba(0,0,0,0.1)'
        }}>
        КЛІКАЙ!
      </button>

      <h2 style={{ marginTop: '40px' }}>Таблиця лідерів (Live)</h2>
      <ul style={{ listStyle: 'none', padding: 0, maxWidth: '400px', margin: '0 auto' }}>
        {leaderboard.map((player, index) => (
          <li key={player.id} style={{ 
            fontSize: '18px', 
            margin: '10px 0', 
            padding: '10px', 
            backgroundColor: '#f1f1f1', 
            borderRadius: '5px',
            display: 'flex',
            justifyContent: 'space-between'
          }}>
            <span><strong>#{index + 1}</strong> Гравець {player.id.substring(0, 5)}...</span>
            <strong>{player.score} очок</strong>
          </li>
        ))}
      </ul>
    </div>
  );
}

export default App;