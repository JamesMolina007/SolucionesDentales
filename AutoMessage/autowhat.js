const express = require('express');
const qrcode = require('qrcode-terminal');
const { Client, LocalAuth, MessageMedia  } = require('whatsapp-web.js');
console.log("Conectando...");
const app = express();

app.use(express.json());

console.log("Creando cliente...");
const client = new Client({
    authStrategy: new LocalAuth()
});

console.log("Creando qr...");
client.on('qr', qr => {
    qrcode.generate(qr, {small: true});
});

console.log("Creando ready...");
client.on('ready', () => {
  app.listen(3000, () => {
    console.log('API escuchando en el puerto 3000');
  });
});

console.log("Creando endpoint...");
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

app.post('/enviar-archivo', (req, res) => {
  const phoneNumber = `504${req.body.to}@c.us`;
  const message = req.body.message;
  const file = req.body.file;
  const media = MessageMedia.fromFilePath(file);

  client.sendMessage(phoneNumber, media, { caption: message }).then(() => {
    console.log('Archivo enviado correctamente');
    res.json({ success: true, message: 'Archivo enviado correctamente' });
  }).catch((error) => {
    console.error('Error al enviar el archivo:', error);
    res.status(500).json({ success: false, message: 'Error al enviar el archivo' });
  });
});

console.log("Inicializando cliente...");
client.initialize();
