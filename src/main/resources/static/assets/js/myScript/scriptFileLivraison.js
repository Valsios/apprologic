document.addEventListener('DOMContentLoaded', function() {
    // Ouvre la modale quand on clique sur "Ajouter pièce jointe"
    document.querySelectorAll('[data-action="upload"]').forEach(button => {
        button.addEventListener('click', function() {
            const modal = new bootstrap.Modal(document.getElementById('uploadModal'));
            modal.show();

            // Stocke l'ID de la livraison associée (si nécessaire)
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
            window.location.reload();

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
    // Crée une modale dynamique
    const modalId = 'fileViewerModal';
    let modalElement = document.getElementById(modalId);

    // Si la modale existe déjà, on la supprime pour la recréer
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

    // Ajout au DOM
    document.body.appendChild(modalElement);

    // Initialisation et affichage de la modale
    const modal = new bootstrap.Modal(modalElement);
    modal.show();

    // Nettoyage quand la modale est fermée
    modalElement.addEventListener('hidden.bs.modal', () => {
        modalElement.remove();
    });
}