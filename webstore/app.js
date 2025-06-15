// DOM Content Loaded Event
document.addEventListener('DOMContentLoaded', function() {
    // Initialize the application
    initializeApp();
});

// Initialize application
function initializeApp() {
    // Add smooth scrolling behavior
    addSmoothScrolling();
    
    // Add keyboard navigation support
    addKeyboardSupport();
    
    // Add intersection observer for animations
    addScrollAnimations();
    
    // Initialize copy notification system
    initializeCopyNotification();
}

// Toggle tool details expansion
function toggleDetails(button) {
    const toolCard = button.closest('.tool-card');
    const details = toolCard.querySelector('.tool-details');
    const isExpanded = !details.classList.contains('hidden');
    
    if (isExpanded) {
        // Collapse
        details.classList.add('hidden');
        button.classList.remove('expanded');
        button.textContent = '+';
        button.setAttribute('aria-expanded', 'false');
    } else {
        // Expand
        details.classList.remove('hidden');
        button.classList.add('expanded');
        button.textContent = '×';
        button.setAttribute('aria-expanded', 'true');
        
        // Smooth scroll to show the expanded content
        setTimeout(() => {
            details.scrollIntoView({ 
                behavior: 'smooth', 
                block: 'nearest',
                inline: 'nearest'
            });
        }, 150);
    }
}

// Copy text to clipboard
async function copyToClipboard(text) {
    try {
        // Find the button that was clicked
        const button = event.target;
        
        // Use the modern Clipboard API if available
        if (navigator.clipboard && window.isSecureContext) {
            await navigator.clipboard.writeText(text);
        } else {
            // Fallback for older browsers
            const textArea = document.createElement('textarea');
            textArea.value = text;
            textArea.style.position = 'fixed';
            textArea.style.left = '-999999px';
            textArea.style.top = '-999999px';
            document.body.appendChild(textArea);
            textArea.focus();
            textArea.select();
            
            try {
                document.execCommand('copy');
            } catch (err) {
                console.error('Failed to copy text: ', err);
                throw new Error('Copy failed');
            } finally {
                textArea.remove();
            }
        }
        
        // Show visual feedback
        showCopyFeedback(button);
        showCopyNotification('Command copied to clipboard!');
        
    } catch (err) {
        console.error('Failed to copy: ', err);
        showCopyNotification('Failed to copy command', 'error');
    }
}

// Show copy feedback on button
function showCopyFeedback(button) {
    const originalText = button.textContent;
    button.classList.add('copying');
    button.textContent = 'Copied!';
    
    setTimeout(() => {
        button.classList.remove('copying');
        button.textContent = originalText;
    }, 1500);
}

// Show copy notification
function showCopyNotification(message, type = 'success') {
    // Remove existing notification if present
    const existingNotification = document.querySelector('.copy-notification');
    if (existingNotification) {
        existingNotification.remove();
    }
    
    // Create notification element
    const notification = document.createElement('div');
    notification.className = `copy-notification ${type}`;
    notification.textContent = message;
    
    // Add to DOM
    document.body.appendChild(notification);
    
    // Trigger animation
    setTimeout(() => {
        notification.classList.add('show');
    }, 10);
    
    // Remove after delay
    setTimeout(() => {
        notification.classList.remove('show');
        setTimeout(() => {
            if (notification.parentNode) {
                notification.remove();
            }
        }, 300);
    }, 3000);
}

// Scroll to section
function scrollToSection(sectionId) {
    const section = document.getElementById(sectionId);
    if (section) {
        const headerOffset = 20; // Small offset from top
        const elementPosition = section.getBoundingClientRect().top;
        const offsetPosition = elementPosition + window.pageYOffset - headerOffset;
        
        window.scrollTo({
            top: offsetPosition,
            behavior: 'smooth'
        });
    }
}

// Add smooth scrolling for anchor links
function addSmoothScrolling() {
    document.querySelectorAll('a[href^="#"]').forEach(anchor => {
        anchor.addEventListener('click', function (e) {
            e.preventDefault();
            const target = document.querySelector(this.getAttribute('href'));
            if (target) {
                target.scrollIntoView({
                    behavior: 'smooth',
                    block: 'start'
                });
            }
        });
    });
}

// Add keyboard support
function addKeyboardSupport() {
    // Handle Enter and Space key for expand buttons
    document.addEventListener('keydown', function(e) {
        if (e.target.classList.contains('expand-btn')) {
            if (e.key === 'Enter' || e.key === ' ') {
                e.preventDefault();
                toggleDetails(e.target);
            }
        }
        
        // Handle Enter and Space for copy buttons
        if (e.target.classList.contains('copy-btn')) {
            if (e.key === 'Enter' || e.key === ' ') {
                e.preventDefault();
                e.target.click();
            }
        }
        
        // Handle Enter and Space for main buttons
        if (e.target.classList.contains('btn')) {
            if (e.key === 'Enter' || e.key === ' ') {
                e.preventDefault();
                e.target.click();
            }
        }
    });
    
    // Make expand buttons focusable
    document.querySelectorAll('.expand-btn').forEach(button => {
        button.setAttribute('tabindex', '0');
        button.setAttribute('role', 'button');
        button.setAttribute('aria-expanded', 'false');
        button.setAttribute('aria-label', 'Toggle tool details');
    });
}

// Add scroll animations
function addScrollAnimations() {
    // Intersection Observer for fade-in animations
    const observerOptions = {
        threshold: 0.1,
        rootMargin: '0px 0px -50px 0px'
    };
    
    const observer = new IntersectionObserver((entries) => {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                entry.target.style.opacity = '1';
                entry.target.style.transform = 'translateY(0)';
            }
        });
    }, observerOptions);
    
    // Observe tool cards and feature cards
    document.querySelectorAll('.tool-card, .feature-card, .tech-card, .install-card').forEach(card => {
        card.style.opacity = '0';
        card.style.transform = 'translateY(20px)';
        card.style.transition = 'opacity 0.6s ease, transform 0.6s ease';
        observer.observe(card);
    });
}

// Initialize copy notification system
function initializeCopyNotification() {
    // Add styles for copy notification if not already present
    if (!document.querySelector('#copy-notification-styles')) {
        const style = document.createElement('style');
        style.id = 'copy-notification-styles';
        style.textContent = `
            .copy-notification {
                position: fixed;
                top: 20px;
                right: 20px;
                background: var(--color-success);
                color: white;
                padding: 12px 20px;
                border-radius: 8px;
                font-weight: 500;
                z-index: 1000;
                transform: translateX(100%);
                transition: transform 0.3s ease;
                box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
            }
            
            .copy-notification.show {
                transform: translateX(0);
            }
            
            .copy-notification.error {
                background: var(--color-error);
            }
        `;
        document.head.appendChild(style);
    }
}

// Add loading states for buttons
function addLoadingState(button, text = 'Loading...') {
    const originalText = button.textContent;
    button.textContent = text;
    button.disabled = true;
    button.style.opacity = '0.7';
    
    return function removeLoadingState() {
        button.textContent = originalText;
        button.disabled = false;
        button.style.opacity = '1';
    };
}

// Utility function to debounce function calls
function debounce(func, wait) {
    let timeout;
    return function executedFunction(...args) {
        const later = () => {
            clearTimeout(timeout);
            func(...args);
        };
        clearTimeout(timeout);
        timeout = setTimeout(later, wait);
    };
}

// Add search functionality (if needed in future)
function initializeSearch() {
    const searchInput = document.querySelector('#search-input');
    if (searchInput) {
        const debouncedSearch = debounce(performSearch, 300);
        searchInput.addEventListener('input', debouncedSearch);
    }
}

function performSearch(event) {
    const query = event.target.value.toLowerCase().trim();
    const toolCards = document.querySelectorAll('.tool-card');
    
    toolCards.forEach(card => {
        const toolName = card.querySelector('h4').textContent.toLowerCase();
        const toolDescription = card.querySelector('p').textContent.toLowerCase();
        const features = Array.from(card.querySelectorAll('.features li')).map(li => li.textContent.toLowerCase()).join(' ');
        
        const isMatch = toolName.includes(query) || 
                       toolDescription.includes(query) || 
                       features.includes(query);
        
        card.style.display = isMatch ? 'block' : 'none';
    });
}

// Track analytics events (placeholder for future implementation)
function trackEvent(eventName, properties = {}) {
    // Placeholder for analytics tracking
    if (window.gtag) {
        window.gtag('event', eventName, properties);
    }
    
    console.log('Event tracked:', eventName, properties);
}

// Add event tracking to copy actions
document.addEventListener('click', function(e) {
    if (e.target.classList.contains('copy-btn')) {
        const commandText = e.target.previousElementSibling?.textContent || 'unknown';
        trackEvent('copy_command', {
            command: commandText,
            location: e.target.closest('.category')?.querySelector('h3')?.textContent || 'unknown'
        });
    }
    
    if (e.target.classList.contains('expand-btn')) {
        const toolName = e.target.closest('.tool-card')?.querySelector('h4')?.textContent || 'unknown';
        const isExpanding = e.target.getAttribute('aria-expanded') === 'false';
        trackEvent('toggle_tool_details', {
            tool: toolName,
            action: isExpanding ? 'expand' : 'collapse'
        });
    }
});

// Handle window resize for responsive adjustments
window.addEventListener('resize', debounce(function() {
    // Adjust any responsive elements if needed
    adjustResponsiveElements();
}, 250));

function adjustResponsiveElements() {
    // Placeholder for responsive adjustments
    // This could include recalculating positions, adjusting layouts, etc.
}

// Add error handling for failed operations
window.addEventListener('error', function(e) {
    console.error('Application error:', e.error);
    // Could show user-friendly error message
});

// Add performance monitoring
if ('performance' in window) {
    window.addEventListener('load', function() {
        const loadTime = performance.now();
        console.log(`Page loaded in ${loadTime.toFixed(2)}ms`);
        
        // Track page load performance
        trackEvent('page_load_performance', {
            load_time: Math.round(loadTime),
            timing: performance.timing
        });
    });
}

// Export functions for potential external use
window.JBangTools = {
    toggleDetails,
    copyToClipboard,
    scrollToSection,
    trackEvent
};