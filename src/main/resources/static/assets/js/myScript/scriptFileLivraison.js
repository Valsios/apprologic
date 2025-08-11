document.addEventListener('DOMContentLoaded', function() {
    document.querySelectorAll('[data-action="upload"]').forEach(button => {
        button.addEventListener('click', function() {
            const modal = new bootstrap.Modal(document.getElementById('uploadModal'));
            modal.show();
            const livraisonId = this.getAttribute('data-livraison-id');
            document.getElementById('submitUpload').setAttribute('data-livraison-id', livraisonId);
        });
    });

    // Gestion de l'upload
    document.getElementById('submitUpload').addEventListener('click', async function() {
        try {
            const formData = new FormData(document.getElementById('uploadForm'));
            const livraisonId = this.dataset.livraisonId;
            if (livraisonId) formData.append('livraisonId', livraisonId);

            const response = await fetch('/bonLivraison/uploadFile', {
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

            bootstrap.Modal.getInstance(document.getElementById('uploadModal')).hide();
            showToast('Fichié attaché.','success');
            setTimeout(() => {
                window.location.reload();
            }, 1500);

        } catch (error) {
            console.error('Erreur détaillée:', error);
            alert(`Échec de l'upload: ${error.message}`);
        }
    });

    // Gestion de l'affichage des pièces jointes existantes
    document.querySelectorAll('[data-action="view-attachment"]').forEach(button => {
        button.addEventListener('click', function() {
            const livraisonId = this.dataset.livraisonId;
            afficherPieceJointe(livraisonId);
        });
    });
});

// Fonction pour afficher la pièce jointe
function afficherPieceJointe(livraisonId) {
    const modalId = 'fileViewerModal';
    let modalElement = document.getElementById(modalId);
    if (modalElement) {
        modalElement.remove();
    }

    // Création du HTML de la modale
    modalElement = document.createElement('div');
    modalElement.id = modalId;
    modalElement.className = 'modal fade';
    modalElement.innerHTML = `
        <div class="modal-dialog modal-xl modal-dialog-centered">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">Pièce jointe #${livraisonId}</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body p-0">
                    <iframe src="/bonLivraison/viewFile/${livraisonId}" 
                            style="width: 100%; height: 80vh; border: none;"
                            title="Pièce jointe ${livraisonId}"></iframe>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Fermer</button>
                </div>
            </div>
        </div>`;
    document.body.appendChild(modalElement);
    const modal = new bootstrap.Modal(modalElement);
    modal.show();
    modalElement.addEventListener('hidden.bs.modal', () => {
        modalElement.remove();
    });
}

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