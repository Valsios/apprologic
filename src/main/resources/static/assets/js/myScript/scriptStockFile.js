document.addEventListener('DOMContentLoaded', function() {
    document.querySelectorAll('[data-action="view-attachment"]').forEach(button => {
        button.addEventListener('click', function() {
            const stockMereId = this.dataset.stockId;
            afficherPieceJointe(stockMereId);
        });
    });
    function afficherPieceJointe(stockMereId) {
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
                    <h5 class="modal-title">Stock mère #${stockMereId}</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body p-0">
                    <iframe src="/documents/viewFileStock/${stockMereId}" 
                            style="width: 100%; height: 80vh; border: none;"
                            title="Pièce jointe ${stockMereId}"></iframe>
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
});