const express = require('express');
const qrcode = require('qrcode-terminal');
const { Client, LocalAuth } = require('whatsapp-web.js');

const app = express();

app.use(express.json());

const client = new Client({
    authStrategy: new LocalAuth()
});

client.on('qr', qr => {
    qrcode.generate(qr, {small: true});
});

client.on('ready', () => {
  app.listen(3000, () => {
    console.log('API escuchando en el puerto 3000');
  });
});


app.post('/enviar-mensaje', (req, res) => {
  const phoneNumber = `504${req.body.to}@c.us`;
  const message = req.body.message;

  client.sendMessage(phoneNumber, message).then(() => {
    console.log('Mensaje enviado correctamente');
    res.json({ success: true, message: 'Mensaje enviado correctamente' });
  }).catch((error) => {
    console.error('Error al enviar el mensaje:', error);
    res.status(500).json({ success: false, message: 'Error al enviar el mensaje' });
  });
});



client.initialize();
