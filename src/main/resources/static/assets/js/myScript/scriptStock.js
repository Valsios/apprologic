$(document).ready(function() {
    // Recherche instantanée avec debounce
    $('#searchInput').on('input', function() {
        performSearch();
    });
    $('#searchDate').on('input', function() {
        performSearch();
    });

    $('#searchType').change(performSearch);

    function performSearch() {
        const searchTerm = $('#searchInput').val().trim();
        const searchDate = $('#searchDate').val();

        const csrfToken = $("meta[name='_csrf']").attr("content");
        const csrfHeader = $("meta[name='_csrf_header']").attr("content");

        // Afficher un indicateur de chargement
        $('#article-table tbody').html('<tr><td colspan="3" class="text-center"><div class="spinner-border text-primary" role="status"></div></td></tr>');

        $.ajax({
            type: "POST",
            url: "/article/stockSearch",
            contentType: "application/x-www-form-urlencoded",
            data: {
                searchTerm: searchTerm,
                searchDate: searchDate
            },
            success: function(data) {
                renderTable(data);
            },
            error: function(xhr) {
                console.error("Erreur lors de la recherche:", xhr.responseText);
                $('#article-table tbody').html('<tr><td colspan="3" class="text-center text-danger">Erreur lors de la recherche</td></tr>');
            },
            beforeSend: function(xhr) {
                xhr.setRequestHeader(csrfHeader, csrfToken); // Ajout du header CSRF
            },

        });
    }

    function renderTable(data) {
        const tbody = $('#article-table tbody');
        tbody.empty();

        if (!data || data.length === 0) {
            tbody.append('<tr><td colspan="3" class="text-center">Aucun résultat trouvé</td></tr>');
            return;
        }

        data.forEach(stock_fille => {
            tbody.append(`
                <tr>
                    <td><strong>${stock_fille.article.codeArticle || ''}</strong>-${stock_fille.article.designation || ''}</td>
                    <td class="text-success fw-bold text-end">${stock_fille.total_entree }</td>
                    <td class="text-danger fw-bold text-end">${stock_fille.total_sortie }</td>
                    <td class="text-center">${stock_fille.stock_date}</td>
                    <td class="text-center">${stock_fille.last_date || ''}</td>
                    <td class="text-center">
                    <button class="btn btn-action " title="Inventaire"
                                data-action="inventaire"
                                th:attr="data-article-id=${stock_fille.article.idArticle}"
                        >
                            <i class="bi bi-list-check"></i>
                        </button>
                    </td>
                </tr>
            `);
        });
    }

});