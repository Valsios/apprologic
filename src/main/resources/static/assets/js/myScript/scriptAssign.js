document.addEventListener('DOMContentLoaded', function() {
    // Écouteur pour le bouton de soumission
    document.getElementById('submitAssign')?.addEventListener('click', async function() {
        try {

            const formData = new FormData(document.getElementById('assignForm'));
            const gisementId = formData.get('gisementId');
            const articleId =formData.get('articleAssignId');
            const capaciteMax = formData.get('capacite');
            const csrfToken = document.querySelector('input[name="_csrf"]').value;

            if (!gisementId || !articleId || !capaciteMax) {
                alert('Veuillez remplir tous les champs requis');
                return;
            }

            const response = await fetch('/local/assign', {
                method: 'POST',
                body: formData,
                headers: {
                    'X-CSRF-TOKEN': csrfToken
                }
            });

            if (response.ok) {
                const result = await response.json();
                console.log('Assignation réussie:', result);

                bootstrap.Modal.getInstance(document.getElementById('assignModal')).hide();
                fetchOccupationData(); // Rafraîchit le tableau principal

                // Afficher un toast de succès (optionnel)
                showToast('Assignation réussie', 'success');
            } else {
                throw new Error(await response.text());
            }
        } catch (error) {
            console.error('Erreur lors de l\'assignation:', error);
            alert(`Erreur: ${error.message}`);
        }
    });
});

// Fonction pour afficher des notifications (optionnelle)
function showToast(message, type = 'success') {
    const toastContainer = document.getElementById('toastContainer') || createToastContainer();
    const toast = document.createElement('div');
    toast.className = `toast show align-items-center text-white bg-${type}`;
    toast.innerHTML = `
        <div class="d-flex">
            <div class="toast-body">${message}</div>
            <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast"></button>
        </div>
    `;
    toastContainer.appendChild(toast);

    setTimeout(() => toast.remove(), 5000);
}

function createToastContainer() {
    const container = document.createElement('div');
    container.id = 'toastContainer';
    container.style.position = 'fixed';
    container.style.top = '20px';
    container.style.right = '20px';
    container.style.zIndex = '1100';
    document.body.appendChild(container);
    return container;
}