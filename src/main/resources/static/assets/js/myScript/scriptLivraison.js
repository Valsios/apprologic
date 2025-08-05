document.addEventListener('DOMContentLoaded', function() {
    // Éléments du DOM
    const addArticleBtn = document.getElementById('add-article');
    const articlesBody = document.getElementById('articles-body');
    const templateRow = document.querySelector('.template-row');

    // Vérification des éléments requis
    if (!addArticleBtn || !articlesBody || !templateRow) {
        console.error("Éléments manquants dans le DOM");
        return;
    }

    // Récupération du token CSRF
    const csrfToken = document.querySelector('input[name="_csrf"]')?.value;
    if (!csrfToken) {
        console.error("Token CSRF manquant");
        return;
    }
    // Autocomplétion des articles
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

            timeout = setTimeout(async () => {
                try {
                    const response = await fetch(`/article/searchContaining?q=${encodeURIComponent(searchText)}`, {
                        headers: {
                            'X-XSRF-TOKEN': csrfToken,
                            'Accept': 'application/json'
                        },
                        credentials: 'include'
                    });

                    if (!response.ok) throw new Error('Erreur réseau');

                    const articles = await response.json();

                    dropdown.innerHTML = articles.map(article => `
                        <div class="autocomplete-item" 
                             data-id="${article.idArticle}" 
                             data-ref="${article.codeArticle}"
                             data-designation="${article.designation}">
                            <strong>${article.codeArticle}</strong> - ${article.designation}
                        </div>
                    `).join('');

                    dropdown.style.display = 'block';
                } catch (error) {
                    console.error('Erreur autocomplétion:', error);
                    dropdown.style.display = 'none';
                }
            }, 300);
        });

        dropdown.addEventListener('click', function(e) {
            if (e.target.classList.contains('autocomplete-item')) {
                const row = input.closest('tr');
                const idInput = row.querySelector('input[name*="idArticle"]');
                if (idInput) {
                    idInput.value = e.target.dataset.id;
                    input.value = `${e.target.dataset.ref} - ${e.target.dataset.designation}`;
                    dropdown.style.display = 'none';
                }
            }
        });

        // Fermer le dropdown quand on clique ailleurs
        document.addEventListener('click', (e) => {
            if (!input.contains(e.target) && !dropdown.contains(e.target)) {
                dropdown.style.display = 'none';
            }
        });
    }

    // Gestion des lignes d'articles
    function initExistingRows() {
        articlesBody.querySelectorAll('tr:not(.template-row)').forEach(row => {
            initAutocomplete(row.querySelector('.article-ref'));
            row.querySelector('.remove-row')?.addEventListener('click', function() {
                row.remove();
                renumberAllRows();
            });
        });
        renumberAllRows();
    }

    function addNewRow() {
        const newRow = templateRow.cloneNode(true);
        newRow.classList.remove('template-row');
        newRow.classList.add('article-row');
        newRow.style.display = 'table-row';
        articlesBody.appendChild(newRow);

        initAutocomplete(newRow.querySelector('.article-ref'));
        newRow.querySelector('.remove-row').addEventListener('click', function() {
            newRow.remove();
            renumberAllRows();
        });

        renumberAllRows();
    }

    function renumberAllRows() {
        articlesBody.querySelectorAll('tr.article-row').forEach((row, index) => {
            row.querySelectorAll('input').forEach(input => {
                input.name = input.name.replace(/lignes(X|\d+)\./, `lignes${index}.`);
            });
        });
    }

    // Événements
    addArticleBtn.addEventListener('click', addNewRow);
    initExistingRows();
});