$(document).ready(function() {
    // Initialisation de DataTable
    const dataTable = $('#article-table').DataTable({
        pagingType: "simple_numbers",
        responsive: true,
        searching: false, // On désactive la recherche intégrée
        ordering: true,
        info: false,
        lengthMenu: [5, 10, 25, 50],
        language: {
            paginate: {
                previous: 'Précédent',
                next: 'Suivant'
            },
        },
        processing: true,
        serverSide: false
    });

    // Recherche instantanée avec debounce
    let searchTimeout;
    $('#searchInput').on('input', function() {
        clearTimeout(searchTimeout);
        searchTimeout = setTimeout(performSearch, 300);
    });

    $('#searchType').change(performSearch);

    function performSearch() {
        const searchTerm = $('#searchInput').val().trim();
        const searchType = $('#searchType').val();
        const csrfToken = $("meta[name='_csrf']").attr("content");
        const csrfHeader = $("meta[name='_csrf_header']").attr("content");

        // Afficher le loading dans DataTable
        dataTable.processing(true);

        $.ajax({
            type: "POST",
            url: "/article/search",
            data: {
                query: searchTerm,
                searchType: searchType
            },
            beforeSend: function(xhr) {
                xhr.setRequestHeader(csrfHeader, csrfToken);
            },
            success: function(response) {
                // Formatage des données pour DataTables
                const rows = response.map(item => [
                    `<span class="text-end">${item.codeArticle || ''}</span>`,
                    `<span class="text-start">${item.designation || ''}</span>`,
                    `<span class="text-end">${item.seuilMin || ''}</span>`,
                    `<span class="text-end">${item.famille.description || ''}</span>`,
                    `<span class="text-start">${item.udm.acronyme || ''}</span>`,

                    `
               <div class="btn-group" role="group">
                            <a class="btn btn-action edit" title="Modifier"
                               th:href="@{'/article/edit/' + ${item.idArticle}}">
                                <i class="bi bi-pencil"></i>
                            </a>
                        </div>
                `
                ]);

                dataTable.clear().rows.add(rows).draw();
            },
            error: function(xhr) {
                console.error("Erreur lors de la recherche:", xhr.responseText);
                dataTable.clear().draw();
                dataTable.row.add(['', 'Erreur lors de la recherche', '', '', '']).draw();
            },
            complete: function() {
                dataTable.processing(false);
            }
        });
    }
});