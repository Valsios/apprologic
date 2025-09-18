document.addEventListener('DOMContentLoaded', function() {

    $('#assignModal').on('shown.bs.modal', function(event) {
        const button = $(event.relatedTarget);
        const gisementId = button.data('gisement-id');
        const articleDesignation = button.data('gisement-article');
        const capaciteMax = button.data('gisement-capacite');
        const udm = button.data('gisement-udm');

        const gisement = button.data('gisement-local')+'-'+button.data('gisement-trave')+'-'+button.data('gisement-alveole')+'-'+button.data('gisement-etagere')+'-'+button.data('gisement-bac');


        $('#articleDesignation').text(articleDesignation);
        $('#capaciteMaxUnitaire').text(capaciteMax+' '+udm);
        $('#gisementId').val(gisementId);
        $('#gisement').text(gisement);
        $('#articleAssign').val('');
        $('#capacite').val('');


    });


        // Initialisation DataTable
        const dataTable = $('#article-table').DataTable({
            pagingType: "simple_numbers",
            // Activation du défilement horizontal
            scrollX: true,

            // Empêcher le wrapping du texte
            responsive: false,
            columnDefs: [
                {
                    targets: '_all',
                    className: 'text-nowrap'
                }
            ],
            searching: false, // On désactive la recherche intégrée
            ordering: true,
            info: false,
            lengthMenu: [10, 20, 30, 50],
            language: {
                paginate: {
                    previous: 'Précédent',
                    next: 'Suivant'
                }
            },
            processing: true,
            // Désactive le traitement côté client pour nos filtres personnalisés
            serverSide: false
        });


    // Stocke l'instance DataTable pour un accès global
    window.dataTable = dataTable;
    setupFilters();
    // Autocomplétion pour le filtre principal
    initAutocomplete({
        inputId: 'articleFilter',
        dropdownId: 'articleDropdown',
        hiddenInputId: 'articleId',
        onSelectCallback: fetchOccupationData
    });

    // Autocomplétion pour la modal d'assignation
    initAutocomplete({
        inputId: 'articleAssign',
        dropdownId: 'articleDropdownAssign',
        hiddenInputId: 'articleAssignId'
    });
});

let abortController = null;

function setupFilters() {
    document.getElementById('localFilter').addEventListener('change', fetchOccupationData);
    document.getElementById('statusFilter').addEventListener('change', fetchOccupationData,filterDisplayedData);
}
function formatTwoDigits(number) {
    return number.toString().padStart(2, '0');
}
async function fetchOccupationData() {
    // Annule la requête précédente si elle existe
    if (abortController) {
        abortController.abort();
    }
    abortController = new AbortController();

    const localId = document.getElementById('localFilter').value;
    const articleId = document.getElementById('articleId').value;

    try {
        const response = await fetch('/local/filtre?' + new URLSearchParams({
            localId: localId || '',
            articleId: articleId || ''
        }), {
            signal: abortController.signal
        });

        if (!response.ok) throw new Error('Erreur serveur');

        const data = await response.json();
        window.currentOccupationData = data; // Stocke les données pour le filtrage
        renderTable(data);
        filterDisplayedData(); // Applique le filtre d'état

    } catch (error) {
        if (error.name !== 'AbortError') {
            console.error('Erreur:', error);
            alert('Erreur lors du chargement des données');
        }
    } finally {
        abortController = null;
    }
}
// Modifiez renderTable() pour utiliser DataTable correctement :
function renderTable(data) {
    // Efface les données existantes
    window.dataTable.clear();

    // Prépare les nouvelles données au format attendu par DataTable
    const rows = data.map(item => {
        const tauxOccupation = ((item.quantite_in - item.quantite_out) / item.capaciteMaxUnnitaire * 100);
        const tauxLibre = 100 - tauxOccupation;
        console.log("ABS : "+item.gisement.idGisement+" " +(Math.abs(tauxLibre-100)<0.01));
        return [
            item.gisement.local.designation,
            formatTwoDigits(item.gisement.trave),
            item.gisement.alveole,
            formatTwoDigits(item.gisement.etagere),
            formatTwoDigits(item.gisement.bac),
            // Colonne article.designation-codeArticle avec condition
            item.article ? `<strong>${item.article.codeArticle}</strong>-${item.article.designation}` : '<span class="text-muted">Pas assigné</span>',
            // Colonne capacité maximum avec condition
            item.article ? item.capaciteMaxUnnitaire : '<span class="text-muted">N/A</span>',
            `<div class="d-flex align-items-center">
                <div class="progress me-2" style="height: 10px; width: 80px;">
                    <div class="progress-bar ${getProgressBarClass(tauxLibre)}"
                         style="width: ${tauxLibre}%"
                         role="progressbar"></div>
                </div>
                <span>${tauxLibre.toFixed(1)}%</span>
            </div>`,
            `
                ${item.isCanBeAssigned ?
                            `<button class="btn btn-action assign"
                        title="Assigner à un nouvel article"
                        data-bs-toggle="modal"
                        data-bs-target="#assignModal"
                        data-gisement-udm="${item.article.udm.description}"
                        data-gisement-local="${item.gisement.local.designation}"
                        data-gisement-trave="${item.gisement.trave}"
                        data-gisement-alveole="${item.gisement.alveole}"
                        data-gisement-etagere="${item.gisement.etagere}"
                        data-gisement-bac="${item.gisement.bac}"
                        data-gisement-id="${item.gisement.idGisement}"
                        data-gisement-article="${item.article.codeArticle+'-'+item.article.designation}"
                        data-gisement-capacite="${item.capaciteMaxUnnitaire}">
                        <i class="bi bi-plus-circle"></i>
                    </button>`
                : ''
            }
            `
        ];
    });

    // Ajoute les nouvelles lignes et redessine le tableau
    window.dataTable.rows.add(rows).draw();
}

// Fonction identique à la logique Thymeleaf
function getProgressBarClass(tauxLibre) {
    return tauxLibre <= 30 ? 'bg-danger' :
        tauxLibre <= 60 ? 'bg-warning' : 'bg-success';
}

function filterDisplayedData() {
    const status = document.getElementById('statusFilter').value;
    if (!window.currentOccupationData) return;

    const filteredData = window.currentOccupationData.filter(item => {
        const tauxLibre = 100 - ((item.quantite_in - item.quantite_out) / item.capaciteMaxUnnitaire * 100);

        switch(status) {
            case 'free': return Math.abs(tauxLibre - 100) < 0.01; // Compare avec tolérance
            case 'occupied': return Math.abs(tauxLibre - 100) >= 0.01;
            default: return true;
        }
    });

    renderTable(filteredData);
}

// Fonction modifiée pour gérer les deux cas
function initAutocomplete(config) {
    const { inputId, dropdownId, hiddenInputId, onSelectCallback } = config;

    const input = document.getElementById(inputId);
    const hiddenInput = document.getElementById(hiddenInputId);

    // Crée le dropdown s'il n'existe pas
    let dropdown = document.getElementById(dropdownId);
    if (!dropdown) {
        dropdown = document.createElement('div');
        dropdown.id = dropdownId;
        dropdown.className = 'autocomplete-dropdown';
        input.parentNode.appendChild(dropdown);
    }

    input.addEventListener('input', debounce(async () => {
        const searchText = input.value.trim();
        hiddenInput.value = '';

        if (searchText.length < 2) {
            dropdown.style.display = 'none';
            return;
        }

        try {
            const response = await fetch(`/article/searchContaining?q=${encodeURIComponent(searchText)}`);
            const articles = await response.json();

            dropdown.innerHTML = articles.map(article => `
                <div class="autocomplete-item" 
                     data-id="${article.idArticle}"
                     data-value="${article.codeArticle} - ${article.designation}">
                    <strong>${article.codeArticle}</strong> - ${article.designation}
                </div>
            `).join('');

            dropdown.style.display = articles.length ? 'block' : 'none';
        } catch (error) {
            console.error('Autocompletion error:', error);
            dropdown.style.display = 'none';
        }
    }, 300));

    // Gestion améliorée du click
    dropdown.addEventListener('click', (e) => {
        const item = e.target.closest('.autocomplete-item');
        if (!item) return;

        input.value = item.dataset.value;
        hiddenInput.value = item.dataset.id;
        dropdown.style.display = 'none';

        if (onSelectCallback) onSelectCallback(item.dataset.id);
    });

    // Fermeture au click externe
    document.addEventListener('click', (e) => {
        if (!input.contains(e.target) && !dropdown.contains(e.target)) {
            dropdown.style.display = 'none';
        }
    });
}

// Helper debounce
function debounce(fn, delay) {
    let timeout;
    return (...args) => {
        clearTimeout(timeout);
        timeout = setTimeout(() => fn(...args), delay);
    };
}

// Fonction helper pour rendre le dropdown
function renderDropdown(dropdown, articles) {
    dropdown.innerHTML = articles.map(article => `
        <div class="autocomplete-item" 
             data-id="${article.idArticle}"
             data-value="${article.codeArticle} - ${article.designation}">
            <strong>${article.codeArticle}</strong> - ${article.designation}
        </div>
    `).join('');
    dropdown.style.display = articles.length ? 'block' : 'none';
}

function resetFilters() {
    document.getElementById('localFilter').value = '';
    document.getElementById('statusFilter').value = 'all';
    document.getElementById('articleFilter').value = '';
    document.getElementById('articleId').value = '';

    // Réinitialisation complète de DataTable
    window.dataTable.destroy();

    // Réinitialisation des données
    window.currentOccupationData = null;

    // Recrée une nouvelle instance DataTable
    window.dataTable = $('#article-table').DataTable({
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
        },
        serverSide: false,
        processing: false
    });

    // Recharge les données initiales
    fetchOccupationData();
}

function getProgressBarClass(taux) {
    taux = parseFloat(taux);
    if (taux <= 30) return 'bg-danger';
    if (taux <= 60) return 'bg-warning';
    return 'bg-success';
}