document.addEventListener('DOMContentLoaded', function() {
    // Ouvre la modale quand on clique sur "Ajouter pièce jointe"
    document.querySelectorAll('[data-action="inventaire"]').forEach(button => {
        button.addEventListener('click', function () {
            const modal = new bootstrap.Modal(document.getElementById('inventaireModal'), {
                backdrop: true, // Active le fond obscurci (par défaut)
                keyboard: true, // Permet de fermer avec la touche Échap
                focus: true    // Met le focus sur la modal
            });
            modal.show();

            // Stocke l'ID de la livraison associée (si nécessaire)
            const articleId = this.getAttribute('data-article-id');
            document.getElementById('submitInventaire').setAttribute('data-article-id', articleId);
        });
    });

    // Gestion de l'upload
    document.getElementById('submitInventaire').addEventListener('click', async function () {
        try {
            const formData = new FormData(document.getElementById('inventaireForm'));
            const articleId = this.dataset.articleId;
            if (articleId) formData.append('articleId', articleId);

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
            window.location.reload();

        } catch (error) {
            console.error('Erreur détaillée:', error);
            alert(`Échec de l'inventaire: ${error.message}`);
        }
    });
});

