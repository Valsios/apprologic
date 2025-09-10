// Déclaration de la fonction avec un nom
function chargerEnfantsConsommateur() {
    const consommateurSelect = document.getElementById('consommateur');
    const enfantContainer = document.getElementById('enfant-container');
    const enfantSelect = document.getElementById('consommateur-enfant');

    // Fonction pour gérer le changement de sélection
    function gererChangementConsommateur() {
        const selectedOption = this.options[this.selectedIndex];

        // Reset le select enfant
        enfantSelect.innerHTML = `<option value="${this.value}" selected>Sélectionnez une unité (optionnel)</option>`;
        enfantContainer.style.display = 'none';

        console.log(`/consommateur/enfants/${this.value}`);
        if (this.value) {
            fetch(`/consommateur/enfants/${this.value}`)
                .then(response => response.json())
                .then(enfants => {
                    console.log(enfants);
                    if (enfants.length > 0) {
                        enfants.forEach(enfant => {
                            const option = document.createElement('option');
                            option.value = enfant.idConsommateur;
                            option.textContent = enfant.description;
                            enfantSelect.appendChild(option);
                        });
                        enfantContainer.style.display = 'block';
                    }
                })
                .catch(error => {
                    console.error('Erreur lors du chargement des enfants:', error);
                });
        }
    }

    // Ajouter l'écouteur d'événement
    consommateurSelect.addEventListener('change', gererChangementConsommateur);

    // Appeler la fonction au chargement de la page si une valeur est déjà sélectionnée
    if (consommateurSelect.value) {
        gererChangementConsommateur.call(consommateurSelect);
    }
}

document.addEventListener('DOMContentLoaded', function() {
    // Initialisation de DataTable
    $('#article-table').DataTable({
        pagingType: "simple_numbers",
        responsive: true,
        searching: false,
        ordering: true,
        info: false,
        lengthMenu: [10, 20, 30, 50],
        language: {
            paginate: {
                previous: 'Précédent',
                next: 'Suivant'
            }
        }
    });

    // Appeler la fonction nommée lors du chargement
    chargerEnfantsConsommateur();
});