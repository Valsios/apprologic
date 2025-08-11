document.addEventListener('DOMContentLoaded', function() {
    //Initialisation
    const addArticleBtn = document.getElementById('add-article');
    const articlesBody = document.getElementById('articles-body');
    const templateRow = document.querySelector('.template-row');

    const consommateurSelect = document.getElementById('consommateur');
    const enfantContainer = document.getElementById('enfant-container');
    const enfantSelect = document.getElementById('consommateur-enfant');

    // Écouteur d'événement pour le changement de sélection
    consommateurSelect.addEventListener('change', function() {
        const selectedOption = this.options[this.selectedIndex];

        // Reset le select enfant
        enfantSelect.innerHTML = `<option value="${this.value}" selected>Sélectionnez un site/filiale (optionnel)</option>`;
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

    // Vérification des éléments requis
    if (!addArticleBtn || !articlesBody || !templateRow) {
        console.error("Éléments manquants dans le DOM");
        return;
    }

    function initAutocomplete(input) {
        const dropdown = document.createElement('div');
        dropdown.className = 'autocomplete-dropdown';
        input.parentNode.appendChild(dropdown);

        let timeout;

        input.addEventListener('input', function() {
            clearTimeout(timeout);
            const searchText = this.value.trim();

            if (searchText.length < 2) {
                dropdown.style.display = 'none';
                return;
            }

            timeout = setTimeout(() => {
                fetch(`/article/searchContaining?q=${encodeURIComponent(searchText)}`)
                    .then(response => response.json())
                    .then(articles => {
                        dropdown.innerHTML = articles.map(article => `
                            <div class="autocomplete-item" 
                                 data-id="${article.idArticle}" 
                                 data-ref="${article.codeArticle}"
                                 data-designation="${article.designation}">
                                <strong>${article.codeArticle}</strong> - ${article.designation}
                            </div>
                        `).join('');
                        dropdown.style.display = 'block';
                    })
                    .catch(console.error);
            }, 300);
        });

        dropdown.addEventListener('click', function(e) {
            if (e.target.classList.contains('autocomplete-item')) {
                const row = input.closest('tr');
                row.querySelector('input[name*="idArticle"]').value = e.target.dataset.id;
                input.value = `${e.target.dataset.ref} - ${e.target.dataset.designation}`;
                dropdown.style.display = 'none';
            }
        });
    }

  //gestion des lignes
    function initExistingRows() {
        // Initialise l'autocomplétion pour les lignes existantes
        document.querySelectorAll('.article-row:not(.template-row) .article-ref').forEach(initAutocomplete);
        renumberAllRows();
    }

    function addNewRow() {
        // Clone la ligne template
        const newRow = templateRow.cloneNode(true);
        newRow.classList.remove('template-row');
        newRow.style.display = 'table-row'; // Affiche la ligne

        // Ajoute au corps du tableau
        articlesBody.appendChild(newRow);

        // Initialise les fonctionnalités
        initAutocomplete(newRow.querySelector('.article-ref'));
        newRow.querySelector('.remove-row').addEventListener('click', function() {
            newRow.remove();
            renumberAllRows();
        });

        renumberAllRows();
    }

    function renumberAllRows() {
        // Sélectionne toutes les lignes sauf le template
        const rows = articlesBody.querySelectorAll('tr:not(.template-row)');

        rows.forEach((row, index) => {
            row.querySelectorAll('input').forEach(input => {
                // Met à jour les noms des champs
                input.name = input.name.replace(/lignes(X|\d+)\./, `lignes${index}.`);
            });
        });
    }

    //event
    addArticleBtn.addEventListener('click', addNewRow);
    // Initialise les lignes existantes au chargement
    initExistingRows();

});