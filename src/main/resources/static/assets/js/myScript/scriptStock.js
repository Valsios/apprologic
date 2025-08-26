$(document).ready(function() {
    // Initialisation de DataTable
    const dataTable = $('#article-table').DataTable({
        pagingType: "simple_numbers",
        responsive: true,
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
        processing : true,
        // Ajoutez cette option pour initialiser avec des données
        initComplete: function() {
            // Attache les handlers immédiatement
            attachInventaireHandlers();
        }
    });

    // Recherche instantanée avec debounce
    let searchTimeout;
    $('#searchInput, #searchDate').on('input', function() {
        clearTimeout(searchTimeout);
        searchTimeout = setTimeout(performSearch, 300);
    });

    $('#searchType').change(performSearch);

    function performSearch() {
        const searchTerm = $('#searchInput').val().trim();
        const searchDate = $('#searchDate').val();
        const csrfToken = $("meta[name='_csrf']").attr("content");
        const csrfHeader = $("meta[name='_csrf_header']").attr("content");

        // Afficher le loading dans DataTable
        dataTable.processing(true);

        $.ajax({
            type: "POST",
            url: "/article/stockSearch",
            data: {
                searchTerm: searchTerm,
                searchDate: searchDate
            },
            beforeSend: function(xhr) {
                xhr.setRequestHeader(csrfHeader, csrfToken);
            },
            success: function(response) {
                const rows = response.map(item => [
                    `<strong>${item.article.codeArticle || ''}</strong>-${item.article.designation || ''}`,
                    `<span class="text-success fw-bold text-end">${item.total_entree}</span>`,
                    `<span class="text-danger fw-bold text-end">${item.total_sortie}</span>`,
                    `<span class="text-center">${item.stock_date}</span>`,
                    `<span class="text-center">${formatDate(item.last_date) || ''}</span>`,

                    `
                <button class="btn btn-action text-center" title="Inventaire"
                        data-action="inventaire"
                        data-article-id="${item.article.idArticle}">
                    <i class="bi bi-list-check"></i>
                </button>
                 <form action="/article/dashboard" method="post" style="display: inline-block;">
                            <input type="hidden" name="_csrf" value="${csrfToken}">
                            <input type="hidden" name="idArticle" value="${item.article.idArticle}">
                            <button type="submit" class="btn btn-action ms-1" title="Statistiques">
                                <i class="bi bi-bar-chart"></i>
                            </button>
                        </form>
                `
                ]);

                dataTable.clear().rows.add(rows).draw();
                attachInventaireHandlers();
            },
            error: function(xhr) {
                console.error("Erreur lors de la recherche:", xhr.responseText);
                dataTable.clear().draw();
                dataTable.row.add(['', 'Erreur lors de la recherche', '', '', '', '']).draw();
            },
            complete: function() {
                dataTable.processing(false);
            }
        });
    }

    function attachInventaireHandlers() {
        $('#article-table').off('click', '[data-action="inventaire"]').on('click', '[data-action="inventaire"]', function() {
            const articleId = $(this).data('article-id');
            const modal = new bootstrap.Modal('#inventaireModal');
            $('#submitInventaire').data('article-id', articleId);
            modal.show();
        });
    }

    // Fonction pour formater les dates en JavaScript
    function formatDate(dateString) {
        if (!dateString) return '';

        const date = new Date(dateString);

        // Vérifier si la date est valide
        if (isNaN(date.getTime())) return dateString;

        // Formater la date comme "yyyy-MM-dd HH:mm"
        const year = date.getFullYear();
        const month = String(date.getMonth() + 1).padStart(2, '0');
        const day = String(date.getDate()).padStart(2, '0');
        const hours = String(date.getHours()).padStart(2, '0');
        const minutes = String(date.getMinutes()).padStart(2, '0');

        return `${year}-${month}-${day} ${hours}:${minutes}`;
    }
});