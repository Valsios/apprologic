document.addEventListener('DOMContentLoaded', function() {
    $('#article-table').DataTable({
        pagingType: "simple_numbers",
        responsive: true,
        searching: false,
        ordering: true,
        info: false,
        lengthMenu: [5, 10, 25, 50],
        language: {
            paginate: {
                previous: 'Précédent',
                next: 'Suivant'
            }
        }
    });
    const consommateurSelect = document.getElementById('consommateur');
    const enfantContainer = document.getElementById('enfant-container');
    const enfantSelect = document.getElementById('consommateur-enfant');

    consommateurSelect.addEventListener('change', function() {
        const selectedOption = this.options[this.selectedIndex];

        // Reset le select enfant
        enfantSelect.innerHTML = `<option value="${this.value}" selected>Sélectionnez une unité (optionnel)</option>`;
        enfantContainer.style.display = 'none';

        console.log(`/consommateur/enfants/${this.value}`);
        if (this.value) {
            fetch(`/consommateur/enfants/${this.value}`)
                .then(response => response.json(),)
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
    });
});