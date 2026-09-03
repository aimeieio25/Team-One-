(function () {
    const STORAGE_KEY = 'preferred-theme';
    const root = document.documentElement;

    // Allows for the toggle switch to be used on every page
    if (localStorage.getItem(STORAGE_KEY) === 'dark') {
        root.setAttribute('data-theme', 'dark');
    }

    function buildButton() {
        const btn = document.createElement('button');
        btn.type = 'button';
        btn.className = 'theme-switch';
        btn.setAttribute('aria-label', 'Toggle dark mode');
        btn.innerHTML = `
            <svg class="sun" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                <circle cx="12" cy="12" r="5"/>
                <line x1="12" y1="1" x2="12" y2="3"/><line x1="12" y1="21" x2="12" y2="23"/>
                <line x1="4.22" y1="4.22" x2="5.64" y2="5.64"/><line x1="18.36" y1="18.36" x2="19.78" y2="19.78"/>
                <line x1="1" y1="12" x2="3" y2="12"/><line x1="21" y1="12" x2="23" y2="12"/>
                <line x1="4.22" y1="19.78" x2="5.64" y2="18.36"/><line x1="18.36" y1="5.64" x2="19.78" y2="4.22"/>
            </svg>
            <svg class="moon" viewBox="0 0 24 24" fill="currentColor">
                <path d="M21 12.79A9 9 0 1 1 11.21 3 7 7 0 0 0 21 12.79z"/>
            </svg>
            <span class="thumb"></span>
        `
        btn.addEventListener('click', function () {
            const isDark = root.getAttribute('data-theme') === 'dark';
            if (isDark) {
                root.removeAttribute('data-theme');
                localStorage.setItem(STORAGE_KEY, 'light');
            } else {
                root.setAttribute('data-theme', 'dark');
                localStorage.setItem(STORAGE_KEY, 'dark');
            }
        });
        return btn;
    }

    document.addEventListener('DOMContentLoaded', function () {
        const container = document.getElementById('theme-toggle-container') || document.body;
        container.appendChild(buildButton());
    });
}) ();
