 const Toast = (() => {

      const ICONS = {
        success: '✔',
        warning: '⚠',
        error:   '✖',
      };

      const TITLES = {
        success: 'Success',
        warning: 'Warning',
        error:   'Error',
      };

      function show(type, title, message, duration = 4000) {
        let container = document.getElementById('toast-container');
        if (!container) {
          container = document.createElement('div');
          container.id = 'toast-container';
          document.body.appendChild(container);
        }

        const toast = document.createElement('div');
        toast.className = `toast ${type}`;
        toast.style.setProperty('--duration', `${duration}ms`);

        toast.innerHTML = `
          <span class="toast-icon">${ICONS[type]}</span>
          <div class="toast-body">
            <div class="toast-title">${title || TITLES[type]}</div>
            ${message ? `<div class="toast-message">${message}</div>` : ''}
          </div>
          <button class="toast-close" onclick="this.closest('.toast').dispatchEvent(new Event('close'))">✕</button>
        `;

        const dismiss = () => {
          toast.classList.add('hide');
          toast.addEventListener('animationend', () => toast.remove(), { once: true });
        };

        toast.addEventListener('close', dismiss);
        toast.addEventListener('click', dismiss);

        container.appendChild(toast);
        setTimeout(dismiss, duration);
      }

      return {
        success: (title, message, duration) => show('success', title, message, duration),
        warning: (title, message, duration) => show('warning', title, message, duration),
        error:   (title, message, duration) => show('error',   title, message, duration),
      };

    })();


