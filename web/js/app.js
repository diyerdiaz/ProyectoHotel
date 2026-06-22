/* ══════════════════════════════════════════════════════
   HOTEL GALES – PORTAL DE PERSONAL
   app.js  |  Toda la lógica de la SPA
   ══════════════════════════════════════════════════════ */

'use strict';

/* ──────────────────────────────────────────────
   ESTADO DE LA APP
────────────────────────────────────────────── */
const App = {
  currentUser: null,
  currentSection: 'inicio',
  sidebarOpen: false,

  /* Datos mock (en prod se reemplazaría por fetch a la API) */
  reportes: [
    { id:'#M-031', hab:'310', desc:'Aire acondicionado no enfría',    prioridad:'alta',  fecha:'Hoy 08:45',     estado:'enproceso' },
    { id:'#M-030', hab:'218', desc:'Ducha con baja presión',          prioridad:'media', fecha:'Ayer 17:20',    estado:'pendiente' },
    { id:'#M-029', hab:'105', desc:'Bombillo fundido en baño',        prioridad:'baja',  fecha:'20/06 14:00',   estado:'resuelto'  },
  ],

  habitaciones: [
    { num:'101', tipo:'Estándar',   estado:'disponible'    },
    { num:'102', tipo:'Estándar',   estado:'ocupada'       },
    { num:'103', tipo:'Estándar',   estado:'limpieza'      },
    { num:'104', tipo:'Estándar',   estado:'disponible'    },
    { num:'105', tipo:'Estándar',   estado:'disponible'    },
    { num:'106', tipo:'Estándar',   estado:'ocupada'       },
    { num:'201', tipo:'Suite Jr',   estado:'disponible'    },
    { num:'202', tipo:'Suite Jr',   estado:'ocupada'       },
    { num:'203', tipo:'Suite Jr',   estado:'mantenimiento' },
    { num:'204', tipo:'Suite Jr',   estado:'disponible'    },
    { num:'205', tipo:'Suite Jr',   estado:'disponible'    },
    { num:'206', tipo:'Suite Jr',   estado:'limpieza'      },
    { num:'301', tipo:'Suite',      estado:'disponible'    },
    { num:'302', tipo:'Suite',      estado:'ocupada'       },
    { num:'303', tipo:'Suite',      estado:'disponible'    },
    { num:'304', tipo:'Suite',      estado:'mantenimiento' },
    { num:'305', tipo:'Suite',      estado:'disponible'    },
    { num:'306', tipo:'Suite',      estado:'ocupada'       },
    { num:'401', tipo:'Pres.',      estado:'disponible'    },
    { num:'402', tipo:'Pres.',      estado:'ocupada'       },
    { num:'501', tipo:'Penthouse',  estado:'disponible'    },
  ],
};

/* ──────────────────────────────────────────────
   INIT
────────────────────────────────────────────── */
document.addEventListener('DOMContentLoaded', () => {
  initAuth();
  initClock();
  initSidebar();
  initNavigation();
  initForms();
  renderHabitaciones();
  updateDate();
});

/* ──────────────────────────────────────────────
   AUTH  (simulado)
────────────────────────────────────────────── */
function initAuth() {
  /* Usuarios demo */
  const USERS = {
    'admin':    { pass: '1234', nombre: 'Administrador', rol: 'admin',    initials: 'AD' },
    'recepcion':{ pass: '1234', nombre: 'Carlos García', rol: 'staff',    initials: 'CG' },
    'cliente':  { pass: '1234', nombre: 'María López',   rol: 'cliente',  initials: 'ML' },
  };

  /* Persistencia mínima */
  const saved = sessionStorage.getItem('hg_user');
  if (saved) {
    App.currentUser = JSON.parse(saved);
    enterDashboard(App.currentUser);
    return;
  }

  /* Login form */
  document.getElementById('login-form').addEventListener('submit', e => {
    e.preventDefault();
    const usr  = document.getElementById('login-user').value.trim().toLowerCase();
    const pass = document.getElementById('login-pass').value;
    const found = USERS[usr];
    if (found && found.pass === pass) {
      App.currentUser = { ...found, usuario: usr };
      sessionStorage.setItem('hg_user', JSON.stringify(App.currentUser));
      enterDashboard(App.currentUser);
    } else {
      showToast('Usuario o contraseña incorrectos', 'error');
      document.getElementById('login-pass').value = '';
    }
  });

  /* Registro form */
  document.getElementById('register-form').addEventListener('submit', e => {
    e.preventDefault();
    showToast('Solicitud enviada. El administrador la revisará.', 'info');
    showScreen('login-screen');
  });

  /* Toggle pass visibility */
  document.getElementById('toggle-pass')?.addEventListener('click', () => {
    const inp = document.getElementById('login-pass');
    inp.type = inp.type === 'password' ? 'text' : 'password';
  });

  /* Password strength meter */
  document.getElementById('reg-contrasena')?.addEventListener('input', e => {
    const bar   = document.getElementById('pass-strength');
    const len   = e.target.value.length;
    const color = len === 0 ? '#dde3ed' : len < 6 ? '#e84855' : len < 10 ? '#f0a500' : '#1aab71';
    const pct   = Math.min((len / 14) * 100, 100);
    bar.style.background = `linear-gradient(to right, ${color} ${pct}%, #dde3ed ${pct}%)`;
  });

  /* Links navegación auth */
  document.getElementById('show-register')?.addEventListener('click', e => {
    e.preventDefault(); showScreen('register-screen');
  });
  document.getElementById('back-to-login')?.addEventListener('click', () => {
    showScreen('login-screen');
  });

  /* Logout */
  document.getElementById('btn-logout')?.addEventListener('click', () => {
    sessionStorage.removeItem('hg_user');
    App.currentUser = null;
    document.body.classList.remove('is-admin');
    showScreen('login-screen');
    showToast('Sesión cerrada correctamente', 'success');
  });
}

function enterDashboard(user) {
  /* Aplicar clase de rol */
  if (user.rol === 'admin') {
    document.body.classList.add('is-admin');
  } else {
    document.body.classList.remove('is-admin');
  }

  /* Actualizar UI con datos del usuario */
  setText('user-display-name', user.nombre);
  setText('user-display-role', capitalizeRole(user.rol));
  setText('user-avatar',    user.initials);
  setText('topbar-avatar',  user.initials);
  setText('welcome-msg',    `Bienvenido de nuevo, ${user.nombre.split(' ')[0]} 👋`);

  showScreen('dashboard-screen');
  updateKPIs();
}

function capitalizeRole(rol) {
  const map = { admin: 'Administrador', staff: 'Personal', cliente: 'Cliente' };
  return map[rol] || rol;
}

/* ──────────────────────────────────────────────
   NAVEGACIÓN
────────────────────────────────────────────── */
function initNavigation() {
  document.querySelectorAll('.nav-item[data-section]').forEach(item => {
    item.addEventListener('click', e => {
      e.preventDefault();
      goSection(item.dataset.section);
    });
  });
}

function goSection(id) {
  /* Ocultar todas */
  document.querySelectorAll('.section').forEach(s => s.classList.remove('active'));
  document.querySelectorAll('.nav-item').forEach(n => n.classList.remove('active'));

  /* Mostrar la correcta */
  const section = document.getElementById('section-' + id);
  const navItem = document.getElementById('nav-' + id);

  if (section) section.classList.add('active');
  if (navItem) navItem.classList.add('active');

  /* Actualizar título topbar */
  const titles = {
    inicio:         'Dashboard',
    asistencia:     'Control de Asistencia',
    habitaciones:   'Estado de Habitaciones',
    mantenimiento:  'Reportes de Mantenimiento',
    clientes:       'Gestión de Clientes',
    reservas:       'Gestión de Reservas',
    facturas:       'Gestión de Facturas',
    empleados:      'Gestión de Empleados',
    tipos:          'Tipos de Habitación',
  };
  setText('section-title', titles[id] || 'Dashboard');
  App.currentSection = id;

  /* Cerrar sidebar en móvil */
  closeSidebar();

  /* Scroll top */
  window.scrollTo({ top: 0, behavior: 'smooth' });
}

/* ──────────────────────────────────────────────
   SIDEBAR (móvil)
────────────────────────────────────────────── */
function initSidebar() {
  const hamburger = document.getElementById('hamburger');
  hamburger?.addEventListener('click', toggleSidebar);

  /* Cerrar al hacer clic fuera */
  document.addEventListener('click', e => {
    const sidebar = document.getElementById('sidebar');
    if (App.sidebarOpen && !sidebar.contains(e.target) && !e.target.closest('#hamburger')) {
      closeSidebar();
    }
  });
}

function toggleSidebar() {
  const sidebar = document.getElementById('sidebar');
  App.sidebarOpen = !App.sidebarOpen;
  sidebar.classList.toggle('open', App.sidebarOpen);
}

function closeSidebar() {
  const sidebar = document.getElementById('sidebar');
  App.sidebarOpen = false;
  sidebar.classList.remove('open');
}

/* ──────────────────────────────────────────────
   RELOJ EN VIVO
────────────────────────────────────────────── */
function initClock() {
  updateClock();
  setInterval(updateClock, 1000);
}

function updateClock() {
  const now  = new Date();
  const time = now.toLocaleTimeString('es-CO', { hour12: false });
  const el   = document.getElementById('live-clock');
  if (el) el.textContent = time;
}

function updateDate() {
  const now    = new Date();
  const opts   = { weekday:'long', year:'numeric', month:'long', day:'numeric' };
  const date   = now.toLocaleDateString('es-CO', opts);
  const capDate= date.charAt(0).toUpperCase() + date.slice(1);

  setText('live-date', capDate);
  setText('topbar-date', now.toLocaleDateString('es-CO', { day:'2-digit', month:'short', year:'numeric' }));
}

/* ──────────────────────────────────────────────
   KPIs
────────────────────────────────────────────── */
function updateKPIs() {
  const disponibles = App.habitaciones.filter(h => h.estado === 'disponible').length;
  const pendientes  = App.reportes.filter(r => r.estado === 'pendiente').length;

  animateNumber('kpi-disponibles', disponibles);
  animateNumber('kpi-reservas', 12);
  animateNumber('kpi-clientes', 38);
  animateNumber('kpi-mantenimiento', pendientes);

  setText('stat-pendientes', pendientes);
  setText('stat-enproceso', App.reportes.filter(r => r.estado === 'enproceso').length);
  setText('stat-resueltos', 14);
}

function animateNumber(id, target) {
  const el = document.getElementById(id);
  if (!el) return;
  let current = 0;
  const step = Math.ceil(target / 20);
  const timer = setInterval(() => {
    current = Math.min(current + step, target);
    el.textContent = current;
    if (current >= target) clearInterval(timer);
  }, 40);
}

/* ──────────────────────────────────────────────
   HABITACIONES
────────────────────────────────────────────── */
function renderHabitaciones(filter = 'all') {
  const grid = document.getElementById('room-grid');
  if (!grid) return;

  const ICONS = {
    disponible: '✅', ocupada: '🔴', limpieza: '🧹', mantenimiento: '🔧',
  };

  const filtered = filter === 'all'
    ? App.habitaciones
    : App.habitaciones.filter(h => h.estado === filter);

  grid.innerHTML = filtered.map(h => `
    <div class="room-cell room-${h.estado}" onclick="openRoomModal(${JSON.stringify(h).replace(/"/g,"'")})">
      <span class="room-icon">${ICONS[h.estado]}</span>
      <span class="room-num">${h.num}</span>
      <span class="room-type">${h.tipo}</span>
    </div>
  `).join('');
}

function filtrarHabitaciones() {
  const val = document.getElementById('filter-estado').value;
  renderHabitaciones(val);
}

function openRoomModal(hab) {
  const estadoLabels = {
    disponible: '<span class="badge badge-green">Disponible</span>',
    ocupada:    '<span class="badge badge-red">Ocupada</span>',
    limpieza:   '<span class="badge badge-gold">En Limpieza</span>',
    mantenimiento: '<span class="badge badge-gray">Mantenimiento</span>',
  };

  setText('modal-room-title', `Habitación ${hab.num}`);
  document.getElementById('modal-room-body').innerHTML = `
    <div style="display:grid;gap:12px">
      <div style="display:flex;justify-content:space-between;align-items:center">
        <span style="font-size:13px;color:var(--text-secondary);font-weight:600;text-transform:uppercase;letter-spacing:.05em">Estado actual</span>
        ${estadoLabels[hab.estado]}
      </div>
      <div style="display:flex;justify-content:space-between;align-items:center">
        <span style="font-size:13px;color:var(--text-secondary);font-weight:600;text-transform:uppercase;letter-spacing:.05em">Tipo</span>
        <span style="font-weight:600">${hab.tipo}</span>
      </div>
      <hr style="border:none;border-top:1px solid var(--border)" />
      <div>
        <label class="form-label" style="margin-bottom:8px;display:block">Cambiar estado a:</label>
        <select class="form-select" id="modal-room-select">
          <option value="disponible" ${hab.estado==='disponible'?'selected':''}>Disponible</option>
          <option value="ocupada"    ${hab.estado==='ocupada'   ?'selected':''}>Ocupada</option>
          <option value="limpieza"   ${hab.estado==='limpieza'  ?'selected':''}>En Limpieza</option>
          <option value="mantenimiento" ${hab.estado==='mantenimiento'?'selected':''}>Mantenimiento</option>
        </select>
      </div>
    </div>
  `;

  document.getElementById('modal-room-action').onclick = () => {
    const newEstado = document.getElementById('modal-room-select').value;
    const h = App.habitaciones.find(r => r.num === hab.num);
    if (h) h.estado = newEstado;
    renderHabitaciones(document.getElementById('filter-estado')?.value || 'all');
    updateKPIs();
    closeModal('modal-room');
    showToast(`Habitación ${hab.num} actualizada a "${newEstado}"`, 'success');
  };

  openModal('modal-room');
}

/* ──────────────────────────────────────────────
   ASISTENCIA
────────────────────────────────────────────── */
function registrarEntrada() {
  const now  = new Date().toLocaleTimeString('es-CO', { hour12: false, hour:'2-digit', minute:'2-digit' });
  setText('clock-status', `✅ Entrada registrada a las ${now}`);
  document.getElementById('btn-entrada').disabled = true;
  document.getElementById('btn-entrada').style.opacity = '.5';
  showToast(`Entrada registrada a las ${now}`, 'success');
}

function registrarSalida() {
  const now  = new Date().toLocaleTimeString('es-CO', { hour12: false, hour:'2-digit', minute:'2-digit' });
  setText('clock-status', `🚪 Salida registrada a las ${now}`);
  document.getElementById('btn-salida').disabled = true;
  document.getElementById('btn-salida').style.opacity = '.5';
  showToast(`Salida registrada a las ${now}`, 'info');
}

function exportarAsistencia() {
  showToast('Exportando reporte de asistencia...', 'info');
}

/* ──────────────────────────────────────────────
   MANTENIMIENTO
────────────────────────────────────────────── */
function crearReporte() {
  const hab  = document.getElementById('rep-hab')?.value?.trim();
  const desc = document.getElementById('rep-desc')?.value?.trim();
  const prio = document.getElementById('rep-prioridad')?.value;

  if (!hab || !desc) {
    showToast('Complete todos los campos requeridos', 'error');
    return;
  }

  const id = `#M-0${(App.reportes.length + 30).toString().padStart(2,'0')}`;
  App.reportes.unshift({ id, hab, desc, prioridad: prio, fecha: 'Ahora', estado: 'pendiente' });
  updateKPIs();
  closeModal('modal-nuevo-reporte');
  showToast(`Reporte ${id} creado exitosamente`, 'success');

  /* Reset form */
  document.getElementById('rep-hab').value = '';
  document.getElementById('rep-desc').value = '';
}

function buscarReporte(q) {
  const rows = document.querySelectorAll('#tbody-mantenimiento tr');
  rows.forEach(row => {
    const text = row.textContent.toLowerCase();
    row.style.display = text.includes(q.toLowerCase()) ? '' : 'none';
  });
}

/* ──────────────────────────────────────────────
   FORMS
────────────────────────────────────────────── */
function initForms() {
  /* Validación visual en inputs con 'required' */
  document.querySelectorAll('.form-input[required]').forEach(input => {
    input.addEventListener('blur', () => validateInput(input));
    input.addEventListener('input', () => {
      if (input.classList.contains('invalid')) validateInput(input);
    });
  });
}

function validateInput(input) {
  const empty = input.value.trim() === '';
  input.classList.toggle('invalid', empty);
  if (empty) {
    input.style.borderColor = 'var(--red)';
    input.style.boxShadow = '0 0 0 3px rgba(232,72,85,.12)';
  } else {
    input.style.borderColor = '';
    input.style.boxShadow = '';
  }
}

/* ──────────────────────────────────────────────
   MODAL HELPERS
────────────────────────────────────────────── */
function openModal(id) {
  const modal = document.getElementById(id);
  if (modal) modal.style.display = 'flex';
}

function closeModal(id) {
  const modal = document.getElementById(id);
  if (modal) modal.style.display = 'none';
}

/* Cerrar modal haciendo clic en el overlay */
document.addEventListener('click', e => {
  if (e.target.classList.contains('modal-overlay')) {
    e.target.style.display = 'none';
  }
});

/* Cerrar con Escape */
document.addEventListener('keydown', e => {
  if (e.key === 'Escape') {
    document.querySelectorAll('.modal-overlay[style*="flex"]').forEach(m => {
      m.style.display = 'none';
    });
  }
});

/* ──────────────────────────────────────────────
   SCREEN SWITCHER
────────────────────────────────────────────── */
function showScreen(id) {
  document.querySelectorAll('.screen').forEach(s => s.classList.remove('active'));
  const target = document.getElementById(id);
  if (target) target.classList.add('active');
}

/* ──────────────────────────────────────────────
   TOAST
────────────────────────────────────────────── */
let toastTimer;
function showToast(msg, type = 'success') {
  const toast = document.getElementById('toast');
  if (!toast) return;
  toast.textContent = msg;
  toast.className = `toast show ${type}`;
  clearTimeout(toastTimer);
  toastTimer = setTimeout(() => toast.classList.remove('show'), 3500);
}

/* ──────────────────────────────────────────────
   UTILITY
────────────────────────────────────────────── */
function setText(id, text) {
  const el = document.getElementById(id);
  if (el) el.textContent = text;
}
