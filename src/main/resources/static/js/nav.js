document.addEventListener('DOMContentLoaded', function() {
    // Mobile menu toggle
    const mobileMenuButton = document.getElementById('mobile-menu-button');
    const navbarNav = document.getElementById('navbar-nav');
    
    if (mobileMenuButton && navbarNav) {
        // Toggle menu when clicking the button
        mobileMenuButton.addEventListener('click', function(e) {
            e.stopPropagation(); // Prevent this click from triggering the document click handler
            navbarNav.classList.toggle('open');
            navbarNav.classList.toggle('active');
        });

        // Close menu when clicking outside
        document.addEventListener('click', function(e) {
            const isClickInsideNav = navbarNav.contains(e.target) || mobileMenuButton.contains(e.target);
            
            if (!isClickInsideNav && navbarNav.classList.contains('open')) {
                navbarNav.classList.remove('open');
                navbarNav.classList.remove('active');
            }
        });

        // Prevent clicks inside the nav from closing it
        navbarNav.addEventListener('click', function(e) {
            e.stopPropagation();
        });
    }
});
    


