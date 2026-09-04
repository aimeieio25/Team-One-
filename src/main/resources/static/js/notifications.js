(function () {
    document.addEventListener('DOMContentLoaded', function () {
        const button = document.getElementById('notification-button');
        const dropdown = document.getElementById('notification-dropdown');

        if (!button || !dropdown) {
            return;
        }

        function closeDropdown() {
            dropdown.classList.remove('show');
            button.setAttribute('aria-expanded', 'false');
        }

        button.addEventListener('click', function (event) {
            event.stopPropagation();

            const isOpen = dropdown.classList.toggle('show');
            button.setAttribute('aria-expanded', String(isOpen));
        });

        dropdown.addEventListener('click', function (event) {
            event.stopPropagation();
        });

        document.addEventListener('click', closeDropdown);

        document.addEventListener('keydown', function (event) {
            if (event.key === 'Escape' && dropdown.classList.contains('show')) {
                closeDropdown();
                button.focus();
            }
        });
    });
})();
