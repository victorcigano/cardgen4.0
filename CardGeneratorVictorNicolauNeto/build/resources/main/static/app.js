// Authentication: store username in sessionStorage and credentials for Basic Auth
function getUser() { return sessionStorage.getItem('cg_user'); }

function isLoggedIn() {
  return !!sessionStorage.getItem('cg_user');
}

function setUser(name) {
  if (name) sessionStorage.setItem('cg_user', name); else {
    sessionStorage.removeItem('cg_user');
    // clear any admin session when user logs out
    isAdminAuthed = false;
  }
  refreshAuthUI();
}

function refreshAuthUI() {
  const user = sessionStorage.getItem('cg_user');
  const userDisplay = document.getElementById('userDisplay');
  const btnLogin = document.getElementById('btnLogin');
  const btnLogout = document.getElementById('btnLogout');
  if (user) {
    userDisplay.textContent = `Olá, ${user}`;
    btnLogin.classList.add('d-none');
    btnLogout.classList.remove('d-none');
  } else {
    userDisplay.textContent = '';
    btnLogin.classList.remove('d-none');
    btnLogout.classList.add('d-none');
  }
}

document.getElementById('submitLogin').addEventListener('click', () => {
  const nameEl = document.getElementById('loginNome');
  const pwdEl = document.getElementById('loginSenha');
  const name = nameEl?.value?.trim();
  if (!name) { showToast('Por favor, informe seu nome antes de entrar.', 'warning'); nameEl?.focus(); return }
  setUser(name);
  // clear password field for hygiene
  if (pwdEl) pwdEl.value = '';
  // hide modal
  const modal = bootstrap.Modal.getInstance(document.getElementById('loginModal'));
  modal.hide();
});

document.getElementById('btnLogout').addEventListener('click', () => { setUser(null); showToast('Desconectado', 'secondary'); });

// Toast helper
function showToast(message, variant = 'primary') {
  const container = document.getElementById('toastContainer');
  const toastEl = document.createElement('div');
  toastEl.className = `toast align-items-center text-bg-${variant} border-0`;
  toastEl.setAttribute('role', 'alert');
  toastEl.setAttribute('aria-live', 'assertive');
  toastEl.setAttribute('aria-atomic', 'true');
  // Sanitize message to prevent XSS
  const sanitizedMessage = message.replace(/[<>"'&]/g, function(match) {
    const escapeMap = { '<': '&lt;', '>': '&gt;', '"': '&quot;', "'": '&#x27;', '&': '&amp;' };
    return escapeMap[match];
  });
  
  toastEl.innerHTML = `
    <div class="d-flex">
      <div class="toast-body">${sanitizedMessage}</div>
      <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast" aria-label="Close"></button>
    </div>
  `;
  container.appendChild(toastEl);
  const bsToast = new bootstrap.Toast(toastEl, { delay: 3000 });
  bsToast.show();
  toastEl.addEventListener('hidden.bs.toast', () => toastEl.remove());
}

async function gerar() {
  if (!isLoggedIn()) { showToast('Você precisa entrar para gerar um cartão.', 'warning'); return }
  const nome = document.getElementById('nome').value || sessionStorage.getItem('cg_user') || 'Anonimo';
  try {
    const headers = { 'X-CG-USER': getUser() || '' };
    const res = await fetch(`/cards/gerar?nomeTitular=${encodeURIComponent(nome)}`, { method: 'POST', headers });
    if (!res.ok) throw new Error('Erro HTTP ' + res.status);
    const card = await res.json();
    showUltimo(card);
    showToast('Cartão gerado', 'success');
    await listar();
  } catch (e) {
    showToast('Erro ao gerar cartão: ' + e.message, 'danger');
  }
}

function showUltimo(card) {
  const div = document.getElementById('ultimo');
  div.innerHTML = '';
  const el = document.createElement('div');
  el.className = 'card-display';
  function fmt(n){ return n.replace(/(\d{4})(?=\d)/g,'$1 '); }
  // Sanitize card data to prevent XSS
  const sanitize = (str) => String(str || '').replace(/[<>"'&]/g, function(match) {
    const escapeMap = { '<': '&lt;', '>': '&gt;', '"': '&quot;', "'": '&#x27;', '&': '&amp;' };
    return escapeMap[match];
  });
  
  el.innerHTML = `
    <div class="d-flex justify-content-between align-items-start">
      <div>
        <div class="small-muted d-flex align-items-center gap-2">${sanitize(card.bandeira)} ${card.teste ? '<span class="badge bg-success ms-2">TEST</span>' : ''}</div>
        <div class="cc-number mt-2">${fmt(sanitize(card.numero))} <button class="btn btn-sm btn-outline-secondary ms-2" data-copy="${sanitize(card.numero)}"><i class="bi bi-clipboard"></i> Copiar</button></div>
      </div>
      <div class="text-end">
        <div><strong>${sanitize(card.nomeTitular)}</strong></div>
        <div class="small-muted">Val: ${sanitize(card.validade)}</div>
      </div>
    </div>
  `;
  div.appendChild(el);
  // animate new card
  requestAnimationFrame(() => { el.classList.add('pulse'); setTimeout(() => el.classList.remove('pulse'), 1100); });
}

async function listar() {
  try {
    const res = await fetch('/cards/listar');
    const list = await res.json();
    const div = document.getElementById('lista');
    div.innerHTML = '';
    if (!Array.isArray(list)) list = list.value || [];
    list.forEach(card => {
      const el = document.createElement('div');
      el.className = 'card-display mb-2';
      // Sanitize card data
      const sanitize = (str) => String(str || '').replace(/[<>"'&]/g, function(match) {
        const escapeMap = { '<': '&lt;', '>': '&gt;', '"': '&quot;', "'": '&#x27;', '&': '&amp;' };
        return escapeMap[match];
      });
      
      el.innerHTML = `
        <div class="row">
          <div class="col-md-8">
            <div class="cc-number">${sanitize(card.numero).replace(/(\d{4})(?=\d)/g,'$1 ')} <button class="btn btn-sm btn-outline-secondary ms-2" data-copy="${sanitize(card.numero)}">Copiar</button></div>
            <div class="small-muted">${sanitize(card.bandeira)} • CVV: ${sanitize(card.cvv)}</div>
          </div>
          <div class="col-md-4 text-end">
            <div><strong>${sanitize(card.nomeTitular)}</strong></div>
            <div class="small-muted">Val: ${sanitize(card.validade)}</div>
          </div>
        </div>
      `;
      // add remove button
      const btn = document.createElement('button');
      btn.className = 'btn btn-sm btn-outline-danger mt-2';
      btn.textContent = 'Remover';
      btn.addEventListener('click', async () => {
        if (!confirm('Remover este cartão?')) return;
        try {
          const headers = { 'X-CG-USER': getUser() || '' };
          const resp = await fetch(`/cards/${encodeURIComponent(card.numero)}`, { method: 'DELETE', headers });
          if (resp.status === 204 || resp.ok) {
            showToast('Cartão removido', 'success');
            await listar();
          } else if (resp.status === 404) {
            showToast('Cartão não encontrado', 'warning');
            await listar();
          } else {
            showToast('Erro ao remover: ' + resp.status, 'danger');
          }
        } catch (e) { showToast('Erro ao remover: ' + e.message, 'danger') }
      });
      el.appendChild(btn);
      // wire copy buttons inside this element
      el.querySelectorAll('[data-copy]').forEach(b => b.addEventListener('click', async (ev) => {
        const text = ev.currentTarget.getAttribute('data-copy');
        try { await navigator.clipboard.writeText(text); showToast('Número copiado', 'success'); } catch (e) { showToast('Falha ao copiar', 'danger'); }
      }));
      div.appendChild(el);
    });
  } catch (e) {
    console.error(e);
  }
}

document.getElementById('gerar').addEventListener('click', gerar);
document.getElementById('limpar').addEventListener('click', () => { document.getElementById('nome').value = '' });
document.getElementById('removerTodos').addEventListener('click', async () => {
  if (!confirm('Remover todos os cartões?')) return;
  try {
  const headers = { 'X-CG-USER': getUser() || '' };
  const resp = await fetch('/cards', { method: 'DELETE', headers });
    if (resp.ok) {
      showToast('Todos os cartões removidos', 'success');
      await listar();
    } else {
      showToast('Erro ao remover todos: ' + resp.status, 'danger');
    }
  } catch (e) { showToast('Erro ao remover todos: ' + e.message, 'danger') }
});
window.addEventListener('load', () => { 
  // Verificar se usuário está logado, senão redirecionar para login
  if (!isLoggedIn()) {
    // Permitir acesso como convidado se vier da tela de login
    const urlParams = new URLSearchParams(window.location.search);
    if (!urlParams.get('guest')) {
      window.location.href = '/login.html';
      return;
    }
  }
  refreshAuthUI(); 
  listar(); 
});

// --- Admin panel functions ---
let isAdminAuthed = false;
// Admin password moved to server-side validation

function getAdminHeaders() {
  const headers = { 'X-CG-USER': getUser() || '' };
  if (isAdminAuthed) headers['X-CG-ADMIN'] = 'authenticated';
  return headers;
}

async function loadAdminList() {
  try {
    if (!isAdminAuthed) { showToast('Autenticação admin necessária', 'warning'); return; }
    const res = await fetch('/admin/cards', { headers: getAdminHeaders() });
    const list = await res.json();
    const tbody = document.querySelector('#adminTable tbody');
    tbody.innerHTML = '';
    (list || []).forEach(card => {
      const tr = document.createElement('tr');
      // Sanitize admin table data
      const sanitize = (str) => String(str || '').replace(/[<>"'&]/g, function(match) {
        const escapeMap = { '<': '&lt;', '>': '&gt;', '"': '&quot;', "'": '&#x27;', '&': '&amp;' };
        return escapeMap[match];
      });
      
      tr.innerHTML = `
        <td>${sanitize(card.numero)}</td>
        <td>${sanitize(card.nomeTitular)}</td>
        <td>${sanitize(card.validade)}</td>
        <td>${sanitize(card.cvv)}</td>
        <td>${sanitize(card.bandeira)}</td>
        <td>${card.teste ? 'SIM' : 'NÃO'}</td>
        <td><button class="btn btn-sm btn-outline-danger admin-delete" data-num="${sanitize(card.numero)}">Remover</button></td>
      `;
      tbody.appendChild(tr);
    });
    document.getElementById('adminCount').textContent = (list || []).length;
    // wire delete buttons
    document.querySelectorAll('.admin-delete').forEach(b => b.addEventListener('click', async (ev) => {
      const numero = ev.currentTarget.getAttribute('data-num');
      if (!confirm('Remover este cartão?')) return;
      try {
  const resp = await fetch(`/admin/cards/${encodeURIComponent(numero)}`, { method: 'DELETE', headers: getAdminHeaders() });
        if (resp.ok) { showToast('Cartão removido', 'success'); loadAdminList(); } else showToast('Falha ao remover', 'danger');
      } catch (e) { showToast('Erro: ' + e.message, 'danger') }
    }));
  } catch (e) { console.error(e); showToast('Erro ao carregar lista', 'danger') }
}

document.getElementById('refreshAdmin')?.addEventListener('click', loadAdminList);
document.getElementById('exportCsv')?.addEventListener('click', async () => {
  try {
    if (!isAdminAuthed) { showToast('Autenticação admin necessária', 'warning'); return; }
    const res = await fetch('/admin/cards', { headers: getAdminHeaders() });
    const list = await res.json();
    const csv = ['numero,nomeTitular,validade,cvv,bandeira,teste']
      .concat((list || []).map(c => `${c.numero},"${c.nomeTitular}",${c.validade},${c.cvv},${c.bandeira},${c.teste}`))
      .join('\n');
    const blob = new Blob([csv], { type: 'text/csv;charset=utf-8;' });
    const url = URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url; a.download = 'cards_export.csv'; document.body.appendChild(a); a.click(); a.remove(); URL.revokeObjectURL(url);
  } catch (e) { showToast('Erro exportando CSV', 'danger') }
});

document.getElementById('deleteAllAdmin')?.addEventListener('click', async () => {
  if (!confirm('Remover todos os cartões?')) return;
  try {
    if (!isAdminAuthed) { showToast('Autenticação admin necessária', 'warning'); return; }
    const resp = await fetch('/admin/cards', { method: 'DELETE', headers: getAdminHeaders() });
    if (resp.ok) { showToast('Todos removidos', 'success'); loadAdminList(); } else showToast('Falha ao remover todos', 'danger');
  } catch (e) { showToast('Erro ao remover todos: ' + e.message, 'danger') }
});

// Admin auth flow: require login + admin password '123'
document.getElementById('btnAdmin')?.addEventListener('click', () => {
  if (!isLoggedIn()) { showToast('Você precisa entrar na aplicação antes de acessar Admin.', 'warning'); return; }
  // show admin auth modal
  const authModalEl = document.getElementById('adminAuthModal');
  const authModal = new bootstrap.Modal(authModalEl);
  authModal.show();
});

document.getElementById('adminAuthSubmit')?.addEventListener('click', async () => {
  const pass = document.getElementById('adminSenha')?.value || '';
  if (!pass.trim()) {
    showToast('Digite a senha de administrador', 'warning');
    return;
  }
  
  try {
    // Validate admin credentials on server
    const response = await fetch('/admin/auth', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'X-CG-USER': getUser() || ''
      },
      body: JSON.stringify({ password: pass })
    });
    
    if (response.ok) {
      isAdminAuthed = true;
      const authModal = bootstrap.Modal.getInstance(document.getElementById('adminAuthModal'));
      authModal.hide();
      const adminModal = new bootstrap.Modal(document.getElementById('adminModal'));
      adminModal.show();
      setTimeout(loadAdminList, 120);
    } else {
      showToast('Credenciais inválidas', 'danger');
    }
  } catch (e) {
    showToast('Erro na autenticação: ' + e.message, 'danger');
  } finally {
    // Clear password input
    const pwdEl = document.getElementById('adminSenha');
    if (pwdEl) pwdEl.value = '';
  }
});

// Small UX improvements: change default placeholder for login modal
const loginNomeInput = document.getElementById('loginNome');
if (loginNomeInput) { loginNomeInput.setAttribute('placeholder', 'Escreva o Nome do titular'); }

// Toggle password visibility in login modal
const toggleBtn = document.getElementById('toggleSenha');
if (toggleBtn) {
  toggleBtn.addEventListener('click', () => {
    const pwd = document.getElementById('loginSenha');
    if (!pwd) return;
    if (pwd.type === 'password') {
      pwd.type = 'text';
      toggleBtn.innerHTML = '<i class="bi bi-eye-slash"></i>';
      toggleBtn.setAttribute('title', 'Ocultar senha');
    } else {
      pwd.type = 'password';
      toggleBtn.innerHTML = '<i class="bi bi-eye"></i>';
      toggleBtn.setAttribute('title', 'Mostrar senha');
    }
    pwd.focus();
  });
}

