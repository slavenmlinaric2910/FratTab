/**
 * Drink Management JavaScript Module
 * Handles all drink-related operations including deletion, UI updates, and inventory management
 */

// Global variables for drink management
let deleteModal;
let currentDrinkToDelete = null;

/**
 * Initialize drink management functionality
 */
function initializeDrinkManagement() {
    console.log('Initializing drink management...');
    
    // Check if delete modal exists
    const deleteModalElement = document.getElementById('deleteDrinkModal');
    if (!deleteModalElement) {
        console.error('Delete modal not found! Make sure the modal HTML is included.');
        return;
    }
    
    // Initialize delete confirmation modal
    try {
        deleteModal = new bootstrap.Modal(deleteModalElement);
        console.log('Delete modal initialized successfully');
        
        // Handle modal stacking - dim background modals when delete modal opens
        deleteModalElement.addEventListener('show.bs.modal', function () {
            console.log('Delete modal showing - dimming background modals');
            // Find all currently open modals and dim them
            document.querySelectorAll('.modal.show:not(#deleteDrinkModal)').forEach(modal => {
                modal.classList.add('modal-dimmed');
            });
        });
        
        deleteModalElement.addEventListener('hidden.bs.modal', function () {
            console.log('Delete modal hidden - restoring background modals');
            // Remove dimming from all modals
            document.querySelectorAll('.modal.modal-dimmed').forEach(modal => {
                modal.classList.remove('modal-dimmed');
            });
        });
        
    } catch (error) {
        console.error('Error initializing delete modal:', error);
        return;
    }
    
    // Test: Find delete buttons on page load
    const deleteButtons = document.querySelectorAll('.delete-drink-btn');
    console.log('Found', deleteButtons.length, 'delete buttons on page load');
    deleteButtons.forEach((btn, index) => {
        console.log(`Delete button ${index}:`, btn, 'ID:', btn.getAttribute('data-drink-id'), 'Name:', btn.getAttribute('data-drink-name'));
    });
    
    // Set up event listeners
    setupDeleteEventListeners();
}

/**
 * Set up event listeners for drink deletion
 */
function setupDeleteEventListeners() {
    console.log('Setting up delete event listeners...');
    
    // Handle delete button clicks
    document.addEventListener('click', function (e) {
        // Only log when clicking on elements that might be delete buttons
        if (e.target.classList.contains('delete-drink-btn') || 
            e.target.closest('.delete-drink-btn') || 
            e.target.classList.contains('fa-trash') ||
            e.target.tagName === 'BUTTON') {
            console.log('Potential delete button click. Target:', e.target, 'Classes:', e.target.className);
        }
        
        const deleteBtn = e.target.closest('.delete-drink-btn');
        console.log('Closest delete button found:', deleteBtn);
        
        if (deleteBtn) {
            handleDeleteButtonClick(deleteBtn);
        }
    });
    
    // Handle confirmation of deletion
    const confirmDeleteBtn = document.getElementById('confirmDeleteBtn');
    if (confirmDeleteBtn) {
        confirmDeleteBtn.addEventListener('click', handleConfirmDelete);
    }
}

/**
 * Handle delete button click
 * @param {HTMLElement} deleteBtn - The delete button that was clicked
 */
function handleDeleteButtonClick(deleteBtn) {
    console.log('Delete button clicked!');
    
    const drinkId = deleteBtn.getAttribute('data-drink-id');
    const drinkName = deleteBtn.getAttribute('data-drink-name');
    
    console.log('Drink ID:', drinkId, 'Drink Name:', drinkName);
    
    if (!drinkId) {
        console.error('Error: Could not find drink ID');
        alert('Error: Could not find drink ID');
        return;
    }
    
    console.log('About to show delete modal...');
    
    // Store current drink info and show confirmation modal
    currentDrinkToDelete = { id: drinkId, name: drinkName, button: deleteBtn };
    
    const drinkNameElement = document.getElementById('drinkNameToDelete');
    console.log('Drink name element:', drinkNameElement);
    if (drinkNameElement) {
        drinkNameElement.textContent = drinkName;
    }
    
    try {
        deleteModal.show();
        console.log('Delete modal should now be visible');
    } catch (error) {
        console.error('Error showing delete modal:', error);
    }
}

/**
 * Handle confirmation of drink deletion
 */
function handleConfirmDelete() {
    if (!currentDrinkToDelete) return;
    
    const { id: drinkId, name: drinkName, button: deleteBtn } = currentDrinkToDelete;
    
    console.log('User confirmed deletion');
    
    // Hide confirmation modal
    deleteModal.hide();
    
    // Disable button and show loading state
    deleteBtn.disabled = true;
    const originalHTML = deleteBtn.innerHTML;
    deleteBtn.innerHTML = '<i class="fas fa-spinner fa-spin"></i>';
    
    console.log('Making API call to:', `/api/v1/drink/deactivate/${drinkId}`);
    
    // Make API call to deactivate drink
    fetch(`/api/v1/drink/deactivate/${drinkId}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Accept': 'application/json'
        }
    })
    .then(response => {
        console.log('Response status:', response.status);
        console.log('Response ok:', response.ok);
        
        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
        return response.json();
    })
    .then(data => {
        console.log('API Response:', data);
        
        // Check for success using correct field name 'status'
        const isSuccess = data.status === 'success';
        
        if (isSuccess) {
            console.log('Deletion successful, removing from UI');
            handleSuccessfulDeletion(deleteBtn, drinkName);
        } else {
            console.error('API returned failure:', data);
            handleDeleteError(deleteBtn, originalHTML, `Failed to delete drink: ${data.message || 'Unknown error'}`);
        }
    })
    .catch(error => {
        console.error('Error deleting drink:', error);
        handleDeleteError(deleteBtn, originalHTML, `Error deleting drink: ${error.message}`);
    });
    
    // Clear current drink
    currentDrinkToDelete = null;
}

/**
 * Handle successful drink deletion
 * @param {HTMLElement} deleteBtn - The delete button
 * @param {string} drinkName - Name of the deleted drink
 */
function handleSuccessfulDeletion(deleteBtn, drinkName) {
    // Remove from modal list
    const drinkItem = deleteBtn.closest('.list-group-item');
    
    // Animate removal
    drinkItem.style.transition = 'all 0.3s ease';
    drinkItem.style.opacity = '0';
    drinkItem.style.transform = 'translateX(100%)';
    
    setTimeout(() => {
        drinkItem.remove();
        
        // Remove from inventory status section
        removeFromInventoryStatus(drinkName);
        
        // Check if any drinks are left in the modal
        const modal = document.querySelector('#drinks');
        const listGroup = modal ? modal.querySelector('.list-group') : null;
        const remainingItems = listGroup ? listGroup.querySelectorAll('.list-group-item') : [];
        
        console.log('Remaining items count:', remainingItems.length);
        
        if (remainingItems.length === 0 && listGroup) {
            // Hide the list group and show no drinks message
            listGroup.style.display = 'none';
            
            // Create and show "no drinks" message
            const parentDiv = listGroup.parentElement;
            const noDrinksDiv = document.createElement('div');
            noDrinksDiv.className = 'text-muted py-2';
            noDrinksDiv.textContent = 'No drinks available';
            noDrinksDiv.id = 'no-drinks-message';
            parentDiv.appendChild(noDrinksDiv);
        }
    }, 300);
    
    // Show success notification
    showSuccessNotification(`"${drinkName}" has been deactivated successfully!`);
    console.log(`Drink "${drinkName}" deactivated successfully`);
}

/**
 * Handle deletion error
 * @param {HTMLElement} deleteBtn - The delete button
 * @param {string} originalHTML - Original button HTML
 * @param {string} errorMessage - Error message to display
 */
function handleDeleteError(deleteBtn, originalHTML, errorMessage) {
    deleteBtn.disabled = false;
    deleteBtn.innerHTML = originalHTML;
    showErrorNotification(errorMessage);
}

/**
 * Remove drink from inventory status section
 * @param {string} drinkName - Name of the drink to remove
 */
function removeFromInventoryStatus(drinkName) {
    console.log('Removing drink from inventory:', drinkName);
    
    // More specific approach - find the inventory section by looking for "Inventory Status" text
    const inventoryHeaders = document.querySelectorAll('.card-header');
    let inventorySection = null;
    
    inventoryHeaders.forEach(header => {
        if (header.textContent.includes('Inventory Status') || header.textContent.includes('Inventory')) {
            inventorySection = header.closest('.card');
            console.log('Found inventory section by header text');
        }
    });
    
    if (!inventorySection) {
        console.log('Inventory section not found by header text, trying alternative approach');
        // Alternative: look for the section with drink items
        const allCards = document.querySelectorAll('.card');
        allCards.forEach(card => {
            const cardBody = card.querySelector('.card-body');
            if (cardBody && cardBody.querySelector('h6 span')) {
                // This looks like it might be the inventory section
                const spans = cardBody.querySelectorAll('h6 span');
                if (spans.length >= 2) {
                    // Check if it has "Drink" and "Qty" headers
                    if (spans[0].textContent.includes('Drink') || spans[1].textContent.includes('Qty')) {
                        inventorySection = card;
                        console.log('Found inventory section by content structure');
                    }
                }
            }
        });
    }
    
    if (!inventorySection) {
        console.log('Could not find inventory section');
        return;
    }
    
    console.log('Inventory section found, looking for drink items...');
    
    // Find all potential drink items in the card body
    const cardBody = inventorySection.querySelector('.card-body');
    const drinkContainers = cardBody.querySelectorAll('.mb-3');
    
    console.log('Found', drinkContainers.length, 'potential drink containers');
    
    drinkContainers.forEach((container, index) => {
        console.log(`Checking container ${index}:`, container);
        
        // Look for h6 elements that contain drink names
        const h6Elements = container.querySelectorAll('h6');
        h6Elements.forEach(h6 => {
            const spans = h6.querySelectorAll('span');
            if (spans.length >= 2) {
                const drinkNameSpan = spans[0];
                const drinkNameText = drinkNameSpan.textContent.trim();
                console.log(`Found drink in inventory: "${drinkNameText}", looking for: "${drinkName}"`);
                
                if (drinkNameText === drinkName) {
                    console.log('MATCH! Removing inventory item for drink:', drinkName);
                    
                    // Animate removal
                    container.style.transition = 'all 0.3s ease';
                    container.style.opacity = '0';
                    container.style.transform = 'translateX(-100%)';
                    
                    setTimeout(() => {
                        container.remove();
                        console.log('Inventory item removed for:', drinkName);
                        
                        // Check if there are any drinks left in inventory
                        const remainingDrinks = cardBody.querySelectorAll('.mb-3 h6 span:first-child');
                        const actualDrinks = Array.from(remainingDrinks).filter(span => 
                            !span.textContent.includes('Drink') && 
                            !span.textContent.includes('Qty') && 
                            span.textContent.trim() !== ''
                        );
                        
                        console.log('Remaining drinks in inventory:', actualDrinks.length);
                        
                        if (actualDrinks.length === 0) {
                            // Show "No drinks" message
                            const existingNoMessage = cardBody.querySelector('.no-drinks-message');
                            if (!existingNoMessage) {
                                const noDrinksDiv = document.createElement('div');
                                noDrinksDiv.className = 'mb-3 no-drinks-message';
                                noDrinksDiv.textContent = 'No Drinks';
                                noDrinksDiv.style.color = '#666';
                                
                                // Insert before the "Order More" button
                                const orderButton = cardBody.querySelector('.btn');
                                if (orderButton) {
                                    cardBody.insertBefore(noDrinksDiv, orderButton);
                                } else {
                                    cardBody.appendChild(noDrinksDiv);
                                }
                                console.log('Added "No Drinks" message');
                            }
                        }
                    }, 300);
                    
                    return; // Exit once we find and remove the item
                }
            }
        });
    });
}

/**
 * Show success notification
 * @param {string} message - Success message to display
 */
function showSuccessNotification(message) {
    showNotification(message, 'success');
}

/**
 * Show error notification
 * @param {string} message - Error message to display
 */
function showErrorNotification(message) {
    showNotification(message, 'danger');
}

/**
 * Show notification with specified type
 * @param {string} message - Message to display
 * @param {string} type - Notification type ('success' or 'danger')
 */
function showNotification(message, type) {
    const notification = document.createElement('div');
    notification.className = `alert alert-${type} alert-dismissible fade show position-fixed`;
    notification.style.top = '20px';
    notification.style.right = '20px';
    notification.style.zIndex = '9999';
    notification.style.minWidth = '300px';
    notification.innerHTML = `
        <strong>${type === 'success' ? 'Success!' : 'Error!'}</strong> ${message}
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    `;
    document.body.appendChild(notification);
    
    // Auto-remove notification after 4 seconds
    setTimeout(() => {
        if (notification.parentElement) {
            notification.remove();
        }
    }, 4000);
}

// Initialize when DOM is loaded
document.addEventListener('DOMContentLoaded', function() {
    initializeDrinkManagement();
});
