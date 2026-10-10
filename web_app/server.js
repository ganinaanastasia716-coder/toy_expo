const http = require('http');
const fs = require('fs');
const path = require('path');

process.on('uncaughtException', (err) => {
  console.error('[ToyExpo Uncaught Exception]:', err.message || err);
});
process.on('unhandledRejection', (reason) => {
  console.error('[ToyExpo Unhandled Rejection]:', reason);
});

const PORT = process.env.SERVER_PORT || 3000;
const PUBLIC_DIR = path.join(__dirname, 'public');
const BOT_TOKEN = process.env.TELEGRAM_BOT_TOKEN || '';
const APP_URL = process.env.APP_URL || 'https://ais-dev-j26bpgydmt66pujpm56vsi-851278613227.asia-east1.run.app';

// MIME types for static assets
const MIME_TYPES = {
  '.html': 'text/html; charset=utf-8',
  '.css': 'text/css; charset=utf-8',
  '.js': 'text/javascript; charset=utf-8',
  '.json': 'application/json; charset=utf-8',
  '.png': 'image/png',
  '.jpg': 'image/jpeg',
  '.jpeg': 'image/jpeg',
  '.svg': 'image/svg+xml',
  '.ico': 'image/x-icon'
};

const server = http.createServer((req, res) => {
  // CORS Headers
  res.setHeader('Access-Control-Allow-Origin', '*');
  res.setHeader('Access-Control-Allow-Methods', 'GET, POST, OPTIONS');
  res.setHeader('Access-Control-Allow-Headers', 'Content-Type');

  if (req.method === 'OPTIONS') {
    res.writeHead(204);
    res.end();
    return;
  }

  const parsedUrl = new URL(req.url, `http://${req.headers.host}`);
  let pathname = parsedUrl.pathname;

  if (pathname === '/') {
    pathname = '/index.html';
  }

  // API endpoint for bot status and health check
  if (pathname === '/api/status') {
    res.writeHead(200, { 'Content-Type': 'application/json' });
    res.end(JSON.stringify({
      status: 'ok',
      service: 'ToyExpo Telegram Mini App & Bot Server',
      botActive: !!BOT_TOKEN,
      appUrl: APP_URL,
      time: new Date().toISOString()
    }));
    return;
  }

  const filePath = path.join(PUBLIC_DIR, pathname);

  fs.stat(filePath, (err, stats) => {
    if (err || !stats.isFile()) {
      // SPA Fallback to index.html
      const fallbackPath = path.join(PUBLIC_DIR, 'index.html');
      fs.readFile(fallbackPath, (fallbackErr, content) => {
        if (fallbackErr) {
          res.writeHead(404, { 'Content-Type': 'text/plain; charset=utf-8' });
          res.end('404 Not Found');
        } else {
          res.writeHead(200, { 'Content-Type': 'text/html; charset=utf-8' });
          res.end(content);
        }
      });
      return;
    }

    const ext = path.extname(filePath).toLowerCase();
    const contentType = MIME_TYPES[ext] || 'application/octet-stream';

    fs.readFile(filePath, (readErr, content) => {
      if (readErr) {
        res.writeHead(500, { 'Content-Type': 'text/plain; charset=utf-8' });
        res.end('500 Internal Server Error');
      } else {
        res.writeHead(200, { 'Content-Type': contentType });
        res.end(content);
      }
    });
  });
});

server.listen(PORT, '0.0.0.0', () => {
  console.log(`====================================================`);
  console.log(`[ToyExpo] Server running on http://0.0.0.0:${PORT}`);
  console.log(`[ToyExpo] Mini App URL: ${APP_URL}`);
  if (BOT_TOKEN) {
    console.log(`[ToyExpo] Telegram Bot Token detected. Starting Bot Polling...`);
    startTelegramBotPolling(BOT_TOKEN);
  } else {
    console.log(`[ToyExpo] TELEGRAM_BOT_TOKEN is not set.`);
    console.log(`[ToyExpo] To connect your bot, run:`);
    console.log(`  export TELEGRAM_BOT_TOKEN="your_token_from_botfather"`);
    console.log(`  node server.js`);
  }
  console.log(`====================================================`);
});

// Zero-Dependency Telegram Bot Polling Implementation
let lastUpdateId = 0;

async function startTelegramBotPolling(token) {
  const apiUrl = `https://api.telegram.org/bot${token}`;

  // Configure bot commands and menu button in Telegram
  await initBotSettings(apiUrl);

  async function poll() {
    try {
      const response = await fetch(`${apiUrl}/getUpdates?offset=${lastUpdateId + 1}&timeout=25`);
      if (!response.ok) {
        setTimeout(poll, 5000);
        return;
      }
      const data = await response.json();
      if (data.ok && Array.isArray(data.result)) {
        for (const update of data.result) {
          lastUpdateId = Math.max(lastUpdateId, update.update_id);
          if (update.message) {
            await handleTelegramMessage(apiUrl, update.message);
          } else if (update.callback_query) {
            await handleTelegramCallbackQuery(apiUrl, update.callback_query);
          }
        }
      }
    } catch (err) {
      console.error('[Bot Polling Error]:', err.message);
    }
    setTimeout(poll, 1000);
  }

  poll();
}

async function initBotSettings(apiUrl) {
  try {
    // 1. Set bot menu commands
    await fetch(`${apiUrl}/setMyCommands`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        commands: [
          { command: 'start', description: '👾 Запустить бота и открыть Mini App' },
          { command: 'expos', description: '📅 Календарь мировых выставок' },
          { command: 'drops', description: '🎯 Эксклюзивные релизы и дропы' },
          { command: 'cities', description: '📍 Столицы дизайнерских игрушек' },
          { command: 'help', description: 'ℹ️ Справка и возможности' }
        ]
      })
    });

    // 2. Set permanent Web App Menu Button in the chat input
    await fetch(`${apiUrl}/setChatMenuButton`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        menu_button: {
          type: 'web_app',
          text: 'ToyExpo 👾',
          web_app: { url: APP_URL }
        }
      })
    });

    console.log('[ToyExpo] Bot commands & web app menu button registered!');
  } catch (e) {
    console.error('[ToyExpo] Failed to setup bot commands:', e.message);
  }
}

async function handleTelegramMessage(apiUrl, message) {
  const chatId = message.chat.id;
  const text = (message.text || '').trim();
  const userName = message.from.first_name || 'Коллекционер';

  if (text.startsWith('/start')) {
    const welcomeText = `Привет, ${userName}! 👾\n\n` +
      `Добро пожаловать в *ToyExpo* — официальный Telegram-гид по выставкам дизайнерских игрушек, urban vinyl, софуби и эксклюзивным арт-дропам!\n\n` +
      `✨ *Что доступно в нашем Mini App:*\n` +
      `• 📅 Мировой календарь выставок (D-Con, WonFes, Pop Toy Show и др.)\n` +
      `• ⏱️ Таймеры обратного отсчета до открытия залов\n` +
      `• 🎯 Радар редких виниловых релизов и лотерей (Grail drops)\n` +
      `• 🌍 Гид по столицам дизайнерских игрушек (Токио, Шанхай, Бангкок)\n` +
      `• 🎒 Планировщик поездок и вишлист\n\n` +
      `Нажмите кнопку ниже, чтобы открыть приложение прямо в Telegram:`;

    await sendMessage(apiUrl, chatId, welcomeText, {
      inline_keyboard: [
        [
          {
            text: "🚀 Открыть ToyExpo Mini App",
            web_app: { url: APP_URL }
          }
        ],
        [
          { text: "📅 Ближайшие выставки", callback_data: "cmd_expos" },
          { text: "🎯 Редкие дропы", callback_data: "cmd_drops" }
        ],
        [
          { text: "📍 Столицы арт-тоя", callback_data: "cmd_cities" },
          { text: "🔔 Подписка на анонсы", callback_data: "cmd_sub" }
        ]
      ]
    });
  } else if (text.startsWith('/expos') || text.toLowerCase().includes('выставк')) {
    await sendExposMessage(apiUrl, chatId);
  } else if (text.startsWith('/drops') || text.toLowerCase().includes('дроп')) {
    await sendDropsMessage(apiUrl, chatId);
  } else if (text.startsWith('/cities') || text.toLowerCase().includes('город')) {
    await sendCitiesMessage(apiUrl, chatId);
  } else if (text.startsWith('/help')) {
    const helpText = `👾 *ToyExpo Telegram Bot & Mini App*\n\n` +
      `*Команды бота:*\n` +
      `/start — Главное меню и запуск приложения\n` +
      `/expos — Список всех мировых выставок и конвентов\n` +
      `/drops — Редкие виниловые дропы и лотереи\n` +
      `/cities — Города и главные центры арт-культуры\n` +
      `/help — Эта справка\n\n` +
      `Вы также можете нажать кнопку *"ToyExpo 👾"* в левом нижнем углу строки ввода, чтобы запустить интерактивное приложение.`;

    await sendMessage(apiUrl, chatId, helpText, {
      inline_keyboard: [
        [{ text: "🚀 Запустить Mini App", web_app: { url: APP_URL } }]
      ]
    });
  } else {
    await sendMessage(apiUrl, chatId, `Привет! Используйте меню ниже для навигации или откройте Mini App:`, {
      inline_keyboard: [
        [{ text: "🚀 Открыть ToyExpo Mini App", web_app: { url: APP_URL } }],
        [
          { text: "📅 Выставки", callback_data: "cmd_expos" },
          { text: "🎯 Дропы", callback_data: "cmd_drops" }
        ]
      ]
    });
  }
}

async function handleTelegramCallbackQuery(apiUrl, query) {
  const chatId = query.message.chat.id;
  const data = query.data;

  // Answer callback query to remove loading icon
  await fetch(`${apiUrl}/answerCallbackQuery`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ callback_query_id: query.id })
  });

  if (data === 'cmd_expos') {
    await sendExposMessage(apiUrl, chatId);
  } else if (data === 'cmd_drops') {
    await sendDropsMessage(apiUrl, chatId);
  } else if (data === 'cmd_cities') {
    await sendCitiesMessage(apiUrl, chatId);
  } else if (data === 'cmd_sub') {
    await sendMessage(apiUrl, chatId, `🔔 *Вы успешно подписались на уведомления! Мы пришлем анонс, когда объявят новые лотереи и даты конвентов.*`, {
      inline_keyboard: [
        [{ text: "🚀 Открыть ToyExpo Mini App", web_app: { url: APP_URL } }]
      ]
    });
  }
}

async function sendExposMessage(apiUrl, chatId) {
  const text = `📅 *Главные мировые выставки Designer Toy (2026–2027):*\n\n` +
    `1. *DesignerCon (D-Con 2026)*\n` +
    `📍 Анахайм, Калифорния (США) • 20–22 ноября 2026\n` +
    `🧸 Urban Vinyl, Dunny customs, инди-арт\n\n` +
    `2. *Wonder Festival (WonFes Winter 2027)*\n` +
    `📍 Тиба / Токио (Япония) • 7–8 февраля 2027\n` +
    `👾 Легендарный софуби (Sofubi), гараж-киты, One-Day лицензии\n\n` +
    `3. *Pop Toy Show (PTS Singapore 2026)*\n` +
    `📍 Сингапур (Sands Expo) • 13–15 ноября 2026\n` +
    `🎁 POP MART, Mega Space Molly, Skullpanda\n\n` +
    `4. *Shanghai Toy Show (STS 2027)*\n` +
    `📍 Шанхай (Китай) • 16–18 апреля 2027\n` +
    `🇨🇳 Крупнейший арт-той экспо в Азии\n\n` +
    `5. *Thailand Toy Expo (TTE 2027)*\n` +
    `📍 Бангкок (CentralWorld) • 22–25 апреля 2027\n` +
    `🇹🇭 Бесплатный вход, огромные инсталляции Coarse\n\n` +
    `6. *Taipei Toy Festival (TTF 2026)*\n` +
    `📍 Тайбэй (Тайвань) • 15–18 октября 2026\n` +
    `🎨 Пионеры азиатского дизайнерского винила`;

  await sendMessage(apiUrl, chatId, text, {
    inline_keyboard: [
      [{ text: "📱 Открыть календарь в Mini App", web_app: { url: APP_URL } }],
      [{ text: "🎯 Редкие дропы", callback_data: "cmd_drops" }, { text: "📍 Столицы", callback_data: "cmd_cities" }]
    ]
  });
}

async function sendDropsMessage(apiUrl, chatId) {
  const text = `🎯 *Эксклюзивные виниловые релизы и дропы (Grail Radar):*\n\n` +
    `• *Mega Space Molly 1000% Cyber Neon*\n` +
    `💰 $980 | Тираж: 200 шт. | 📍 PTS Singapore\n\n` +
    `• *Labubu The Monsters Vinyl Sculpture*\n` +
    `💰 $280 | Тираж: 150 шт. | 📍 Pop Toy Show\n\n` +
    `• *Byron x Rangeas Galactic Sofubi Duo*\n` +
    `💰 28,000 JPY | Тираж: 50 сетов | 📍 WonFes Winter\n\n` +
    `• *Coarse Noop & Paw: Bangkok Glow Edition*\n` +
    `💰 $450 | Тираж: 250 шт. | 📍 TTE Bangkok\n\n` +
    `• *TEQ63 'Ghost Sector' 6-inch Vinyl*\n` +
    `💰 $120 | Тираж: 300 шт. | 📍 DesignerCon`;

  await sendMessage(apiUrl, chatId, text, {
    inline_keyboard: [
      [{ text: "🎯 Открыть Радар Релизов в Mini App", web_app: { url: APP_URL } }],
      [{ text: "📅 Ближайшие выставки", callback_data: "cmd_expos" }]
    ]
  });
}

async function sendCitiesMessage(apiUrl, chatId) {
  const text = `📍 *Мировые столицы Design Toy культуры:*\n\n` +
    `🇯🇵 *Токио (Япония)* — Районы Акихабара, Накано Бродвей, Харадзюку. Центр софуби и винтажного винила.\n\n` +
    `🇨🇳 *Шанхай (Китай)* — Район West Bund, арт-галереи, мекка флагманов Pop Mart и Finding Unicorn.\n\n` +
    `🇹🇭 *Бангкок (Таиланд)* — Siam Square, CentralWorld. Любимая столица международных скульпторов.\n\n` +
    `🇺🇸 *Лос-Анджелес (США)* — Little Tokyo, Melrose Ave. Родина DesignerCon и контркультурного винила.`;

  await sendMessage(apiUrl, chatId, text, {
    inline_keyboard: [
      [{ text: "🌍 Смотреть интерактивную карту", web_app: { url: APP_URL } }]
    ]
  });
}

async function sendMessage(apiUrl, chatId, text, replyMarkup) {
  try {
    await fetch(`${apiUrl}/sendMessage`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({
        chat_id: chatId,
        text: text,
        parse_mode: 'Markdown',
        reply_markup: replyMarkup
      })
    });
  } catch (e) {
    console.error('Error sending telegram message:', e.message);
  }
}
