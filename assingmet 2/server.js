const express = require('express');
const path = require('path');

const app = express();
const PORT = process.env.PORT || 3003;

let submittedName = '';

app.use(express.urlencoded({ extended: false }));
app.use(express.static(path.join(__dirname, 'public')));

app.post('/submit', (req, res) => {
    submittedName = (req.body.name || '').trim();

    if (!submittedName) {
        return res.redirect('/?error=' + encodeURIComponent('Please enter your name before continuing.'));
    }

    return res.redirect('/welcome?name=' + encodeURIComponent(submittedName));
});

app.get('/welcome', (req, res) => {
    const name = (req.query.name || submittedName || '').toString().trim();

    const safeName = name
        .replace(/&/g, '&amp;')
        .replace(/</g, '&lt;')
        .replace(/>/g, '&gt;')
        .replace(/\"/g, '&quot;')
        .replace(/'/g, '&#039;');

    const title = safeName ? `Hello, ${safeName}!` : 'Hello!';

    res.send(`<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Greeting</title>
  <link rel="stylesheet" href="/styles.css">
</head>
<body>
  <main class="page-wrap">
    <section class="card card-small">
      <h1>${title}</h1>
      <p class="subtitle">Name received by POST and shown through GET.</p>
      <a class="link-button" href="/">Go Back</a>
    </section>
  </main>
</body>
</html>`);
});

app.listen(PORT, () => {
    console.log(`Server running at http://localhost:${PORT}`);
});
