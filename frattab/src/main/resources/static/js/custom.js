document.addEventListener('DOMContentLoaded', function () {
    console.log('Custom JS loaded - Dashboard page');

    // Initialize modals safely (only if they exist)
    const memberModalElement = document.getElementById('memberModal');
    let memberModal = null;
    
    if (memberModalElement) {
        memberModal = new bootstrap.Modal(memberModalElement);
        console.log('Member modal initialized');
        
        // Handle card clicks to open modal
        document.querySelectorAll('.clickable-card').forEach(card => {
            card.addEventListener('click', function () {
                const memberName = this.querySelector('.card-title').textContent;
                document.getElementById('modalMemberName').textContent = memberName;
                memberModal.show();
            });
        });

        // Increment/decrement handler
        memberModalElement.addEventListener('click', function (e) {
            const inputGroup = e.target.closest('.input-group');
            if (inputGroup) {
                const input = inputGroup.querySelector('.amount-input');
                let value = parseInt(input.value) || 0;

                if (e.target.classList.contains('increment')) {
                    value++;
                    console.log('Incremented to:', value);
                } else if (e.target.classList.contains('decrement')) {
                    value = Math.max(0, value - 1);
                    console.log('Decremented to:', value);
                }

                input.value = value;
            }
        });

        // Submit handler
        const submitDrinksBtn = document.getElementById('submitDrinks');
        if (submitDrinksBtn) {
            submitDrinksBtn.addEventListener('click', function () {
                const drinksData = Array.from(document.querySelectorAll('#drinksTableBody tr')).map(row => {
                    const drinkName = row.querySelector('td:first-child').textContent;
                    const price = row.querySelector('td:nth-child(2)').textContent.replace(' €', ''); // Removing the Euro sign
                    const amount = parseInt(row.querySelector('.amount-input').value) || 0;

                    return {
                        drinkName,
                        price: parseFloat(price),  // Convert price to a number
                        amount
                    };
                });

                console.log('Submitting:', drinksData);
                memberModal.hide();
            });
        }
    } else {
        console.log('Member modal not found - skipping member modal initialization');
    }

    // Note: Drink deletion functionality has been moved to drink-management.js
    
});
