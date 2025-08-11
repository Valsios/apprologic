document.addEventListener('DOMContentLoaded', function() {

    document.querySelectorAll('[data-action="inventaire"]').forEach(button => {
        button.addEventListener('click', function () {
            const modal = new bootstrap.Modal(document.getElementById('inventaireModal'), {
                backdrop: true,
                keyboard: true,
                focus: true
            });
            modal.show();
            const articleId = this.getAttribute('data-article-id');
            document.getElementById('submitInventaire').setAttribute('data-article-id', articleId);
        });
    });

    //inventaire
    document.getElementById('submitInventaire').addEventListener('click', async function () {
        try {
            const formData = new FormData(document.getElementById('inventaireForm'));

            const articleId = $(this).data('article-id');

            if (!articleId) throw new Error("ID d'article manquant");

            formData.append('articleId', articleId);

            const response = await fetch('/stock/inventaire', {
                method: 'POST',
                body: formData,
                headers: {
                    'X-CSRF-TOKEN': document.querySelector('input[name="_csrf"]').value
                }
            });

            const data = await response.json();

            if (!response.ok) {
                throw new Error(data.message || "Erreur serveur");
            }

            bootstrap.Modal.getInstance(document.getElementById('inventaireModal')).hide();
            // Afficher le toast de succès avant de recharger la page
            showToast('Inventaire mis à jour avec succès', 'success');

            // Attendre un peu pour que l'utilisateur puisse voir le toast
            setTimeout(() => {
                window.location.reload();
            }, 1500);

        } catch (error) {
            console.error('Erreur détaillée:', error);
            alert(`Échec de l'inventaire: ${error.message}`);
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

